package com.example.munf_project_v2;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

public class MediaService extends Service {

    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MediaBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnErrorListener((mediaPlayer,what,extra)->{
            // Error Handling: fehlt
            switch(what){
                default:
                    return false;
            }
        });


        mediaPlayer.setOnPreparedListener(mediaPlayer1 -> {
            // wenn Mediaplayer prepared ist:
            mediaPlayer.seekTo(0); // um auf Anfang des Audiofiles zu springen
            mediaPlayer.start();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release(); // alle resourcen befreien
        mediaPlayer = null;
    }

    // um mit dem Service nach außen zu kommunizieren:
    public class MediaBinder extends Binder{

        public boolean play(@RawRes int mediafile){
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getResources().openRawResourceFd(mediafile));
                mediaPlayer.prepareAsync();

                return true;
            } catch (IOException ex){
                return false;
            }
        }

    }
}
