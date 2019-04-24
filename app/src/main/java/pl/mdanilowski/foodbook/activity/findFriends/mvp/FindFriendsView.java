package pl.mdanilowski.foodbook.activity.findFriends.mvp;


import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.findFriends.FindFriendsActivity;

public class FindFriendsView extends FrameLayout {

    @BindView(R.id.toolbarDefault)
    Toolbar toolbar;

    @BindView(R.id.rvFoundPeople)
    RecyclerView rvFoundPeople;

    public FindFriendsView(@NonNull FindFriendsActivity activity) {
        super(activity);
        inflate(activity, R.layout.activity_find_friends,this);
        ButterKnife.bind(this);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        setupRecyclerView(activity);
    }

    public void onCreateOptionsMenu(Menu menu, FindFriendsActivity activity){
        activity.getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.onActionViewExpanded();
        searchView.setMaxWidth(10000);
        SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(this.getContext(), FindFriendsActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
    }

    void setupRecyclerView(FindFriendsActivity activity){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvFoundPeople.setLayoutManager(linearLayoutManager);
        rvFoundPeople.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
    }

    void setAdapterForRecyclerView(RecyclerView.Adapter adapterForRecyclerView) {
        rvFoundPeople.setAdapter(adapterForRecyclerView);
    }
}
