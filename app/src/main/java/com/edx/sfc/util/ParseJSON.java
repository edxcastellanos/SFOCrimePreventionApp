package com.edx.sfc.util;

import android.os.AsyncTask;

import com.edx.sfc.objects.Crime;

public class ParseJSON extends AsyncTask<String, Void, Crime[]> {
    @Override
    protected Crime[] doInBackground(String... params) {
        String result = params[0];

        return JSONReaderSFC.readSFCCrimes(result);
    }
}
