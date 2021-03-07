package com.project.whats_cookin;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecipeResponse implements Serializable {

    @SerializedName("results")
    private Recipe[] recipeResult;
    @SerializedName("totalResults")
    private int totalResults;

    public Recipe[] getRecipeResult() {
        return recipeResult;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
