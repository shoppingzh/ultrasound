package com.littlezheng.proxyimageviewer.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.littlezheng.proxyimageviewer.R;
import com.littlezheng.proxyimageviewer.entity.ImageInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/8/19/019.
 */

public class ImageAdapter extends ArrayAdapter<ImageInfo> {

    private static final String TAG = "ImageAdapter";

    private Context mContext;
    private int mResource;

    private boolean useProxy;

    public ImageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ImageInfo> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(mResource,null);
            viewHolder.imageIv = (ImageView) convertView.findViewById(R.id.iv_image);
//            viewHolder.idTv = (TextView) convertView.findViewById(R.id.tv_id);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageInfo info = getItem(position);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        if(!useProxy){
            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),info.getId(),opts);
            viewHolder.imageIv.setImageBitmap(bmp);
        }else{
            ImageLoader.getInstance().displayImage("drawable://"+info.getId(),viewHolder.imageIv);
        }
//        viewHolder.idTv.setText(info.getId());

        return convertView;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    class ViewHolder{
        ImageView imageIv;
        TextView idTv;
    }

}
