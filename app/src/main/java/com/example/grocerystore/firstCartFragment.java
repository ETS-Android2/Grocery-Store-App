package com.example.grocerystore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class firstCartFragment extends Fragment implements CartAdapter.DeleteItem, CartAdapter.TotalPrice{
    @Override
    public void getTotalPrice(double d) {
        txtSum.setText(String.valueOf(d)+" $");
    }

    @Override
    public void onDeleteResult(GroceryItem item) {
        Utils.deleteItemFromCart(getActivity(),item);
        ArrayList<GroceryItem> cartItems = Utils.getCartItems(getActivity());
        if(null!=cartItems){
            if(cartItems.size()>0){
                imgNoItems.setVisibility(View.GONE);
                itemsRelLayout.setVisibility(View.VISIBLE);
                adapter.setItems(cartItems);
            }else{
                imgNoItems.setVisibility(View.VISIBLE);
                itemsRelLayout.setVisibility(View.GONE);
            }

        }else{
            imgNoItems.setVisibility(View.VISIBLE);
            itemsRelLayout.setVisibility(View.GONE);
        }
    }

    private RecyclerView recyclerView;
    private TextView txtSum;
    ImageView imgNoItems;
    private Button btnNext;
    private RelativeLayout itemsRelLayout;

    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_cart_fragment,container,false);
        initViews(view);
        adapter = new CartAdapter(getActivity(),this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<GroceryItem> cartItems = Utils.getCartItems(getActivity());
        if(null!=cartItems){
            if(cartItems.size()>0){
                imgNoItems.setVisibility(View.GONE);
                itemsRelLayout.setVisibility(View.VISIBLE);
                adapter.setItems(cartItems);
            }else{
                imgNoItems.setVisibility(View.VISIBLE);
                itemsRelLayout.setVisibility(View.GONE);
            }

        }else{
            imgNoItems.setVisibility(View.VISIBLE);
            itemsRelLayout.setVisibility(View.GONE);
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getParentFragment() != null;
                FragmentTransaction transaction = getParentFragment().getChildFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in,R.anim.fade_out);
                transaction.replace(R.id.cartRelLayout,new secondCartFragment());
                transaction.commit();
            }
        });
        return view;
    }
    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        txtSum = view.findViewById(R.id.txtTotalPrice);
        imgNoItems = view.findViewById(R.id.txtNoItem);
        btnNext = view.findViewById(R.id.btnNext);
        itemsRelLayout = view.findViewById(R.id.itemsRelLayout);
    }
}
