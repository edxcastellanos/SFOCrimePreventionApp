package com.edx.sfc.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.edx.sfc.objects.Crime;
import com.edx.sfc.sanfranciscocrime.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GetDistrictColors extends AsyncTask<String, Void, HashMap<String, Integer>> {
    private ProgressDialog progressDialog;
    private Context context;

    public GetDistrictColors(Context context) {
        this.context = context;
    }

    @Override
    protected HashMap<String, Integer> doInBackground(String... url) {
        Crime[] crimes = getCrimes(url[0]);

        Map<String, Integer> mostCrimesDistrictMap = getMostCrimesDistrictTable(crimes);

        HashMap<String, Integer> districtsColor = new HashMap<>();

        int danger = 1;
        mostCrimesDistrictMap = sortByValue(mostCrimesDistrictMap);
        for (String key : mostCrimesDistrictMap.keySet()) {
            switch (danger) {
                case 1:
                    districtsColor.put(key, context.getResources().getColor(R.color.Danger1));
                    break;
                case 2:
                    districtsColor.put(key, context.getResources().getColor(R.color.Danger2));
                    break;
                case 3:
                    districtsColor.put(key, context.getResources().getColor(R.color.Danger3));
                    break;
                case 4:
                    districtsColor.put(key, context.getResources().getColor(R.color.Danger4));
                    break;
                case 5:
                    districtsColor.put(key, context.getResources().getColor(R.color.Danger5));
                    break;
                case 6:
                    districtsColor.put(key, context.getResources().getColor(R.color.Danger6));
                    break;
                case 7:
                    districtsColor.put(key, context.getResources().getColor(R.color.Danger7));
                    break;
                case 8:
                default:
                    districtsColor.put(key, context.getResources().getColor(R.color.Danger8));
                    break;
            }
            danger++;
        }
        return districtsColor;
    }


    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(HashMap<String, Integer> districtColors) {
        super.onPostExecute(districtColors);
        progressDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    public static Crime[] getCrimes(String urlStr) {
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
        return JSONReaderSFC.readSFCCrimes(jsonStr);
    }

    public static TreeMap<String, Integer> sortByValue(Map<String, Integer> unsorted) {
        TreeMap<String, Integer> sorted = new TreeMap<>(new ValueComparator(unsorted));
        sorted.putAll(unsorted);
        return sorted;
    }

    public static HashMap<String, Integer> getMostCrimesDistrictTable(Crime[] crimes) {
        HashMap<String, Integer> distinctDistricts = new HashMap<>();
        try {
            for (Crime crime : crimes) {
                if (!distinctDistricts.containsKey(crime.getPdDistrict())) {
                    distinctDistricts.put(crime.getPdDistrict(), 1);
                } else {
                    int newValue = distinctDistricts.get(crime.getPdDistrict()) + 1;
                    distinctDistricts.put(crime.getPdDistrict(), newValue);
                }
            }
        } catch (NullPointerException npe) {
            Log.i("Exception: ", npe.getMessage());
        }
        Log.i("Districts", distinctDistricts.toString());

        return distinctDistricts;
    }
}
