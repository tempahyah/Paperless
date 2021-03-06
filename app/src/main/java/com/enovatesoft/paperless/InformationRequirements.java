package com.enovatesoft.paperless;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.enovatesoft.paperless.adapters.RecyclerViewDataRequirementsAdapater;
import com.enovatesoft.paperless.models.DataRequirements;
import com.enovatesoft.paperless.models.SubSection;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;

public class InformationRequirements extends AppCompatActivity {

    private ProgressDialog pDialog, pSave;
    private CardView cardView;
    private Button submit_details;

    private TextView nameField;
    public TextView uIdField;

    List<DataRequirements> allSubSectionData;
    private String TEST_URL="http://192.168.8.9/paperless/side.php";
    private String rim, getName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_requirements);
        nameField = findViewById(R.id.nameField);
        uIdField = findViewById(R.id.uIdField);
        submit_details = findViewById(R.id.submit_details);

        cardView = findViewById(R.id.card_view);



        pSave = new ProgressDialog(InformationRequirements.this);

        submit_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pSave.setMessage("Saving Scanned Documents...");
                pSave.setCancelable(false);
                if (!pSave.isShowing())
                    pSave.show();
                Intent newCustomer = new Intent(InformationRequirements.this, NewCustomer.class);
                if (pSave != null)
                    pSave.dismiss();
                startActivity(newCustomer);
            }
        });
        allSubSectionData = new ArrayList<>();
        testWebService(TEST_URL);
        Intent reqts = getIntent();
        String name = reqts.getExtras().getString("name").toUpperCase();
        String uId = reqts.getExtras().getString("uid");

        getName = reqts.getExtras().getString("uid");
        rim = reqts.getExtras().getString("uid");

        //Binding values to TextViews
        nameField.setText(name);
        uIdField.setText("Client ID: "+uId);

        SharedPreferences mPref= this.getSharedPreferences("session", MODE_WORLD_READABLE);
        mPref.edit().putString("uid", rim).commit();

    }


    public void showProgressDialog() {

        pDialog = new ProgressDialog(InformationRequirements.this);
        pDialog.setMessage("Loading Information requirements...");
        pDialog.setCancelable(false);

        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void dismissProgressDialog() {
        if (pDialog != null)
            pDialog.dismiss();

    }

    public void testWebService(String url)
    {
        showProgressDialog();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dismissProgressDialog();


                if (statusCode == 200 && response != null) {
                    Log.i("response-", response.toString());


                    try {
                        JSONArray dataArary= response.getJSONArray("data");



                        for(int i=0;i<dataArary.length();i++)
                        {
                            JSONObject sectionObj= (JSONObject) dataArary.get(i);

                            String subTitle= sectionObj.getString("subtitle");

                            List<SubSection> subSections= new ArrayList<>();


                            JSONArray sectionsArray=sectionObj.getJSONArray("subsection");



                            for(int j=0;j<sectionsArray.length();j++)
                            {

                                JSONObject obj= (JSONObject) sectionsArray.get(j);

                                String sid = obj.getString("sid");

                                SubSection subSection = new SubSection();
                                subSection.setName(obj.getString("document_type")+"  ");
                                subSection.setImage("http://192.168.8.9/paperless/doc_capture2.png");
                                subSection.setSid(sid);

                                subSections.add(subSection);


                            }

                            DataRequirements data= new DataRequirements();
                            data.setSubTitle(subTitle);
                            data.setSubSection(subSections);


                            allSubSectionData.add(data);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(InformationRequirements.this,"Parsing Error",Toast.LENGTH_SHORT).show();
                    }


                    if(allSubSectionData!=null) {
                        //initCollapsingToolbar();

                        RecyclerView my_recycler_view =  findViewById(R.id.my_recycler_view_sub_section);

                        my_recycler_view.setHasFixedSize(true);

                        RecyclerViewDataRequirementsAdapater adapter = new RecyclerViewDataRequirementsAdapater(InformationRequirements.this,allSubSectionData);

                        my_recycler_view.setLayoutManager(new LinearLayoutManager(InformationRequirements.this, LinearLayoutManager.VERTICAL, false));

                        my_recycler_view.setAdapter(adapter);

                    }


                }
                else {
                    Toast.makeText(InformationRequirements.this,"Connection Error",Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dismissProgressDialog();
                Toast.makeText(InformationRequirements.this,"Connection Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dismissProgressDialog();
                Toast.makeText(InformationRequirements.this,"Connection Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.information_requirements, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            finish();
            startActivity(getIntent());
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }



}