package com.enovatesoft.paperless;

import android.os.Bundle;
import android.support.v7.cardview.*;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.enovatesoft.paperless.adapters.RecyclerViewDataAdapter;
import com.enovatesoft.paperless.models.Data;
import com.enovatesoft.paperless.models.Section;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class NewCustomer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //CardView cardView;
    private ProgressDialog pDialog;
    private ImageView imageView;


    List<Data> allSampleData;


    private String TEST_URL="http://192.168.8.9/dynamic.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        //cardView = findViewById(R.id.card_view);


        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profilepic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.itemImage);


        //setSupportActionBar(toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("Paperless");

        }
        //initCollapsingToolbar();

        allSampleData = new ArrayList<Data>();

        testWebService(TEST_URL);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void showProgressDialog() {

        pDialog = new ProgressDialog(NewCustomer.this);
        pDialog.setMessage("Loading New Customers...");
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

                            String title= sectionObj.getString("title");


                            List<Section> sections= new ArrayList<Section>();


                            JSONArray sectionsArray=sectionObj.getJSONArray("section");

                            for(int j=0;j<sectionsArray.length();j++)
                            {

                                final JSONObject obj= (JSONObject) sectionsArray.get(j);


                                Section section = new Section();
                                String id = obj.getString("rim");
                                String name = obj.getString("name");
                                String image = obj.getString("image");

                                section.setName(name);
                                section.setuId("Client ID: "+id);
                                section.setImage(image);

                                sections.add(section);
                            }

                            Data data= new Data();
                            data.setTitle(title);
                            data.setSection(sections);

                            allSampleData.add(data);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(NewCustomer.this,"Parsing Error",Toast.LENGTH_SHORT).show();
                    }


                    // Converst json to Object Model data
                   /* Gson gson = new Gson();
                    Type collectionType = new TypeToken<Data>() {
                    }.getType();
                    allSampleData = gson.fromJson(response.toString(), collectionType);*/


                    // setting data to RecyclerView

                    if(allSampleData!=null) {
                        //initCollapsingToolbar();

                        RecyclerView my_recycler_view =  findViewById(R.id.my_recycler_view);

                        my_recycler_view.setHasFixedSize(true);

                        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(NewCustomer.this,allSampleData);

                        my_recycler_view.setLayoutManager(new LinearLayoutManager(NewCustomer.this, LinearLayoutManager.VERTICAL, false));

                        my_recycler_view.setAdapter(adapter);

                    }




                }
                else {
                    Toast.makeText(NewCustomer.this,"Connection Error",Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dismissProgressDialog();
                Toast.makeText(NewCustomer.this,"Connection Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dismissProgressDialog();
                Toast.makeText(NewCustomer.this,"Connection Error",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_customer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this,"Still Under Development", Toast.LENGTH_LONG).show();

            return true;
        }

        if (id == R.id.action_refresh) {
            finish();
            startActivity(getIntent());
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_customers) {

            Intent newCustomer = new Intent(this, NewCustomer.class);
            startActivity(newCustomer);

        } else if (id == R.id.nav_new_transactions) {

        } else if (id == R.id.nav_existing_customers) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
