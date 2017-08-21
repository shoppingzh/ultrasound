package com.littlezheng.drawingboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.PopupMenu;
import android.text.Layout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DisplayWindow displayWindow;

    private Button measureBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        measureBtn = (Button) findViewById(R.id.btn_measure);
        measureBtn.setOnClickListener(this);

        displayWindow = new DisplayWindow(this);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layout_wrapper);
        linearLayout.addView(displayWindow);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_measure:
                PopupMenu popupMenu = new PopupMenu(this,v, Gravity.TOP);
                popupMenu.getMenuInflater().inflate(R.menu.menu_measure,popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_measure_length:
                                break;
                        }
                        return false;
                    }
                });
                break;
        }
    }

}
