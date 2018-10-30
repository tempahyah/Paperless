package com.enovatesoft.paperless.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enovatesoft.paperless.R;
import com.enovatesoft.paperless.models.TransactionDetails;

import java.util.List;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.SingleTransactionRowHolder> {

    private Context mContext;
    private List<TransactionDetails> itemList;

    public TransactionListAdapter(Context mContext, List<TransactionDetails> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public SingleTransactionRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_single_card, null);
        SingleTransactionRowHolder trans = new SingleTransactionRowHolder(v);
        return trans;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleTransactionRowHolder holder, int position) {
        TransactionDetails transactionDetails = itemList.get(position);

        holder.account_name.setText(transactionDetails.getAccount_name());
        holder.account_number.setText(transactionDetails.getAccount_number());

        /**
         * Dialog Items
         */
        holder.dialogTransaction_id.setText(transactionDetails.getTransaction_idDiaog());
        holder.dialogTransaction_date.setText(transactionDetails.getTransaction_dateDiaog());
        holder.dialogAccount_number.setText(transactionDetails.getAccount_numberDiaog());
        holder.dialogTransaction_type.setText(transactionDetails.getTransaction_typeDiaog());
        holder.dialogAmount.setText(transactionDetails.getAmountDiaog());
        holder.dialogChannel.setText(transactionDetails.getChannelDiaog());
        holder.dialogBranch.setText(transactionDetails.getBranchDiaog());


        Glide.with(mContext)
                .load(transactionDetails.getCustomerImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.icon_trans)
                .into(holder.customerImage);

    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

    public class SingleTransactionRowHolder extends RecyclerView.ViewHolder{

        protected TextView account_name, account_number;
        protected ImageView customerImage;
        protected CardView transCard;
        protected TextView dialogTransaction_id, dialogTransaction_date, dialogAccount_number, dialogTransaction_type,  dialogAmount, dialogChannel, dialogBranch;
        protected TextView transaction_idDiaog, transaction_dateDiaog, account_numberDiaog, transaction_typeDiaog,  amountDiaog, channelDiaog, branchDiaog;

        public SingleTransactionRowHolder(View itemView) {
            super(itemView);
            this.account_name = itemView.findViewById(R.id.accountName);
            this.account_number = itemView.findViewById(R.id.accountNumber);
            this.customerImage = itemView.findViewById(R.id.customerImage);

            this.transCard = itemView.findViewById(R.id.card_view_transactions);

            this.dialogTransaction_id  = itemView.findViewById(R.id.dialogTransactionId);
            this.dialogTransaction_date = itemView.findViewById(R.id.dialogTransactionDate);
            this.dialogAccount_number = itemView.findViewById(R.id.dialogAccountNumber);
            this.dialogTransaction_type = itemView.findViewById(R.id.dialogTransactionType);
            this.dialogAmount = itemView.findViewById(R.id.dialogAmount);
            this.dialogChannel = itemView.findViewById(R.id.dialogChannel);
            this.dialogBranch = itemView.findViewById(R.id.dialogBranch);


            account_name.setGravity(Gravity.CENTER);
            account_number.setGravity(Gravity.CENTER);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(mContext);
                            dialog.setTitle((account_name.getText().toString()));
                            dialog.setContentView(R.layout.transaction_dialog);
                            Button dialogButton = dialog.findViewById(R.id.backButton);
                            transaction_idDiaog = dialog.findViewById(R.id.transactionID);
                            transaction_dateDiaog = dialog.findViewById(R.id.transactionDate);
                            account_numberDiaog = dialog.findViewById(R.id.accountNumberDialog);
                            transaction_typeDiaog = dialog.findViewById(R.id.transactionType);
                            amountDiaog = dialog.findViewById(R.id.amountDialog);
                            channelDiaog = dialog.findViewById(R.id.channelDialog);
                            branchDiaog = dialog.findViewById(R.id.branchDialog);


                            /**
                             * Binding Details to Dialog
                             */
                            transaction_idDiaog.setText(dialogTransaction_id.getText().toString());
                            transaction_dateDiaog.setText(dialogTransaction_date.getText().toString());
                            account_numberDiaog.setText(dialogAccount_number.getText().toString());
                            transaction_typeDiaog.setText(dialogTransaction_type.getText().toString());
                            amountDiaog.setText(dialogAmount.getText().toString());
                            channelDiaog.setText(dialogChannel.getText().toString());
                            branchDiaog.setText(dialogBranch.getText().toString());

                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();


                        }
                    });

                }
            });
            customerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setTitle((account_name.getText().toString()));
                    dialog.setContentView(R.layout.transaction_dialog);
                    Button dialogButton = dialog.findViewById(R.id.backButton);
                    transaction_idDiaog = dialog.findViewById(R.id.transactionID);
                    transaction_dateDiaog = dialog.findViewById(R.id.transactionDate);
                    account_numberDiaog = dialog.findViewById(R.id.accountNumberDialog);
                    transaction_typeDiaog = dialog.findViewById(R.id.transactionType);
                    amountDiaog = dialog.findViewById(R.id.amountDialog);
                    channelDiaog = dialog.findViewById(R.id.channelDialog);
                    branchDiaog = dialog.findViewById(R.id.branchDialog);


                    /**
                     * Binding Details to Dialog
                     */
                    transaction_idDiaog.setText(dialogTransaction_id.getText().toString());
                    transaction_dateDiaog.setText(dialogTransaction_date.getText().toString());
                    account_numberDiaog.setText(dialogAccount_number.getText().toString());
                    transaction_typeDiaog.setText(dialogTransaction_type.getText().toString());
                    amountDiaog.setText(dialogAmount.getText().toString());
                    channelDiaog.setText(dialogChannel.getText().toString());
                    branchDiaog.setText(dialogBranch.getText().toString());

                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });

        }
    }
}
