package com.enovatesoft.paperless.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enovatesoft.paperless.CameraCapture;
import com.enovatesoft.paperless.CameraUI;
import com.enovatesoft.paperless.R;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.enovatesoft.paperless.models.Document;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.SingleItemRowHolder> {
    private Context mContext;
    private List<Document> itemsList;

    public DocumentAdapter( Context mContext, List<Document> itemsList) {
        this.mContext = mContext;
        this.itemsList = itemsList;
    }



    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_barcode_item, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
        Document singleItem = itemsList.get(i);
        holder.tvDocumentType.setText(singleItem.getDocument_type());
        holder.tvSettingID.setText(singleItem.getSetting_id());
        holder.image.setImageResource(singleItem.getmImageDrawable());


    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView tvDocumentType, tvSettingID, settingName, already_captured;
        CardView cardView;

        protected RecyclerView recycler_view_list;


        public SingleItemRowHolder(@NonNull View itemView) {
            super(itemView);

            this.image = itemView.findViewById(R.id.document_image);
            this.tvDocumentType = itemView.findViewById(R.id.document_type);
            this.tvSettingID = itemView.findViewById(R.id.setting_id);
            this.settingName = itemView.findViewById(R.id.setting_id_tv);
            this.already_captured = itemView.findViewById(R.id.already_captured);
            this.cardView = itemView.findViewById(R.id.card_view);
            //this.recycler_view_list = itemView.findViewById(R.id.recycler_list_document);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(tvDocumentType.getCurrentTextColor()== Color.RED && tvDocumentType.getCurrentTextColor()== Color.RED && settingName.getCurrentTextColor()== Color.RED ){
                        Toast.makeText(mContext, "Already Captured", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else{
                        tvDocumentType.setTextColor(Color.RED);
                        tvSettingID.setTextColor(Color.RED);
                        settingName.setTextColor(Color.RED);

                        Intent capture = new Intent (mContext, CameraCapture.class);
                        capture.putExtra("sid",tvSettingID.getText().toString());
                        mContext.startActivity(capture);
                        already_captured.setText("Already Captured");
                        already_captured.setTextColor(Color.BLUE);

                    }

                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(tvDocumentType.getCurrentTextColor()== Color.RED && tvDocumentType.getCurrentTextColor()== Color.RED && settingName.getCurrentTextColor()== Color.RED ){
                        Toast.makeText(mContext, "Already Captured", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else{
                        tvDocumentType.setTextColor(Color.RED);
                        tvSettingID.setTextColor(Color.RED);
                        settingName.setTextColor(Color.RED);

                        Intent capture = new Intent (mContext, CameraCapture.class);
                        capture.putExtra("sid",tvSettingID.getText().toString());
                        mContext.startActivity(capture);

                        /**
                         * Lock the cardview
                         */

                        already_captured.setText("Already Captured");
                        already_captured.setTextColor(Color.BLUE);

                    }

                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tvDocumentType.getCurrentTextColor()== Color.RED && tvDocumentType.getCurrentTextColor()== Color.RED && settingName.getCurrentTextColor()== Color.RED ){
                        Toast.makeText(mContext, "Already Captured", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else{
                        tvDocumentType.setTextColor(Color.RED);
                        tvSettingID.setTextColor(Color.RED);
                        settingName.setTextColor(Color.RED);

                        Intent capture = new Intent (mContext, CameraCapture.class);
                        capture.putExtra("sid",tvSettingID.getText().toString());
                        mContext.startActivity(capture);

                        /**
                         * Lock the cardview
                         */

                        already_captured.setText("Already Captured");
                        already_captured.setTextColor(Color.BLUE);
                    }
                }
            });

        }
    }
}