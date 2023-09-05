package com.example.bottom_navigationbar_view;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

    private static SoundManager instance;
    private final SoundPool soundPool;
    protected boolean sound_;
    private final int soundButton1;
//    private final int soundButton2;

    private SoundManager(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();

        soundButton1 = soundPool.load(context, R.raw.sound_button_2, 1);
//        soundButton2 = soundPool.load(context, R.raw.sound_button2, 1);
    }

    public static SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }

    // Enable sound
    public void enableSound() {
        this.sound_ = true;
    }

    // Disable sound
    public void disableSound() {
        this.sound_ = false;
    }

    // Check if sound is enabled
    public boolean isSoundEnabled() {
        return this.sound_;
    }

    public void playButton1Sound() {
        soundPool.play(soundButton1, 1.0f, 1.0f, 1, 0, 1.0f);
    }

//    public void playButton2Sound() {
//        soundPool.play(soundButton2, 1.0f, 1.0f, 1, 0, 1.0f);
//    }
}
