package com.example.grocerystore;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//the first, second, and third cart fragments will replace this fragment
public class cartFragment extends Fragment {
    Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_cart, container, false);
        initViews(view);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.cartRelLayout,new firstCartFragment());
        transaction.commit();
        return view;
    }
    private void initViews(View view){
        toolbar=view.findViewById(R.id.toolbar);
    }
}