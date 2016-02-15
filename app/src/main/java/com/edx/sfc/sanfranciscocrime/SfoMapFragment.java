package com.edx.sfc.sanfranciscocrime;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edx.sfc.objects.Crime;
import com.edx.sfc.util.GetJSON;
import com.edx.sfc.util.ParseJSON;
import com.edx.sfc.util.ValueComparator;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SfoMapFragment extends Fragment implements OnMapReadyCallback {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);

        MapFragment mapFragment = new MapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.mapView, mapFragment).commit();
        mapFragment.getMapAsync(this);

        return v;
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

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        //San Francisco location point
        double latitude = 37.766710;
        double longitude = -122.42507;
        //Map location update
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12);
        googleMap.animateCamera(cameraUpdate);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date today = new Date();

        java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(today);
        cal.add(GregorianCalendar.DAY_OF_YEAR, -30);//retrieve data of last month

        String startDateStr = df.format(cal.getTime());
        String todayDateStr = df.format(today);
        System.out.println("startDate: " + startDateStr);
        System.out.println("todayDate: " + todayDateStr);

        String urlStr = "https://data.sfgov.org/resource/cuks-n6tp.json?$where=date%20between%20%27" + startDateStr + "T00:00:00%27%20and%20%27" + todayDateStr + "T23:59:59%27&$limit=50000";

        new GetJSON(getActivity()) {
            @Override
            protected void onPostExecute(String result) {
                new ParseJSON() {
                    @Override
                    protected void onPostExecute(Crime[] crimes) {
                        Map<String, Integer> mostCrimesDistrictMap = getMostCrimesDistrictTable(crimes);

                        HashMap<String, Integer> districtsColor = new HashMap<>();

                        int danger = 1;
                        mostCrimesDistrictMap = sortByValue(mostCrimesDistrictMap);
                        for (String key : mostCrimesDistrictMap.keySet()) {
                            switch (danger) {
                                case 1:
                                    districtsColor.put(key, getResources().getColor(R.color.Danger1));
                                    break;
                                case 2:
                                    districtsColor.put(key, getResources().getColor(R.color.Danger2));
                                    break;
                                case 3:
                                    districtsColor.put(key, getResources().getColor(R.color.Danger3));
                                    break;
                                case 4:
                                    districtsColor.put(key, getResources().getColor(R.color.Danger4));
                                    break;
                                case 5:
                                    districtsColor.put(key, getResources().getColor(R.color.Danger5));
                                    break;
                                case 6:
                                    districtsColor.put(key, getResources().getColor(R.color.Danger6));
                                    break;
                                case 7:
                                    districtsColor.put(key, getResources().getColor(R.color.Danger7));
                                    break;
                                case 8:
                                default:
                                    districtsColor.put(key, getResources().getColor(R.color.Danger8));
                                    break;
                            }
                            danger++;
                        }

                        float[] hsv = new float[3];
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", java.util.Locale.getDefault());
                        try {
                            for (Crime crime : crimes) {
                                Color.colorToHSV(districtsColor.get(crime.getPdDistrict()), hsv);
                                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                    @Override
                                    public View getInfoWindow(Marker marker) {
                                        return null;
                                    }

                                    @Override
                                    public View getInfoContents(Marker marker) {
                                        LinearLayout infoLayout = new LinearLayout(getActivity());
                                        infoLayout.setOrientation(LinearLayout.VERTICAL);

                                        TextView tvTitle = new TextView(getActivity());
                                        tvTitle.setTextColor(Color.BLACK);
                                        tvTitle.setTypeface(null, Typeface.BOLD);
                                        tvTitle.setText(marker.getTitle());

                                        TextView tvSnippet = new TextView(getActivity());
                                        tvSnippet.setTextColor(Color.GRAY);
                                        tvSnippet.setText(marker.getSnippet());

                                        Button btnCheckDetail = new Button(getActivity());
                                        btnCheckDetail.setText(R.string.button_detail_text);

                                        infoLayout.addView(tvTitle);
                                        infoLayout.addView(tvSnippet);
                                        infoLayout.addView(btnCheckDetail);

                                        return infoLayout;
                                    }

                                });
                                googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(crime.getLocation().getLatitude(), crime.getLocation().getLongitude()))
                                                .title("District\t\t\t\t: " + crime.getPdDistrict()
                                                        + "\nDescription\t: " + crime.getDescript())
                                                .icon(BitmapDescriptorFactory.defaultMarker(hsv[0]))
                                                .snippet("Inc Number\t:" + crime.getIncidntNumber()
                                                        + "\nCategory\t\t\t: " + crime.getCategory()
                                                        + "\nDate\t\t\t\t\t: " + df.format(crime.getDatetime()))
                                                .alpha(.8f)
                                );

                                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        boolean showNoInternetMessage = false;
                                        ConnectivityManager conMgr = (ConnectivityManager) getActivity()
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
                                            Intent intent = new Intent(getActivity(), MessageActivity.class);
                                            intent.putExtra("message", "Please turn your WiFi or your mobile data plan ON (Carrier charges may apply");
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(getActivity(), DetailActivity.class);
                                            intent.putExtra("incidntNumber", marker.getSnippet().split("\n")[0].split(":")[1]);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.execute(result);
                super.onPostExecute(result);
            }
        }.execute(urlStr);
    }
}