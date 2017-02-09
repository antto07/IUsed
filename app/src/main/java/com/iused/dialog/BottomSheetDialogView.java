package com.iused.dialog;

/**
 * Created by Antto on 09-10-2016.
 */
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.iused.R;
import com.iused.fragments.PageFragment;

public class BottomSheetDialogView {

    private RadioGroup radioGroupMain_sort_by = null;
    private RadioButton rad_btn_sort_by = null;
    private int rad_int_sort_by;

    private RadioGroup radioGroupMain_posted_within = null;
    private RadioButton rad_btn_posted_within = null;
    private int rad_int_posted_within;

    private CheckBox chk_distance= null;
    private TextView txt_cancel= null;
    private TextView txt_done= null;

    private static String[] sStringList;

    static {
        sStringList = new String[50];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sStringList.length; i++) {
            stringBuilder.append(i + 1);
            sStringList[i] = stringBuilder.toString();
        }
    }

    /**
     * remember to call setLocalNightMode for dialog
     *
     * @param context
     * @param dayNightMode current day night mode
     */
    public BottomSheetDialogView(Context context, int dayNightMode) {
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.getDelegate().setLocalNightMode(dayNightMode);

        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_sorting, null);

//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bottom_sheet_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setAdapter(new SimpleAdapter());

        txt_cancel= (TextView) view.findViewById(R.id.txt_dialog_cancel);
        txt_done= (TextView) view.findViewById(R.id.txt_dialog_done);

        radioGroupMain_sort_by= (RadioGroup) view.findViewById(R.id.radioGroup_sort);
        rad_btn_sort_by= (RadioButton) view.findViewById(rad_int_sort_by);

        radioGroupMain_posted_within= (RadioGroup) view.findViewById(R.id.radioGroup_posted_within);
        rad_btn_posted_within= (RadioButton) view.findViewById(rad_int_posted_within);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(rad_btn_sort_by.getText().toString().equalsIgnoreCase("")){

                }
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    public static void show(Context context, int dayNightMode) {
        new BottomSheetDialogView(context, dayNightMode);
    }

//    private static class ViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView mTextView;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            mTextView = (TextView) itemView.findViewById(R.id.list_item_text_view);
//        }
//    }
//
//    private static class SimpleAdapter extends RecyclerView.Adapter<ViewHolder> {
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            View view = inflater.inflate(R.layout.list_item, null);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.mTextView.setText(sStringList[position]);
//        }
//
//        @Override
//        public int getItemCount() {
//            return sStringList.length;
//        }
//    }
}