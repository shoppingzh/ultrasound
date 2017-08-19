package com.littlezheng.proxyimageviewer.entity;

/**
 * Created by Administrator on 2017/8/19/019.
 */

public class ImageInfo {

    private int id;

    public ImageInfo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "id=" + id +
                '}';
    }
}
