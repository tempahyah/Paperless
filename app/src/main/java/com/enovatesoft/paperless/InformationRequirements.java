package com.enovatesoft.paperless;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
    private Button submit_details;

    private TextView nameField, uIdField;

    List<DataRequirements> allSubSectionData;
    private String TEST_URL="http://192.168.8.9/paperless/imageAPI.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_requirements);
        nameField = findViewById(R.id.nameField);
        uIdField = findViewById(R.id.uIdField);
        submit_details = findViewById(R.id.submit_details);
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

        //Binding values to TextViews
        nameField.setText(name);
        uIdField.setText(uId);

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

                List<SubSection> Nationality= new ArrayList<>();
                List<SubSection> Residence= new ArrayList<>();
                List<SubSection> Education= new ArrayList<>();
                List<SubSection> Employment= new ArrayList<>();

                //Nationality
                SubSection passport = new SubSection("Passport","http://mochasoft.com/input/icon180.png");
                Nationality.add(passport);

                SubSection BirthCertificate = new SubSection("Birth Certificate","http://mochasoft.com/input/icon180.png");
                Nationality.add(BirthCertificate);

                SubSection driverLicence = new SubSection("Driving License","http://mochasoft.com/input/icon180.png");
                Nationality.add(driverLicence);

                SubSection voterID = new SubSection("Voter's ID  ","http://mochasoft.com/input/icon180.png");
                Nationality.add(voterID);

                SubSection national_id = new SubSection("National ID","http://mochasoft.com/input/icon180.png");
                Nationality.add(national_id);

                SubSection school_id = new SubSection("School ID","http://mochasoft.com/input/icon180.png");
                Nationality.add(school_id);

                //Employment
                SubSection empID = new SubSection("Employee ID","http://mochasoft.com/input/icon180.png");
                Employment.add(empID);

                SubSection empLetter = new SubSection("Employment Letter","http://mochasoft.com/input/icon180.png");
                Employment.add(empLetter);

                SubSection empRef = new SubSection("Employment Reference","http://mochasoft.com/input/icon180.png");
                Employment.add(empRef);

                SubSection memorundum = new SubSection("Memorandum of Association","http://mochasoft.com/input/icon180.png");
                Employment.add(memorundum);


                //Residence
                SubSection lcLetter = new SubSection("LC Letter","http://mochasoft.com/input/icon180.png");
                Residence.add(lcLetter);

                SubSection utilityBill = new SubSection("Utility Bill  ","http://mochasoft.com/input/icon180.png");
                Residence.add(utilityBill);

                SubSection tenancyAgreement = new SubSection("Tenancy Agreement  ","http://mochasoft.com/input/icon180.png");
                Residence.add(tenancyAgreement);

                SubSection residentialID = new SubSection("Residential ID","http://mochasoft.com/input/icon180.png");
                Residence.add(residentialID);

                //Education
                SubSection univTrans = new SubSection("University Transcript","http://mochasoft.com/input/icon180.png");
                Education.add(univTrans);

                SubSection diplomaCert = new SubSection("Diploma Certificate","http://mochasoft.com/input/icon180.png");
                Education.add(diplomaCert);

                SubSection uce = new SubSection("U.C.E Certificate","http://mochasoft.com/input/icon180.png");
                Education.add(uce);

                SubSection ple = new SubSection("P.L.E Recommendation","http://mochasoft.com/input/icon180.png");
                Education.add(ple);

                SubSection UACE = new SubSection("U.A.C.E Certificate","http://mochasoft.com/input/icon180.png");
                Education.add(UACE);

                SubSection otherCertificate = new SubSection("Other Education Document","http://mochasoft.com/input/icon180.png");
                Education.add(otherCertificate);

                DataRequirements dataEmployment = new DataRequirements();
                dataEmployment.setSubTitle("Employment");
                dataEmployment.setSubSection(Employment);

                DataRequirements dataResidence = new DataRequirements();
                dataResidence.setSubTitle("Residence");
                dataResidence.setSubSection(Residence);

                DataRequirements dataNationality = new DataRequirements();
                dataNationality.setSubTitle("Nationality");
                dataNationality.setSubSection(Nationality);

                DataRequirements dataEducation = new DataRequirements();
                dataEducation.setSubTitle("Education");
                dataEducation.setSubSection(Education);


                allSubSectionData.add(dataEmployment);
                allSubSectionData.add(dataResidence);
                allSubSectionData.add(dataNationality);
                allSubSectionData.add(dataEducation);


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



                                /*JSONObject obj= (JSONObject) sectionsArray.get(j);

                                SubSection subSection = new SubSection();

                                subSection.setName(obj.getString("document_type"));
                                subSection.setImage(obj.getString("doc_path"));


                                subSections.add(subSection);*/
                            }

                            /*DataRequirements data= new DataRequirements();
                            data.setSubTitle(subTitle);
                            data.setSubSection(subSections);*/

                            /*DataRequirements data2 = new DataRequirements();
                            data2.setSubTitle("yahaya");
                            data2.setSubSection(subSections2);

                            allSubSectionData.add(data2);*/

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