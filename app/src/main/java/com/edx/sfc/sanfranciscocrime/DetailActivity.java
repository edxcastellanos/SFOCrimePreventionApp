package com.edx.sfc.sanfranciscocrime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.edx.sfc.adapters.ListViewAdapter;
import com.edx.sfc.objects.Crime;
import com.edx.sfc.util.GetJSON;
import com.edx.sfc.util.ParseJSON;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String incidntNumber = intent.getStringExtra("incidntNumber");
        String urlStr = "https://data.sfgov.org/resource/cuks-n6tp.json?incidntnum=" + incidntNumber;
        new GetJSON(this) {
            @Override
            protected void onPostExecute(String result) {
                new ParseJSON() {
                    @Override
                    protected void onPostExecute(Crime[] crimes) {
                        ArrayList<Crime> crimeArrayList = new ArrayList<>();
                        crimeArrayList.addAll(Arrays.asList(crimes));

                        ListView listViewCity = (ListView) findViewById(R.id.incidentList);

                        ListViewAdapter adaptador = new ListViewAdapter(getApplicationContext(),
                                android.R.layout.simple_list_item_1, crimeArrayList);

                        listViewCity.setAdapter(adaptador);
                    }
                }.execute(result);
                super.onPostExecute(result);
            }
        }.execute(urlStr);

    }
}
