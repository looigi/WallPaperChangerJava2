package com.looigi.wallpaperchanger2.UtilitiesVarie;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class ProportionalVideoView extends VideoView {
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;

    public ProportionalVideoView(Context context) {
        super(context);
    }

    public ProportionalVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProportionalVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVideoSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (mVideoWidth > 0 && mVideoHeight > 0) {
            float videoProportion = (float) mVideoWidth / mVideoHeight;
            float screenProportion = (float) width / height;

            if (videoProportion > screenProportion) {
                // video più largo → riduce altezza
                height = (int) (width / videoProportion);
            } else {
                // video più alto → riduce larghezza
                width = (int) (height * videoProportion);
            }
        }

        setMeasuredDimension(width, height);
    }
}
