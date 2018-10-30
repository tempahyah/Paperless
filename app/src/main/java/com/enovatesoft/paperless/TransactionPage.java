package com.enovatesoft.paperless;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.enovatesoft.paperless.adapters.RecyclerViewTransactionsAdapter;
import com.enovatesoft.paperless.adapters.TransactionListAdapter;
import com.enovatesoft.paperless.models.DataTransaction;
import com.enovatesoft.paperless.models.TransactionDetails;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;

public class TransactionPage extends AppCompatActivity {

    List<DataTransaction> allSubSectionData;

    private String TEST_URL="http://192.168.8.9/paperless/transaction.php";

    private ProgressDialog pDialog, pSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_page);

        pSave = new ProgressDialog(this);


        allSubSectionData = new ArrayList<>();
        testWebService(TEST_URL);

    }

    public void showProgressDialog() {

        pDialog = new ProgressDialog(TransactionPage.this);
        pDialog.setMessage("Loading Transaction Information...");
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
                        JSONArray dataArary= response.getJSONArray("transactions");



                        for(int i=0;i<dataArary.length();i++)
                        {
                            JSONObject sectionObj= (JSONObject) dataArary.get(i);

                            String subTitle= sectionObj.getString("transaction_type");

                            List<TransactionDetails> subSections= new ArrayList<>();


                            JSONArray sectionsArray=sectionObj.getJSONArray("customers");



                            for(int j=0;j<sectionsArray.length();j++)
                            {

                                JSONObject obj= (JSONObject) sectionsArray.get(j);

                                TransactionDetails subSection = new TransactionDetails();
                                subSection.setAccount_name(obj.getString("account_name"));

                                subSection.setAccount_number("Account No.: "+obj.getString("account_number"));
                                subSection.setCustomerImage("http://192.168.8.9/paperless/iconTrans.png");

                                //Dialog Setting Items
                                subSection.setTransaction_idDiaog(obj.getString("transaction_id"));
                                subSection.setTransaction_dateDiaog(obj.getString("transaction_date"));
                                subSection.setAccount_numberDiaog(obj.getString("account_number"));
                                subSection.setTransaction_typeDiaog(obj.getString("transaction_type"));
                                subSection.setAmountDiaog(obj.getString("amount"));
                                subSection.setChannelDiaog(obj.getString("channel"));
                                subSection.setBranchDiaog(obj.getString("branch"));

                                subSections.add(subSection);

                            }

                            DataTransaction data= new DataTransaction();
                            data.setTitle(subTitle);
                            data.setSection(subSections);


                            allSubSectionData.add(data);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TransactionPage.this,"Parsing Error",Toast.LENGTH_SHORT).show();
                    }


                    if(allSubSectionData!=null) {
                        //initCollapsingToolbar();

                        RecyclerView my_recycler_view =  findViewById(R.id.my_recycler_view_transaction_list);

                        my_recycler_view.setHasFixedSize(true);

                        RecyclerViewTransactionsAdapter adapter = new RecyclerViewTransactionsAdapter(TransactionPage.this,allSubSectionData);

                        my_recycler_view.setLayoutManager(new LinearLayoutManager(TransactionPage.this, LinearLayoutManager.VERTICAL, false));

                        my_recycler_view.setAdapter(adapter);

                    }

                }
                else {
                    Toast.makeText(TransactionPage.this,"Connection Error",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dismissProgressDialog();
                Toast.makeText(TransactionPage.this,"Connection Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dismissProgressDialog();
                Toast.makeText(TransactionPage.this,"Connection Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transaction_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /**
         * Handle action bar item clicks here. The action bar will
         automatically handle clicks on the Home/Up button, so long
         as you specify a parent activity in AndroidManifest.xml.
         * */

        int id = item.getItemId();

        /**
         * Noinspection SimplifiableIfStatement
         **/


        if (id == R.id.pageRefresh) {
            finish();
            startActivity(getIntent());

        }

        return super.onOptionsItemSelected(item);
    }

}
