package com.enovatesoft.paperless.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enovatesoft.paperless.CameraUI;
import com.enovatesoft.paperless.R;


import java.util.List;
import com.enovatesoft.paperless.models.SubSection;

import static android.content.Context.MODE_WORLD_READABLE;

public class SubSectionListAdapter extends RecyclerView.Adapter<SubSectionListAdapter.SingleItemRowHolder2> {
    private List <SubSection> subSectionsList;
    private Context mContext;
    private String sid;
    private boolean mStarted = false;

    public SubSectionListAdapter( Context mContext, List<SubSection> subSectionsList) {
        this.mContext = mContext;
        this.subSectionsList = subSectionsList;
    }

    @NonNull
    @Override
    public SingleItemRowHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_section_single_card,null);
        SingleItemRowHolder2 sh = new SingleItemRowHolder2(view);
        return sh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SingleItemRowHolder2 holder2, int position) {
        SubSection subSectionSingleItem = subSectionsList.get(position);

        holder2.subName.setText(subSectionSingleItem.getName());
        holder2.subSid.setText(subSectionSingleItem.getSid());


        Glide.with(mContext)
                .load(subSectionSingleItem.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.capture)
                .into(holder2.subImage);

        holder2.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // sid = holder2.subSid.getText().toString();
                //showPopUpMenu(holder2.overflow);

                if(holder2.subName.getCurrentTextColor()==Color.RED){
                    Toast.makeText(mContext, "Already Captured", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    sid = holder2.subSid.getText().toString();
                    showPopUpMenu(holder2.overflow);
                    holder2.subName.setTextColor(Color.RED);

                }
            }
        });

    }
    private void showPopUpMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_item,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popupMenu.show();

    }


    @Override
    public int getItemCount() {
        return (null != subSectionsList ? subSectionsList.size() : 0);
    }

    public class SingleItemRowHolder2 extends RecyclerView.ViewHolder {

        protected TextView subName, subSid;
        protected ImageView subImage, overflow;
        protected CardView cardView2;
        @SuppressLint("WorldReadableFiles")
        SharedPreferences mPrefSid;

        public SingleItemRowHolder2(View view) {
            super(view);
            this.subName = view.findViewById(R.id.subName);
            this.subSid = view.findViewById(R.id.subSid);
            this.subImage = view.findViewById(R.id.subImage);
            this.overflow =  view.findViewById(R.id.overflow);
            this.cardView2 = view.findViewById(R.id.card_view2);
            this.mPrefSid =  mContext.getSharedPreferences("session", MODE_WORLD_READABLE);

            subName.setGravity(Gravity.CENTER);
            subSid.setGravity(Gravity.CENTER);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            subImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {


                    mStarted = !mStarted;
                    if(subName.getCurrentTextColor()==Color.RED){
                        Toast.makeText(mContext, "Already Captured", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{

                            sid = subSid.getText().toString();
                            showPopUpMenu(subImage);
                            subName.setTextColor(Color.RED);

                    }

                }


            });


        }

    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }
        //int position;

        @SuppressLint("NewApi")
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {


            //SubSection subSectionSingleItem = subSectionsList.get(position) ;
            switch (menuItem.getItemId()){

                case R.id.action_camera:

                    Toast.makeText(mContext, "Redirecting to Camera Application. . . ", Toast.LENGTH_SHORT).show();
                    Intent sidIntent = new Intent (mContext, CameraUI.class);
                    sidIntent.putExtra("sid",sid);
                    mContext.startActivity(sidIntent);
                    return true;

                case R.id.action_like:
                    Toast.makeText(mContext, "Redirecting to Barcode Reader. . . .", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.action_fingerprint:
                    Toast.makeText(mContext, "Getting Fingerprint . . . .", Toast.LENGTH_SHORT).show();
                    return true;

                default:

            }

            return false;
        }
    }


}