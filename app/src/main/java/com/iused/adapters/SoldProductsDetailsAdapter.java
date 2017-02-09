package com.iused.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.iused.R;
import com.iused.bean.SoldProductsBean;
import com.iused.main.MainActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vamsi on 13-10-2016.
 */
public class SoldProductsDetailsAdapter extends RecyclerView.Adapter<SoldProductsDetailsAdapter.CustomViewHolder>{

    public Context context;
    public ArrayList<SoldProductsBean> main_Products_Bean;
    OnItemClickListener onItemClickListener;
    private static CountDownTimer countDownTimer;

    public SoldProductsDetailsAdapter(Context context, ArrayList<SoldProductsBean> mainProductsBeanCategories) {
        this.context=context;
        this.main_Products_Bean= mainProductsBeanCategories;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sold_products_details, parent,false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {

        try {
            holder.txt_buyer_name.setText(main_Products_Bean.get(position).getUsername());
            holder.txt_offer_till.setText(main_Products_Bean.get(position).getOfferTill());
            holder.txt_offer_price.setText(main_Products_Bean.get(position).getCurrency()+" "+main_Products_Bean.get(position).getAmount());
        }catch (Exception e){
            holder.txt_buyer_name.setText("Title");
            holder.txt_offer_till.setText("");
            holder.txt_offer_price.setText("");
        }

         String str_hours=main_Products_Bean.get(position).getOfferTill();
        String str_expires_on=main_Products_Bean.get(position).getExpireson();
//        Log.e("expires_on",str_expires_on);
//
////        Log.e("expires_on_first",str_expires_on.substring(0,str_expires_on.indexOf(":")));
//        Log.e("expires_on_last",between(str_expires_on, ":", ":"));
//        Log.e("expires_last_two",str_expires_on.substring(Math.max(str_expires_on.length() - 2, 0)));

        try {

            if(str_expires_on.startsWith("-")){
                holder.txt_timer.setText("Expired");
                holder.txt_timer_hm_only.setText("Expired");
            }
            else {
                //            int noOfMinutes = Integer.parseInt(main_Products_Bean.get(position).getOfferTill()) * 60 * 1000;
                int noOfMinutes = Integer.parseInt(str_expires_on.substring(0,str_expires_on.indexOf(":"))) *60 * 60 * 1000+Integer.parseInt(between(str_expires_on, ":", ":"))*60*1000+Integer.parseInt(str_expires_on.substring(Math.max(str_expires_on.length() - 2, 0)))*1000;
//                Log.e("minutes_millis",noOfMinutes+"");

                holder.txt_timer_hm_only.setText(str_expires_on.substring(0,str_expires_on.indexOf(":"))+"h"+" : "+Integer.parseInt(between(str_expires_on, ":", ":"))+"m");
                countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
                    public void onTick(long millisUntilFinished) {
                        long millis = millisUntilFinished;
                        //Convert milliseconds into hour,minute and seconds
                        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                        holder.txt_timer.setText("Expires in  " + hms);//set text
                    }

                    public void onFinish() {

//                        Intent intent=new Intent(context, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        context.startActivity(intent);

                        holder.txt_timer.setText("Expired"); //On finish change timer text
                        countDownTimer = null;//set CountDownTimer to null
//                startTimer.setText(getString(R.string.start_timer));//Change button text
                    }
                }.start();
            }

        }catch (Exception e){

        }



//        String imageUrl = "http://api.learn2crack.com/android/images/donut.png";

//        if(!loadRottenPoster){
//        Log.e("photo",main_Products_Bean.get(position).getPhoto());
        try {
            Picasso.with(context)
                    .load(main_Products_Bean.get(position).getPhoto())
                    .placeholder(R.drawable.no_image)
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .fit().centerInside()
                    .into(holder.img_product,new PicassoCallback(main_Products_Bean.get(position).getPhoto(),holder));
        }catch (Exception e){
            Picasso.with(context)
                    .load("http://52.41.70.254/pics/user.jpg")
                    .placeholder(R.drawable.no_image)
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
                    .fit().centerInside()
                    .into(holder.img_product,new PicassoCallback("http://52.41.70.254/pics/user.jpg",holder));
        }

    }

    static String between(String value, String a, String b) {
        // Return a substring between the two strings.
        int posA = value.indexOf(a);
        if (posA == -1) {
            return "";
        }
        int posB = value.lastIndexOf(b);
        if (posB == -1) {
            return "";
        }
        int adjustedPosA = posA + a.length();
        if (adjustedPosA >= posB) {
            return "";
        }
        return value.substring(adjustedPosA, posB);
    }

    @Override
    public int getItemCount() {
        return (null != main_Products_Bean ? main_Products_Bean.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView img_product;
        private TextView txt_buyer_name;
        private TextView txt_offer_till;
        private TextView txt_offer_price;
        private TextView txt_timer;
        private TextView txt_timer_hm_only;

        public CustomViewHolder(View view) {
            super(view);

            this.img_product=(ImageView)view.findViewById(R.id.img_product_photo);
            this.txt_buyer_name=(TextView)view.findViewById(R.id.txt_user_name);
            this.txt_offer_till= (TextView) view.findViewById(R.id.txt_offered_till);
            this.txt_offer_price= (TextView) view.findViewById(R.id.txt_amount);
            this.txt_timer= (TextView) view.findViewById(R.id.txt_timer);
            this.txt_timer_hm_only= (TextView) view.findViewById(R.id.txt_timer_hm_only);

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
