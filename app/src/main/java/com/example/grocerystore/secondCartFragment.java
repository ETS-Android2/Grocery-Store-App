package com.example.grocerystore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;;

public class secondCartFragment extends Fragment {
    public static final String ORDER_KEY = "order_key";
    private EditText edtTxtAddress, edtTxtZipCode, edtTxtPhoneNumber, edtTxtEmail;
    private Button btnNext, btnBack;
    private TextView txtWarning;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_cart_fragment,container,false);
        initViews(view);
        Bundle bundle = getArguments();      //the arguments for this bundle will be received when we go back from the third cart fragment
        if(null!=bundle){
            String jsonOrder = bundle.getString(ORDER_KEY);
            if(null!=jsonOrder){
                Gson gson = new Gson();
                Type type = new TypeToken<Order>(){}.getType();
                Order order = gson.fromJson(jsonOrder,type);
                if(null!=order){
                    edtTxtAddress.setText(order.getAddress());
                    edtTxtPhoneNumber.setText(order.getPhoneNumber());
                    edtTxtZipCode.setText(order.getZipCode());
                    edtTxtEmail.setText(order.getEmail());
                }
            }
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction =getParentFragment().getChildFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in,R.anim.fade_out);;
                transaction.replace(R.id.cartRelLayout,new firstCartFragment());
                transaction.commit();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()){
                    txtWarning.setVisibility(View.GONE);
                    ArrayList<GroceryItem> cartItems = Utils.getCartItems(getActivity());
                    if(cartItems!=null){
                        Order order = new Order();
                        order.setItems(cartItems);
                        order.setAddress(edtTxtAddress.toString());
                        order.setPhoneNumber(edtTxtPhoneNumber.getText().toString());
                        order.setZipCode(edtTxtZipCode.getText().toString());
                        order.setEmail(edtTxtEmail.getText().toString());
                        order.setTotalPrice(calculateTotalPrice(cartItems));
                        Gson gson = new Gson();
                        String jsonOrder = gson.toJson(order);
                        Bundle bundle = new Bundle();
                        bundle.putString(ORDER_KEY,jsonOrder);  //This bundle will store the details of our order for our third cart fragment
                        thirdCatFragment thirdCatFragment = new thirdCatFragment();
                        thirdCatFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getParentFragment().getChildFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_in,R.anim.fade_out);;
                        fragmentTransaction.replace(R.id.cartRelLayout,thirdCatFragment);
                        fragmentTransaction.commit();

                    }
                }else{
                    txtWarning.setVisibility(View.VISIBLE);
                    txtWarning.setText("Please fill all the above fields");
                }
            }
        });
        return view;
    }
    private boolean validateData(){
        if (edtTxtAddress.getText().toString().equals("") || edtTxtPhoneNumber.getText().toString().equals("") ||
                edtTxtZipCode.getText().toString().equals("") || edtTxtEmail.getText().toString().equals("")) {
            return false;
        }

        return true;
    }
    private void initViews(View view) {
        edtTxtAddress = view.findViewById(R.id.edtTxtAddress);
        edtTxtZipCode = view.findViewById(R.id.edtTxtZipCode);
        edtTxtPhoneNumber = view.findViewById(R.id.edtTxtPhoneNumber);
        edtTxtEmail = view.findViewById(R.id.edtTxtEmail);
        btnNext = view.findViewById(R.id.btnNext);
        btnBack = view.findViewById(R.id.btnBack);
        txtWarning = view.findViewById(R.id.txtWarning);
    }
    private double calculateTotalPrice(ArrayList<GroceryItem> items){
        double price = 0;
        for (GroceryItem item:items){
            price+=item.getPrice();
        }
        price = Math.round((price*100.0)/100.0);
        return price;

    }
}
