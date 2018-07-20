package com.waterfairy.videoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AudioPlayActivity extends AppCompatActivity implements View.OnClickListener, OnVideoPlayListener {
    public static final String EXTRA_PATH = "audio_path";
    public static final String EXTRA_TITLE = "audio_title";
    private AudioPlayerView audioPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);
        audioPlayerView = findViewById(R.id.audio_player);
        audioPlayerView.setAutoPlay(true);
        audioPlayerView.setOnPlayListener(this);
        audioPlayerView.setPath(getIntent().getStringExtra(EXTRA_PATH));


        initView();
    }

    private void initView() {
        findViewById(R.id.img_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText(getIntent().getStringExtra(EXTRA_TITLE));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back) {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) return true;
        return super.onKeyDown(keyCode, event);

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
    protected void onDestroy() {
        super.onDestroy();
        if (audioPlayerView != null) {
            audioPlayerView.release();
        }
    }
}
