package com.edx.sfc.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.edx.sfc.objects.Crime;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetCrimes extends AsyncTask<String, Void, Crime[]> {
    private ProgressDialog progressDialog;
    private Context context;

    public GetCrimes(Context context) {
        this.context = context;
    }

    @Override
    protected Crime[] doInBackground(String... url) {
        return getCrimes(url[0]);
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Crime[] result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
    }


    @Override
    protected void onProgressUpdate(Void... values) {
    }

    public static Crime[] getCrimes(String urlStr){
        String jsonStr = "";
        URL url_;

        try {
            url_ = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url_.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            StringBuilder sb = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((jsonStr = br.readLine()) != null) {
                sb.append(jsonStr);
            }

            br.close();
            jsonStr = sb.toString();
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Crime[] crimes = JSONReaderSFC.readSFCCrimes(jsonStr);

        return crimes;
    }
}
