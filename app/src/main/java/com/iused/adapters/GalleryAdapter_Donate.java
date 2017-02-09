package com.iused.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.iused.R;
import com.iused.bean.CustomGallery;
import com.iused.fragments.Donate_Product_Activity;

import java.util.ArrayList;

/**
 * Created by Antto on 30/11/2016.
 */
public class GalleryAdapter_Donate extends BaseAdapter{


    private Context mContext;
    private LayoutInflater infalter;
    private ArrayList<CustomGallery> data = new ArrayList<CustomGallery>();
//	ImageLoader imageLoader;

    private boolean isActionMultiplePick;

    public GalleryAdapter_Donate(Context c) {
        infalter = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = c;
        // clearCache();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CustomGallery getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setMultiplePick(boolean isMultiplePick) {
        this.isActionMultiplePick = isMultiplePick;
    }

    public void selectAll(boolean selection) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).isSeleted = selection;

        }
        notifyDataSetChanged();
    }

    public boolean isAllSelected() {
        boolean isAllSelected = true;

        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).isSeleted) {
                isAllSelected = false;
                break;
            }
        }

        return isAllSelected;
    }

    public boolean isAnySelected() {
        boolean isAnySelected = false;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                isAnySelected = true;
                break;
            }
        }

        return isAnySelected;
    }

    public ArrayList<CustomGallery> getSelected() {
        ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted) {
                dataT.add(data.get(i));
            }
        }

        return dataT;
    }

    public void addAll(ArrayList<CustomGallery> files) {

        try {
            this.data.clear();
            this.data.addAll(files);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }

    public void changeSelection(View v, int position) {

        if (data.get(position).isSeleted) {
            data.get(position).isSeleted = false;
        } else {
            data.get(position).isSeleted = true;
        }

        ((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(data
                .get(position).isSeleted);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = infalter.inflate(R.layout.gallery_item, null);
            holder = new ViewHolder();
            holder.imgQueue = (ImageView) convertView
                    .findViewById(R.id.imgQueue);

            holder.imgQueueMultiSelected = (ImageView) convertView
                    .findViewById(R.id.imgQueueMultiSelected);
            holder.img_delete= (ImageView) convertView.findViewById(R.id.deleteimage);

            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//					String j=data.get(position).toString();
//					Log.e("url_del_path",j);
                    if(Donate_Product_Activity.image_uris.size()==0){
                        Donate_Product_Activity.dataT.clear();
                        data.clear();
                        notifyDataSetChanged();
                    }
                    else {
                        Donate_Product_Activity.dataT.remove(position);
                        data.remove(position);
//					data.clear();
                        Donate_Product_Activity.image_uris.remove(position);
                        notifyDataSetChanged();
                    }

                }
            });

            if (isActionMultiplePick) {
                holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
            } else {
                holder.imgQueueMultiSelected.setVisibility(View.GONE);
            }

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imgQueue.setTag(position);

//		try {
//
//			imageLoader.displayImage("file://" + data.get(position).sdcardPath,
//					holder.imgQueue, new SimpleImageLoadingListener() {
//						@Override
//						public void onLoadingStarted(String imageUri, View view) {
//							holder.imgQueue
//									.setImageResource(R.drawable.no_media);
//							super.onLoadingStarted(imageUri, view);
//						}
//					});
//
//			if (isActionMultiplePick) {
//
//				holder.imgQueueMultiSelected
//						.setSelected(data.get(position).isSeleted);
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

        holder.imgQueue.setImageBitmap(data.get(position).sdcardPath);

//		try {
//			Picasso.with(context)
//					.load(data.get(position).sdcardPath)
//					//.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
//					//.error(R.drawable.user_placeholder_error)
//					.into(holder.imgQueue,new PicassoCallback(data.get(position).sdcardPath,holder));
//		}catch (Exception e){
//			Picasso.with(context)
//					.load("http://52.41.70.254/pics/user.jpg")
//					//.placeholder(R.drawable.user_placeholder) not considering has thumbnails are small size
//					//.error(R.drawable.user_placeholder_error)
//					.into(holder.imgQueue,new PicassoCallback("http://52.41.70.254/pics/user.jpg",holder));
//		}

        return convertView;
    }

    public class ViewHolder {
        ImageView imgQueue;
        ImageView imgQueueMultiSelected;
        ImageView img_delete;
    }

//	public void clearCache() {
//		imageLoader.clearDiscCache();
//		imageLoader.clearMemoryCache();
//	}

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

}
