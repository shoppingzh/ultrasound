package com.littlezheng.displaymodule;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.littlezheng.displaymodule.display.test.BitmapLoadStrategy;
import com.littlezheng.displaymodule.display.test.CircleStrategy;
import com.littlezheng.displaymodule.display.DisplayView;
import com.littlezheng.displaymodule.display.test.HelloDisplayStrategy;
import com.littlezheng.displaymodule.display.test.RectStrategy;
import com.littlezheng.displaymodule.display.test.TimeStrategy;
import com.littlezheng.displaymodule.display.test.TimeStrategyDecorator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

    private DisplayView displayView;

    private HelloDisplayStrategy helloStrategy;
    private RectStrategy rectStrategy;
    private CircleStrategy circleStrategy;
    private TimeStrategy timeStrategy;
    private TimeStrategyDecorator timeStrategyDecorator;
    private BitmapLoadStrategy bitmapLoadStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helloStrategy = new HelloDisplayStrategy(this);
        rectStrategy = new RectStrategy(this);
        circleStrategy = new CircleStrategy(this);
        timeStrategy = new TimeStrategy(this);
        timeStrategyDecorator = new TimeStrategyDecorator(this,helloStrategy);
        bitmapLoadStrategy = new BitmapLoadStrategy(this,BitmapFactory.decodeResource(getResources(),R.drawable.img_3));

        displayView = new DisplayView(this);
        displayView.setOnWindowSizeChangedListener(new DisplayView.OnWindowSizeChangedListener() {
            @Override
            public void onWindowSizeChanged(int width, int height) {
                helloStrategy.init(width, height);
                rectStrategy.init(width, height);
                circleStrategy.init(width, height);
                timeStrategy.init(width, height);
                timeStrategyDecorator.init(width, height);
                bitmapLoadStrategy.init(width, height);
            }
        });
        displayView.setDisplayStrategy(helloStrategy);
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
                displayView.setDisplayStrategy(rectStrategy);
                break;
            case R.id.item_mode_circle:
                displayView.setDisplayStrategy(circleStrategy);
                break;
            case R.id.item_time:
                displayView.setDisplayStrategy(timeStrategy);
                break;
            case R.id.item_time_hello:
                displayView.setDisplayStrategy(timeStrategyDecorator);
                break;
            case R.id.item_load_bmp:
                displayView.setDisplayStrategy(bitmapLoadStrategy);
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
