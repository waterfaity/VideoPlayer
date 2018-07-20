package com.waterfairy.videoplayer;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/7/20 11:13
 * @info:
 */
public interface OnVideoPlayListener {

    void onError(String errMsg);

    void onWorm();

    void onPlayComplete();

    void onPausePlay();

    void onStartPlay();
}
