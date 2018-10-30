package com.enovatesoft.paperless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransactionSearch extends AppCompatActivity {

    public class Transact {
        /*
        INSTANCE FIELDS
         */
        private int id;
        private String amount;
        private String name;
        private String imageURL;
        /*
        GETTERS AND SETTERS
         */

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }
        public void setAmount(String amount) {
            this.amount = amount;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getImageURL() {
            return imageURL;
        }
        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    class FilterHelper extends Filter {
        ArrayList<Transact> currentList;
        ListViewAdapter2 adapter;
        Context c;

        public FilterHelper(ArrayList<Transact> currentList, ListViewAdapter2 adapter,Context c) {
            this.currentList = currentList;
            this.adapter = adapter;
            this.c=c;
        }
        /*
        - Perform actual filtering.
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults=new FilterResults();

            if(constraint != null && constraint.length()>0)
            {
                //CHANGE TO UPPER
                constraint=constraint.toString().toUpperCase();

                //HOLD FILTERS WE FIND
                ArrayList<Transact> foundFilters=new ArrayList<>();

                Transact spacecraft=null;

                //ITERATE CURRENT LIST
                for (int i=0;i<currentList.size();i++)
                {
                    spacecraft= currentList.get(i);

                    //SEARCH
                    if(spacecraft.getName().toUpperCase().contains(constraint) )
                    {
                        //ADD IF FOUND
                        foundFilters.add(spacecraft);
                    }
                }

                //SET RESULTS TO FILTER LIST
                filterResults.count=foundFilters.size();
                filterResults.values=foundFilters;
            }else
            {
                //NO ITEM FOUND.LIST REMAINS INTACT
                filterResults.count=currentList.size();
                filterResults.values=currentList;
            }

            //RETURN RESULTS
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapter.setTransacts((ArrayList<Transact>) filterResults.values);
            adapter.refresh();
        }
    }

    public class ListViewAdapter2 extends BaseAdapter implements Filterable {

        Context c;
        ArrayList<Transact> transacts;
        public ArrayList<Transact> currentList;
        FilterHelper filterHelper;

        public ListViewAdapter2(Context c, ArrayList<Transact> transacts) {
            this.c = c;
            this.transacts = transacts;
            this.currentList=transacts;
        }
        @Override
        public int getCount() {
            return transacts.size();
        }
        @Override
        public Object getItem(int i) {
            return transacts.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view= LayoutInflater.from(c).inflate(R.layout.model_transactions,viewGroup,false);
            }

            TextView txtName = view.findViewById(R.id.nameTextView);
            TextView txtPropellant = view.findViewById(R.id.propellantTextView);
            ImageView spacecraftImageView = view.findViewById(R.id.spacecraftImageView);

            final Transact s= (Transact) this.getItem(i);

            txtName.setText(s.getName());
            txtPropellant.setText(s.getAmount());


            if(s.getImageURL() != null && s.getImageURL().length()>0)
            {
                Picasso.get().load(s.getImageURL()).placeholder(R.drawable.icon_trans).into(spacecraftImageView);
            }else {
                //Toast.makeText(c, "Empty Image URL", Toast.LENGTH_LONG).show();
                Picasso.get().load(R.drawable.icon_trans).into(spacecraftImageView);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(c, s.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }
        public void setTransacts(ArrayList<Transact> filteredSpacecrafts)
        {
            this.transacts=filteredSpacecrafts;

        }
        @Override
        public Filter getFilter() {
            if(filterHelper==null)
            {
                filterHelper=new FilterHelper(currentList,this,c);
            }

            return filterHelper;
        }
        public void refresh(){
            notifyDataSetChanged();
        }
    }

    /*
    Our HTTP Client
     */
    public class JSONDownloader {

        //SAVE/RETRIEVE URLS
        private static final String JSON_DATA_URL="http://192.168.8.9/paperless/transaction_search.php";
        //INSTANCE FIELDS
        private final Context c;

        public JSONDownloader(Context c) {
            this.c = c;
        }
        /*
        Fetch JSON Data
         */
        public ArrayList<Transact> retrieve(final ListView mListView, final ProgressBar myProgressBar)
        {
            final ArrayList<Transact> downloadedData=new ArrayList<>();
            myProgressBar.setIndeterminate(true);
            myProgressBar.setVisibility(View.VISIBLE);

            AndroidNetworking.get(JSON_DATA_URL)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject jo;
                            Transact s;
                            try
                            {
                                for(int i=0;i<response.length();i++)
                                {
                                    jo=response.getJSONObject(i);

                                    int id=jo.getInt("transaction_id");
                                    String name=jo.getString("account_name");
                                    String amount=jo.getString("amount");
                                    String imageURL=("http://192.168.8.9/paperless/iconTrans.png");

                                    s=new Transact();
                                    s.setId(id);
                                    s.setName(name);
                                    s.setAmount(amount);
                                    s.setImageURL(imageURL);

                                    downloadedData.add(s);
                                }
                                myProgressBar.setVisibility(View.GONE);

                            }catch (JSONException e)
                            {
                                myProgressBar.setVisibility(View.GONE);
                                Toast.makeText(c, "GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIEVED. "+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            anError.printStackTrace();
                            myProgressBar.setVisibility(View.GONE);
                            Toast.makeText(c, "UNSUCCESSFUL :  ERROR IS : "+anError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            return downloadedData;
        }
    }

    ArrayList<Transact> transacts = new ArrayList<>();
    ListView myListView;
    ListViewAdapter2 adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_search);

        myListView= findViewById(R.id.myListView);
        final ProgressBar myProgressBar= findViewById(R.id.myProgressBar);
        SearchView mySearchView=findViewById(R.id.mySearchView);

        mySearchView.setIconified(true);
        mySearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });

        transacts=new JSONDownloader(TransactionSearch.this).retrieve(myListView,myProgressBar);
        adapter=new ListViewAdapter2(this,transacts);
        myListView.setAdapter(adapter);
    }
}
