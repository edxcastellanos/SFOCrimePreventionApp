package edx.sfc.sanfranciscocrime;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edx.sfc.objects.Crime;
import edx.sfc.util.JSONReaderSFC;
import edx.sfc.util.ValueComparator;

public class MapFragment extends Fragment {
    MapView mapView;
    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
        Date today = new Date();

        java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(today);
        cal.add(GregorianCalendar.DAY_OF_YEAR, -30);//retrieve data of last month

        String startDateStr = df.format(cal.getTime());
        String todayDateStr = df.format(today);
        System.out.println("startDate: " + startDateStr);
        System.out.println("todayDate: " + todayDateStr);

        String urlStr = "https://data.sfgov.org/resource/cuks-n6tp.json?$where=date%20between%20%27"+startDateStr+"T00:00:00%27%20and%20%27"+todayDateStr+"T23:59:59%27&$limit=50000";
        View v = inflater.inflate(R.layout.map_fragment, container, false);

        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setIndoorLevelPickerEnabled(false);

        new GetJSON(){
            @Override
            protected void onPostExecute( String result ) {
                Crime[] crimes = JSONReaderSFC.readSFCCrimes(result);
                Map<String, Integer> mostCrimesDistrictMap = getMostCrimesDistrictTable(crimes);
                HashMap<String, Integer> districtsColor = new HashMap<String, Integer>();
                int danger = 1;

                mostCrimesDistrictMap = sortByValue(mostCrimesDistrictMap);

                for(String key: mostCrimesDistrictMap.keySet()){
                    switch(danger){
                        case 1: districtsColor.put(key, getResources().getColor(R.color.Danger1));
                            break;
                        case 2: districtsColor.put(key, getResources().getColor(R.color.Danger2));
                            break;
                        case 3: districtsColor.put(key, getResources().getColor(R.color.Danger3));
                            break;
                        case 4: districtsColor.put(key, getResources().getColor(R.color.Danger4));
                            break;
                        case 5: districtsColor.put(key, getResources().getColor(R.color.Danger5));
                            break;
                        case 6: districtsColor.put(key, getResources().getColor(R.color.Danger6));
                            break;
                        case 7: districtsColor.put(key, getResources().getColor(R.color.Danger7));
                            break;
                        case 8: districtsColor.put(key, getResources().getColor(R.color.Danger8));
                            break;
                        default: districtsColor.put(key, getResources().getColor(R.color.Danger8));
                    }
                    danger++;
                }

                float[] hsv = new float[3];
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                for(int i=0; i<crimes.length; i++) {
                    try {
                        Color.colorToHSV(districtsColor.get(crimes[i].getPdDistrict()), hsv);

                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(crimes[i].getLocation().getLatitude(), crimes[i].getLocation().getLongitude()))
                                .title("District\t\t\t\t: " + crimes[i].getPdDistrict()
                                        + "\nDescription\t: " + crimes[i].getDescript().toString())
                                .icon(BitmapDescriptorFactory.defaultMarker(hsv[0]))
                                .snippet("Inc Number\t:" + crimes[i].getIncidntNumber()
                                        + "\nCategory\t\t\t: " + crimes[i].getCategory()
                                        + "\nDate\t\t\t\t\t: " + df.format(crimes[i].getDatetime())
                                        + "\nAddress\t\t\t: " + crimes[i].getAddress().toString()
                                        + "\nDay Week\t\t: " + crimes[i].getDayOfWeek().toString()
                                        + "\nResolution\t\t: " + crimes[i].getResolution().toString()
                                        + "\nLatitude\t\t\t: " + crimes[i].getLocation().getLatitude()
                                        + "\nLongitude\t\t: " + crimes[i].getLocation().getLongitude()));

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

        try {
            map.setMyLocationEnabled(true);
        }catch(SecurityException se){
            se.printStackTrace();
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

    public static TreeMap<String, Integer> sortByValue(Map<String, Integer> unsorted){
        TreeMap<String, Integer> sorted = new TreeMap<String, Integer>(new ValueComparator(unsorted));
        sorted.putAll(unsorted);
        return sorted;
    }

    public static HashMap<String, Integer> getMostCrimesDistrictTable(Crime[] crimes){
        HashMap<String, Integer> distinctDistricts = new HashMap<String, Integer>();
        try {
            for (int i = 0; i < crimes.length; i++) {
                if (!distinctDistricts.containsKey(crimes[i].getPdDistrict())) {
                    distinctDistricts.put(crimes[i].getPdDistrict(), new Integer(1));
                } else {
                    int newValue = distinctDistricts.get(crimes[i].getPdDistrict()) + 1;
                    distinctDistricts.put(crimes[i].getPdDistrict(), newValue);
                }
            }
        }catch(NullPointerException npe){
            Log.i("Exception: " , npe.getMessage());
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

    private class GetJSON extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;
        @Override
        protected String doInBackground(String... url) {
            String urlStr = url[0];
            String jsonStr = "";
            URL url_;
            HttpURLConnection urlConnection = null;
            try {
                url_ = new URL(urlStr);
                urlConnection = (HttpURLConnection) url_.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                StringBuilder sb = new StringBuilder();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                while ((jsonStr= br.readLine()) != null){
                    sb.append(jsonStr);
                }

                br.close();
                jsonStr = sb.toString();

            }catch(MalformedURLException mue){
                mue.printStackTrace();
            }catch(IOException ioe) {
                ioe.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }
            return jsonStr;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading data...");
            progressDialog.show();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }


        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
