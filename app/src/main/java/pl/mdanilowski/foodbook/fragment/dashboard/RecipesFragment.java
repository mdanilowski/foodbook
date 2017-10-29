package pl.mdanilowski.foodbook.fragment.dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.UnknownHostException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.addRecipe.AddRecipeActivity;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.RecipesAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.service.FoodBookService;
import pl.mdanilowski.foodbook.utils.InformationDialog;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RecipesFragment extends Fragment {

    CompositeSubscription compositeSubscription;

    @Inject
    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    @BindView(R.id.rvRecipesRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.pbRecipes)
    ProgressBar pbRecipes;

    @BindView(R.id.fabAddRecipe)
    FloatingActionButton fabAddRecipe;

    RecipesAdapter recipesAdapter;
    FoodBookService foodBookService = new FoodBookService();

    private OnFragmentInteractionListener onFragmentInteractionListener;

    public RecipesFragment() {
    }

    public static RecipesFragment newInstance() {
        RecipesFragment fragment = new RecipesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        firebaseUser = firebaseAuth.getCurrentUser();
        compositeSubscription = new CompositeSubscription();
        recipesAdapter = new RecipesAdapter(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, viewGroup);
        pbRecipes.setVisibility(View.GONE);
        fabAddRecipe.setOnClickListener(__ -> AddRecipeActivity.start(getActivity()));
        return viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recipesAdapter);
        compositeSubscription.add(observeUserRecipes());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeSubscription.remove(observeUserRecipes());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
        compositeSubscription.clear();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    Subscription observeUserRecipes() {
        return getUsersRecipes(firebaseUser.getUid())
                .subscribe(documentSnapshot -> {
                    recipesAdapter.addRecipe(documentSnapshot);
                    pbRecipes.setVisibility(View.GONE);
                    new Handler().postDelayed(() -> recyclerView.smoothScrollToPosition(0), 1);
                }, throwable -> {
                    if (throwable instanceof UnknownHostException) {
                        InformationDialog informationDialog = InformationDialog.newInstance("Failed loading data", "Check network connection and try again");
                        informationDialog.show(getChildFragmentManager(), "fragment_alert");
                    }
                    pbRecipes.setVisibility(View.GONE);
                });
    }

    Observable<Recipe> getUsersRecipes(String uid) {
        pbRecipes.setVisibility(View.VISIBLE);
        return foodBookService.getUsersRecipesRealtime(uid);
    }
}
