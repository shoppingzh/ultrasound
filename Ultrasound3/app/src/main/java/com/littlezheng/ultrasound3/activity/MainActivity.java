package com.littlezheng.ultrasound3.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.littlezheng.ultrasound3.adapter.FolderAdapter;
import com.littlezheng.ultrasound3.adapter.Snapshot;
import com.littlezheng.ultrasound3.adapter.SnapshotAdapter;
import com.littlezheng.ultrasound3.ultrasound.Application;
import com.littlezheng.ultrasound3.ultrasound.Configuration;
import com.littlezheng.ultrasound3.ultrasound.IApplication;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.Colors;
import com.littlezheng.ultrasound3.ultrasound.base.Mode;
import com.littlezheng.ultrasound3.util.FileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private Button freezeBtn;
    private Button measureBtn;
    private Button imageBtn;
    private Button videoBtn;
    private Button modeBtn;

    private Button incContrastBtn;
    private Button decContrastBtn;
    private Button incLightnessBtn;
    private Button decLightnessBtn;
    private Button incGainBtn;
    private Button decGainBtn;
    private Button incNearGainBtn;
    private Button decNearGainBtn;
    private Button incFarGainBtn;
    private Button decFarGainBtn;
    private Button incDepthBtn;
    private Button decDepthBtn;
    private Button pseudoColorBtn;

    private UContext uContext;
    private IApplication app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        freezeBtn = (Button) findViewById(R.id.btn_freeze);
        measureBtn = (Button) findViewById(R.id.btn_measure);
        imageBtn = (Button) findViewById(R.id.btn_image);
        videoBtn = (Button) findViewById(R.id.btn_video);
        modeBtn = (Button) findViewById(R.id.btn_mode);

        incContrastBtn = (Button) findViewById(R.id.btn_inc_contrast);
        decContrastBtn = (Button) findViewById(R.id.btn_dec_contrast);
        incLightnessBtn = (Button) findViewById(R.id.btn_inc_lightness);
        decLightnessBtn = (Button) findViewById(R.id.btn_dec_lightness);
        incGainBtn = (Button) findViewById(R.id.btn_inc_gain);
        decGainBtn = (Button) findViewById(R.id.btn_dec_gain);
        incNearGainBtn = (Button) findViewById(R.id.btn_inc_near_gain);
        decNearGainBtn = (Button) findViewById(R.id.btn_dec_near_gain);
        incFarGainBtn = (Button) findViewById(R.id.btn_inc_far_gain);
        decFarGainBtn = (Button) findViewById(R.id.btn_dec_far_gain);
        incDepthBtn = (Button) findViewById(R.id.btn_inc_depth);
        decDepthBtn = (Button) findViewById(R.id.btn_dec_depth);
        pseudoColorBtn = (Button) findViewById(R.id.btn_pseudo_color);

        freezeBtn.setOnClickListener(this);
        measureBtn.setOnClickListener(this);
        imageBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);
        modeBtn.setOnClickListener(this);
        incContrastBtn.setOnClickListener(this);
        decContrastBtn.setOnClickListener(this);
        incLightnessBtn.setOnClickListener(this);
        decLightnessBtn.setOnClickListener(this);
        incGainBtn.setOnClickListener(this);
        decGainBtn.setOnClickListener(this);
        incNearGainBtn.setOnClickListener(this);
        decNearGainBtn.setOnClickListener(this);
        incFarGainBtn.setOnClickListener(this);
        decFarGainBtn.setOnClickListener(this);
        incDepthBtn.setOnClickListener(this);
        decDepthBtn.setOnClickListener(this);
        pseudoColorBtn.setOnClickListener(this);

        //申请相关权限
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        //加载图片加载框架
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);


        Configuration configuration = new Configuration(this, "ultrasound.xml");
        uContext = new UContext(configuration);

        app = new Application(uContext);
        app.initWindow((ViewGroup)findViewById(R.id.display_window_wrapper));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //冻结/解冻
            case R.id.btn_freeze:
                if(app.isFrozen()){
                    unfreeze();
                }else{
                    freeze();
                }
                break;

            //测量
            case R.id.btn_measure:
                PopupMenu measureMenu = showPopupMenu(v, R.menu.menu_measure);
                measureMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_length:
                                break;
                            case R.id.item_area_per:
                                break;
                            case R.id.item_clear_measure:
                                break;
                        }
                        return false;
                    }
                });
                break;

            //图像相关
            case R.id.btn_image:
                PopupMenu imageMenu = showPopupMenu(v, R.menu.menu_image);
                imageMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_save_image:
                                app.takeSnapshot();
                                Toast t = Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT);
                                t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                t.show();
                                break;
                            case R.id.item_view_images:
                                viewImages();
                                break;
                        }
                        return false;
                    }
                });
                break;

            //视频相关
            case R.id.btn_video:
                PopupMenu videoMenu = showPopupMenu(v, R.menu.menu_video);
                videoMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_replay:
                                app.replay();
                                break;
                            case R.id.item_save_video:
                                app.saveVideo();
                                break;
                            case R.id.item_view_videos:
                                listVideos();
                                break;
                        }
                        return false;
                    }
                });
                break;

            //模式相关
            case R.id.btn_mode:
                PopupMenu modeMenu = showPopupMenu(v, R.menu.menu_mode);
                modeMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_mode_b:
                                app.changeMode(Mode.MODE_B);
                                break;
                            case R.id.item_mode_m:
                                app.changeMode(Mode.MODE_M);
                                break;
                            case R.id.item_mode_bb:
                                app.changeMode(Mode.MODE_BB);
                                break;
                            case R.id.item_mode_bm:
                                app.changeMode(Mode.MODE_BM);
                                break;
                        }
                        return false;
                    }
                });
                break;

            //色彩调节
            case R.id.btn_pseudo_color:
                PopupMenu colorMenu = showPopupMenu(v, R.menu.menu_pseudo_color);
                colorMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_color_normal:
                                app.changePseudoColor(Colors.PseudoColor.COLOR_NORMAL);
                                break;
                            case R.id.item_color_red:
                                app.changePseudoColor(Colors.PseudoColor.COLOR_RED);
                                break;
                            case R.id.item_color_yellow:
                                app.changePseudoColor(Colors.PseudoColor.COLOR_YELLOW);
                                break;
                            case R.id.item_color_mix:
                                app.changePseudoColor(Colors.PseudoColor.COLOR_MIX);
                                break;
                        }
                        return false;
                    }
                });
                break;

            //参数调节
            case R.id.btn_inc_contrast:
                app.increase(UContext.PARAM_CONTRAST);
                break;
            case R.id.btn_dec_contrast:
                app.decrease(UContext.PARAM_CONTRAST);
                break;
            case R.id.btn_inc_lightness:
                app.increase(UContext.PARAM_LIGHTNESS);
                break;
            case R.id.btn_dec_lightness:
                app.decrease(UContext.PARAM_LIGHTNESS);
                break;
            case R.id.btn_inc_gain:
                app.increase(UContext.PARAM_GAIN);
                break;
            case R.id.btn_dec_gain:
                app.decrease(UContext.PARAM_GAIN);
                break;
            case R.id.btn_inc_near_gain:
                app.increase(UContext.PARAM_NEAR_GAIN);
                break;
            case R.id.btn_dec_near_gain:
                app.decrease(UContext.PARAM_NEAR_GAIN);
                break;
            case R.id.btn_inc_far_gain:
                app.increase(UContext.PARAM_FAR_GAIN);
                break;
            case R.id.btn_dec_far_gain:
                app.decrease(UContext.PARAM_FAR_GAIN);
                break;
            case R.id.btn_inc_depth:
                app.increase(UContext.PARAM_DEPTH);
                break;
            case R.id.btn_dec_depth:
                app.decrease(UContext.PARAM_DEPTH);
                break;
        }
    }



    /**
     * 冻结表现
     */
    private void freeze() {
        measureBtn.setEnabled(true);
        imageBtn.setEnabled(true);
        videoBtn.setEnabled(true);
        modeBtn.setEnabled(false);
        pseudoColorBtn.setEnabled(false);
        incContrastBtn.setEnabled(false);
        decContrastBtn.setEnabled(false);
        incLightnessBtn.setEnabled(false);
        decLightnessBtn.setEnabled(false);
        incGainBtn.setEnabled(false);
        decGainBtn.setEnabled(false);
        incNearGainBtn.setEnabled(false);
        decNearGainBtn.setEnabled(false);
        incFarGainBtn.setEnabled(false);
        decFarGainBtn.setEnabled(false);
        incDepthBtn.setEnabled(false);
        decDepthBtn.setEnabled(false);

        app.freeze();
    }

    private void unfreeze() {
        measureBtn.setEnabled(false);
        imageBtn.setEnabled(false);
        videoBtn.setEnabled(false);
        modeBtn.setEnabled(true);
        pseudoColorBtn.setEnabled(true);
        incContrastBtn.setEnabled(true);
        decContrastBtn.setEnabled(true);
        incLightnessBtn.setEnabled(true);
        decLightnessBtn.setEnabled(true);
        incGainBtn.setEnabled(true);
        decGainBtn.setEnabled(true);
        incNearGainBtn.setEnabled(true);
        decNearGainBtn.setEnabled(true);
        incFarGainBtn.setEnabled(true);
        decFarGainBtn.setEnabled(true);
        incDepthBtn.setEnabled(true);
        decDepthBtn.setEnabled(true);

        app.unfreeze();
    }

    /**
     * 显示保存的图片
     */
    private void viewImages() {
        //显示快照存储文件夹
        final ListView folderListView = new ListView(this);
        final List<String> folders = FileUtils.listFolders(UContext.IMAGE_STORAGE_PATH);
        if(folders == null || folders.isEmpty()){
            Toast.makeText(this,"没有保存任何图像！",Toast.LENGTH_SHORT).show();
            return;
        }
        final ArrayAdapter<String> adapter = new FolderAdapter(this,R.layout.folder_list_item,folders);
        folderListView.setAdapter(adapter);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(folderListView);
        dialog.show();

        folderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String folderName = adapter.getItem(position);
                List<Snapshot> snapshots = listSnapshots(folderName);
                if(snapshots == null || snapshots.isEmpty()){
                    Toast.makeText(MainActivity.this,"该文件夹下没有图像！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //使用gridview进行显示
                GridView snapshotGridView = new GridView(MainActivity.this);
                snapshotGridView.setNumColumns(3);
                snapshotGridView.setPadding(10,10,10,10);
                final ArrayAdapter<Snapshot> snapshotArrayAdapter = new SnapshotAdapter(MainActivity.this,
                        R.layout.image_list_item,snapshots);
                snapshotGridView.setAdapter(snapshotArrayAdapter);
                final AlertDialog dialog2 = new AlertDialog.Builder(MainActivity.this).create();
                dialog2.setView(snapshotGridView);
                dialog2.show();

                //点击显示
                snapshotGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Snapshot snapshot = snapshotArrayAdapter.getItem(position);
                        app.showSnapshot(BitmapFactory.decodeFile(snapshot.getPathName()));
                        dialog2.dismiss();
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    /**
     * 获取该文件夹下的所有快照
     * @param folderName
     * @return
     */
    private List<Snapshot> listSnapshots(String folderName) {
        List<File> files =  FileUtils.listFilesInFolder(UContext.IMAGE_STORAGE_PATH +
                "/" + folderName);
        List<Snapshot> snapshots = null;
        if(files != null && !files.isEmpty()){
            snapshots = new ArrayList<>();
            for(File file : files){
                snapshots.add(new Snapshot(file.toString(),file.getName()));
            }
        }
        return snapshots;
    }

    /**
     * 列举所有视频
     */
    private void listVideos() {
        ListView videoLv = new ListView(this);
        final List<File> videos = FileUtils.listFilesInFolder(UContext.VIDEO_STORAGE_PATH);
        List<String> videoNames = new ArrayList<>();
        if(videos != null){
            for(File file : videos){
                videoNames.add(file.getName() +
                        " ("+(String.format("%.2f", file.length()/1024/1024.0))+"M)");
            }
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,videoNames);
        videoLv.setAdapter(adapter);

        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(videoLv);
        dialog.show();

        videoLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File video = videos.get(position);
                app.play(video);
                dialog.cancel();
            }
        });
    }


    /**
     * 显示弹出菜单
     * @param v 触发视图
     * @param menuRes 菜单资源文件id
     * @return
     */
    private PopupMenu showPopupMenu(View v, int menuRes){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
        popupMenu.getMenuInflater().inflate(menuRes, popupMenu.getMenu());
        popupMenu.show();

        return popupMenu;
    }

    @Override
    protected void onPause() {
        super.onPause();
        freeze();
    }

    @Override
    protected void onStop() {
        super.onStop();
        freeze();
    }


}
