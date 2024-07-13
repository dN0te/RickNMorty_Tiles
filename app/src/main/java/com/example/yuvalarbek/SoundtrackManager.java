package com.example.yuvalarbek;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundtrackManager {
    private static SoundtrackManager instance;
    private MediaPlayer mediaPlayer;

    private SoundtrackManager() {
        // Private constructor to prevent instantiation
    }

    public static synchronized SoundtrackManager getInstance() {
        if (instance == null) {
            instance = new SoundtrackManager();
        }
        return instance;
    }

    public void startSoundtrack(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.evilmorty_st);
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(0.4f, 0.4f); // Set volume to 70%
            mediaPlayer.start();
        }
    }

    public void stopSoundtrack() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
