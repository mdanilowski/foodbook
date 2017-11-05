package pl.mdanilowski.foodbook.fragment.dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.SearchResultsAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.service.FoodBookService;
import pl.mdanilowski.foodbook.utils.Storage.FoodBookSimpleStorage;

public class SearchFragment extends Fragment {

    private OnFragmentInteractionListener onFragmentInteractionListener;
    public static final String USER = "user";
    AdapterClickListener listener;

    @Inject
    FoodBookService foodBookService;

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    FoodBookSimpleStorage foodBookSimpleStorage;

    @BindView(R.id.rvSearch)
    RecyclerView rvSearch;

    SearchResultsAdapter searchResultsAdapter;
    String queryText = "";

    public SearchFragment() {
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseAuth.getCurrentUser();
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
        searchResultsAdapter = new SearchResultsAdapter(this, user -> listener.onAdapterItemClick(user.getUid()));
        rvSearch.setLayoutManager(linearLayoutManager);
        rvSearch.setAdapter(searchResultsAdapter);
        return viewGroup;
    }

    @Override
    public void onResume() {
        super.onResume();
        queryText = ((DashboardActivity) getActivity()).getPresenter().getSearchQuery();
        if (!queryText.isEmpty()) {
            String[] queryStringTable = queryText.split(" ");
            if (foodBookSimpleStorage.getUser() != null)
                foodBookService.getUsersByNameAndSurename(queryStringTable).subscribe(users -> {
                    searchResultsAdapter.setUsers(users);
                }, Throwable::printStackTrace);
        }
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
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public interface AdapterClickListener {
        void onAdapterItemClick(String uid);
    }
}
