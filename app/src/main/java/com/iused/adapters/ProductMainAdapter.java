package com.iused.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iused.R;
import com.iused.bean.MainProductsBean;
import com.iused.bean.MainProductsBeanCategories;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import com.squareup.picasso.Callback;

/**
 * Created by Antto on 28-09-2016.
 */
public class ProductMainAdapter extends RecyclerView.Adapter<ProductMainAdapter.CustomViewHolder>{

    public Context context;
    public ArrayList<MainProductsBean> main_Products_Bean;
    OnItemClickListener onItemClickListener;
    private SharedPreferences pref = null;
    public String str_currency_symbol;

    public ProductMainAdapter(Context context, ArrayList<MainProductsBean> mainProductsBeanCategories) {
        this.context=context;
        this.main_Products_Bean= mainProductsBeanCategories;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_main_item, parent,false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        pref = context.getSharedPreferences("user_details", Context.MODE_PRIVATE);
        if (pref != null){
            str_currency_symbol=pref.getString("currency_symbol","");
//            Log.e("currency",str_currency_symbol);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        try {

            if(main_Products_Bean.get(position).getProductName().length()>=25){
                holder.txt_title.setText(main_Products_Bean.get(position).getProductName().substring(0,25)+"..");
            }
            else {
                holder.txt_title.setText(main_Products_Bean.get(position).getProductName());
            }

            holder.txt_distance.setText(" "+Math.round(Double.parseDouble(main_Products_Bean.get(position).getDistance()))+" Km(s)");
            holder.txt_price.setText(str_currency_symbol+" "+main_Products_Bean.get(position).getPrice()+" +");
            holder.txt_used_for.setText(main_Products_Bean.get(position).getUsedFor()+" old");
            holder.txt_condition.setText(main_Products_Bean.get(position).getCondition()+"("+main_Products_Bean.get(position).getUsedFor()+" old)");
        }catch (Exception e){
            holder.txt_title.setText("Title");
            holder.txt_distance.setText("0.0");
            holder.txt_price.setText("--");
            holder.txt_used_for.setText("--");
            holder.txt_condition.setText("--");
        }

//        String imageUrl = "http://api.learn2crack.com/android/images/donut.png";

//        if(!loadRottenPoster){
        try {
            Picasso.with(context)
                    .load(main_Products_Bean.get(position).getImage())
                    .placeholder(R.drawable.no_image)
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
//                    .resize(600,200)
                    .fit().centerInside()
                    .into(holder.img_product,new PicassoCallback(main_Products_Bean.get(position).getImage(),holder));
        }catch (Exception e){
            Picasso.with(context)
                    .load("http://52.41.70.254/pics/user.jpg")
                    //.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
                    //.error(R.drawable.user_placeholder_error)
//                    .resize(600,200)
                    .fit().centerInside()
                    .placeholder(R.drawable.no_image)
                    .into(holder.img_product,new PicassoCallback(main_Products_Bean.get(position).getImage(),holder));
        }

    }

    @Override
    public int getItemCount() {
        return main_Products_Bean.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView img_product;
        private TextView txt_title;
        private TextView txt_distance;
        private TextView txt_price;
        private TextView txt_used_for;
        private TextView txt_condition;


        public CustomViewHolder(View view) {
            super(view);

            this.img_product=(ImageView)view.findViewById(R.id.img_product);
            this.txt_title=(TextView)view.findViewById(R.id.txt_title);
            this.txt_distance= (TextView) view.findViewById(R.id.txt_distance_main);
            this.txt_price= (TextView) view.findViewById(R.id.txt_price);
            this.txt_used_for= (TextView) view.findViewById(R.id.txt_used_for);
            this.txt_condition= (TextView) view.findViewById(R.id.txt_condition);

            this.txt_distance.bringToFront();

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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void SetOnItemClickListener(final OnItemClickListener onItemClickListener1){
        this.onItemClickListener=onItemClickListener1;
    }

    class PicassoCallback implements Callback{

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
