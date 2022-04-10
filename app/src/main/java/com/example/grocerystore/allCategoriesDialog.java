package com.example.grocerystore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
public class allCategoriesDialog extends DialogFragment {
    public interface GetCategory{
        void onGetCategoryResult(String category);    //The categories displayed will be be updated in the search fragment using this callback interface
    }
    private Context context;
    private GetCategory getCategory;
    public static final String ALL_CATEGORIES = "categories";
    public static final String CALLING_ACTIVITY = "calling_activity";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_all_categories,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(view);
        Bundle bundle = getArguments();
        if(null!=bundle){
            ArrayList<String> categories = bundle.getStringArrayList(ALL_CATEGORIES);
            if(null!=categories){
                ListView listView = view.findViewById(R.id.listView);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                        categories);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        getCategory = (GetCategory) getParentFragment();
                        try {
                            getCategory.onGetCategoryResult(categories.get(i));
                            dismiss();
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        }
        return builder.create();
    }
}
