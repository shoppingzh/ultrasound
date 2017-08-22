package com.littlezheng.ultrasound.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.littlezheng.ultrasound.task.ConfigTask;
import com.littlezheng.ultrasound.ultrasound.Configuration;
import com.littlezheng.ultrasound.R;
import com.littlezheng.ultrasound.adapter.FolderAdapter;
import com.littlezheng.ultrasound.adapter.Snapshot;
import com.littlezheng.ultrasound.adapter.SnapshotAdapter;
import com.littlezheng.ultrasound.ultrasound.process.ImageCreator;
import com.littlezheng.ultrasound.ultrasound.transfer.api.AsynUdpSender;
import com.littlezheng.ultrasound.ultrasound.transfer.api.BufferedUdpReceiver;
import com.littlezheng.ultrasound.ultrasound.transfer.api.UdpSender;
import com.littlezheng.ultrasound.ultrasound.transfer.data.ModeController;
import com.littlezheng.ultrasound.ultrasound.transfer.data.Param;
import com.littlezheng.ultrasound.ultrasound.transfer.data.RunController;
import com.littlezheng.ultrasound.ultrasound.transfer.validate.UdpPacket406Validator;
import com.littlezheng.ultrasound.ultrasound.transfer.validate.UdpPacketValidator;
import com.littlezheng.ultrasound.ultrasound.view.DisplayView;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;
import com.littlezheng.ultrasound.ultrasound.view.strategy.ImageShowStrategy;
import com.littlezheng.ultrasound.ultrasound.view.strategy.TimeStrategy;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.BModeStrategyDecorator;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.MModeStrategyDecorator;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.ParamsStrategyDecorator;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.RunStateStrategyDecorator;
import com.littlezheng.ultrasound.util.FileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DisplayView.OnWindowSizeChangedListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private DisplayView displayView;

    /////////////////UI
    //基本功能
    private Button freezeBtn;
    private Button playVideoBtn;
    private Button clearAnnotatesBtn;
    private Button saveSnapshotBtn;
    private Button saveVideoBtn;
    private Button previewBtn;
    private Button addPatientBtn;
    //模式切换
    private Button bModeBtn;
    private Button bbModeBtn;
    private Button mModeBtn;
    private Button bmModeBtn;
    //参数控制
    private Button incLightnessBtn;
    private Button decLightnessBtn;
    private Button incContrastBtn;
    private Button decContrastBtn;
    private Button incGainBtn;
    private Button decGainBtn;
    private Button incNearGainBtn;
    private Button decNearGainBtn;
    private Button incFarGainBtn;
    private Button decFarGainBtn;
    private Button incDepthBtn;
    private Button decDepthBtn;

    ////////////////数据回传
    //运行控制器
    private RunController runController;
    //可调参数
    private Param contrast;
    private Param lightness;
    private Param gain;
    private Param nearGain;
    private Param farGain;
    private Param depth;
    private Param mModeSpeed;
    //模式控制器
    private ModeController modeController;

    /////////////////绘制策略
    //基本显示策略：显示时间、运行状态、参数列表
    private DisplayStrategy timeStrategy;
    private RunStateStrategyDecorator runStateStrategyDecorator;
    private ParamsStrategyDecorator paramsStrategyDecorator;
    //图片显示策略
    private ImageShowStrategy imageShowStrategy;
    //工作模式策略
    private BModeStrategyDecorator bModeStrategyDecorator;
    private MModeStrategyDecorator mModeStrategyDecorator;

    private Configuration conf;
    private BufferedUdpReceiver receiver;
    private UdpSender sender;
    private ImageCreator imageCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        freezeBtn = (Button) findViewById(R.id.btn_freeze);
        playVideoBtn = (Button) findViewById(R.id.btn_play_video);
        clearAnnotatesBtn = (Button) findViewById(R.id.btn_clear_annotates);
        saveSnapshotBtn = (Button) findViewById(R.id.btn_save_snapshot);
        saveVideoBtn = (Button) findViewById(R.id.btn_save_video);
        previewBtn = (Button) findViewById(R.id.btn_preview);
        addPatientBtn = (Button) findViewById(R.id.btn_add_patient);
        bModeBtn = (Button) findViewById(R.id.btn_mode_b);
        bbModeBtn = (Button) findViewById(R.id.btn_mode_bb);
        mModeBtn = (Button) findViewById(R.id.btn_mode_m);
        bmModeBtn = (Button) findViewById(R.id.btn_mode_bm);
        incLightnessBtn = (Button) findViewById(R.id.btn_inc_lightness);
        decLightnessBtn = (Button) findViewById(R.id.btn_dec_lightness);
        incContrastBtn = (Button) findViewById(R.id.btn_inc_contrast);
        decContrastBtn = (Button) findViewById(R.id.btn_dec_contrast);
        incGainBtn = (Button) findViewById(R.id.btn_inc_gain);
        decGainBtn = (Button) findViewById(R.id.btn_dec_gain);
        incNearGainBtn = (Button) findViewById(R.id.btn_inc_near_gain);
        decNearGainBtn = (Button) findViewById(R.id.btn_dec_near_gain);
        incFarGainBtn = (Button) findViewById(R.id.btn_inc_far_gain);
        decFarGainBtn = (Button) findViewById(R.id.btn_dec_far_gain);
        incDepthBtn = (Button) findViewById(R.id.btn_inc_depth);
        decDepthBtn = (Button) findViewById(R.id.btn_dec_depth);

        freezeBtn.setOnClickListener(this);
        playVideoBtn.setOnClickListener(this);
        clearAnnotatesBtn.setOnClickListener(this);
        saveSnapshotBtn.setOnClickListener(this);
        saveVideoBtn.setOnClickListener(this);
        previewBtn.setOnClickListener(this);
        addPatientBtn.setOnClickListener(this);
        bModeBtn.setOnClickListener(this);
        bbModeBtn.setOnClickListener(this);
        mModeBtn.setOnClickListener(this);
        bmModeBtn.setOnClickListener(this);
        incLightnessBtn.setOnClickListener(this);
        decLightnessBtn.setOnClickListener(this);
        incContrastBtn.setOnClickListener(this);
        decContrastBtn.setOnClickListener(this);
        incGainBtn.setOnClickListener(this);
        decGainBtn.setOnClickListener(this);
        incNearGainBtn.setOnClickListener(this);
        decNearGainBtn.setOnClickListener(this);
        incFarGainBtn.setOnClickListener(this);
        decFarGainBtn.setOnClickListener(this);
        incDepthBtn.setOnClickListener(this);
        decDepthBtn.setOnClickListener(this);

        //申请相关权限
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        //回传参数配置
        runController = new RunController(0xF0);
        contrast = new Param(0xFA,"对比度",32,48,16,1);
        lightness = new Param(0xFB,"亮度",32,46,20,1);
        gain = new Param(0xFC,"总增",16,32,1,1);
        nearGain = new Param(0xFD,"近增",16,32,1,1);
        farGain = new Param(0xFE,"远增",16,32,1,1);
        depth = new Param(0xF9,"深度",13,19,0,1);
        mModeSpeed = new Param(0xF3,"速度",3,3,0,1);
        modeController = new ModeController(0xF2);

        //配置策略
        timeStrategy = new TimeStrategy(this);
        runStateStrategyDecorator = new RunStateStrategyDecorator(this, timeStrategy, runController);
        paramsStrategyDecorator = new ParamsStrategyDecorator(this, runStateStrategyDecorator,
                contrast, lightness, gain, nearGain, farGain, depth, mModeSpeed);
        imageShowStrategy = new ImageShowStrategy(this);
        bModeStrategyDecorator = new BModeStrategyDecorator(this, paramsStrategyDecorator);
        mModeStrategyDecorator = new MModeStrategyDecorator(this, paramsStrategyDecorator);

        displayView = new DisplayView(this);
        displayView.setOnWindowSizeChangedListener(this);
        displayView.setDisplayStrategy(paramsStrategyDecorator);
        ((LinearLayout)findViewById(R.id.display_window_wrapper)).addView(displayView);

        //加载图片加载框架
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

        conf = Configuration.getInstance(this);
        new ConfigTask(this).execute(conf);
        try {
            receiver = new BufferedUdpReceiver(Configuration.UDP_RECEIVE_PORT,
                    Configuration.UDP_RECEIVE_PACKET_QUEUE_CAPACITY,
                    Configuration.UDP_RECEIVE_PACKET_SIZE);
            sender = new AsynUdpSender(InetAddress.getByName(Configuration.AP_IP),
                    Configuration.UDP_SEND_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        runController.addObserver(sender);
        lightness.addObserver(sender);
        gain.addObserver(sender);
        nearGain.addObserver(sender);
        farGain.addObserver(sender);
        depth.addObserver(sender);
        mModeSpeed.addObserver(sender);
        modeController.addObserver(sender);

        UdpPacketValidator validator = new UdpPacket406Validator(Configuration.UDP_RECEIVE_PACKET_SIZE);
        imageCreator = new ImageCreator(conf,receiver,validator);
        depth.addObserver(imageCreator);
        changeBMode();

        //应用启动发送冻结指令并切换到B模式
        runController.freeze();
        modeController.setMode(ModeController.Mode.MODE_B);

    }

    private void changeBMode() {
        imageCreator.addObserver(bModeStrategyDecorator);
        imageCreator.deleteObserver(mModeStrategyDecorator);

        imageCreator.setbEnable(true);
        imageCreator.setmEnable(false);
        displayView.setDisplayStrategy(bModeStrategyDecorator);
        modeController.setMode(ModeController.Mode.MODE_B);
    }

    private void changeBBMode() {
        imageCreator.addObserver(bModeStrategyDecorator);
        imageCreator.setbEnable(true);
        imageCreator.setmEnable(false);
        displayView.setDisplayStrategy(bModeStrategyDecorator);
        modeController.setMode(ModeController.Mode.MODE_B);
    }

    private void changeMMode() {
        imageCreator.addObserver(mModeStrategyDecorator);
        imageCreator.deleteObserver(bModeStrategyDecorator);

        imageCreator.setbEnable(false);
        imageCreator.setmEnable(true);
        displayView.setDisplayStrategy(mModeStrategyDecorator);
        modeController.setMode(ModeController.Mode.MODE_M);
    }

    private void changeBMMode() {
        imageCreator.addObserver(bModeStrategyDecorator);
        imageCreator.setbEnable(true);
        imageCreator.setmEnable(false);
        displayView.setDisplayStrategy(bModeStrategyDecorator);
        modeController.setMode(ModeController.Mode.MODE_B);
    }

    @Override
    public void onWindowSizeChanged(int width, int height) {
        runStateStrategyDecorator.init(width, height);
        imageShowStrategy.init(width, height);
        bModeStrategyDecorator.init(width, height);
        mModeStrategyDecorator.init(width, height);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_freeze:
                if(runController.isFreeze()){
                    runController.unFreeze();
                    receiver.start();
                    imageCreator.start();
                }else{
                    runController.freeze();
                    receiver.stop();
                    imageCreator.stop();
                }
                break;
            case R.id.btn_clear_annotates:
                break;
            case R.id.btn_save_snapshot:
                displayView.captureScreen();
                break;
            case R.id.btn_save_video:
                break;
            case R.id.btn_preview:
                preview();
                break;
            case R.id.btn_add_patient:
                break;
            case R.id.btn_mode_b:
                changeBMode();
                break;
            case R.id.btn_mode_bb:
                break;
            case R.id.btn_mode_m:
                PopupMenu speedMenu = new PopupMenu(this,v, Gravity.TOP);
                speedMenu.getMenuInflater().inflate(R.menu.menu_m_mode_speed,speedMenu.getMenu());
                speedMenu.show();
                speedMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        changeMMode();
                        switch (item.getItemId()){
                            case R.id.item_speed_default:
                                mModeSpeed.toDefault();
                                break;
                            case R.id.item_speed_inc:
                                mModeSpeed.increase();
                                break;
                            case R.id.item_speed_dec:
                                mModeSpeed.decrease();
                                break;
                        }
                        return false;
                    }
                });
                break;
            case R.id.btn_mode_bm:

                break;
            case R.id.btn_inc_contrast:
                contrast.increase();
                Log.d(TAG,"对比度："+contrast.getDesc());
                break;
            case R.id.btn_dec_contrast:
                contrast.decrease();
                break;
            case R.id.btn_inc_lightness:
                lightness.increase();
                break;
            case R.id.btn_dec_lightness:
                lightness.decrease();
                break;
            case R.id.btn_inc_gain:
                gain.increase();
                break;
            case R.id.btn_dec_gain:
                gain.decrease();
                break;
            case R.id.btn_inc_near_gain:
                nearGain.increase();
                break;
            case R.id.btn_dec_near_gain:
                nearGain.decrease();
                break;
            case R.id.btn_inc_far_gain:
                farGain.increase();
                break;
            case R.id.btn_dec_far_gain:
                farGain.decrease();
                break;
            case R.id.btn_inc_depth:
                depth.increase();
                break;
            case R.id.btn_dec_depth:
                depth.decrease();
                break;
        }
    }

    /*****************************图片查看********************************/
    private void preview() {
        //显示快照存储文件夹
        final ListView folderListView = new ListView(this);
        final List<String> folders = FileUtils.listFolders(Configuration.SNAPSHOT_SAVE_FOLDER);
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
//                ListView snapshotListView = new ListView(MainActivity.this);
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
                        imageShowStrategy.loadImage(BitmapFactory.decodeFile(snapshot.getPathName()));
                        displayView.setDisplayStrategy(imageShowStrategy);
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
        List<File> files =  FileUtils.listFilesInFolder(Configuration.SNAPSHOT_SAVE_FOLDER+"/"+folderName);
        List<Snapshot> snapshots = null;
        if(files != null && !files.isEmpty()){
            snapshots = new ArrayList<>();
            for(File file : files){
                snapshots.add(new Snapshot(file.toString(),file.getName()));
            }
        }
        return snapshots;
    }


}
