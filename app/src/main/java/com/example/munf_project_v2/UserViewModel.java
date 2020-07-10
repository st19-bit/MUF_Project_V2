package com.example.munf_project_v2;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;


import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class UserViewModel extends BaseViewModel {

    private Handler handler = new Handler(Looper.getMainLooper());
    private UserLiveData userLiveData = new UserLiveData();

    // Default Constructor:
    public UserViewModel(@NonNull Application application) {
        super(application);
    }
    // MutableLiveData erlaubt Daten der Livedata zu ändern

    public LiveData<Userdata> setUser (Userdata userdata){
        userLiveData.insertUser(userdata);

        return userLiveData;
    }

    public LiveData<Userdata> getUser(Userdata userdata){
        return getDatabase().getUserDao().getUser();
    }

    public LiveData<Userdata> userInserted() {

        return userLiveData;
    }

    public class UserLiveData extends LiveData<Userdata>{

        private AtomicBoolean active = new AtomicBoolean();

        public void insertUser(Userdata userdata){
            Runnable r = () -> {
                getDatabase().getUserDao().insert(userdata); // von BaseViewModel
                if(active.get()) {
                    handler.post(() -> {
                        setValue(userdata);
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
