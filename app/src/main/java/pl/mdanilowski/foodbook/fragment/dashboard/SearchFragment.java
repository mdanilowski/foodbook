package pl.mdanilowski.foodbook.fragment.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.RecipesAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.service.FoodBookService;
import pl.mdanilowski.foodbook.utils.Storage.FoodBookSimpleStorage;

public class SearchFragment extends Fragment {

    public static final String USER = "user";
    AdapterClickListener listener;

    @Inject
    FoodBookService foodBookService;

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    FoodBookSimpleStorage foodBookSimpleStorage;

    @BindView(R.id.rvRecipes)
    RecyclerView rvRecipes;

    RecipesAdapter recipesAdapter;
    String queryText = "";

    public SearchFragment() {
    }

    public FoodBookService getFoodBookService() {
        return foodBookService;
    }

    public static SearchFragment newInstance(AdapterClickListener listener) {
        SearchFragment fragment = new SearchFragment();
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewGroup = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, viewGroup);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recipesAdapter = new RecipesAdapter(this, recipe -> RecipeDetailsActivity.start(getActivity(), firebaseAuth.getUid(), recipe.getRid()));
        rvRecipes.setLayoutManager(linearLayoutManager);
        rvRecipes.setAdapter(recipesAdapter);
        return viewGroup;
    }

    @Override
    public void onResume() {
        super.onResume();
        queryText = ((DashboardActivity) getActivity()).getPresenter().getSearchQuery();
        if (!queryText.isEmpty()) {
            if (foodBookSimpleStorage.getUser() != null)
//                foodBookService.getUsersByNameAndSurename(queryText)
//                        .subscribe(users -> {
//                            for (User u : users) {
//                                System.out.println(u.getName());
//                            }
//                            searchResultsAdapter.setUsers(users);
//                        }, Throwable::printStackTrace);
                foodBookService.getRecipesByWords(queryText).subscribe(recipes -> {
                   recipesAdapter.setRecipes(recipes);
                });
        }
    }

    public interface AdapterClickListener {
        void onAdapterItemClick(String uid);
    }
}
