package com.waterfairy.videoplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/7/20 16:49
 * @info:
 */
public class AudioPlayerView extends RelativeLayout implements PlayButton.OnPlayClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private SimpleDateFormat simpleDateFormat;
    //view
    private PlayButton playButton;
    private TextView mTVTime;
    private SeekBar mSeekBar;

    private MediaPlayer mediaPlayer;

    private String path;//
    private String totalTimeStr = "";

    private OnVideoPlayListener onPlayClickLitener;
    private OnPlayProgressListener onPlayProgressListener;

    private final int STATE_INIT = 0;
    private final int STATE_PLAYING = 1;
    private final int STATE_PAUSING = 2;


    private boolean isPreparing;
    private boolean autoPlay;

    private int seekTime;
    private int videoState;
    private int pauseTime;

    public AudioPlayerView(Context context) {
        this(context, null);
    }

    public AudioPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(LayoutInflater.from(context).inflate(R.layout.layout_play_audio, this, false));
        findView();
        initView();
    }

    public void dismissMaxButton() {
        LayoutParams layoutParams = (LayoutParams) mTVTime.getLayoutParams();
        layoutParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
        mTVTime.setPadding(0, 0, (int) (getContext().getResources().getDisplayMetrics().density * 10), 0);
    }

    private void initView() {
        playButton.setOnPlayClickListener(this);
        mSeekBar.setMax(100);
        mSeekBar.setProgress(0);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    private void findView() {
        playButton = findViewById(R.id.bt_play);

        mTVTime = findViewById(R.id.time);
        mSeekBar = findViewById(R.id.progress_bar);

    }


    public void setPath(String path) {
        this.path = path;
        if (autoPlay) {
            initVideo();
        }
    }

    @Override
    public void onPlayClick() {
        if (videoState == STATE_INIT) {
            initVideo();
        } else if (videoState == STATE_PLAYING) {
            pause();
        } else if (videoState == STATE_PAUSING) {
            play();
        }
    }

    private void initVideo() {
        if (!isPreparing) {
            isPreparing = true;
            if (!TextUtils.isEmpty(path) && new File(path).exists()) {
                try {
                    if (mediaPlayer == null) mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setOnCompletionListener(this);
                    mediaPlayer.setOnErrorListener(this);
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                    if (onPlayClickLitener != null) onPlayClickLitener.onError("播放器初始化失败");
                }
            } else {
                if (onPlayClickLitener != null) onPlayClickLitener.onError("文件不存在");
            }
        }
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mSeekBar.setMax(mediaPlayer.getDuration());
        mSeekBar.setProgress(0);
        isPreparing = false;
        if (seekTime != 0) {
            mediaPlayer.seekTo(seekTime);
            seekTime = 0;
        }
        play();
    }

    public void pause() {
        handler.removeMessages(1);
        if (videoState == STATE_PLAYING) {
            mediaPlayer.pause();
            videoState = STATE_PAUSING;
            playButton.setState(true);
        }
    }

    public void release() {
        handler.removeMessages(1);
        if (mediaPlayer != null) mediaPlayer.release();
        videoState = STATE_INIT;
    }

    public void play() {
        if (videoState != STATE_PLAYING) {
            mediaPlayer.start();
            videoState = STATE_PLAYING;
            playButton.setState(false);
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        handler.removeMessages(1);
        if (onPlayClickLitener != null) onPlayClickLitener.onPlayComplete();
        playButton.setState(true);
        mSeekBar.setProgress(0);
        mTVTime.setText(getTimeStr(0, 0));
        videoState = STATE_PAUSING;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        handler.removeMessages(1);
        if (onPlayClickLitener != null) onPlayClickLitener.onError("文件不存在");
        videoState = STATE_INIT;
        return false;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (mediaPlayer != null) {
                    mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    mTVTime.setText(getTimeStr(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration()));
                    if (onPlayProgressListener != null)
                        onPlayProgressListener.onPlayProgress(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        }
    };

    /**
     * 时间转换
     *
     * @param currentPosition
     * @param totalTime
     * @return
     */
    private String getTimeStr(int currentPosition, int totalTime) {
        if (simpleDateFormat == null) {
            if (totalTime > 60 * 60 * 1000) {
                simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            } else {
                simpleDateFormat = new SimpleDateFormat("mm:ss");
            }
            totalTimeStr = simpleDateFormat.format(new Date(totalTime));
        }
        return simpleDateFormat.format(new Date(currentPosition)) + "/" + totalTimeStr;
    }

    /**
     * 播放监听
     *
     * @param onPlayClickListener
     */
    public void setOnPlayListener(OnVideoPlayListener onPlayClickListener) {
        this.onPlayClickLitener = onPlayClickListener;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * seekBar¬跳到指定位置
     *
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (videoState != STATE_INIT)
            seek(seekBar.getProgress());
        else mSeekBar.setProgress(0);
    }

    /**
     * 跳到指定位置
     *
     * @param time
     */
    private void seek(int time) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(time);
        }
    }


    /**
     * 初始化播放开始时间
     *
     * @param seekTime
     */
    public void initSeekTime(int seekTime) {
        if (seekTime < 0) seekTime = 0;
        this.seekTime = seekTime;
    }

    /**
     * 自动播放
     *
     * @param autoPlay
     */
    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public void onResume() {
        if (pauseTime != 0) {
            seek(pauseTime);
            pauseTime = 0;
            play();
        }
    }

    public void onPause() {
        if (videoState == STATE_PLAYING) {
            pauseTime = mediaPlayer.getCurrentPosition();
            pause();
        } else {
            pauseTime = 0;
        }
    }

}
