package com.enovatesoft.paperless;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.enovatesoft.paperless.adapters.DocumentAdapter;
import com.enovatesoft.paperless.models.Document;

import java.util.ArrayList;
import java.util.List;

public class DocumentsRequired extends AppCompatActivity {

    List<Document> documentList;
    RecyclerView my_recycler_view;

    private TextView tvCustomerName, tvCustomerRim;

    private String[] split = new String[30];
    private String[] split2 = new String[70];

    String document_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_required);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerRim = findViewById(R.id.tvCustomerRim);

        my_recycler_view =  findViewById(R.id.recycler_list_document);
        my_recycler_view.setHasFixedSize(true);

        addItems();

        SharedPreferences mPref= this.getSharedPreferences("session", MODE_WORLD_READABLE);
        mPref.edit().putString("rim", tvCustomerRim.getText().toString()).commit();
    }

    private void addItems() {

        documentList = new ArrayList<>();
        String barcode = getIntent().getStringExtra("code");
        /**
         * Get the Scanned Barcode
         * split it and attach it to the model for the list view
         */

        split = barcode.split(", ");
        split2 = barcode.split("/");

        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(getApplicationContext(), "Barcode is empty!", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            //tvResults.setText(barcode);
            tvCustomerName.setText(split[0]);
            tvCustomerRim.setText(split[1]);

            for(int i = 1; i<split2.length; i+=2){

                documentList.add(new Document(R.drawable.capture, split2[i+1], split2[i]));

            }
            DocumentAdapter myAdapter = new DocumentAdapter(this, documentList);

            my_recycler_view.setLayoutManager(new LinearLayoutManager(DocumentsRequired.this, LinearLayoutManager.VERTICAL, false));

            my_recycler_view.setAdapter(myAdapter);

        }
        DocumentAdapter myAdapter = new DocumentAdapter(this, documentList);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(DocumentsRequired.this, LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(myAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent new_Clients = new Intent(DocumentsRequired.this, NewCustomer.class);
        new_Clients.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(new_Clients);

    }
}
