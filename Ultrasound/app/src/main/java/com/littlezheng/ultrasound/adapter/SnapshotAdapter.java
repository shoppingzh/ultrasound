package com.littlezheng.ultrasound.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.littlezheng.ultrasound.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/8/13/013.
 */

public class SnapshotAdapter extends ArrayAdapter<Snapshot>{

    private static final String TAG = "SnapshotAdapter";

    private Context mContext;
    private int mResource;

    private List<Snapshot> snapshots;

    public SnapshotAdapter(@NonNull Context context, @LayoutRes int resource,
                           @NonNull List<Snapshot> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        snapshots = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Snapshot snapshot = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(mResource,null);
            viewHolder.imgIv = (ImageView) convertView.findViewById(R.id.iv_snapshot);
//            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.tv_snapshot_name);
//            viewHolder.deleteBtn = (Button) convertView.findViewById(R.id.btn_delete_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();

        /*opts.inJustDecodeBounds = true;
        opts.inSampleSize = 16;
        opts.inScaled = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inJustDecodeBounds = false;
        viewHolder.imgIv.setImageBitmap(BitmapFactory.decodeFile(snapshot.getPathName(),opts));*/
        ImageLoader.getInstance().displayImage("file:///"+snapshot.getPathName(),viewHolder.imgIv);
        /*viewHolder.nameTv.setText(snapshot.getName());
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean rst = FileUtils.delete(snapshot.getPathName());
                if(rst){
                    Toast.makeText(mContext,"删除成功！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext,"删除失败！",Toast.LENGTH_SHORT).show();
                }
                remove(snapshot);
                notifyDataSetChanged();

            }
        });*/

        return convertView;
    }

    class ViewHolder{
        ImageView imgIv;
        TextView nameTv;
        Button deleteBtn;
    }

}
