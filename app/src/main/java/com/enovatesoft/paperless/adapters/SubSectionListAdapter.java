package com.enovatesoft.paperless.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.enovatesoft.paperless.InformationRequirements;
import com.enovatesoft.paperless.R;


import java.util.List;
import com.enovatesoft.paperless.models.SubSection;

import java.util.List;

public class SubSectionListAdapter extends RecyclerView.Adapter<SubSectionListAdapter.SingleItemRowHolder2> {
    private List <SubSection> subSectionsList;
    private Context mContext;

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

        Glide.with(mContext)
                .load(subSectionSingleItem.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.capture)
                .into(holder2.subImage);

        holder2.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpMenu(holder2.overflow);
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

        protected TextView subName;
        protected ImageView subImage, overflow;


        public SingleItemRowHolder2(View view) {
            super(view);

            this.subName = view.findViewById(R.id.subName);
            this.subImage = view.findViewById(R.id.subImage);
            this.overflow =  view.findViewById(R.id.overflow);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(v.getContext(), subName.getText(), Toast.LENGTH_SHORT).show();


                }
            });
            subImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        }

    }
    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener() {
        }

        @SuppressLint("NewApi")
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()){

                case R.id.action_camera:
                    Toast.makeText(mContext, "Camera App opening. . . ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    mContext.startActivity(intent);
                    return true;

                case R.id.action_like:
                    Toast.makeText(mContext, "Scanning Bar/QR Code", Toast.LENGTH_SHORT).show();
                    //IntentIntegrator integrator = new IntentIntegrator(ItemAdapter.this);
                    //integrator.initiateScan();
                    //Intent scan = new Intent(mContext, BarcodeQRcodeScan.class);
                    //myIntent.putExtra("key", value); //Optional parameters
                   // mContext.startActivity(scan);
                    return true;

                default:

            }

            return false;
        }
    }

}
