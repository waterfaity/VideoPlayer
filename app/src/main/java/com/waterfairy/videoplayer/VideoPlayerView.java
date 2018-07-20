package com.waterfairy.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/7/20 10:38
 * @info:
 */
public class VideoPlayerView extends RelativeLayout implements PlayButton.OnPlayClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private SimpleDateFormat simpleDateFormat;
    //view
    private VideoView videoView;
    private RelativeLayout mRLButton;
    private RelativeLayout mRLBack;
    private TextView mTVTitle;
    private PlayButton playButton;
    private ImageView mIVMaxWindow;
    private ImageView mIVBack;
    private TextView mTVTime;
    private SeekBar mSeekBar;
    private Toast toast;


    private MediaPlayer mediaPlayer;

    private String title;
    private String path;//
    private String totalTimeStr = "";

    private OnVideoPlayListener onPlayClickLitener;
    private OnClickMaxWindowListener onMaxWindowClickListener;
    private OnPlayProgressListener onPlayProgressListener;
    private OnBackClickListener onBackClickListener;

    private final int STATE_INIT = 0;
    private final int STATE_PLAYING = 1;
    private final int STATE_PAUSING = 2;


    private boolean showBack;
    private boolean isPreparing;
    private boolean autoPlay;

    private int seekTime;
    private int videoState;

    public VideoPlayerView(Context context) {
        this(context, null);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(LayoutInflater.from(context).inflate(R.layout.layout_video_play, this, false));
        findView();
        initView();
    }

    public void dismissMaxButton() {
        mIVMaxWindow.setVisibility(GONE);
        LayoutParams layoutParams = (LayoutParams) mTVTime.getLayoutParams();
        layoutParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
        mTVTime.setPadding(0, 0, (int) (getContext().getResources().getDisplayMetrics().density * 10), 0);
    }

    private void initView() {
        playButton.setOnPlayClickListener(this);
        toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        videoView.setOnClickListener(this);
        findViewById(R.id.bg_view).setOnClickListener(this);
        mIVMaxWindow.setOnClickListener(this);
        mIVBack.setOnClickListener(this);
        mSeekBar.setMax(100);
        mSeekBar.setProgress(0);
        mRLBack.setVisibility(GONE);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    private void findView() {
        playButton = findViewById(R.id.bt_play);
        videoView = findViewById(R.id.video_view);
        mRLButton = findViewById(R.id.rel_play);
        mIVMaxWindow = findViewById(R.id.img_max_window);
        mTVTime = findViewById(R.id.time);
        mSeekBar = findViewById(R.id.progress_bar);
        mRLBack = findViewById(R.id.rel_back);
        mIVBack = findViewById(R.id.img_back);
        mTVTitle = findViewById(R.id.tv_title);
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
                videoView.setVideoPath(path);
                videoView.setOnPreparedListener(this);
                videoView.setOnCompletionListener(this);
                videoView.setOnErrorListener(this);
                videoView.start();
            } else {
                if (onPlayClickLitener != null) onPlayClickLitener.onError("文件不存在");
            }
        }
    }


    private void showToast(String content) {
        if (toast != null) {
            toast.setText(content);
            toast.show();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer = mp;
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
            videoView.pause();
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
            videoView.start();
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


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.video_view || v.getId() == R.id.bg_view) {
            if (mRLButton.getVisibility() == GONE) {
                mRLButton.setVisibility(VISIBLE);
                if (showBack) {
                    mRLBack.setVisibility(VISIBLE);
                }
            } else {
                mRLBack.setVisibility(GONE);
                mRLButton.setVisibility(GONE);
            }
        } else if (R.id.img_max_window == v.getId()) {
            if (onMaxWindowClickListener != null) onMaxWindowClickListener.onMaxWindowClick();
            pause();
            Intent intent = new Intent(getContext(), VideoPlayActivity.class);
            intent.putExtra(VideoPlayActivity.EXTRA_PATH, path);
            if (mediaPlayer != null) {
                intent.putExtra(VideoPlayActivity.EXTRA_TIME, mediaPlayer.getCurrentPosition());
            }
            intent.putExtra(VideoPlayActivity.EXTRA_TITLE, title);
            getContext().startActivity(intent);

        } else if (R.id.img_back == v.getId()) {
            handler.removeMessages(1);
            release();
            if (onBackClickListener != null) onBackClickListener.onBackClick();
        }
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

    /**
     * 最大化监听
     *
     * @param onMaxWindowClickListener
     */
    public void setOnMaxWindowClickListener(OnClickMaxWindowListener onMaxWindowClickListener) {
        this.onMaxWindowClickListener = onMaxWindowClickListener;
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
     * 返回按钮
     *
     * @param onBackClickListener
     */
    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        showBack = onBackClickListener != null;
        if (showBack) mRLBack.setVisibility(VISIBLE);
        this.onBackClickListener = onBackClickListener;
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

    /**
     * title  和  控制view  隐藏
     */
    public void dismissControlView() {
        mRLBack.setVisibility(GONE);
        mRLButton.setVisibility(GONE);
    }

    /**
     * title
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
        if (!TextUtils.isEmpty(title)) {
            mTVTitle.setText(title);
        }
    }
}
