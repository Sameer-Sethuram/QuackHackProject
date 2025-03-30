package com.example.billsplitter.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View.OnClickListener;

import com.example.billsplitter.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.FormBody;

import org.json.JSONObject;


public class StripeConnect extends AppCompatActivity implements OnClickListener {
    private static final String TAG = StripeConnect.class.getCanonicalName();

    private static final String STRIPE_API_KEY = "sk_test_51R83Nf08Ddkfxai7rwFVfVdfo8UKD12u1tlO1gkk8ajYfMWObENYajo7RZ82Tfm5VDlutrCNfUs5QCmGkDeBOowe006PUPKOKk";
    private static final String STRIPE_CLIENT_KEY = "ca_S2BvJnaFjAEo2mFQNqrwL3sYizOLnZpr";
    private static final String REDIRECT_URI = "https://owenungaro.com/quackhackathon/";

    private EditText textbox;

    private String code = "code";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stripe_connect);

        //Checks if activity has URI
        Intent intent = getIntent();
        Uri uri = intent.getData();

        if(uri != null && "quackhackproject".equals(uri.getScheme())) {
            String authorizationCode = uri.getQueryParameter("code");

            //If authorization code exists, move onto next method
            if(authorizationCode != null && !authorizationCode.isEmpty()) {
                Log.d(TAG, "Recieved Stripe OAuth code: " + authorizationCode);
                handleOAuthCallback(authorizationCode);
            } else {
                Log.e(TAG, "Authorization code missing in URI.");
            }
        } else {
            String oauthUrl = generateOAuthUrl();
            Log.d(TAG, "OAuth Connect URL: " + oauthUrl);
            //Opens popup for login
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(oauthUrl));
            startActivity(browserIntent);
        }

        textbox = findViewById(R.id.code_text);
        Button button = findViewById(R.id.authentication);
        button.setOnClickListener(this);
    }

    //Creates OAuth URL to redirect users to Stripe
    private String generateOAuthUrl() {
        return "https://connect.stripe.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + STRIPE_CLIENT_KEY
                + "&scope=read_write"
                + "&redirect_uri=" + REDIRECT_URI;
    }

    private void handleOAuthCallback(String authorizationCode) {
        new Thread(() -> {
            try {
                //Makes HTTP request
                OkHttpClient client = new OkHttpClient();

                //Stripe uses that request to connect to its external accounts API
                RequestBody requestBody = new FormBody.Builder()
                        .add("client_secret", STRIPE_API_KEY) // API Key
                        .add("code", authorizationCode) //Authorization Code
                        .add("grant_type", "authorization_code") //OAuth 2.0
                        .build();

                //Builds Post Request
                Request request = new Request.Builder()
                        .url("https://connect.stripe.com/oauth/token") //Stripes endpoint
                        .post(requestBody)
                        .build();

                //Executes HTTP request
                Response response = client.newCall(request).execute();

                if(response.isSuccessful()) { //HTTP 200
                    //Reads request as string
                    String responseBody = response.body().string();
                    //Converts string into JSON
                    JSONObject json = new JSONObject(responseBody);

                    //Extracts Stripe account ID and access token
                    String connectedAccountId = json.getString("stripe_user_id");
                    String accessToken = json.getString("access_token");

                    Log.i(TAG, "Connected account ID: " + connectedAccountId);
                    Log.i(TAG, "Access token: " + accessToken);
                    fetchBankAccountDetails(connectedAccountId);
                } else {
                    Log.e(TAG, "Stripe OAuth failed: HTTP " + response.code());
                }

            } catch (Exception e) {
                Log.e(TAG, "OAuth token exchange failed.", e);
            }
        }).start();
    }

    //Fetches bank account details (primarily routing number)
    private void fetchBankAccountDetails(String connectedAccountId) {
        new Thread(() -> {
            try {
                //Makes HTTP request
                OkHttpClient client = new OkHttpClient();

                //Stripe uses that request to connect to its external accounts API
                Request request = new Request.Builder()
                        .url("https://api.stripe.com/v1/accounts/" + connectedAccountId + "/external_accounts") //Calling API
                        .addHeader("Authorization", "Bearer " + STRIPE_API_KEY) //Tells Scripe the API key
                        .addHeader("Stripe-Account", connectedAccountId) //Tells Scripe which account you want the information from
                        .get().build(); //GET request

                Response response = client.newCall(request).execute(); //Executes HTTP Request

                if(response.isSuccessful()) {
                    String body = response.body().string(); //Reads JSON as String
                    JSONObject json = new JSONObject(body); //Parses into JSON object

                    if(json.has("data")) {
                        JSONObject firstBank = json.getJSONArray("data").getJSONObject(0); //Gets first bank from list
                        String routingNumber = firstBank.getString("routing_number");
                        String lastFour = firstBank.getString("last4");
                        String bankName = firstBank.getString("bank_name");

                        Log.i(TAG, "Bank:" + bankName);
                        Log.i(TAG, "Routing Number: " + routingNumber);
                        Log.i(TAG, "Last 4 digits: " + lastFour);
                    } else {
                        Log.w(TAG, "No bank accounts found.");
                    }
                } else {
                    Log.e(TAG, "Failed to fetch bank details. HTTP " + response.code());
                }
            } catch(Exception e) {
                Log.e(TAG, "Error fetching bank account details: " + e);
            }
        }).start();
    }

    @Override
    public void onClick(View v){
        String inputCode = textbox.getText().toString();
        this.code = inputCode;
        handleOAuthCallback(inputCode);
    }

}
