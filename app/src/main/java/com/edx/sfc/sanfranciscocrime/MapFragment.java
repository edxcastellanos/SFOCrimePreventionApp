package com.edx.sfc.sanfranciscocrime;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edx.sfc.objects.Crime;
import com.edx.sfc.util.GetJSON;
import com.edx.sfc.util.JSONReaderSFC;
import com.edx.sfc.util.ValueComparator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
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

public class MapFragment extends Fragment {
    MapView mapView;
    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        View v = inflater.inflate(R.layout.map_fragment, container, false);


        //Validate Google Play Services to show maps
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        int requestCode = 10;
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this.getActivity(), requestCode);
        if (status != ConnectionResult.SUCCESS) {
            dialog.setTitle("Google Play Services Are not Available");
            dialog.show();
        } else {
            mapView = (MapView) v.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);

            map = mapView.getMap();
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setMapToolbarEnabled(false);
            map.getUiSettings().setCompassEnabled(false);
            map.getUiSettings().setIndoorLevelPickerEnabled(false);

            new GetJSON(getContext()) {
                @Override
                protected void onPostExecute(String result) {
                    Log.i("result", result);
                    Crime[] crimes = JSONReaderSFC.readSFCCrimes(result);
                    Map<String, Integer> mostCrimesDistrictMap = getMostCrimesDistrictTable(crimes);
                    HashMap<String, Integer> districtsColor = new HashMap<>();
                    int danger = 1;

                    mostCrimesDistrictMap = sortByValue(mostCrimesDistrictMap);

                    for (String key : mostCrimesDistrictMap.keySet()) {
                        switch (danger) {
                            case 1:
                                districtsColor.put(key, ContextCompat.getColor(getContext(), R.color.Danger1));
                                break;
                            case 2:
                                districtsColor.put(key, ContextCompat.getColor(getContext(), R.color.Danger2));
                                break;
                            case 3:
                                districtsColor.put(key, ContextCompat.getColor(getContext(), R.color.Danger3));
                                break;
                            case 4:
                                districtsColor.put(key, ContextCompat.getColor(getContext(), R.color.Danger4));
                                break;
                            case 5:
                                districtsColor.put(key, ContextCompat.getColor(getContext(), R.color.Danger5));
                                break;
                            case 6:
                                districtsColor.put(key, ContextCompat.getColor(getContext(), R.color.Danger6));
                                break;
                            case 7:
                                districtsColor.put(key, ContextCompat.getColor(getContext(), R.color.Danger7));
                                break;
                            case 8:
                            default:
                                districtsColor.put(key, ContextCompat.getColor(getContext(), R.color.Danger8));
                                break;
                        }
                        danger++;
                    }

                    float[] hsv = new float[3];
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", java.util.Locale.getDefault());

                    for (Crime crime : crimes) {
                        try {
                            Color.colorToHSV(districtsColor.get(crime.getPdDistrict()), hsv);

                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(crime.getLocation().getLatitude(), crime.getLocation().getLongitude()))
                                    .title("District\t\t\t\t: " + crime.getPdDistrict()
                                            + "\nDescription\t: " + crime.getDescript())
                                    .icon(BitmapDescriptorFactory.defaultMarker(hsv[0]))
                                    .snippet("Inc Number\t:" + crime.getIncidntNumber()
                                            + "\nCategory\t\t\t: " + crime.getCategory()
                                            + "\nDate\t\t\t\t\t: " + df.format(crime.getDatetime())
                                            + "\nAddress\t\t\t: " + crime.getAddress()
                                            + "\nDay Week\t\t: " + crime.getDayOfWeek()
                                            + "\nResolution\t\t: " + crime.getResolution()
                                            + "\nLatitude\t\t\t: " + crime.getLocation().getLatitude()
                                            + "\nLongitude\t\t: " + crime.getLocation().getLongitude()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                LinearLayout infoLayout = new LinearLayout(getContext());
                                infoLayout.setOrientation(LinearLayout.VERTICAL);

                                TextView title = new TextView(getContext());
                                title.setTextColor(Color.BLACK);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(getContext());
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                infoLayout.addView(title);
                                infoLayout.addView(snippet);

                                return infoLayout;
                            }
                        });
                    }
                    super.onPostExecute(result);
                }
            }.execute(urlStr);
        }

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //San Francisco location point
        double latitude = 37.766710;
        double longitude = -122.42507;
        //Map location update
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12);
        map.animateCamera(cameraUpdate);

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
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
