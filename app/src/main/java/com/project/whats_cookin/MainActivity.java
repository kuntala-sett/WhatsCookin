package com.project.whats_cookin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuItemCompat;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("");
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
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