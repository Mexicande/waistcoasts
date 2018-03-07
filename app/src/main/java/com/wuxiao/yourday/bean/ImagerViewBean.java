package com.wuxiao.yourday.bean;

import android.widget.ImageView;

/**
 * Created by apple on 2017/4/19.
 */

public class ImagerViewBean {
    private ImageView image;

    public ImagerViewBean(ImageView image) {
        this.image = image;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ImagerViewBean{" +
                "image=" + image +
                '}';
    }
}
