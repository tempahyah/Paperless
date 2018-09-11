package com.enovatesoft.paperless.adapters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enovatesoft.paperless.R;
import com.enovatesoft.paperless.models.DataRequirements;

import java.util.List;

public class RecyclerViewDataRequirementsAdapater extends RecyclerView.Adapter<RecyclerViewDataRequirementsAdapater.itemRowHolder2> {

    private List <DataRequirements> dataList;
    private Context mContext;
    int currentScrollX = 0;

    public RecyclerViewDataRequirementsAdapater(Context mContext, List<DataRequirements> dataList){
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public itemRowHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_section_list, null);
        itemRowHolder2 ih = new itemRowHolder2(v);
        return ih;

    }

    @Override
    public void onBindViewHolder(@NonNull itemRowHolder2 itemRowHolder2, int position) {
        final String subSectionName = dataList.get(position).getSubTitle();
        List singleSubSectionItems = dataList.get(position).getSubSection();
        itemRowHolder2.subTitle.setText(subSectionName);
        SubSectionListAdapter itemListDataAdapter = new SubSectionListAdapter(mContext, singleSubSectionItems);

        itemRowHolder2.recycler_view_list.setHasFixedSize(true);
        itemRowHolder2.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder2.recycler_view_list.setAdapter(itemListDataAdapter);


        itemRowHolder2.recycler_view_list.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class itemRowHolder2 extends RecyclerView.ViewHolder {

        protected TextView subTitle;

        protected RecyclerView recycler_view_list;




        public itemRowHolder2(View view) {
            super(view);

            this.subTitle = view.findViewById(R.id.subTitle);
            this.recycler_view_list = view.findViewById(R.id.recycler_view_list_sub_section);



        }

    }
}
