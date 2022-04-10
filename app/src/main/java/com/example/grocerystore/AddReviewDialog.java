package com.example.grocerystore;

import static com.example.grocerystore.GroceryActivity.GROCERY_ITEM_KEY;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddReviewDialog extends DialogFragment {
    public interface AddReview{               //Setting up a callback interface so reviews are added from the grocery activity
        void onAddReviewResult(Review review);
    }
    private AddReview addReview;
    private TextView txtItemName, txtWarning;
    private EditText edtTxtUserName;
    private EditText edtTxtReview;
    private Button btnAddReview;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_review,null);
        initViews(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Bundle bundle = getArguments();
        if (null!=bundle){
            final GroceryItem item = bundle.getParcelable(GROCERY_ITEM_KEY);
            if (null!=item){
                txtItemName.setText(item.getName());
                btnAddReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtItemName.setText(item.getName());
                        String userName = edtTxtUserName.getText().toString();
                        String review = edtTxtReview.getText().toString();
                        String date = getCurrentDate();
                        if(userName.equals("")||review.equals("")){
                            txtWarning.setText("Fill all the blanks");
                            txtWarning.setVisibility(View.VISIBLE);
                        }else{
                            txtWarning.setVisibility(View.GONE);
                            try {
                                addReview=(AddReview) getActivity();
                                addReview.onAddReviewResult(new Review(item.getId(),userName,review,date));  //calling the callBack interface
                                dismiss();
                            }catch (ClassCastException e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
        return builder.create();
    }
    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        return sdf.format(calendar.getTime());
    }
    private void initViews(View view) {
        txtItemName = view.findViewById(R.id.txtItemName);
        txtWarning = view.findViewById(R.id.txtWarning);
        edtTxtUserName = view.findViewById(R.id.edtTxtUserName);
        edtTxtReview = view.findViewById(R.id.edtTxtReview);
        btnAddReview = view.findViewById(R.id.btnAddReview);
    }
}
