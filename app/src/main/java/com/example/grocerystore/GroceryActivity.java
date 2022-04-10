package com.example.grocerystore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.grocerystore.GroceryItem;
import com.example.grocerystore.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class GroceryActivity extends AppCompatActivity implements AddReviewDialog.AddReview {
    private static final String TAG = "GroceryItemActivity";
    boolean isBound;    //Will be used to determine if the service to track the time of the user has completed that is, what the activity is destroyed
    private TrackUserTime mService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TrackUserTime.LocalBinder binder = (TrackUserTime.LocalBinder) iBinder;
            mService = binder.getService();    //now we are connected to our service
            isBound=true;
            mService.setItem(incomingItem);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound=false;
        }
    };
    @Override
    public void onAddReviewResult(Review  review) {     //The function will be executed here, so changes are visible in this activity
        Utils.addReview(this,review);
        Utils.changeUserPoint(this,incomingItem,3);
        ArrayList<Review> reviews = Utils.getReviewsByID(this, review.getGroceryItemId());
        if(null!=reviews){
            adapter.setReviews(reviews);
        }

    }

    public static final String GROCERY_ITEM_KEY = "incoming_item";

    private RecyclerView reviewsRecView;
    private TextView txtName, txtPrice, txtDescription, txtAddReview;
    private ImageView itemImage, firstEmptyStar, firstFilledStar, secondEmptyStar, secondFilledStar,
            thirdEmptyStar, thirdFilledStar;
    private Button btnAddToCart;
    private RelativeLayout firstStarRelLayout, secondStarRelLayout, thirdStarRelLayout;
    private MaterialToolbar toolbar;
    private GroceryItem incomingItem;
    private ReviewsAdapter adapter = new ReviewsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);
        initViews();
        setSupportActionBar(toolbar);
        adapter=new ReviewsAdapter();
        Intent intent = getIntent();
        if(intent!=null){
            incomingItem=intent.getParcelableExtra(GROCERY_ITEM_KEY);
            if(null!=incomingItem){
                Utils.changeUserPoint(this,incomingItem,1);
                txtName.setText(incomingItem.getName());
                txtDescription.setText(incomingItem.getDesc());
                txtPrice.setText(String.valueOf(incomingItem.getPrice())+" $");
                Glide.with(this).asBitmap().load(incomingItem.getImageUrl()).into(itemImage);
                ArrayList<Review> reviews = Utils.getReviewsByID(this,incomingItem.getId());
                reviewsRecView.setAdapter(adapter);
                reviewsRecView.setLayoutManager(new LinearLayoutManager(this));
                if(reviews!=null){
                    if(reviews.size()>0){
                        adapter.setReviews(reviews);
                    }
                }
                btnAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.addItemToCart(GroceryActivity.this,incomingItem);
                        Toast.makeText(GroceryActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    }
                });
                txtAddReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddReviewDialog dialog = new AddReviewDialog();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(GROCERY_ITEM_KEY,incomingItem);
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(),"Add a Review");
                    }
                });
                handleRating();
            }

        }
    }
    private void handleRating(){
        switch(incomingItem.getRating()){
            case 0:
                firstEmptyStar.setVisibility(View.VISIBLE);
                firstFilledStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 1:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 2:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 3:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        firstStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(incomingItem.getRating()!=1){
                    Utils.changeRating(GroceryActivity.this,incomingItem.getId(),1);
                    Utils.changeUserPoint(GroceryActivity.this,incomingItem,(1-incomingItem.getRating())*2);
                    incomingItem.setRating(1);
                    handleRating();
                }
            }
        });
        secondStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(incomingItem.getRating()!=2){
                    Utils.changeRating(GroceryActivity.this,incomingItem.getId(),2);
                    Utils.changeUserPoint(GroceryActivity.this,incomingItem,(2-incomingItem.getRating())*2);
                    incomingItem.setRating(2);
                    handleRating();
                }
            }
        });
        thirdStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(incomingItem.getRating()!=3){
                    Utils.changeRating(GroceryActivity.this,incomingItem.getId(),3);
                    Utils.changeUserPoint(GroceryActivity.this,incomingItem,(3-incomingItem.getRating())*2);
                    incomingItem.setRating(3);
                    handleRating();
                }
            }
        });

    }

    private void initViews() {
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDesc);
        txtPrice = findViewById(R.id.txtPrice);
        txtAddReview = findViewById(R.id.txtAddReview);
        itemImage = findViewById(R.id.itemImage);
        firstEmptyStar = findViewById(R.id.firstEmptyStar);
        firstFilledStar = findViewById(R.id.firstFilledStar);
        secondEmptyStar = findViewById(R.id.secondEmptyStar);
        secondFilledStar = findViewById(R.id.secondFilledStar);
        thirdEmptyStar = findViewById(R.id.thirdEmptyStar);
        thirdFilledStar = findViewById(R.id.thirdFilledStar);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        reviewsRecView = findViewById(R.id.reviewsRecyclerView);
        firstStarRelLayout = findViewById(R.id.firstStarRelLayout);
        secondStarRelLayout = findViewById(R.id.secondStarRelLayout);
        thirdStarRelLayout = findViewById(R.id.ThirdStarRelLayout);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,TrackUserTime.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBound){
            unbindService(connection);
        }
    }
}