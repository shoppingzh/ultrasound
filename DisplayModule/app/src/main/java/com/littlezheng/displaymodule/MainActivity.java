package com.littlezheng.displaymodule;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.littlezheng.displaymodule.display.test.CircleStrategy;
import com.littlezheng.displaymodule.display.DisplayView;
import com.littlezheng.displaymodule.display.test.HelloDrawStrategy;
import com.littlezheng.displaymodule.display.test.RectStrategy;
import com.littlezheng.displaymodule.display.DisplayViewListener;
import com.littlezheng.displaymodule.display.test.TimeStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

    private DisplayView displayView;

    private HelloDrawStrategy helloStrategy;
    private RectStrategy rectStrategy;
    private CircleStrategy circleStrategy;
    private TimeStrategy timeStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helloStrategy = new HelloDrawStrategy();
        rectStrategy = new RectStrategy();
        circleStrategy = new CircleStrategy();
        timeStrategy = new TimeStrategy();
        displayView = new DisplayView(this, new DisplayViewListener() {
            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                helloStrategy.init(gl, width, height);
                rectStrategy.init(gl, width, height);
                circleStrategy.init(gl, width, height);
                timeStrategy.init(width, height);
            }
        });
        displayView.setDrawStrategy(helloStrategy);
        setContentView(displayView);

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_mode_rect:
                displayView.setDrawStrategy(rectStrategy);
                break;
            case R.id.item_mode_circle:
                displayView.setDrawStrategy(circleStrategy);
                break;
            case R.id.item_time:
                displayView.setDrawStrategy(timeStrategy);
                break;

            case R.id.item_capture_screen:
                displayView.captureScreen();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public static void saveBitmap(Bitmap bmp, String path) throws IOException {
        File externalPath = Environment.getExternalStorageDirectory();
        OutputStream out = new FileOutputStream(new File(externalPath,path+"/"+System.currentTimeMillis()+".jpg"));
        bmp.compress(Bitmap.CompressFormat.JPEG,100,out);
        out.close();
    }

}
