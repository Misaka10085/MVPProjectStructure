package com.misakanetwork.mvpprojectstructure.bean;

import java.io.Serializable;

/**
 * Created By：Misaka10085
 * on：2021/7/6
 * package：com.misakanetwork.mvpprojectstructure.bean
 * class name：ImageSize
 * desc：ImageSize
 */
public class ImageSize implements Serializable {

    private int width;
    private int height;

    public ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
