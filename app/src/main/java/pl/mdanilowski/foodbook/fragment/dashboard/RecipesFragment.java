package pl.mdanilowski.foodbook.fragment.dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.RecipesAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.service.FoodBookService;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RecipesFragment extends Fragment {

    CompositeSubscription compositeSubscription;

    @Inject
    FirebaseFirestore db;

    @Inject
    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    @BindView(R.id.rvRecipesRecyclerView)
    RecyclerView recyclerView;

    RecipesAdapter recipesAdapter;
    ArrayList<Recipe> recipes = new ArrayList<>();
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, viewGroup);
        return viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recipesAdapter = new RecipesAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(recipesAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        compositeSubscription.add(observeUserRecipes());
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
                .subscribe(documentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : documentSnapshots)
                        recipes.add(documentSnapshot.toObject(Recipe.class));
                    recipesAdapter.setRecipes(recipes);
                }, Throwable::printStackTrace);
    }

    Observable<List<DocumentSnapshot>> getUsersRecipes(String uid) {
        return foodBookService.getUsersRecipes(uid);
    }
}
