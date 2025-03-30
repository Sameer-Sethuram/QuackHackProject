package com.example.billsplitter.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.billsplitter.R;
import com.example.billsplitter.databases.BankDao;
import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.databases.UserDao;
import com.example.billsplitter.entities.Bank;
import com.example.billsplitter.entities.User;
import com.example.billsplitter.ui.UserAdapter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.FormBody;

import org.json.JSONObject;

import java.util.List;


public class StripePayment extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = StripeConnect.class.getCanonicalName();

    private static final String STRIPE_API_KEY = "sk_test_51R83Nf08Ddkfxai7rwFVfVdfo8UKD12u1tlO1gkk8ajYfMWObENYajo7RZ82Tfm5VDlutrCNfUs5QCmGkDeBOowe006PUPKOKk";
    private static final String STRIPE_CLIENT_KEY = "ca_S2BvJnaFjAEo2mFQNqrwL3sYizOLnZpr";
    private static final String REDIRECT_URI = "https://owenungaro.com/quackhackathon/";

    private EditText textbox;

    private String code = "code";

    private TabDatabase tabdb;

    private UserAdapter userAdapter;
    private String connectedAccountId;
    private int amountInCents;

    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stripe_pay);

        tabdb = TabDatabase.getInstance(this);
        //Checks if activity has URI
        Intent intent = getIntent();
        Uri uri = intent.getData();

        /*if(uri != null && "quackhackproject".equals(uri.getScheme())) {
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
        }*/

        /*TextView selected = findViewById(R.id.display_selected_user);
        if(user!=null) {
            selected.setText(getString(R.string.selected, user.userName));
        }else{
            selected.setText(getString(R.string.selected, "null user"));
        }*/

        UserDao userDao = tabdb.userDao();
//                    String userName = getIntent().getExtras().getString();
        //User user = userDao.fetchUserByName(userName);
        userAdapter = new UserAdapter(this);
        ListView userList = findViewById(R.id.user_list_view);
        userList.setAdapter(userAdapter);
        userDao.fetchAllUsers().observe(this, usr->{
            userAdapter.setElements(usr);
            userAdapter.notifyDataSetChanged();
        });
        userList.setOnItemClickListener(this);

//        textbox = findViewById(R.id.code_text);
        Button button = findViewById(R.id.authentication_pay);
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
                    connectedAccountId = json.getString("stripe_user_id");
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

    //Fetches bank account details (primarily routing number)

    private void payUserToDummy(String senderConnectedAccountId, int amountInCents) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                // PaymentIntent to charge the user and send to platform account
                RequestBody requestBody = new FormBody.Builder()
                        .add("amount", String.valueOf(amountInCents))
                        .add("currency", "usd")
                        .add("payment_method_types[]", "card")
                        .add("payment_method", "pm_card_visa") // Test payment method
                        .add("description", "User pays platform")
                        .add("confirm", "true")
                        .add("on_behalf_of", senderConnectedAccountId) // Charge on behalf of user
                        .build();

                Request request = new Request.Builder()
                        .url("https://api.stripe.com/v1/payment_intents")
                        .post(requestBody)
                        .addHeader("Authorization", "Bearer " + STRIPE_API_KEY)
                        .build();

                Response response = client.newCall(request).execute();
                String body = response.body().string();

                if (response.isSuccessful()) {
                    Log.i(TAG, "User paid platform: $" + (amountInCents / 100.0));
                    Log.d(TAG, "Response: " + body);
                } else {
                    Log.e(TAG, "Payment failed. HTTP " + response.code());
                    Log.e(TAG, "BODY: " + body);
                }

            } catch (Exception e) {
                Log.e(TAG, "Error processing payment to platform", e);
            }
        }).start();
    }


//    private void payDummyToUser() {
//        new Thread(() -> {
//            UserDao userDao = tabdb.userDao();
//            BankDao bankDao = tabdb.bankDao();
//            List<User> users = userDao.fetchAllUsers().getValue();
//
//            for (User user : users) {
//                // Skip dummy user
//                if (user.userName.equals("Dummy Platform User")) continue;
//
//                double amountToPay = Double.parseDouble(user.amountOwed);
//                int amountInCents = (int)(amountToPay * 100);
//
//                if (amountToPay > 0) {
//                    Bank userBank = bankDao.fetchBankingWithBankId(user.userId); // Using userId to find their bank
//                    if (userBank != null) {
//                        String connectedAccountId = String.valueOf(userBank.getBankAccount()); // assuming this maps to Stripe ID
//
//                        // Now pay them from dummy/platform balance
//                        payConnectedUser(connectedAccountId, amountInCents); // convert to cents
//                    } else {
//                        Log.e(TAG, "No bank account found for user ID: " + user.userId);
//                    }
//                }
//            }
//        }).start();
//    }
//
//
//    private void payAllFromDummy(List<User> userList) {
//        String dummyPlatformAccountId = "EMPTY CHANGE";
//        for(User user : userList) {
//            double amount = user.amountOwed;
//            Bank bank = bankDao.fetchBankingWithBankId(user.userId);
//            if (amount > 0 && bank != null && bank.getBankAccount() != null && !bank.getBankAccount().isEmpty()) {
//                payConnectedUser(bank.getBankAccount(), (int)(amount * 100));
//            }
//
//        }
//    }


    @Override
    public void onClick(View v) {
//        String inputCode = textbox.getText().toString();
//        this.code = inputCode;
//        textbox.setText("");
//        Log.i(TAG, "TEST WORKS!!!!");
//        handleOAuthCallback(inputCode);

        payUserToDummy(String.valueOf(user.userId), amountInCents);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        user = userAdapter.getItem(position);
        double amountToPay = user.amountOwed;
        int amountInCents = (int)(amountToPay*100);




    }
}
