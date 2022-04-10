package com.example.grocerystore;

import static com.example.grocerystore.allCategoriesDialog.ALL_CATEGORIES;
import static com.example.grocerystore.allCategoriesDialog.CALLING_ACTIVITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
private DrawerLayout drawer;
private NavigationView navigationView;
private MaterialToolbar toolbar;
private NavController navController;
public BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        Utils.initSharedPreferences(this);
        setSupportActionBar(toolbar);

        NavController navController = Navigation.findNavController(this,  R.id.nav_host_fragment);  //in order to navigate using the bottom navigation view
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.drawer_closed, //setting up the drawer
                R.string.drawer_closed
        );

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.about:
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("About Us")
                                .setMessage("Developed by Mustafa Jawad")
                                .setPositiveButton("Visit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent websiteIntent = new Intent(MainActivity.this,WebsiteActivity.class);
                                        startActivity(websiteIntent);
                                    }
                                }).create().show();
                        break;
                    case R.id.legalTerms:
                        new AlertDialog.Builder(MainActivity.this).setTitle("Terms")
                                .setMessage("There are no terms, enjoy")
                                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).create().show();
                        break;
                    case R.id.licences:
                        LicencesDialog licencesDialog = new LicencesDialog();
                        licencesDialog.show(getSupportFragmentManager(),"licences");
                    default:
                        break;
                }
                return false;
            }
        });

    }
    void initViews(){
        bottomNavigationView = findViewById(R.id.bottomNavView);
        drawer=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigationView);
        toolbar=findViewById(R.id.toolbar);
    }

}