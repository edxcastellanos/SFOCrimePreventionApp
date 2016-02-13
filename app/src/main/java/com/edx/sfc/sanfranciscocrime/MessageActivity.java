package com.edx.sfc.sanfranciscocrime;

import android.app.Activity;
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

        TextView tv = (TextView) findViewById(R.id.tvMessage);

        tv.setText(message);

    }

}
