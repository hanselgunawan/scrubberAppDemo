package com.hanseltritama.scrubberappdemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    AudioManager audioManager; // To communicate with Android audio system
    int mediaSecond;

    public void onPlay(View view) {
        mediaPlayer.start();
    }

    public void onPause(View view) {
        mediaPlayer.pause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView minuteTV = findViewById(R.id.minuteTextView);
        final TextView secondTV = findViewById(R.id.secondTextView);
        mediaPlayer =  MediaPlayer.create(this, R.raw.blinded_by_light);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        SeekBar volumeControl = findViewById(R.id.seekBar);
        final SeekBar scrubber = findViewById(R.id.seekBar2);
        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(currVolume);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scrubber.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 1000);

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        scrubber.setMax(mediaPlayer.getDuration());

        scrubber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Scrubber: ", Integer.toString(progress));
                mediaSecond = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Double minute = Math.floor(Math.floor(mediaSecond / 1000) / 60);
                Double second = Math.floor(mediaSecond / 1000) % 60;
                String minuteStr = "0" + Integer.toString(minute.intValue()) + ":";
                String secondStr = Integer.toString(second.intValue());
                if(second < 10) secondStr = "0" + secondStr;
                mediaPlayer.seekTo(mediaSecond);
                minuteTV.setText(minuteStr);
                secondTV.setText(secondStr);
            }
        });
    }
}