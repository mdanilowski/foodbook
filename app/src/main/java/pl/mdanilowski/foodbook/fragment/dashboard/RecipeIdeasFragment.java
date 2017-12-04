package pl.mdanilowski.foodbook.fragment.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.mdanilowski.foodbook.R;

public class RecipeIdeasFragment extends Fragment {

    public RecipeIdeasFragment() {
    }

    public static RecipeIdeasFragment newInstance() {
        RecipeIdeasFragment fragment = new RecipeIdeasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_ideas, container, false);
    }
}
