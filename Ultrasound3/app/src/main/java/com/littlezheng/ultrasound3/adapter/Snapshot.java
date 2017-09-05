package com.littlezheng.ultrasound3.adapter;

/**
 * Created by Administrator on 2017/8/13/013.
 */

public class Snapshot {

    private String pathName;
    private String name;

    public Snapshot(String pathName,String name) {
        this.pathName = pathName;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getPathName() {
        return pathName;
    }

    @Override
    public String toString() {
        return "Snapshot{" +
                "pathName='" + pathName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
