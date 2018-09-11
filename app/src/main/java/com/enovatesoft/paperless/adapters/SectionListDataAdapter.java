package com.enovatesoft.paperless.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enovatesoft.paperless.InformationRequirements;
import com.enovatesoft.paperless.R;
import com.enovatesoft.paperless.models.Section;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private List<Section> itemsList;
    private Context mContext;

    public SectionListDataAdapter(Context context, List<Section> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        Section singleItem = itemsList.get(i);

        holder.tvName.setText(singleItem.getName());
        holder.tvUid.setText(singleItem.getuId());


        Glide.with(mContext)
                .load(singleItem.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.profilepic)
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvName;
        protected TextView tvUid;
        protected  CardView cardView;


        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvName = view.findViewById(R.id.name);
            this.tvUid = view.findViewById(R.id.uId);
            this.itemImage =  view.findViewById(R.id.itemImage);
            this.cardView = view.findViewById(R.id.card_view);
            tvName.setGravity(Gravity.CENTER);
            tvUid.setGravity(Gravity.CENTER);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent reqts = new Intent (view.getContext(), InformationRequirements.class);
                            reqts.putExtra("name",tvName.getText().toString());
                            reqts.putExtra("uid",tvUid.getText().toString());
                            view.getContext().startActivity(reqts);


                        }
                    });

                }
            });

            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent reqts = new Intent (view.getContext(), InformationRequirements.class);
                            reqts.putExtra("name",tvName.getText().toString());
                            reqts.putExtra("uid",tvUid.getText().toString());
                            view.getContext().startActivity(reqts);


                        }
                    });
                }
            });



        }

    }

}