package com.example.grocerystore;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class TrackUserTime extends Service {
    private int seconds =0;
    private boolean shouldFinish;
    private GroceryItem item;  //to save the incoming item
    private IBinder binder = new LocalBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {   //Responsible for binding the service to the client
        trackTime();  //Time will begin to be tracked when we are bound to our service
        return binder;
    }
    public class LocalBinder extends Binder{  //Binder extends IBinder interface. Class is public as it will be used from main activity
        TrackUserTime getService(){
            return TrackUserTime.this;
        }
    }
    private void trackTime(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!shouldFinish){
                    try {
                        Thread.sleep(1000);
                        seconds++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setItem(GroceryItem item) {
        this.item = item;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shouldFinish = true;
        int minutes = seconds/60;
        if(minutes!=0){
            if(null!=item){
                Utils.changeUserPoint(this,item,minutes);
            }

        }
    }
}
