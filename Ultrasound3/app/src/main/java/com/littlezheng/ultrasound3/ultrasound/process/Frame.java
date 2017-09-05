package com.littlezheng.ultrasound3.ultrasound.process;

/**
 * Created by Administrator on 2017/8/11/011.
 */

public abstract class Frame {

    protected final byte[][] data;
    protected final int width;
    protected final int height;

    protected boolean horizontal;

    public Frame(int width,int height,boolean horizontal){
        this.width = width;
        this.height = height;
        this.horizontal = horizontal;
        if(horizontal){
            data = new byte[height][width];
        }else{
            data = new byte[width][height];
        }
    }

    /**
     * 向一帧中写入一条线的数据
     * @param num 写入的行号或列号
     * @param entity 行数据或列数据
     * @param srcPos 行数据或列数据开始位置
     * @param dstPos 帧中目的行或列的开始位置
     * @param len 行数据或列数据写入的长度
     */
    public abstract void put(int num,byte[] entity,int srcPos,int dstPos,int len);

    /**
     * 在下一条线中写入数据
     * @param entity 行数据或列数据
     * @param srcPos 行数据或列数据开始位置
     * @param dstPos 帧中目的行或列的开始位置
     * @param len 行数据或列数据写入的长度
     */
    public abstract void put(byte[] entity,int srcPos,int dstPos,int len);

    /**
     * 在下一条线中写入数据
     * @param entity 行数据或列数据
     * @param len 行数据或列数据写入的长度
     */
    public abstract void put(byte[] entity,int len);

    /**
     * 在下一条线中写入数据
     * @param entity 行数据或列数据
     */
    public abstract void put(byte[] entity);

    /**
     * 清空帧数据
     */
    public abstract void clear();

    /**
     * 帧数据是否已满
     * @return
     */
    public abstract boolean full();

    /**
     * 获取帧数据
     * @return
     */
    public byte[][] getData() {
        return data;
    }

    /**
     * 获取克隆后的帧数据
     * @return
     */
    public byte[][] getCloneData(){
        byte[][] copyData = data.clone();
        int len;
        for(int i=0;i<(len=copyData.length);i++){
            copyData[i] = data[i].clone();
        }
        return copyData;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

}
