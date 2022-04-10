package com.example.grocerystore;

import static com.example.grocerystore.secondCartFragment.ORDER_KEY;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class thirdCatFragment extends Fragment {

    private static final String TAG = "";
    private Button btnBack, btnCheckout;
    private TextView txtItems, txtAddress, txtPhoneNumber, txtTotalPrice;
    private RadioGroup rgPayment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_cart_fragment,container,false);
        initViews(view);
        Bundle bundle = getArguments();    //getting the order information from the second cart activity
        if(null!=bundle){
            String jsonOrder = bundle.getString(ORDER_KEY);
            if(null!=jsonOrder){
                Gson gson = new Gson();
                Type type = new TypeToken<Order>(){}.getType();
                Order order = gson.fromJson(jsonOrder,type);
                if(null!=order){
                    String items ="";
                    for (GroceryItem i: order.getItems()) {
                        items += "\n\t" + i.getName();
                    }

                    txtItems.setText(items);
                    txtAddress.setText(order.getAddress());
                    Log.d(TAG, "onCreateView:"+order.getAddress()) ;
                    txtPhoneNumber.setText(order.getPhoneNumber());
                    txtTotalPrice.setText(String.valueOf(order.getTotalPrice()));
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle backBundle = new Bundle();
                            backBundle.putString(ORDER_KEY,jsonOrder);
                            secondCartFragment secondCartFragment = new secondCartFragment();
                            secondCartFragment.setArguments(backBundle);
                            FragmentTransaction transaction = getParentFragment().getChildFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.slide_in,R.anim.fade_out);;
                            transaction.replace(R.id.cartRelLayout,secondCartFragment);
                            transaction.commit();
                        }
                    });
                    btnCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (rgPayment.getCheckedRadioButtonId()){
                                case R.id.rbPayPal:
                                    order.setPaymentMethod("PayPal");
                                    break;
                                case R.id.rbCreditCard:
                                    order.setPaymentMethod("CreditCard");
                                    break;
                                default:
                                    order.setPaymentMethod("Unknown");
                                    break;

                            }
                            order.setSuccess(true);     //these lines of code will send a fake payment request to json placeholder
                            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
                            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://jsonplaceholder.typicode.com/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .client(client)
                                    .build();
                            OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
                            Call<Order> call = endPoint.newOrder(order);
                            call.enqueue(new Callback<Order>() {
                                @Override
                                public void onResponse(Call<Order> call, Response<Order> response) {
                                    Log.d(TAG, "onResponse: response:"+response.code());
                                    if(response.isSuccessful()){
                                        Bundle resultBundle = new Bundle();
                                        resultBundle.putString(ORDER_KEY,gson.toJson(response.body()));
                                        PaymentResultFragment paymentResultFragment = new PaymentResultFragment();
                                        paymentResultFragment.setArguments(resultBundle);
                                        FragmentTransaction transaction = getParentFragment().getChildFragmentManager().beginTransaction();
                                        transaction.replace(R.id.cartRelLayout,paymentResultFragment);
                                        transaction.commit();


                                    }
                                }

                                @Override
                                public void onFailure(Call<Order> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }
                    });
                }

            }
        }
        return view;
    }
    private void initViews(View view) {
        rgPayment = view.findViewById(R.id.rgPaymentMethod);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        txtItems = view.findViewById(R.id.txtItems);
        txtTotalPrice = view.findViewById(R.id.txtPrice);
        btnBack = view.findViewById(R.id.btnBack);
        btnCheckout = view.findViewById(R.id.btnCheckout);
    }
}
