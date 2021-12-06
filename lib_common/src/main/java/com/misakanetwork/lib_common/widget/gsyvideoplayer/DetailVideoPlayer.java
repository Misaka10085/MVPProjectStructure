package com.misakanetwork.lib_common.widget.gsyvideoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.misakanetwork.lib_common.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.lib_common.widget.gsyvideoplayer
 * class name：DetailVideoPlayer
 * desc：DetailVideoPlayer
 */
public class DetailVideoPlayer extends StandardGSYVideoPlayer {
    private RelativeLayout playOrPauseRl; // 左下角播放、暂停rl
    private ImageView stateIv; // 左下角播放、暂停iv
    private RelativeLayout fullScreenRl; // 全屏按钮rl

    public DetailVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public DetailVideoPlayer(Context context) {
        super(context);
    }

    public DetailVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_layout_detail;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        fullScreenRl = findViewById(R.id.full_screen_rl);
        playOrPauseRl = findViewById(R.id.play_or_pause_rl);
        stateIv = findViewById(R.id.state_iv);
        stateIv.setImageResource(R.mipmap.ic_detail_play);
        playOrPauseRl.setOnClickListener(this);
        getFullscreenButton().setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.GONE);
        playOrPauseRl.setVisibility(View.GONE);
        mBackButton.setVisibility(View.GONE);
        playOrPauseRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    onVideoPause();
                } else {
                    clickStartIcon();
                }
                updateStartImage();
            }
        });
        fullScreenRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIfCurrentIsFullscreen) {
                    startWindowFullscreen(getContext(), false, true);
                } else {
                    backFromFull(getContext());
                }
            }
        });
    }

    @Override
    protected void updateStartImage() {
        if (mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                playOrPauseRl.setVisibility(View.VISIBLE);
                stateIv.setImageResource(R.mipmap.ic_detail_pause);
                imageView.setVisibility(View.GONE);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                playOrPauseRl.setVisibility(View.GONE);
                stateIv.setImageResource(R.mipmap.ic_detail_play);
                imageView.setVisibility(View.VISIBLE);
            } else {
                stateIv.setImageResource(R.mipmap.ic_detail_play);
                imageView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void resolveFullVideoShow(Context context, GSYBaseVideoPlayer gsyVideoPlayer, FrameLayout frameLayout) {
        fullScreenRl.setVisibility(View.GONE);
        super.resolveFullVideoShow(context, gsyVideoPlayer, frameLayout);
    }

    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        fullScreenRl.setVisibility(View.VISIBLE);
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
    }
}
