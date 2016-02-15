package com.edx.sfc.sanfranciscocrime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle("Error");

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvMessage.setText(message);
    }

}
