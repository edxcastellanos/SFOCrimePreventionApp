package com.edx.sfc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edx.sfc.objects.Crime;
import com.edx.sfc.sanfranciscocrime.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Crime> {
    private int resource;
    private Context context;

    public ListViewAdapter(Context ctx, int resourceId, List<Crime> objects) {
        super(ctx, resourceId, objects);
        setResource(resourceId);
        context = ctx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
        /* create a new view of my layout and inflate it in the row */
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_view_linear_adapter, parent, false);
        }
        /* Extract the crime's object to show */
        Crime crime = getItem(position);

        TextView tvDistrict = (TextView) convertView.findViewById(R.id.tvDistrict);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        TextView tvIncNumber = (TextView) convertView.findViewById(R.id.tvIncNumber);
        TextView tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
        TextView tvDayWeek = (TextView) convertView.findViewById(R.id.tvDayWeek);
        TextView tvResolution = (TextView) convertView.findViewById(R.id.tvResolution);

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", java.util.Locale.getDefault());

        String strDistrict = (context.getString(R.string.District) + crime.getPdDistrict());
        String strDescription = (context.getText(R.string.Description) + crime.getDescript());
        String strIncNumber = (context.getString(R.string.IncNumber) + crime.getIncidntNumber());
        String strCategory = (context.getString(R.string.Category) + crime.getCategory());
        String strDate = (context.getString(R.string.Date) + df.format(crime.getDatetime()));
        String strAddress = (context.getString(R.string.Address) + crime.getAddress());
        String strDayWeek = (context.getString(R.string.DayWeek) + crime.getDayOfWeek());
        String strResolution = (context.getString(R.string.Resolution) + crime.getResolution());

        tvDistrict.setText(strDistrict);
        tvDescription.setText(strDescription);
        tvIncNumber.setText(strIncNumber);
        tvCategory.setText(strCategory);
        tvDate.setText(strDate);
        tvAddress.setText(strAddress);
        tvDayWeek.setText(strDayWeek);
        tvResolution.setText(strResolution);

        return convertView;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
