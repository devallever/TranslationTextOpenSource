package com.allever.lib.recommend;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.allever.lib.common.util.Tool;
import com.allever.lib.ui.widget.ShakeHelper;
import com.bumptech.glide.Glide;

/**
 * @author allever
 */
public class RecommendImageView extends RelativeLayout {

    private ImageView mIv;

    private Recommend mData;

    private String mPkg;
    private String mDefaultUrl;
    private Object mDefaultIcon;
    private String mJumpUrl = "";
    private Object mIcon;

    public RecommendImageView(Context context) {
        this(context, null);
    }

    public RecommendImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecommendImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void initData() {
        //获取随机的一个
        mData = RecommendGlobal.INSTANCE.getRandomItem();

        if (mData != null) {
            mIcon = mData.getIconUrl();
            mJumpUrl = RecommendGlobal.INSTANCE.getItemUrl(mData);
        } else {
            mIcon = mDefaultIcon;
            if ("google".equals(RecommendGlobal.INSTANCE.getChannel())) {
                mJumpUrl = "https://play.google.com/store/apps/details?id=" + mPkg;
            } else if ("xiaomi".equals(RecommendGlobal.INSTANCE.getChannel())) {
                mJumpUrl = "http://app.mi.com/details?id=" + mPkg;
            } else {
                mJumpUrl = mDefaultUrl;
            }
        }

        if ("".equals(mJumpUrl)) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            Glide.with(getContext()).load(mIcon).into(mIv);
        }
    }

    private void init() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.recommend_image_view, this);
        mIv = root.findViewById(R.id.icon);

        ObjectAnimator objectAnimator = ShakeHelper.INSTANCE.createShakeAnimator(this, true);
        objectAnimator.start();
        mIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Tool.openAppInPlay(getContext(), mJumpUrl);
            }
        });
    }

    public void setRecommendData(Object icon, String pkg, String url) {
        mDefaultIcon = icon;
        mPkg = pkg;
        mDefaultUrl = url;
        initData();
    }

    public void refresh() {
        initData();
    }
}
