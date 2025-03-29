package com.example.billsplitter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.stripe.Stripe;
import com.stripe.model.OAuthToken;
import com.stripe.param.OauthTokenCreateParams;

public class StripeConnect extends AppCompatActivity{
    private static final String TAG = "StripeConnect";

    private static final String STRIPE_API_KEY = "sk_test_51R83Nf08Ddkfxai7rwFVfVdfo8UKD12u1tlO1gkk8ajYfMWObENYajo7RZ82Tfm5VDlutrCNfUs5QCmGkDeBOowe006PUPKOKk";
    private static final String STRIPE_CLIENT_KEY = "ca_S27fa4MxGhtMoSeS783TvDpBzU2QUhtf";
    private static final String REDIRECT_URI = "quackhackproject://stripe/oauth/callback";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                Stripe.apiKey = STRIPE_API_KEY;

                //Authorization code -> Access token
                OAuthTokenCreateParams params = OAuthTokenCreateParams.builder().setCode(authorizationCode).build();
                OAuthToken token = OAuthToken.create(params);

                //Connect to account form access token
                String connectedAccountID = token.getStripeUserID();
                String accessToken = token.getAccessToken();

                Log.i(TAG, "Connected account ID: " + connectedAccountId);
                Log.i(TAG, "Access token: " + accessToken);
            } catch(Exception e) {
                Log.e(TAG, "OAuth token exchange failed.", e); //error handling
            }
        }).start();
    }
}
