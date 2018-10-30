package com.enovatesoft.paperless.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.enovatesoft.paperless.R;
import com.enovatesoft.paperless.models.DataTransaction;
import java.util.List;

public class RecyclerViewTransactionsAdapter extends RecyclerView.Adapter<RecyclerViewTransactionsAdapter.SingleTransactionSection> {

    private List<DataTransaction> dataList;
    private Context mContext;

    public RecyclerViewTransactionsAdapter( Context mContext, List<DataTransaction> dataList) {

        this.mContext = mContext;
        this.dataList = dataList;

    }

    @NonNull
    @Override
    public SingleTransactionSection onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.transaction_list,null);
        SingleTransactionSection sh = new SingleTransactionSection(view);
        return sh;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleTransactionSection holder, int position) {
        final String sectionName = dataList.get(position).getTitle();

        List singleSectionItems = dataList.get(position).getSection();

        holder.transactionTitle.setText(sectionName);
        TransactionListAdapter transactionListAdapter = new TransactionListAdapter(mContext, singleSectionItems);

        holder.recycler_view_transaction_list.setHasFixedSize(true);
        holder.recycler_view_transaction_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.recycler_view_transaction_list.setAdapter(transactionListAdapter);

        holder.recycler_view_transaction_list.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class SingleTransactionSection extends RecyclerView.ViewHolder{
        protected TextView transactionTitle;
        protected RecyclerView recycler_view_transaction_list;


        public SingleTransactionSection(View view) {
            super(view);

            this.transactionTitle = view.findViewById(R.id.transactionTitle);
            this.recycler_view_transaction_list = view.findViewById(R.id.recycler_view_list_transactions);

        }
    }

}
