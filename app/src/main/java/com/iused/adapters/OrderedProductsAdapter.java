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
import com.iused.bean.MainProductsBean;
import com.iused.bean.OrderedProductsBean;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Antto on 10-10-2016.
 */
public class OrderedProductsAdapter extends RecyclerView.Adapter<OrderedProductsAdapter.CustomViewHolder>{

    public Context context;
    public ArrayList<OrderedProductsBean> main_Products_Bean;
    OnItemClickListener onItemClickListener;

    public OrderedProductsAdapter(Context context, ArrayList<OrderedProductsBean> mainProductsBeanCategories) {
        this.context=context;
        this.main_Products_Bean= mainProductsBeanCategories;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordered_products_item, parent,false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        try {
            holder.txt_title.setText(main_Products_Bean.get(position).getProductName());
            holder.txt_price.setText(main_Products_Bean.get(position).getCurrency()+" "+main_Products_Bean.get(position).getAmount());

            if(main_Products_Bean.get(position).getStatus().equalsIgnoreCase("0")){
                holder.txt_status.setText("Seller is reviewing...");
            }
            else if(main_Products_Bean.get(position).getStatus().equalsIgnoreCase("1")){
                holder.txt_status.setText("Accepted");
            }
            else if(main_Products_Bean.get(position).getStatus().equalsIgnoreCase("2")){
                holder.txt_status.setText("Rejected/Cancelled");
            }
            else if(main_Products_Bean.get(position).getStatus().equalsIgnoreCase("3")){
                holder.txt_status.setText("Completed");
            }

        }catch (Exception e){
            holder.txt_title.setText("Title");
            holder.txt_price.setText("--");
        }

        Log.e("product_name",main_Products_Bean.get(position).getProductName());

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
        private TextView txt_price;
        private TextView txt_status;

        public CustomViewHolder(View view) {
            super(view);

            this.img_product=(ImageView)view.findViewById(R.id.img_product);
            this.txt_title=(TextView)view.findViewById(R.id.txt_title);
            this.txt_price= (TextView) view.findViewById(R.id.txt_price);
            this.txt_status= (TextView) view.findViewById(R.id.txt_status);

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
