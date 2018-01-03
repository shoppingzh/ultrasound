package com.littlezheng.newultrasound;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.littlezheng.newultrasound.adapter.FolderAdapter;
import com.littlezheng.newultrasound.adapter.Snapshot;
import com.littlezheng.newultrasound.adapter.SnapshotAdapter;
import com.littlezheng.newultrasound.core.CalculateSupport;
import com.littlezheng.newultrasound.core.Depth;
import com.littlezheng.newultrasound.core.FrameGenerator;
import com.littlezheng.newultrasound.core.Param;
import com.littlezheng.newultrasound.core.SampleDataLoadTask;
import com.littlezheng.newultrasound.core.SimpleFrameGenerator;
import com.littlezheng.newultrasound.displayer.CommonDisplayer;
import com.littlezheng.newultrasound.displayer.DisplayView;
import com.littlezheng.newultrasound.displayer.Displayer;
import com.littlezheng.newultrasound.displayer.DisplayerFactory;
import com.littlezheng.newultrasound.displayer.MeasureDisplayer;
import com.littlezheng.newultrasound.displayer.WorkModeDisplayer;
import com.littlezheng.newultrasound.displayer.measuremaker.LineMaker;
import com.littlezheng.newultrasound.displayer.measuremaker.RectMaker;
import com.littlezheng.newultrasound.transmission.BufferedUdpReceiver;
import com.littlezheng.newultrasound.transmission.ContinuousReceiver;
import com.littlezheng.newultrasound.transmission.Packet406Validator;
import com.littlezheng.newultrasound.transmission.PacketValidator;
import com.littlezheng.newultrasound.transmission.Receiver;
import com.littlezheng.newultrasound.transmission.UdpSender;
import com.littlezheng.newultrasound.util.FileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Receiver.ReceiveListener {

    private static final String TAG = "MainActivity";
    private static final String IMAGE_STORAGE_PATH = "/aaa/img/";
    private static final String REMOTE_IP = "192.168.1.1";
    private static final int REMOTE_PORT = 5001;

    private Button freezeBtn;
    private Button measureBtn;
    private Button imageBtn;
    private Button videoBtn;
//    private Button modeBtn;
    private Button pseudoColorBtn;
    private Button bModeBtn;
    private Button bbModeBtn;
    private Button mModeBtn;
    private Button bmModeBtn;

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
    private Button incSpeedBtn;
    private Button decSpeedBtn;
    private Button incDepthBtn;
    private Button decDepthBtn;

    private Param contrast = new Param(32, 48, 16, 1);
    private Param lightness = new Param(32, 46, 20, 1);
    private Param gain = new Param(16, 32, 1, 1);
    private Param nearGain = new Param(16, 32, 1, 1);
    private Param farGain = new Param(16, 32, 1, 1);
    private Depth depth = Depth.getInstance();
    private Param speed = new Param(3, 3, 0, 1);

    private DisplayView displayView;
    private ContinuousReceiver receiver;
    private PacketValidator validator;
    private FrameGenerator frameGenerator;

    private boolean freeze = true;
    private byte[] backFatData = new byte[400];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        freezeBtn = (Button) findViewById(R.id.btn_freeze);
        measureBtn = (Button) findViewById(R.id.btn_measure);
        imageBtn = (Button) findViewById(R.id.btn_image);
        videoBtn = (Button) findViewById(R.id.btn_video);
        //modeBtn = (Button) findViewById(R.id.btn_mode);
        pseudoColorBtn = (Button) findViewById(R.id.btn_pseudo_color);
        bModeBtn = (Button) findViewById(R.id.btn_b_mode);
        bbModeBtn = (Button) findViewById(R.id.btn_bb_mode);
        mModeBtn = (Button) findViewById(R.id.btn_m_mode);
        bmModeBtn = (Button) findViewById(R.id.btn_bm_mode);

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
        incSpeedBtn = (Button) findViewById(R.id.btn_inc_speed);
        decSpeedBtn = (Button) findViewById(R.id.btn_dec_speed);
        incDepthBtn = (Button) findViewById(R.id.btn_inc_depth);
        decDepthBtn = (Button) findViewById(R.id.btn_dec_depth);

        freezeBtn.setOnClickListener(this);
        measureBtn.setOnClickListener(this);
        imageBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);
