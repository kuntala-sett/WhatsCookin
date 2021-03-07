package com.project.whats_cookin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private HomepageAdapter mAdapter;
    private ArrayList<HomepageItems> mItemList;
    //private int[] imgDrawableList1 = new int[]{R.drawable.placeholder_food, R.drawable.placeholder_food, R.drawable.placeholder_food, R.drawable.placeholder_food,R.drawable.placeholder_food, R.drawable.placeholder_food, R.drawable.placeholder_food};
    private String[] nameList1 = new String[]{"Chicken","Fish","Eggs","Pineapple","Pasta","Yoghurt","Pumpkin"};
   // private int[] imgDrawableList2 = new int[]{R.drawable.apple, R.drawable.mango,R.drawable.straw, R.drawable.pineapple,R.drawable.orange,R.drawable.blue,R.drawable.water};
    private String[] nameList2 = new String[]{"Chinese","Italian" ,"Mexican","Indian","Japanese","Mediterranean","Desserts"};
    //private int[] imgDrawableList3 = new int[]{R.drawable.apple, R.drawable.mango,R.drawable.straw, R.drawable.pineapple,R.drawable.orange,R.drawable.blue,R.drawable.water};
    private String[] nameList3 = new String[]{"Keto","Paleo" ,"Vegan","Gluten Free","Salad","Rice","Shakes"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("");
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        setupViews();
    }

    private void setupViews(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyc_view_1);
        mItemList = getModelList(nameList1);
        setupRecyclerViews();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyc_view_2);
        mItemList = getModelList(nameList2);
        setupRecyclerViews();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyc_view_3);
        mItemList = getModelList(nameList3);
        setupRecyclerViews();
    }
    private void setupRecyclerViews(){
        mAdapter = new HomepageAdapter(mItemList, this);
        mRecyclerView.setAdapter(mAdapter);
        //for horizontal recyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
    }
    private ArrayList<HomepageItems> getModelList(String[] currentList) {
        ArrayList<HomepageItems> arrList = new ArrayList<HomepageItems>();

        for(String s : currentList){
            HomepageItems obj = new HomepageItems();
            obj.setName(s);
            obj.setImgDrawable(R.drawable.placeholder_food);
            arrList.add(obj);
        }
        return arrList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        MenuItem searchBar = menu.findItem(R.id.searchBar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchBar);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.search_view_hint));
        searchView.setBackgroundColor(getResources().getColor(R.color.transparent));
        searchView.setPadding(-50, 0, 0 , 0);
        searchView.setMaxWidth(Integer.MAX_VALUE);

//      TextView searchEditText = (TextView) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView.findViewById(id);
        searchEditText.setTextColor(getResources().getColor(R.color.rose));
        final Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.shippori_regular);
        searchEditText.setTypeface(typeface);
        searchEditText.setTextSize(14.0f);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading Recipes...");
                progressDialog.show();

                Log.d("Query", query);

                Call<RecipeResponse> call = RecipeClientInstance.getRecipes(query, MainActivity.this);
                call.enqueue(new Callback<RecipeResponse>() {
                    @Override
                    public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                        progressDialog.dismiss();
//                        Toast.makeText(MainActivity.this, "Success : " + response.body().getTotalResults() + " results fetched!", Toast.LENGTH_SHORT).show();
                        int total = (int)response.body().getTotalResults();
                        if(total == 0){
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle(getString(R.string.uh_oh));
                            alertDialog.setMessage(getString(R.string.not_found));
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }else {
                            Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                            //Ensure that the response class(and all its nested class, if any) implements Serializable
                            intent.putExtra("responseObject", response.body());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<RecipeResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle(getString(R.string.oops));
                        alertDialog.setMessage(getString(R.string.error));
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //when implementing filter(autocomplete)
                return true;
            }
        });
        return true;
    }

}