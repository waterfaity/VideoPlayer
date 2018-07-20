package com.waterfairy.videoplayer;

import android.content.ContentUris;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

public class VideoPlayActivity extends AppCompatActivity implements OnBackClickListener, OnVideoPlayListener {

    public static final String EXTRA_PATH = "video_path";
    public static final String EXTRA_TITLE = "video_title";
    public static final String EXTRA_TIME = "video_seek_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String videoPath = getIntent().getStringExtra(EXTRA_PATH);
        int seekTime = getIntent().getIntExtra(EXTRA_TIME, 0);
        VideoPlayerView player = findViewById(R.id.player);
        player.setOnBackClickListener(this);
        player.setOnPlayListener(this);
        player.dismissMaxButton();
        player.setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        player.initSeekTime(seekTime);
        player.setAutoPlay(true);
        player.dismissControlView();
        player.setPath(videoPath);
    }


    @Override
    public void onBackClick() {
        finish();
    }

    @Override
    public void onError(String errMsg) {
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWorm() {

    }

    @Override
    public void onPlayComplete() {

    }

    @Override
    public void onPausePlay() {

    }

    @Override
    public void onStartPlay() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) return true;
        return super.onKeyDown(keyCode, event);
    }
}
