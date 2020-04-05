package com.example.audiovideofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class AudioVideo extends AppCompatActivity implements View.OnClickListener{

    // UI element initialization

    private VideoView mVideoView;
    private Button btnPlayVideo, btnPlayMusic, btnPauseMusic;
    private SeekBar seekBarVolume, seekBarMusicControl;

    private MediaController mMediaController;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private int maxVolume, currentVolume;
    private Timer mTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiovideo);

        mVideoView = findViewById(R.id.videoView);
        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        btnPlayMusic = findViewById(R.id.btnPlayMusic);
        btnPauseMusic = findViewById(R.id.btnPauseMusic);

        seekBarVolume = findViewById(R.id.seekBarVolume);
        seekBarMusicControl = findViewById(R.id.seekBarMusicControl);
        mMediaController = new MediaController(this);



        //Volume control variables and controller are defined in this class
        volumeControl();

        //Music duration control using seekbar
        musicSeekDuration();

        btnPlayVideo.setOnClickListener(this);
        btnPlayMusic.setOnClickListener(this);
        btnPauseMusic.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnPlayVideo:
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.handwash);
                mVideoView.setMediaController(mMediaController);
                mVideoView.setVideoURI(uri);
                mMediaController.setAnchorView(mVideoView);
                mVideoView.start();
                break;
            case R.id.btnPlayMusic:
                mMediaPlayer.start();
                mTimer = new Timer();
                mTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        seekBarMusicControl.setProgress(mMediaPlayer.getCurrentPosition());
                    }
                },0,1000);
                break;
            case R.id.btnPauseMusic:
                mMediaPlayer.pause();
                mTimer.cancel();
                break;

        }


    }
    private void volumeControl(){
        mMediaPlayer = MediaPlayer.create(this, R.raw.sound);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBarVolume.setMax(maxVolume);
        seekBarVolume.setProgress(currentVolume);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    private void musicSeekDuration(){
        int musicDuration = mMediaPlayer.getDuration();
        int currentPosition = mMediaPlayer.getCurrentPosition();
        seekBarMusicControl.setMax(musicDuration);
        seekBarMusicControl.setProgress(currentPosition);


        seekBarMusicControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mMediaPlayer.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                    mMediaPlayer.pause();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    mMediaPlayer.start();
            }
        });





    }


}
