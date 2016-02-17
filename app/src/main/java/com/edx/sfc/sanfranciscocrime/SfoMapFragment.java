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
import com.edx.sfc.util.GetCrimes;
import com.edx.sfc.util.GetMarkerOptions;
import com.edx.sfc.util.ValueComparator;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
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
    private String startDateStr, todayDateStr;
    private int limit, offset;
    private static int crimesLength;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date today = new Date();

        java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(today);
        cal.add(GregorianCalendar.DAY_OF_YEAR, -30);//retrieve data of last month

        startDateStr = df.format(cal.getTime());
        todayDateStr = df.format(today);
        System.out.println("startDate: " + startDateStr);
        System.out.println("todayDate: " + todayDateStr);

        limit = 200;
        offset = 0;

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
        crimesLength = crimes.length;
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

        //San Francisco location point
        double latitude = 37.766710;
        double longitude = -122.42507;
        //Map location update
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12);
        googleMap.animateCamera(cameraUpdate);

        String urlStr = "https://data.sfgov.org/resource/cuks-n6tp.json?$where=date%20between%20%27"
                + startDateStr + "T00:00:00%27%20and%20%27" + todayDateStr + "T23:59:59%27&$limit=50000";

        new GetCrimes(getActivity()) {
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
                PopulateMap(googleMap, districtsColor);
                super.onPostExecute(crimes);
            }
        }.execute(urlStr);
    }

    public void PopulateMap(final GoogleMap googleMap, final HashMap<String, Integer> districtsColor) {
        String urlStr = "https://data.sfgov.org/resource/cuks-n6tp.json?$where=date%20between%20%27"
                + startDateStr + "T00:00:00%27%20and%20%27" + todayDateStr + "T23:59:59%27&$limit=" + limit
                + "&$offset=" + offset;

        while (offset < crimesLength) {
            new GetMarkerOptions(getActivity(), districtsColor) {
                @Override
                protected void onPostExecute(MarkerOptions[] crimesMarkers) {
                    try {
                        for (MarkerOptions crimeMarker : crimesMarkers) {

                            //This has to be in the main thread, but the previous steps can be in an Async Task.
                            googleMap.addMarker(crimeMarker);

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
                    super.onPostExecute(crimesMarkers);
                }
            }.execute(urlStr);
        }
        offset += 200;
    }
}