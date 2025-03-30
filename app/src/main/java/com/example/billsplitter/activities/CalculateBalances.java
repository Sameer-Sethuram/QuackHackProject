//package com.example.billsplitter.activities;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import android.view.View.OnClickListener;
//
//import com.example.billsplitter.R;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okhttp3.FormBody;
//
//import org.json.JSONObject;
//
//public class CalculateBalances {
//    private static final String TAG = StripeConnect.class.getCanonicalName();
//    private static final String STRIPE_API_KEY = "sk_test_51R83Nf08Ddkfxai7rwFVfVdfo8UKD12u1tlO1gkk8ajYfMWObENYajo7RZ82Tfm5VDlutrCNfUs5QCmGkDeBOowe006PUPKOKk";
//    private static final String STRIPE_CLIENT_KEY = "ca_S2BvJnaFjAEo2mFQNqrwL3sYizOLnZpr";
//    private static final String REDIRECT_URI = "https://owenungaro.com/quackhackathon/";
//    private void payConnectedUser(String senderAccountId, String recipientAccountId, int amountInCents) {
//        new Thread(() -> {
//            try {
//                OkHttpClient client = new OkHttpClient();
//
//                //SET TEST AMOUNT IN CENTS!!
//                RequestBody requestBody = new FormBody.Builder()
//                        .add("amount", String.valueOf(amountInCents)) //Amount in cents
//                        .add("currency", "usd")
//                        .add("payment_method_types[]", "card")
//                        .add("description", "Payment from one user to another")
//                        .add("confirm", "true")
//                        .add("on_behalf_of", senderAccountId)
//                        .add("transfer_data[destination]", recipientAccountId)
//                        .build();
//
//                Request request = new Request.Builder()
//                        .url("https://api.stripe.com/v1/payment_intents")
//                        .post(requestBody)
//                        .addHeader("Authorization", "Bearer " + STRIPE_API_KEY)
//                        .build();
//
//                Response response = client.newCall(request).execute();
//                String body = response.body().string();
//
//                if(response.isSuccessful()) {
//                    Log.i(TAG, "Payment sucessful: $ " + (amountInCents / 100.0));
//                    Log.d(TAG, "Response: " + body);
//                } else {
//                    Log.e(TAG, "Transfer failed. HTTP " + response.code());
//                    Log.e(TAG, "BODY: " + response.body().string());
//                }
//            } catch(Exception e) {
//                Log.e(TAG, "Error sending payment: " + e);
//            }
//        }).start();
//    }
//
//    private void PayAllOtherUsers(String payerAccountId) {
//
//        for(String recipientAccountId : stripeAccountList) {
//            if(!recipientAccountId.equals(payerAccountId)) {
//                payConnectedUser(recipientAccountId);
//            }
//        }
//    }
//}
