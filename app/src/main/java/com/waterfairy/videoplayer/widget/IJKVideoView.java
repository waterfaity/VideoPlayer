package com.waterfairy.videoplayer.widget;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019/2/19 10:19
 * @info:
 */
public class IJKVideoView extends FrameLayout {
    private static final String TAG = "IJKVideoView";
    private final Context mContext;
    private String mVideoPath;
    //ijk 提供
    private IMediaPlayer mMediaPlayer;
    private SurfaceView surfaceView;
    private VideoPlayerListener listener;
    private OnPlayingListener onPlayingListener;

    public void setOnPlayingListener(OnPlayingListener onPlayingListener) {
        this.onPlayingListener = onPlayingListener;
        if (handler == null) {
            handler = getHandle();
        }
    }

    private android.os.Handler getHandle() {
        return new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (onPlayingListener != null && mMediaPlayer != null) {
                    onPlayingListener.onPlaying(mMediaPlayer.getCurrentPosition(), mMediaPlayer.getDuration());
                    handler.sendEmptyMessageDelayed(0, 1000);
                }
            }
        };
    }

    public IJKVideoView(@NonNull Context context) {
        this(context, null);
    }

    public IJKVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void setVideoPath(String videoPath) {
        if (TextUtils.isEmpty(mVideoPath)) {
            mVideoPath = videoPath;
            createSurfaceView();
        } else {
            mVideoPath = videoPath;
            load();
        }
    }


    private void createSurfaceView() {
        surfaceView = new SurfaceView(mContext);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                load();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT, Gravity.CENTER);
        surfaceView.setLayoutParams(layoutParams);
        this.addView(surfaceView);
    }

    /**
     * 载入播放器
     */
    private void load() {
        createPlayer();
        try {
            mMediaPlayer.setDataSource(mVideoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setDisplay(surfaceView.getHolder());
        mMediaPlayer.prepareAsync();
    }

    /**
     * 创建player
     */
    private void createPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
//        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
//开启硬解码        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        mMediaPlayer = ijkMediaPlayer;

        mMediaPlayer.setOnVideoSizeChangedListener(getVideoSizeChanged());
        if (listener != null) {
            mMediaPlayer.setOnPreparedListener(listener);
            mMediaPlayer.setOnInfoListener(listener);
            mMediaPlayer.setOnSeekCompleteListener(listener);
            mMediaPlayer.setOnBufferingUpdateListener(listener);
            mMediaPlayer.setOnErrorListener(listener);
            mMediaPlayer.setOnCompletionListener(listener);
        }
    }

    private IMediaPlayer.OnVideoSizeChangedListener getVideoSizeChanged() {
        return new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                int height1 = getHeight();
                int width1 = getWidth();
                ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
                if (width / (float) height > width1 / (float) height1) {
                    //视频宽
                    layoutParams.width = width1;
                    layoutParams.height = (int) (width1 * (height / (float) width));
                } else {
                    //视频高
                    layoutParams.height = height1;
                    layoutParams.width = (int) (height1 * (width / (float) height));
                }
                surfaceView.setLayoutParams(layoutParams);
            }
        };
    }

    public IMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void setListener(VideoPlayerListener listener) {
        this.listener = listener;
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnPreparedListener(listener);
        }
    }


    /**
     * -------======--------- 下面封装了一下控制视频的方法
     */

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            if (handler != null)
                handler.sendEmptyMessage(0);
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (handler != null) {
            handler.removeMessages(0);
            handler = null;
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            if (handler != null) handler.removeMessages(0);
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            if (handler != null) handler.removeMessages(0);

        }
    }


    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            if (handler != null) handler.removeMessages(0);

        }
    }


    public long getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }


    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }


    public void seekTo(long l) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(l);
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null) return mMediaPlayer.isPlaying();
        return false;
    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    public interface OnPlayingListener {
        void onPlaying(long progress, long total);
    }


    android.os.Handler handler;

    public interface VideoPlayerListener extends IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnCompletionListener {
//        /**
//         * 调用播放
//         *
//         * @param iMediaPlayer
//         */
//        @Override
//        void onPrepared(IMediaPlayer iMediaPlayer);
//
//        @Override
//        boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1);
//
//        @Override
//        void onSeekComplete(IMediaPlayer iMediaPlayer);
//
//        @Override
//        void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i);
//
//        @Override
//        boolean onError(IMediaPlayer iMediaPlayer, int i, int i1);
    }
}
