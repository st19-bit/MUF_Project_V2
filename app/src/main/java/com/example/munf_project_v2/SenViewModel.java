package com.example.munf_project_v2;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.concurrent.atomic.AtomicBoolean;

public class SenViewModel extends BaseViewModel{

    private Handler handler = new Handler(Looper.getMainLooper());
    private AccelerationLiveData accelerationLiveData = new AccelerationLiveData();
    public SenViewModel(@NonNull Application application) {super(application);}

    final LiveData<Depot> setSensor(Depot depot){
        accelerationLiveData.insertSensor(depot);
        return accelerationLiveData;
    }

    public LiveData<Depot> getDepot(){
        return getDatabase().getSensorDao().getMessung();
    }

    public LiveData<Depot> messungInserted(){
        return accelerationLiveData;
    }

    public class AccelerationLiveData extends LiveData<Depot> {
        private AtomicBoolean active = new AtomicBoolean();

        public void insertSensor(Depot depot) {
            Runnable r = () -> {
                getDatabase().getSensorDao().insert(depot);
                if(active.get()) {
                    handler.post(() -> {
                        setValue(depot);
                    });
                }
            };
            Thread t = new Thread(r);
            t.start();
        }

        @Override
        protected void onActive() {
            active.set(true);
        }

        @Override
        protected void onInactive() {
            active.set(false);
        }
    }



}

