package com.littlezheng.newultrasound_displaymodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ((ViewGroup)findViewById(R.id.layout_window)).addView(new DisplayView(this));
    }

}
