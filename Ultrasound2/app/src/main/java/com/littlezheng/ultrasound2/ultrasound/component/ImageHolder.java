package com.littlezheng.ultrasound2.ultrasound.component;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/8/27/027.
 */

public class ImageHolder implements Iterable<StorableFrame>, Serializable {

    private static final String TAG = "ImageHolder";

    private static final long serialVersionUID = 1951440854688295325L;

    private StorableFrame[] frames;
    private int capacity;

    private int pos = 0;
    private boolean empty = true;
    private int i;

    public ImageHolder(int capacity){
        this.capacity = capacity;
        frames = new StorableFrame[capacity];
    }

    public void add(StorableFrame frame){
        empty = false;
        if(pos >= capacity) {
            pos = 0;
        }
        frames[pos] = null;
        frames[pos++] = frame;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public Iterator iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<StorableFrame>,Serializable{

        int cursor = ImageHolder.this.pos;
        boolean firstIn = true;

        @Override
        public boolean hasNext() {
            if(cursor == 0) return false;
            if(cursor == pos && firstIn){
                firstIn = false;
                return true;
            }

            return cursor != pos;
        }

        @Override
        public StorableFrame next() {
            if(cursor >= capacity) cursor = 0;
            StorableFrame next = frames[cursor];
//            Log.d(TAG,"cursor: "+ cursor);
            cursor++;

            return next;
        }
    }

}
