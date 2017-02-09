package com.iused.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.iused.R;
import com.iused.bean.DonorResponsesBean;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by yash on 19-10-2016.
 */
public class DonorOptionsAdapter extends RecyclerView.Adapter<DonorOptionsAdapter.CustomViewHolder>{


    public Context context;
    public ArrayList<DonorResponsesBean> main_Products_Bean;
    OnItemClickListener onItemClickListener;

    public DonorOptionsAdapter(Context context, ArrayList<DonorResponsesBean> mainProductsBeanCategories) {
        this.context=context;
        this.main_Products_Bean= mainProductsBeanCategories;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donor_responses_item, parent,false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        try {
            holder.txt_title.setText(main_Products_Bean.get(position).getProductName());
            holder.txt_no_of_requests.setText("No of Requests : "+main_Products_Bean.get(position).getCount());
            holder.txt_used_for.setText(main_Products_Bean.get(position).getUsedFor()+" old");

            if(main_Products_Bean.get(position).getStatus().equalsIgnoreCase("0")){
                holder.txt_status.setText("Donor reviews now...");
            }
            else if(main_Products_Bean.get(position).getStatus().equalsIgnoreCase("1")){
                holder.txt_status.setText("Donor Accepted.Call now.");
                holder.txt_contact_donor.setVisibility(View.VISIBLE);
            }
            else if(main_Products_Bean.get(position).getStatus().equalsIgnoreCase("2")){
                holder.txt_status.setText("Donated to someone");
            }
            else if(main_Products_Bean.get(position).getStatus().equalsIgnoreCase("3")){
                holder.txt_status.setText("Completed");
                holder.txt_contact_donor.setVisibility(View.VISIBLE);
            }
//            else if(main_Products_Bean.get(position).getStatus().equalsIgnoreCase("4")){
//                holder.txt_status.setText("Published");
//            }
//            else if(main_Products_Bean.get(position).getStatus().equalsIgnoreCase("5")){
//                holder.txt_status.setText("New Reqeust(Pending from Admin to Approve)");
//            }
//            else if(main_Products_Bean.get(position).getStatus().equalsIgnoreCase("6")){
//                holder.txt_status.setText("UnPublished for now");
//            }

        }catch (Exception e){
            holder.txt_title.setText("Title");
            holder.txt_no_of_requests.setText("0");
            holder.txt_status.setText("Published");
        }

//        String imageUrl = "http://api.learn2crack.com/android/images/donut.png";

//        if(!loadRottenPoster){
        try {
            Picasso.with(context)
                    .load(main_Products_Bean.get(position).getProductImage())
                    .placeholder(R.drawable.no_image)
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
//                    .resize(200,200)
                    .fit().centerInside()
                    .into(holder.img_product,new PicassoCallback(main_Products_Bean.get(position).getProductImage(),holder));
        }catch (Exception e){
            Picasso.with(context)
                    .load("http://52.41.70.254/pics/user.jpg")
                    .placeholder(R.drawable.no_image)
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
//                    .resize(200,200)
                    .fit().centerInside()
                    .into(holder.img_product,new PicassoCallback(main_Products_Bean.get(position).getProductImage(),holder));
        }

    }

    @Override
    public int getItemCount() {
        return (null != main_Products_Bean ? main_Products_Bean.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView img_product;
        private TextView txt_title;
        private TextView txt_no_of_requests;
        private TextView txt_status;
        private TextView txt_used_for;
        private TextView txt_contact_donor;

        public CustomViewHolder(View view) {
            super(view);

            this.img_product=(ImageView)view.findViewById(R.id.img_product);
            this.txt_title=(TextView)view.findViewById(R.id.txt_title);
            this.txt_no_of_requests= (TextView) view.findViewById(R.id.txt_no_of_requests);
            this.txt_status= (TextView) view.findViewById(R.id.txt_status_sold);
            this.txt_used_for= (TextView) view.findViewById(R.id.txt_used_for_product);
            this.txt_contact_donor= (TextView) view.findViewById(R.id.txt_contact_donor);

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
