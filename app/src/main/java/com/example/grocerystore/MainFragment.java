package com.example.grocerystore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainFragment extends Fragment {
    private RecyclerView newItemsRecView, popularItemsRecView, suggestedItemsRecView;
    private GroceryItemAdapter newItemsAdapter, popularItemsAdapter, suggestedItemsAdapter;

Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onResume() {
        initRecViews();
        super.onResume();
    }

    void initViews(View view) {


        newItemsRecView = view.findViewById(R.id.newItemsRecView);
        popularItemsRecView = view.findViewById(R.id.popularItemRecView);
        suggestedItemsRecView = view.findViewById(R.id.suggestedItemsRecView);
        //bottomNavigationView = view.findViewById(R.id.bottomNavView);
    }

    void initRecViews() {
        newItemsAdapter = new GroceryItemAdapter(getActivity());
        newItemsRecView.setAdapter(newItemsAdapter);
        newItemsRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        popularItemsAdapter = new GroceryItemAdapter(getActivity());
        popularItemsRecView.setAdapter(popularItemsAdapter);
        popularItemsRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        suggestedItemsAdapter = new GroceryItemAdapter(getActivity());
        suggestedItemsRecView.setAdapter(suggestedItemsAdapter);
        suggestedItemsRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        ArrayList<GroceryItem> newItems = Utils.getAllItems(getActivity());
        if (newItems != null) {
            Comparator<GroceryItem> newItemsComparator = new Comparator<GroceryItem>() {  //using the comparator library in java to sort the various items
                @Override
                public int compare(GroceryItem groceryItem, GroceryItem t1) {

                    return groceryItem.getId() - t1.getId();
                }
            };
            Comparator<GroceryItem> reverseComparator = Collections.reverseOrder(newItemsComparator);
            Collections.sort(newItems, reverseComparator);
            newItemsAdapter.setItems(newItems);
        }
        ArrayList<GroceryItem> popularItems = Utils.getAllItems(getActivity());
        if (null != popularItems) {
            Comparator<GroceryItem> popularComparator = new Comparator<GroceryItem>() {
                @Override
                public int compare(GroceryItem groceryItem, GroceryItem t1) {
                    return groceryItem.getPopularityPoint() - t1.getPopularityPoint();
                }
            };
            Collections.sort(popularItems, Collections.reverseOrder(popularComparator));
            popularItemsAdapter.setItems(popularItems);
        }
        ArrayList<GroceryItem> suggestedItems = Utils.getAllItems(getActivity());
        if (null != suggestedItems) {
            Comparator<GroceryItem> suggestedComparator = new Comparator<GroceryItem>() {
                @Override
                public int compare(GroceryItem groceryItem, GroceryItem t1) {
                    return groceryItem.getUserPoint() - t1.getUserPoint();
                }
            };
            Collections.sort(suggestedItems, Collections.reverseOrder(suggestedComparator));
            suggestedItemsAdapter.setItems(suggestedItems);
        }

    }
}
