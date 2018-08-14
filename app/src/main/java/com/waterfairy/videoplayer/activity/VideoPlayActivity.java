package com.waterfairy.videoplayer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.waterfairy.videoplayer.R;
import com.waterfairy.videoplayer.listener.OnBackClickListener;
import com.waterfairy.videoplayer.listener.OnMediaPlayListener;
import com.waterfairy.videoplayer.widget.VideoPlayerView;

public class VideoPlayActivity extends AppCompatActivity implements OnBackClickListener, OnMediaPlayListener {

    public static final String EXTRA_PATH = "video_path";
    public static final String EXTRA_TITLE = "video_title";
    public static final String EXTRA_TIME = "video_seek_time";

    private VideoPlayerView player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player_video_play);
        String videoPath = getIntent().getStringExtra(EXTRA_PATH);
        int seekTime = getIntent().getIntExtra(EXTRA_TIME, 0);
        seekTime -= 500;
        if (seekTime < 0) seekTime = 0;
        player = findViewById(R.id.player);
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
    public void onMediaError(String errMsg) {
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMediaPrepared() {

    }

    @Override
    public void onMediaPlayComplete() {

    }

    @Override
    public void onMediaPause() {

    }

    @Override
    public void onMediaPlay() {

    }

    @Override
    public void onMediaRelease() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        player.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }
}
