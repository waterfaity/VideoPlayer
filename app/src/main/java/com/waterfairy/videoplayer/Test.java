package com.waterfairy.videoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        VideoPlayerView videoPlayerView = findViewById(R.id.video_player_view);
        videoPlayerView.setTitle("hhhhhhhhh");
        videoPlayerView.setPath("/sdcard/test/video/video_test.mp4");

    }
}
