package com.waterfairy.videoplayer.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.waterfairy.videoplayer.R;
import com.waterfairy.videoplayer.activity.AudioPlayActivity;
import com.waterfairy.videoplayer.listener.OnMediaPlayListener;
import com.waterfairy.videoplayer.listener.OnPlayProgressListener;
import com.waterfairy.videoplayer.widget.AudioPlayerView;
import com.waterfairy.videoplayer.widget.VideoPlayerView;

public class Test extends AppCompatActivity implements OnPlayProgressListener, OnMediaPlayListener {
    VideoPlayerView videoPlayerView;
    AudioPlayerView audioPlayer;
    private static final String TAG = "avPlayTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player_test);
        videoPlayerView = findViewById(R.id.video_player_view);
        videoPlayerView.setTitle("hhhhhhhhh");
        videoPlayerView.setOnPlayListener(this);

        videoPlayerView.setPath("/sdcard/test/video/video_test.mp4");
        videoPlayerView.setOnPlayProgressListener(this);
        audioPlayer = findViewById(R.id.audio_player);
        audioPlayer.setPath("/sdcard/test/audio/audio_test.mp3");
        audioPlayer.setOnPlayListener(this);
        audioPlayer.setOnPlayProgressListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPlayer.release();
        videoPlayerView.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoPlayerView != null) videoPlayerView.onPause();
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, AudioPlayActivity.class);
        intent.putExtra(AudioPlayActivity.EXTRA_TITLE, "hhh");
        intent.putExtra(AudioPlayActivity.EXTRA_PATH, "/sdcard/test/audio/audio_test.mp3");
        startActivity(intent);
    }

    @Override
    public void onPlayProgress(long current, long duration) {
        Log.i(TAG, "onPlayProgress: " + current + "/" + duration);
    }

    @Override
    public void onMediaError(String errMsg) {
        Log.i(TAG, "onMediaError: " + errMsg);
    }

    @Override
    public void onMediaPrepared() {
        Log.i(TAG, "onMediaPrepared: ");
    }

    @Override
    public void onMediaPlayComplete() {
        Log.i(TAG, "onMediaPlayComplete: ");
    }

    @Override
    public void onMediaPause() {
        Log.i(TAG, "onMediaPause: ");
    }

    @Override
    public void onMediaPlay() {
        Log.i(TAG, "onMediaPlay: ");

    }

    @Override
    public void onMediaRelease() {
        Log.i(TAG, "onMediaRelease: ");
    }
}
