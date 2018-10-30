package com.enovatesoft.paperless;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.HashMap;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.enovatesoft.paperless.helper.SQLiteHandler;
import com.enovatesoft.paperless.helper.SessionManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.enovatesoft.paperless.adapters.RecyclerViewDataAdapter;
import com.enovatesoft.paperless.models.Data;
import com.enovatesoft.paperless.models.Section;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;


public class NewCustomer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //CardView cardView;
    private ProgressDialog pDialog;

    private TextView tvUserName, tvUserEmail;

    private SQLiteHandler db;
    private SessionManager session;

    private boolean fabExpanded = false;
    private FloatingActionButton fabSearch,fabNewCustomers, fabTransaction, fabExistingCustomers;
    private LinearLayout layoutFabNewCustomers;
    private LinearLayout layoutFabTransactions;
    private LinearLayout layoutFabExistingCustomers;
    private RelativeLayout obstrucuterView;

    List<Data> allSampleData;

    private String TEST_URL="http://192.168.8.9/dynamic.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);


        //setSupportActionBar(toolbar);
        if (toolbar != null) {
            //setSupportActionBar(toolbar);
            toolbar.setTitle("New Customers");



        }
        //initCollapsingToolbar();
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        allSampleData = new ArrayList<Data>();

        testWebService(TEST_URL);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        tvUserName = headerView.findViewById(R.id.tvUserName);
        tvUserEmail = headerView.findViewById(R.id.tvUserEmail);

        // Displaying the user details on the screen
        tvUserName.setText("Logged in as "+name);
        tvUserEmail.setText(email);
        obstrucuterView =findViewById(R.id.obstructor);


        fabSearch = this.findViewById(R.id.fabSearch);
        fabNewCustomers = this.findViewById(R.id.fabNewCustomers);
        fabTransaction = this.findViewById(R.id.fabTransaction);
        fabExistingCustomers = this.findViewById(R.id.fabExistingCustomers);

        layoutFabNewCustomers = this.findViewById(R.id.layoutFabNewCustomers);
        layoutFabTransactions =  this.findViewById(R.id.layoutFabTransactions);
        layoutFabExistingCustomers = this.findViewById(R.id.layoutFabExistingCustomers);

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });


        //Only main FAB is visible in the beginning
        closeSubMenusFab();

    }

    //closes FAB submenus
    private void closeSubMenusFab(){
        layoutFabNewCustomers.setVisibility(View.INVISIBLE);
        layoutFabTransactions.setVisibility(View.INVISIBLE);
        layoutFabExistingCustomers.setVisibility(View.INVISIBLE);
        fabSearch.setImageResource(R.drawable.search_white);
        fabExpanded = false;
        if (obstrucuterView.getVisibility() == View.VISIBLE)
            obstrucuterView.setVisibility(View.INVISIBLE);
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        layoutFabNewCustomers.setVisibility(View.VISIBLE);
        layoutFabTransactions.setVisibility(View.VISIBLE);
        layoutFabExistingCustomers.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fabSearch.setImageResource(R.drawable.ic_close);
        fabExpanded = true;
        if (obstrucuterView.getVisibility() == View.INVISIBLE)
            obstrucuterView.setVisibility(View.VISIBLE);


        fabNewCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent (NewCustomer.this, SearchActivity.class);
                startActivity(search);
            }
        });

        fabTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transacts = new Intent (NewCustomer.this, TransactionSearch.class);
                startActivity(transacts);

            }
        });

        fabExistingCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewCustomer.this,"Still Under Development...", Toast.LENGTH_SHORT).show();

            }
        });

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

                            List<Section> sections= new ArrayList<>();

                            JSONArray sectionsArray=sectionObj.getJSONArray("section");

                            for(int j=0;j<sectionsArray.length();j++)
                            {

                                final JSONObject obj= (JSONObject) sectionsArray.get(j);

                                Section section = new Section();
                                String id = obj.getString("rim");
                                String name = obj.getString("name");
                                String image = obj.getString("image");

                                section.setName(name);
                                section.setuId(id);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            NewCustomer.super.onBackPressed();
                        }
                    }).create().show();
        }
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        /** Launching the login activity
         *
         **/
        Intent intent = new Intent(NewCustomer.this, LoginActivity.class);
        startActivity(intent);
        finish();
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

        if (id == R.id.action_logout) {
            logoutUser();
        }

        if (id == R.id.action_search) {
            Intent search = new Intent(this, SearchActivity.class);
            startActivity(search);
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
            //Toast.makeText(NewCustomer.this, "Still Under Development", Toast.LENGTH_LONG).show();

            Intent transactions = new Intent(this, TransactionPage.class);
            startActivity(transactions);

        }else if (id == R.id.nav_conkev_web) {
            Intent qrcode = new Intent(this, ScanActivity.class);
            startActivity(qrcode);

        }else if (id == R.id.nav_share) {
            Intent about_us = new Intent(this, About.class);
            startActivity(about_us);

        } else if (id == R.id.nav_send) {
            Toast.makeText(NewCustomer.this, "Still Under Development", Toast.LENGTH_LONG).show();

        }else if (id==R.id.nav_logout){
            logoutUser();

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
