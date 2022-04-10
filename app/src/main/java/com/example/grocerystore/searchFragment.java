package com.example.grocerystore;
import static com.example.grocerystore.allCategoriesDialog.ALL_CATEGORIES;
import static com.example.grocerystore.allCategoriesDialog.CALLING_ACTIVITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class searchFragment extends Fragment implements allCategoriesDialog.GetCategory {
    @Override
    public void onGetCategoryResult(String category) {
        ArrayList<GroceryItem> groceryItems = Utils.getItemsByCategory(getActivity(),category);
        if(null!=groceryItems){
            adapter.setItems(groceryItems);
            increaseUserPoint(groceryItems);
        }
    }

    private MaterialToolbar toolbar;
    private EditText searchBox;
    private ImageView btnSearch;
    private TextView firstCat, secondCat, thirdCat, txtAllCategories;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private GroceryItemAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_search,container,false);
        initViews(view);
        adapter = new GroceryItemAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSearch();
            }
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                initSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ArrayList<String> categories = Utils.getCategories(getActivity());
        if (null != categories) {
            if (categories.size()>0) {
                if (categories.size() == 1) {
                    showCategories(categories, 1);
                }else if (categories.size() == 2) {
                    showCategories(categories, 2);
                }else {
                    showCategories(categories, 3);
                }
            }
        }
        txtAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allCategoriesDialog allCategoriesDialog = new allCategoriesDialog();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(ALL_CATEGORIES,Utils.getCategories(getActivity()));  //All the categories will be sent to the categories dialog
                allCategoriesDialog.setArguments(bundle);
                allCategoriesDialog.show(searchFragment.this.getChildFragmentManager(),"all categories dialog");
            }
        });
        return view;
    }
    private void increaseUserPoint(ArrayList<GroceryItem> items){
        for(GroceryItem i:items){
            Utils.changeUserPoint(getActivity(),i,1);
        }
    }

    private void showCategories(final ArrayList<String> categories, int i) {
        switch (i) {
            case 1:
                firstCat.setVisibility(View.VISIBLE);
                firstCat.setText(categories.get(0));
                secondCat.setVisibility(View.GONE);
                thirdCat.setVisibility(View.GONE);
                firstCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(getActivity(), categories.get(0));
                        if (null != items) {
                            adapter.setItems(items);
                        }
                    }
                });
                break;
            case 2:
                firstCat.setVisibility(View.VISIBLE);
                firstCat.setText(categories.get(0));
                secondCat.setVisibility(View.VISIBLE);
                secondCat.setText(categories.get(1));
                thirdCat.setVisibility(View.GONE);
                firstCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(getActivity(), categories.get(0));
                        if (null != items) {
                            adapter.setItems(items);
                        }
                    }
                });
                secondCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(getActivity(), categories.get(1));
                        if (null != items) {
                            adapter.setItems(items);
                        }
                    }
                });
                break;
            case 3:
                firstCat.setVisibility(View.VISIBLE);
                firstCat.setText(categories.get(0));
                secondCat.setVisibility(View.VISIBLE);
                secondCat.setText(categories.get(1));
                thirdCat.setVisibility(View.VISIBLE);
                thirdCat.setText(categories.get(2));
                firstCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(getActivity(), categories.get(0));
                        if (null != items) {
                            adapter.setItems(items);
                        }
                    }
                });
                secondCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(getActivity(), categories.get(1));
                        if (null != items) {
                            adapter.setItems(items);
                        }
                    }
                });
                thirdCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<GroceryItem> items = Utils.getItemsByCategory(getActivity(), categories.get(2));
                        if (null != items) {
                            adapter.setItems(items);
                        }
                    }
                });
                break;
            default:
                firstCat.setVisibility(View.GONE);
                secondCat.setVisibility(View.GONE);
                thirdCat.setVisibility(View.GONE);
                break;
        }
    }

    private void initSearch(){
        if (!searchBox.getText().toString().equals("")) {
            String name = searchBox.getText().toString();
            ArrayList<GroceryItem> items = Utils.searchForItems(getActivity(),name);
            if(items!=null){
                adapter.setItems(items);
                increaseUserPoint(items);
            }
        }
    }
    private void initViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        searchBox = view.findViewById(R.id.searchBox);
        btnSearch = view.findViewById(R.id.btnSearch);
        firstCat = view.findViewById(R.id.txtFirstCat);
        secondCat = view.findViewById(R.id.txtSecondCat);
        thirdCat = view.findViewById(R.id.txtThirdCat);
        txtAllCategories = view.findViewById(R.id.txtAllCategories);
        bottomNavigationView = view.findViewById(R.id.bottomNavView);
        recyclerView = view.findViewById(R.id.recyclerView);
    }
}
