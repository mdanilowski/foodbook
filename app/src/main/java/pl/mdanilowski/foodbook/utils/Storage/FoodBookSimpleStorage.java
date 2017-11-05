package pl.mdanilowski.foodbook.utils.Storage;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.utils.Constants;

public class FoodBookSimpleStorage {

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String _USER = "user";

    @SuppressLint("CommitPrefEdits")
    public FoodBookSimpleStorage() {
        this.sharedPreferences = App.getApplicationInstance().getSharedPreferences(Constants.FOOD_BOOK_PREFERENCES, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
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
}
