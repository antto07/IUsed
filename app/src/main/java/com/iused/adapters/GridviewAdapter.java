package com.iused.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.iused.R;
import com.iused.main.Sell_Products_Activity;

import java.util.ArrayList;

/**
 * Created by Antto on 29/11/2016.
 */
public class GridviewAdapter extends BaseAdapter{
    private ArrayList<String> listCountry;
    private Activity activity;
    Bitmap bitmap;

    public GridviewAdapter(Activity activity,ArrayList<String> listCountry) {
        super();
        this.listCountry = listCountry;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listCountry.size();
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return listCountry.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder
    {
        public ImageView imgViewFlag;
        public ImageView img_delete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();

        if(convertView==null)
        {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.gallery_item, null);

            view.imgViewFlag = (ImageView) convertView.findViewById(R.id.imgQueue);
            view.img_delete= (ImageView) convertView.findViewById(R.id.deleteimage);

            convertView.setTag(view);
        }
        else
        {
            view = (ViewHolder) convertView.getTag();
        }

//        StringToBitMap(listCountry.get(position));

        try {
            byte [] encodeByte= Base64.decode(listCountry.get(position),Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Log.e("bitmap",bitmap.toString());
        } catch(Exception e) {
            e.getMessage();
            return null;
        }

        view.imgViewFlag.setImageBitmap(bitmap);

        view.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sell_Products_Activity.list_images.remove(position);
                listCountry.remove(position);
                Sell_Products_Activity.image_uris.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Log.e("bitmap",bitmap.toString());
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}