//        modeBtn.setOnClickListener(this);
        pseudoColorBtn.setOnClickListener(this);
        bModeBtn.setOnClickListener(this);
        bbModeBtn.setOnClickListener(this);
        mModeBtn.setOnClickListener(this);
        bmModeBtn.setOnClickListener(this);

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
        incSpeedBtn.setOnClickListener(this);
        decSpeedBtn.setOnClickListener(this);
        decDepthBtn.setOnClickListener(this);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        //加载图片加载框架
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

        new SampleDataLoadTask(this).execute();
        UdpSender.init(REMOTE_IP, REMOTE_PORT);
        try {
            receiver = new BufferedUdpReceiver(5001, 406, 100);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        validator = new Packet406Validator();
        frameGenerator = new SimpleFrameGenerator();
        receiver.addListener(this);

        displayView = new DisplayView(this);
        ((ViewGroup)findViewById(R.id.display_window_wrapper)).addView(displayView);

        updateContrast();
        updateLightness();
        updateGain();
        updateNearGain();
        updateFarGain();
        updateDepth();
        updateSpeed();

        freeze();
    }

    @Override
    public void onReceive(byte[] data) {
        if(!validator.validate(data)) return;

        int id = data[403] & 0xff;
        if(id == 64){
            backFatData = Arrays.copyOfRange(data, 2, 402);
        }

        frameGenerator.putData(data);
    }


    //////////////////////////冻结与解冻
    
    private void freeze() {
        freeze = true;
        receiver.stop();
        CommonDisplayer.updateRunState(freeze);

        //与冻结相关的功能
        measureBtn.setEnabled(true);
        imageBtn.setEnabled(true);
        videoBtn.setEnabled(true);

        //与冻结不相关的功能
        //modeBtn.setEnabled(false);
        bModeBtn.setEnabled(false);
        bbModeBtn.setEnabled(false);
        mModeBtn.setEnabled(false);
        bmModeBtn.setEnabled(false);
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
        incSpeedBtn.setEnabled(false);
        decSpeedBtn.setEnabled(false);
        incDepthBtn.setEnabled(false);
        decDepthBtn.setEnabled(false);

        UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xf0, 0x00, (byte) 0xaa, 0x55});
    }


    private void unfreeze() {
        freeze = false;
        receiver.start();
        CommonDisplayer.updateRunState(freeze);

        //与运行相关的功能
//        modeBtn.setEnabled(true);
        bModeBtn.setEnabled(true);
        bbModeBtn.setEnabled(true);
        mModeBtn.setEnabled(true);
        bmModeBtn.setEnabled(true);
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
        incSpeedBtn.setEnabled(true);
        decSpeedBtn.setEnabled(true);
        incDepthBtn.setEnabled(true);
        decDepthBtn.setEnabled(true);

        //与运行不相关的功能
        measureBtn.setEnabled(false);
        imageBtn.setEnabled(false);
        videoBtn.setEnabled(false);

        UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xf0, (byte) 0xff, (byte) 0xaa, 0x55});
    }

    //////////////////////////工作模式切换

    private void toBMode() {
        displayView.show(DisplayerFactory.createBModeDisplayer(frameGenerator));
        UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xf2, 0x00, (byte) 0xaa, 0x55});
    }

    private void toMMode() {
        displayView.show(DisplayerFactory.createMModeDisplayer(frameGenerator));
        UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xf2, (byte) 0xe0, (byte) 0xaa, 0x55});
    }

    private void toBBMode() {
        displayView.show(DisplayerFactory.createBBModeDisplayer(frameGenerator, true));
        UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xf2, 0x00, (byte) 0xaa, 0x55});
    }

    private void toBMMode() {
        WorkModeDisplayer displayer =
                DisplayerFactory.createBMModeDisplayer(frameGenerator, true);
        displayView.show(displayer);
    }

    //////////////////////////参数调节

    private void updateContrast(){
        UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xfa,
                (byte) contrast.getValue(), (byte) 0xaa, 0x55});
        CommonDisplayer.updateContrast(contrast.getValue());
    }

    private void updateLightness(){
        UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xfb,
                (byte) lightness.getValue(), (byte) 0xaa, 0x55});
        CommonDisplayer.updateLightness(lightness.getValue());
    }

    private void updateGain(){
        UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xfc,
                (byte) gain.getValue(), (byte) 0xaa, 0x55});
        CommonDisplayer.updateGain(gain.getValue());
    }

    private void updateNearGain(){
        UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xfd,
                (byte) nearGain.getValue(), (byte) 0xaa, 0x55});
        CommonDisplayer.updateNearGain(nearGain.getValue());
    }

    private void updateFarGain(){
        UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xfe,
                (byte) farGain.getValue(), (byte) 0xaa, 0x55});
        CommonDisplayer.updateFarGain(farGain.getValue());
    }

    private void updateDepth(){
        UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xf9,
                (byte) depth.getValue(), (byte) 0xaa, 0x55});
        CommonDisplayer.updateDepth(depth.getValue());
    }

    private void updateSpeed(){
        UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xf3,
                (byte) speed.getValue(), (byte) 0xaa, 0x55});
        CommonDisplayer.updateSpeed(speed.getValue());
    }

    ///////////////////////////////图像保存

    private boolean saveImage(Bitmap image){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        File dir = new File(Environment.getExternalStorageDirectory() + IMAGE_STORAGE_PATH +
                df.format(new Date()));
        if(!dir.exists()) dir.mkdirs();
        String fileName = System.currentTimeMillis() + ".jpg";

        OutputStream out = null;
        boolean saveResult = true;
        try {
            out = new FileOutputStream(new File(dir, fileName));
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            saveResult = false;
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(image != null && !image.isRecycled()) image.recycle();
        }

        return saveResult;
    }


    ///////////////////////////////图像显示

    /**
     * 获取该文件夹下的所有快照
     *
     * @param folderName
     * @return
     */
    private List<Snapshot> listSnapshots(String folderName) {
        List<File> files = FileUtils.listFilesInFolder(IMAGE_STORAGE_PATH +
                "/" + folderName);
        List<Snapshot> snapshots = null;
        if (files != null && !files.isEmpty()) {
            snapshots = new ArrayList<>();
            for (File file : files) {
                snapshots.add(new Snapshot(file.getAbsolutePath(), file.getName()));
            }
        }
        return snapshots;
    }

    /**
     * 显示保存的图片
     */
    private void viewImages() {
        //显示快照存储文件夹
        final ListView folderListView = new ListView(this);
        final List<String> folders = FileUtils.listFolders(IMAGE_STORAGE_PATH);
        if (folders == null || folders.isEmpty()) {
            Toast.makeText(this, "没有保存任何图像！", Toast.LENGTH_SHORT).show();
            return;
        }
        final ArrayAdapter<String> adapter = new FolderAdapter(this, R.layout.folder_list_item, folders);
        folderListView.setAdapter(adapter);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(folderListView);
        dialog.show();

        folderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String folderName = adapter.getItem(position);
                List<Snapshot> snapshots = listSnapshots(folderName);
                if (snapshots == null || snapshots.isEmpty()) {
                    Toast.makeText(MainActivity.this, "该文件夹下没有图像！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //使用gridview进行显示
                GridView snapshotGridView = new GridView(MainActivity.this);
                snapshotGridView.setNumColumns(3);
                snapshotGridView.setPadding(10, 10, 10, 10);
                final ArrayAdapter<Snapshot> snapshotArrayAdapter = new SnapshotAdapter(MainActivity.this,
                        R.layout.image_list_item, snapshots);
                snapshotGridView.setAdapter(snapshotArrayAdapter);
                final AlertDialog dialog2 = new AlertDialog.Builder(MainActivity.this).create();
                dialog2.setView(snapshotGridView);
                dialog2.show();

                //点击显示
                snapshotGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final Snapshot snapshot = snapshotArrayAdapter.getItem(position);
                        displayView.show(DisplayerFactory.createImageShowDisplayer(
                                BitmapFactory.decodeFile(snapshot.getPathName())));
                        dialog2.dismiss();
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    ///////////////////////////通用功能

    /**
     * 显示弹出菜单
     *
     * @param v       触发视图
     * @param menuRes 菜单资源文件id
     * @return
     */
    private PopupMenu showPopupMenu(View v, int menuRes) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
        popupMenu.getMenuInflater().inflate(menuRes, popupMenu.getMenu());
        popupMenu.show();

        return popupMenu;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //冻结/解冻
            case R.id.btn_freeze:
                if(freeze){
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
                        Displayer displayer = displayView.getDisplayer();
                        MeasureDisplayer measureDisplayer = null;
                        if(displayer instanceof MeasureDisplayer){
                            measureDisplayer = (MeasureDisplayer) displayer;
                        }else{
                            measureDisplayer = DisplayerFactory.createMeasureDisplayer(displayer);
                        }
                        displayView.show(measureDisplayer);
                        switch (item.getItemId()) {
                            case R.id.item_length:
                                measureDisplayer.addShapeMaker(new LineMaker());
                                break;
                            case R.id.item_area_per:
                                measureDisplayer.addShapeMaker(new RectMaker());
                                break;
                            case R.id.item_measure_backfat:
                                CommonDisplayer.updateBackFat(CalculateSupport.calculateBackFat(backFatData));
                                break;
                            case R.id.item_clear_measure:
                                measureDisplayer.removeCurrent();
                                measureDisplayer.removeAllShapeMaker();
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
                        switch (item.getItemId()) {
                            case R.id.item_save_image:
                                if(saveImage(displayView.getScreenImage())){
                                    Toast.makeText(MainActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this, "保存失败！", Toast.LENGTH_SHORT).show();
                                }
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
                        switch (item.getItemId()) {
                            case R.id.item_replay:
                                break;
                            case R.id.item_save_video:
                                break;
                            case R.id.item_view_videos:
                                break;
                        }
                        return false;
                    }
                });
                break;

            //模式相关
//            case R.id.btn_mode:
//                PopupMenu modeMenu = showPopupMenu(v, R.menu.menu_mode);
//                modeMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.item_mode_b:
//                                toBMode();
//                                break;
//                            case R.id.item_mode_m:
//                                toMMode();
//                                break;
//                            case R.id.item_mode_bb:
//                                toBBMode();
//                                break;
//                            case R.id.item_mode_bm:
//                                toBMMode();
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                break;
            case R.id.btn_b_mode:
                toBMode();
                break;
            case R.id.btn_bb_mode:
                toBBMode();
                break;
            case R.id.btn_m_mode:
                toMMode();
                break;
            case R.id.btn_bm_mode:
                toBMMode();
                break;

            //色彩调节
            case R.id.btn_pseudo_color:
                PopupMenu colorMenu = showPopupMenu(v, R.menu.menu_pseudo_color);
                colorMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_color_normal:
                                frameGenerator.setPseudoColor(FrameGenerator.PseudoColor.NORMAL);
                                break;
                            case R.id.item_color_red:
                                frameGenerator.setPseudoColor(FrameGenerator.PseudoColor.RED);
                                break;
                            case R.id.item_color_yellow:
                                frameGenerator.setPseudoColor(FrameGenerator.PseudoColor.YELLOW);
                                break;
                            case R.id.item_color_mix:
                                frameGenerator.setPseudoColor(FrameGenerator.PseudoColor.MIX);
                                break;
                        }
                        return false;
                    }
                });
                break;

            //参数调节
            case R.id.btn_inc_contrast:
                if(contrast.increase()) updateContrast();
                break;
            case R.id.btn_dec_contrast:
                if(contrast.decrease()) updateContrast();
                break;
            case R.id.btn_inc_lightness:
                if(lightness.increase()) updateLightness();
                break;
            case R.id.btn_dec_lightness:
                if(lightness.decrease()) updateLightness();
                break;
            case R.id.btn_inc_gain:
                if(gain.increase()) updateGain();
                break;
            case R.id.btn_dec_gain:
                if(gain.decrease()) updateGain();
                break;
            case R.id.btn_inc_near_gain:
                if(nearGain.increase()) updateNearGain();
                break;
            case R.id.btn_dec_near_gain:
                if(nearGain.decrease()) updateNearGain();
                break;
            case R.id.btn_inc_far_gain:
                if(farGain.increase()) updateFarGain();
                break;
            case R.id.btn_dec_far_gain:
                if(farGain.decrease()) updateFarGain();
                break;
            case R.id.btn_inc_speed:
                if(speed.increase()) updateSpeed();
                break;
            case R.id.btn_dec_speed:
                if(speed.decrease()) updateSpeed();
                break;
            case R.id.btn_inc_depth:
                if(depth.increase()) updateDepth();
                break;
            case R.id.btn_dec_depth:
                if(depth.decrease()) updateDepth();
                break;
        }
    }


}
