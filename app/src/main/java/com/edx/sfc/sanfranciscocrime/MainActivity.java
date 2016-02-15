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

/*  public static final int MY_INTERNET_PERMISSION_GRANTED = 7;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_INTERNET_PERMISSION_GRANTED: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        checkStartActivity
                } else {
                    Intent intent = new Intent(this, MessageActivity.class);
                    intent.putExtra("message", "Please give Internet Permission");
                    startActivity(intent);
                }
            }
        }
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Request permission
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_INTERNET_PERMISSION_GRANTED);
        }*/
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
