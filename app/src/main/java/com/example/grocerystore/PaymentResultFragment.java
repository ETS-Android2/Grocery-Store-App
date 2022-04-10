package com.example.grocerystore;

import static com.example.grocerystore.secondCartFragment.ORDER_KEY;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class PaymentResultFragment extends Fragment {
    private TextView txtMessage;
    private Button btnHome;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_result,container,false);
        initViews(view);
        Bundle bundle = getArguments();   //getting the order information from the third cart fragment
        if(null!=bundle){
            String jsonOrder = bundle.getString(ORDER_KEY);
            if(null!=jsonOrder){
                Gson gson = new Gson();
                Type type = new TypeToken<Order>(){}.getType();
                Order order = gson.fromJson(jsonOrder,type);
                if(null!=order){
                    if(order.isSuccess()){
                        txtMessage.setText("Payment Successful");
                        Utils.clearCartItems(getActivity());
                        for(GroceryItem items:order.getItems()){
                            Utils.increasePopularityPoint(getActivity(),items,1);
                            Utils.changeUserPoint(getActivity(),items,4);
                        }
                    }else{
                        txtMessage.setText("Payment Failed");
                    }
                }
            }
        }
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavView);
                if(null!=bottomNavigationView){
                    bottomNavigationView.setSelectedItemId(R.id.mainFragment);  //Fix This
                }

                FragmentTransaction transaction = getParentFragment().getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.cartRelLayout,new MainFragment());
                transaction.commit();

            }
        });
        return view;
    }
    private void initViews(View view) {
        txtMessage = view.findViewById(R.id.txtMessage);
        btnHome = view.findViewById(R.id.btnHome);


    }
}
