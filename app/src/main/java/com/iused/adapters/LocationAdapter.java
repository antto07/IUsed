package com.iused.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iused.R;
import com.iused.bean.LocationBean;

import java.util.List;

/**
 * Created by ${ADMIN} on 10/11/2016.
 */

public class LocationAdapter extends BaseAdapter {

    private Context context = null;
    private List<LocationBean> bean = null;

    public LocationAdapter(Context context, List<LocationBean> bean) {
        this.context = context;
        this.bean = bean;
    }

    @Override
    public int getCount() {
        try {
            return bean.size();
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.adapter_listing_location, null);
        TextView listPlace = (TextView) convertView.findViewById(R.id.place);
        TextView city = (TextView) convertView.findViewById(R.id.city);

        listPlace.setText(bean.get(position).getPlace());
        city.setText(bean.get(position).getCity()+ " "+ bean.get(position).getCountry());
        return convertView;
    }
}
