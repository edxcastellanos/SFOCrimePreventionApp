package edx.sfc.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetJSON extends AsyncTask<String, Void, String> {
    private ProgressDialog progressDialog;
    private Context context;

    public GetJSON(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... url) {
        String urlStr = url[0];
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

        return jsonStr;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
    }


    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
