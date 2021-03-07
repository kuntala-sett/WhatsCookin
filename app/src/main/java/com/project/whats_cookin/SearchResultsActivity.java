package com.project.whats_cookin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Recipe> recipeList;
    private DataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();
        RecipeResponse response = (RecipeResponse)intent.getSerializableExtra("responseObject");
        actionBar.setTitle(response.getTotalResults() + " " + getString(R.string.result_found));

        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeList = new ArrayList<>(Arrays.asList(response.getRecipeResult()));
        mAdapter = new DataAdapter(recipeList);
        mRecyclerView.setAdapter(mAdapter);

    }
}