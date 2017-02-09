package com.iused.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iused.R;
import com.iused.bean.DonatedProductsBean;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 21/10/2016.
 */
public class DonateProductsDetailsAdapter extends RecyclerView.Adapter<DonateProductsDetailsAdapter.CustomViewHolder>{

    public Context context;
    public ArrayList<DonatedProductsBean> main_Products_Bean;
    OnItemClickListener onItemClickListener;

    public DonateProductsDetailsAdapter(Context context, ArrayList<DonatedProductsBean> mainProductsBeanCategories) {
        this.context=context;
        this.main_Products_Bean= mainProductsBeanCategories;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donate_products_details, parent,false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        try {
            holder.txt_buyer_name.setText(main_Products_Bean.get(position).getUsername());
            holder.txt_offer_till.setText(main_Products_Bean.get(position).getOfferTill());
            holder.txt_offer_price.setText(main_Products_Bean.get(position).getAmount());
            holder.txt_emotional_message.setText(main_Products_Bean.get(position).getEmotional_message());

//            if(main_Products_Bean.get(position).getExpires_in().startsWith("-")){
//                holder.txt_contact.setText("Expired");
//            }
//            else {
//                holder.txt_contact.setText("View Contact");
//            }

        }catch (Exception e){
            holder.txt_buyer_name.setText("Title");
            holder.txt_offer_till.setText("");
            holder.txt_offer_price.setText("");
            holder.txt_emotional_message.setText("");
            holder.txt_contact.setText("View Contact");
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

    @Override
    public int getItemCount() {
        return (null != main_Products_Bean ? main_Products_Bean.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView img_product;
        private TextView txt_buyer_name;
        private TextView txt_offer_till;
        private TextView txt_offer_price;
        private TextView txt_emotional_message;
        private TextView txt_contact;

        public CustomViewHolder(View view) {
            super(view);

            this.img_product=(ImageView)view.findViewById(R.id.img_product_photo);
            this.txt_buyer_name=(TextView)view.findViewById(R.id.txt_user_name);
            this.txt_offer_till= (TextView) view.findViewById(R.id.txt_offered_till);
            this.txt_offer_price= (TextView) view.findViewById(R.id.txt_amount);
            this.txt_emotional_message= (TextView) view.findViewById(R.id.txt_emotional_msg);
            this.txt_contact= (TextView) view.findViewById(R.id.txt_contact);

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
