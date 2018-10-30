package com.enovatesoft.paperless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CustomerDetails extends AppCompatActivity {
    TextView tvResults, customerName, customerRim, settingsID;
    Button captureDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        tvResults = findViewById(R.id.tvResults);

        customerName = findViewById(R.id.customerName);
        customerRim = findViewById(R.id.customerRim);
        settingsID = findViewById(R.id.settingsID);

        captureDoc = findViewById(R.id.captureDoc);
        String barcode = getIntent().getStringExtra("code");
        final String[] split = barcode.split(", ");


        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(getApplicationContext(), "Barcode is empty!", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            //tvResults.setText(barcode);
            customerName.setText(split[0]);
            customerRim.setText(split[1]);
            settingsID.setText(split[2]);
        }

        captureDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent capture = new Intent(CustomerDetails.this, QRCapture.class);
                capture.putExtra("rim", split[1]);
                capture.putExtra("sid",split[2]);
                startActivity(capture);
            }
        });




    }
}
