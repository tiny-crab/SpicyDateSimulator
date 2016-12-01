package com.kch.spicydatesimulator;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by evan on 11/30/2016.
 */

public class MediaService extends Service {
    private final IBinder mBinder = new LocalBinder();
    private MediaPlayer mPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = MediaPlayer.create(this, R.raw.main_theme);
        Log.d("Service", "Started");
        mPlayer.setLooping(true);
        mPlayer.start();
    }

    @Override
    public void onDestroy() {
        mPlayer.stop();
        mPlayer.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        MediaService getService() {
            return MediaService.this;
        }
    }
}
