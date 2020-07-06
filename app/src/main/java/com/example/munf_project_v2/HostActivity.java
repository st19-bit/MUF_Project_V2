package com.example.munf_project_v2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HostActivity extends AppCompatActivity {
    private MediaServiceConnection mediaServiceConnection = null;
    private MediaService.MediaBinder mediaBinder;
    private Button button_start;
    private Button button_stop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_start = findViewById(R.id.button_start);
        button_start.setOnClickListener(view -> {
            if (mediaBinder == null) return;
            mediaBinder.play(R.raw.start);
        });
        button_start.setEnabled(false);

        button_stop = findViewById(R.id.button_stop);
        button_stop.setOnClickListener(view -> {
            if (mediaBinder == null) return;
            mediaBinder.play(R.raw.stop);
        });
        button_stop.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaServiceConnection == null) {
            bindService(
                    new Intent(this, MediaService.class),
                    mediaServiceConnection = new MediaServiceConnection(),
                    BIND_AUTO_CREATE
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaServiceConnection != null) {
            unbindService(mediaServiceConnection);
            mediaServiceConnection = null;
        }
    }

    private final class MediaServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mediaBinder = (MediaService.MediaBinder) service;
            button_start.setEnabled(true);
            button_stop.setEnabled(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mediaBinder = null;
            button_start.setEnabled(false);
            button_stop.setEnabled(false);
        }
    }
}
