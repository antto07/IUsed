package com.iused.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.iused.R;
import com.iused.bean.DonatedByOthersBean;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Antto on 10-10-2016.
 */
public class DonatedByOthersAdapter extends RecyclerView.Adapter<DonatedByOthersAdapter.CustomViewHolder>{

    public Context context;
    public ArrayList<DonatedByOthersBean> main_Products_Bean;
    OnItemClickListener onItemClickListener;

    public DonatedByOthersAdapter(Context context, ArrayList<DonatedByOthersBean> mainProductsBeanCategories) {
        this.context=context;
        this.main_Products_Bean= mainProductsBeanCategories;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donated_by_others_item, parent,false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        try {

            if(main_Products_Bean.get(position).getProductName().length()>=15){
                holder.txt_title.setText(" "+main_Products_Bean.get(position).getProductName().substring(0,12)+"..");
            }
            else {
                holder.txt_title.setText(" "+main_Products_Bean.get(position).getProductName());
            }

//            holder.txt_title.setText(main_Products_Bean.get(position).getProductName());
            holder.txt_distance.setText(Math.round(Double.parseDouble(main_Products_Bean.get(position).getDistance()))+" Km(s)");
            holder.txt_price.setText("₹ "+main_Products_Bean.get(position).getPrice());
            holder.txt_used_for.setText("("+main_Products_Bean.get(position).getUsedFor()+" old"+")");
            holder.txt_message.setText(main_Products_Bean.get(position).getDescription());
            holder.txt_condition.setText(" "+main_Products_Bean.get(position).getCondition());
            holder.linear_bottom_panel.bringToFront();
        }catch (Exception e){
            holder.txt_title.setText("Title");
            holder.txt_distance.setText("0.0");
            holder.txt_price.setText("--");
            holder.txt_used_for.setText("--");
            holder.txt_message.setText("--");
            holder.linear_bottom_panel.bringToFront();
        }

//        String imageUrl = "http://api.learn2crack.com/android/images/donut.png";

//        if(!loadRottenPoster){
        try {
            Picasso.with(context)
                    .load(main_Products_Bean.get(position).getImage())
                    .placeholder(R.drawable.no_image)
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
//                    .resize(200,200)
                    .fit().centerInside()
                    .into(holder.img_product,new PicassoCallback(main_Products_Bean.get(position).getImage(),holder));
        }catch (Exception e){
            Picasso.with(context)
                    .load("http://52.41.70.254/pics/user.jpg")
                    .placeholder(R.drawable.no_image)
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
//                    .resize(200,200)
                    .fit().centerInside()
                    .into(holder.img_product,new PicassoCallback(main_Products_Bean.get(position).getImage(),holder));
        }

    }

    @Override
    public int getItemCount() {
        return (null != main_Products_Bean ? main_Products_Bean.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView img_product;
        private TextView txt_title;
        private TextView txt_distance;
        private TextView txt_price;
        private TextView txt_used_for;
        private TextView txt_message;
        private LinearLayout linear_bottom_panel;
        private TextView txt_condition;
        private LinearLayout linear_distance;

        public CustomViewHolder(View view) {
            super(view);

            this.img_product=(ImageView)view.findViewById(R.id.img_product);
            this.txt_title=(TextView)view.findViewById(R.id.txt_title);
            this.txt_distance= (TextView) view.findViewById(R.id.txt_distance_donation);
            this.txt_price= (TextView) view.findViewById(R.id.txt_price);
            txt_condition= (TextView) view.findViewById(R.id.txt_condition_donation);
            this.txt_used_for= (TextView) view.findViewById(R.id.txt_used_for);
            this.txt_message= (TextView) view.findViewById(R.id.txt_message);
            this.linear_bottom_panel= (LinearLayout) view.findViewById(R.id.linear_bottom_layout);
            this.linear_distance= (LinearLayout) view.findViewById(R.id.linear_distance);

            this.linear_distance.bringToFront();

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener onItemClickListener1){
        this.onItemClickListener=onItemClickListener1;
    }

    class PicassoCallback implements Callback {

        String thumbnail_poster;
        CustomViewHolder holder_callback;

        public PicassoCallback(String thumbnail,CustomViewHolder holder) {
            this.thumbnail_poster=thumbnail;
            this.holder_callback=holder;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {
//            loadRottenPoster=true;
            Picasso.with(context).load("http://52.41.70.254/pics/user.jpg").into(holder_callback.img_product);
        }
    }

}
