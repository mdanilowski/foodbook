package pl.mdanilowski.foodbook.fragment.dashboard;

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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;

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

    @Inject
    FoodBookService foodBookService;

    FirebaseUser firebaseUser;

    @BindView(R.id.rvRecipesRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.pbRecipes)
    ProgressBar pbRecipes;

    @BindView(R.id.fabAddRecipe)
    FloatingActionButton fabAddRecipe;

    RecipesAdapter recipesAdapter;
    OnAdapterItemClickListener listener;

    public RecipesFragment() {
    }

    public static RecipesFragment newInstance(OnAdapterItemClickListener listener) {
        RecipesFragment fragment = new RecipesFragment();
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        firebaseUser = firebaseAuth.getCurrentUser();
        compositeSubscription = new CompositeSubscription();
        recipesAdapter = new RecipesAdapter(this, recipe -> listener.onAdapterItemClick(recipe));
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
        pbRecipes.setVisibility(View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        compositeSubscription.clear();
    }

    Subscription observeUserRecipes() {
        return getUsersRecipes(firebaseUser.getUid())
                .subscribe(documentChange -> {
                    Recipe recipe = documentChange.getDocument().toObject(Recipe.class);
                    recipe.setRid(documentChange.getDocument().getId());
                    switch (documentChange.getType()) {
                        case ADDED:
                            recipesAdapter.addRecipe(recipe);
                            new Handler().postDelayed(() -> recyclerView.smoothScrollToPosition(0), 1);
                            foodBookService.getCommentsForRecipe(firebaseUser.getUid(), recipe.getRid())
                                    .subscribe(comments -> {
                                        recipe.setComments(comments);
                                        recipesAdapter.updateRecipe(recipe);
                                        new Handler().postDelayed(() -> recyclerView.smoothScrollToPosition(0), 1);
                                    }, e -> {
                                        Toast.makeText(this.getContext(), "Could not load comments", Toast.LENGTH_SHORT).show();
                                    });
                            break;
                        case MODIFIED:
                            recipesAdapter.updateRecipe(recipe);
                            break;
                        case REMOVED:
                            recipesAdapter.deleteRecipe(recipe);
                            break;
                    }
                }, throwable -> {
                    if (throwable instanceof UnknownHostException) {
                        InformationDialog informationDialog = InformationDialog.newInstance("Failed loading data", "Check network connection and try again");
                        informationDialog.show(getChildFragmentManager(), "fragment_alert");
                    }
                    pbRecipes.setVisibility(View.GONE);
                });
    }

    Observable<DocumentChange> getUsersRecipes(String uid) {
        return foodBookService.getUsersRecipesRealtime(uid);
    }


    public interface OnAdapterItemClickListener {
        void onAdapterItemClick(Recipe recipe);
    }
}
