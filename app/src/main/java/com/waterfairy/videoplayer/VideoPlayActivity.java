package com.waterfairy.videoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class VideoPlayActivity extends AppCompatActivity implements OnBackClickListener, OnVideoPlayListener {

    public static final String EXTRA_PATH = "video_path";
    public static final String EXTRA_TITLE = "video_title";
    public static final String EXTRA_TIME = "video_seek_time";

    private VideoPlayerView player;
    private String TAG = "videoPlay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        String videoPath = getIntent().getStringExtra(EXTRA_PATH);
        int seekTime = getIntent().getIntExtra(EXTRA_TIME, 0);
        seekTime -= 2000;
        if (seekTime < 0) seekTime = 0;
        Log.i(TAG, "onCreate: " + seekTime);
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
        if (keyCode == KeyEvent.KEYCODE_BACK) return true;
        return super.onKeyDown(keyCode, event);
    }
}
