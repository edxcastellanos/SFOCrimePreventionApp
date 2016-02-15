package com.edx.sfc.sanfranciscocrime;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {
    private boolean startMainActivity = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkStartActivity();
    }

    public void checkStartActivity() {
        //Validate Internet Connection
        boolean showNoInternetMessage = false;
        Context ctx = this;
        ConnectivityManager conMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null) {
            showNoInternetMessage = true;
        } else {
            if (!i.isConnected())
                showNoInternetMessage = true;
            if (!i.isAvailable())
                showNoInternetMessage = true;
        }
        if (showNoInternetMessage) {
            Intent intent = new Intent(ctx, MessageActivity.class);
            intent.putExtra("message", "Please turn your WiFi or your mobile data plan ON (Carrier charges may apply");
            startActivity(intent);
        } else {
            //Validate Google Play Services to show maps
            GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
            int status = googleAPI.isGooglePlayServicesAvailable(this);
            if (status != ConnectionResult.SUCCESS) {
                Intent intent = new Intent(this, MessageActivity.class);
                intent.putExtra("message", "Please install Google Play Services to use this application");
                startActivity(intent);
            } else {
                startMainActivity = true;
            }
        }

        if (startMainActivity) {
            setContentView(R.layout.activity_main);
        }
    }
}
