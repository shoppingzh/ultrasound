package com.littlezheng.proxyimageviewer;

import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.littlezheng.proxyimageviewer.adapter.ImageAdapter;
import com.littlezheng.proxyimageviewer.entity.ImageInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button directLoadBtn;
    private Button proxyLoadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        directLoadBtn = (Button) findViewById(R.id.btn_load_direct);
        proxyLoadBtn = (Button) findViewById(R.id.btn_load_proxy);
        directLoadBtn.setOnClickListener(this);
        proxyLoadBtn.setOnClickListener(this);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

//        long start = System.currentTimeMillis();
//        for(int id : Utils.listDrawableIds()){
//            BitmapFactory.decodeResource(getResources(),id);
//        }
//        Log.d(TAG,"加载20张图片用时:"+(System.currentTimeMillis()-start)+"ms");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_load_direct:
                loadImages(false);
                break;
            case R.id.btn_load_proxy:
                loadImages(true);
                break;
        }
    }

    private void loadImages(boolean useProxy) {
        List<ImageInfo> images = new ArrayList<>();
        List<Integer> ids = Utils.listDrawableIds();
        for(int id : ids){
            images.add(new ImageInfo(id));
        }
        Log.d(TAG,images.toString());

        GridView gridView = new GridView(this);
        gridView.setNumColumns(GridView.AUTO_FIT);
//        gridView.setPadding(10,10,10,10);
//        ListView lv = new ListView(this);
        ImageAdapter adapter = new ImageAdapter(this,R.layout.image_list_item,images);
        adapter.setUseProxy(useProxy);
        gridView.setAdapter(adapter);

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(gridView);
        alertDialog.show();

    }



}
