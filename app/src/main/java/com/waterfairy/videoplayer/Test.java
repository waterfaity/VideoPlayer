package com.waterfairy.videoplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        VideoPlayerView videoPlayerView = findViewById(R.id.video_player_view);
        videoPlayerView.setTitle("hhhhhhhhh");
        videoPlayerView.setPath("/sdcard/test/video/video_test.mp4");

        AudioPlayerView audioPlayer = findViewById(R.id.audio_player);
        audioPlayer.setPath("/sdcard/test/audio/audio_test.mp3");


    }

    public void onClick(View view) {
        Intent intent = new Intent(this, AudioPlayActivity.class);
        intent.putExtra(AudioPlayActivity.EXTRA_TITLE,"hhh");
        intent.putExtra(AudioPlayActivity.EXTRA_PATH,"/sdcard/test/audio/audio_test.mp3");
        startActivity(intent);

    }
}
