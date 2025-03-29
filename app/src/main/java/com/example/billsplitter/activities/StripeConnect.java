package com.example.billsplitter.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.billsplitter.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.FormBody;

import org.json.JSONObject;


public class StripeConnect extends AppCompatActivity{
    private static final String TAG = StripeConnect.class.getCanonicalName();

    private static final String STRIPE_API_KEY = "sk_test_51R83Nf08Ddkfxai7rwFVfVdfo8UKD12u1tlO1gkk8ajYfMWObENYajo7RZ82Tfm5VDlutrCNfUs5QCmGkDeBOowe006PUPKOKk";
    private static final String STRIPE_CLIENT_KEY = "ca_S27fa4MxGhtMoSeS783TvDpBzU2QUhtf";
    private static final String REDIRECT_URI = "quackhackproject://stripe/oauth/callback";

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
        }
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
                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("client_secret", STRIPE_API_KEY)
                        .add("code", authorizationCode)
                        .add("grant_type", "authorization_code")
                        .build();

                Request request = new Request.Builder()
                        .url("https://connect.stripe.com/oauth/token")
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject json = new JSONObject(responseBody);

                    String connectedAccountId = json.getString("stripe_user_id");
                    String accessToken = json.getString("access_token");

                    Log.i(TAG, "Connected account ID: " + connectedAccountId);
                    Log.i(TAG, "Access token: " + accessToken);
                } else {
                    Log.e(TAG, "Stripe OAuth failed: HTTP " + response.code());
                }

            } catch (Exception e) {
                Log.e(TAG, "OAuth token exchange failed.", e);
            }
        }).start();
    }

}
