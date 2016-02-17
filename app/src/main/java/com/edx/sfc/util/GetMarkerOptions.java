package com.edx.sfc.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.edx.sfc.objects.Crime;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class GetMarkerOptions extends AsyncTask<String, Void, MarkerOptions[]> {
    private ProgressDialog progressDialog;
    private Context context;
    private HashMap<String, Integer> districtsColor;
    private DateFormat df;

    public GetMarkerOptions(Context context, HashMap<String, Integer> districtsColor) {
        this.context = context;
        this.districtsColor = districtsColor;
        df = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
    }

    @Override
    protected MarkerOptions[] doInBackground(String... url) {
        Crime[] crimes = GetCrimes.getCrimes(url[0]);
        MarkerOptions[] crimeMarkers = new MarkerOptions[crimes.length];

        float[] hsv = new float[3];
        for (int i = 0; i < crimes.length; i++) {
            Color.colorToHSV(districtsColor.get(crimes[i].getPdDistrict()), hsv);
            //This has to be in the main thread, but the previous steps can be in an Async Task.
            crimeMarkers[i] = new MarkerOptions()
                    .position(new LatLng(crimes[i].getLocation().getLatitude(), crimes[i].getLocation().getLongitude()))
                    .title("District\t\t\t\t: " + crimes[i].getPdDistrict()
                            + "\nDescription\t: " + crimes[i].getDescript())
                    .icon(BitmapDescriptorFactory.defaultMarker(hsv[0]))
                    .snippet("Inc Number\t:" + crimes[i].getIncidntNumber()
                            + "\nCategory\t\t\t: " + crimes[i].getCategory()
                            + "\nDate\t\t\t\t\t: " + df.format(crimes[i].getDatetime()))
                    .alpha(.8f);
        }

        return crimeMarkers;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(MarkerOptions[] result) {
        progressDialog.dismiss();
    }


    @Override
    protected void onProgressUpdate(Void... values) {
    }

}
