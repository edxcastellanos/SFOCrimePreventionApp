package com.edx.sfc.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetNumIncidents extends AsyncTask<String, Void, Integer> {
    private ProgressDialog progressDialog;
    private Context context;

    public GetNumIncidents(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... url) {
        return getNumberIncidents(url[0]);
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading number of incidents...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
    }


    @Override
    protected void onProgressUpdate(Void... values) {
    }

    public static int getNumberIncidents(String urlStr) {
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
        return JSONReaderSFC.readJSONIncidentNumber(jsonStr);
    }
}
