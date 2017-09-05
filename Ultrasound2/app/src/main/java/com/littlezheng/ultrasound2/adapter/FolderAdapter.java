package com.littlezheng.ultrasound2.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.littlezheng.ultrasound2.R;

import java.util.List;

/**
 * Created by Administrator on 2017/8/13/013.
 */

public class FolderAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int mResource;
    private List<String> mFolders;

    public FolderAdapter(@NonNull Context context, @LayoutRes int resource,
                         @NonNull List<String> folders) {
        super(context, resource, folders);
        mContext = context;
        mResource = resource;
        mFolders = folders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String folderName = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(mResource,null);
            viewHolder.imgIv = (ImageView) convertView.findViewById(R.id.iv_folder_img);
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.tv_folder_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nameTv.setText(folderName);

        return convertView;
    }

    class ViewHolder{
        ImageView imgIv;
        TextView nameTv;
    }

}
