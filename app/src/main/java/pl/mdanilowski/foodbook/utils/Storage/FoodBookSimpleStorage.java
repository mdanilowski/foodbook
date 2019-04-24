package pl.mdanilowski.foodbook.utils.Storage;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.utils.Constants;

public class FoodBookSimpleStorage {

    private static FoodBookSimpleStorage foodBookSimpleStorage = null;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String _USER = "user";
    private static final String _LIKED_RECIPES = "liked_recipes";
    private static final String _DEEP_LINK = "deep_link";

    @SuppressLint("CommitPrefEdits")
    private FoodBookSimpleStorage() {
        this.sharedPreferences = App.getApplicationInstance().getSharedPreferences(Constants.FOOD_BOOK_PREFERENCES, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public static FoodBookSimpleStorage getInstance() {
        if (foodBookSimpleStorage == null) {
            return new FoodBookSimpleStorage();
        } else return foodBookSimpleStorage;
    }

    public void saveUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(_USER, json);
        editor.commit();
    }

    public User getUser() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(_USER, null);
        return gson.fromJson(json, User.class);
    }

    public void saveDeepLink(Uri link) {
        if (link != null) {
            editor.putString(_DEEP_LINK, link.toString());
        } else editor.putString(_DEEP_LINK, null);
        editor.commit();
    }

    public Uri getPendingDeepLink() {
        String deepLink = sharedPreferences.getString(_DEEP_LINK, null);
        if (deepLink != null) {
            return Uri.parse(deepLink);
        } else {
            return null;
        }
    }

    public void saveLikedRecipes(Set<String> likedRecipes) {
        editor.putStringSet(_LIKED_RECIPES, likedRecipes);
        editor.commit();
    }

    public void addRecipeToLiked(String rid) {
        Set<String> set = sharedPreferences.getStringSet(_LIKED_RECIPES, null);
        if (set == null) {
            set = new HashSet<>();
        }
        if (set != null && !set.contains(rid)) {
            set.add(rid);
            editor.putStringSet(_LIKED_RECIPES, set);
            editor.commit();
        }
    }

    public void removeLikedRecipe(String rid) {
        Set<String> set = sharedPreferences.getStringSet(_LIKED_RECIPES, null);
        if (set != null) {
            set.remove(rid);
            editor.putStringSet(_LIKED_RECIPES, set);
            editor.commit();
        }
    }

    public Set<String> getUsersLikedRecipes() {
        return sharedPreferences.getStringSet(_LIKED_RECIPES, null);
    }
}
