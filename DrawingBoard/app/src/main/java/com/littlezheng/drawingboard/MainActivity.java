package com.littlezheng.drawingboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.littlezheng.drawingboard.api.CircleMaker;
import com.littlezheng.drawingboard.api.LineMaker;
import com.littlezheng.drawingboard.api.OvalMaker;
import com.littlezheng.drawingboard.strategy.BasicStrategy;
import com.littlezheng.drawingboard.strategy.MeasureStrategy;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DisplayView displayView;

    private Button measureBtn;
    private Button exitMeasureBtn;

    private BasicStrategy basicStrategy;
    private MeasureStrategy measureStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        measureBtn = (Button) findViewById(R.id.btn_measure);
        exitMeasureBtn = (Button) findViewById(R.id.btn_exit_measure);
        measureBtn.setOnClickListener(this);
        exitMeasureBtn.setOnClickListener(this);

        //策略
        basicStrategy = new BasicStrategy(this);
        measureStrategy= new MeasureStrategy(this, basicStrategy);

        displayView = new DisplayView(this, basicStrategy);
        displayView.setOnWindowSizeChangedListener(new DisplayView.OnWindowSizeChangedListener() {
            @Override
            public void onWindowSizeChanged(int width, int height) {
                measureStrategy.initMask(width, height);
            }
        });
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layout_wrapper);
        linearLayout.addView(displayView);
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
                                displayView.setStrategy(measureStrategy);
                                measureStrategy.addShapeMaker(new LineMaker());
                                break;
                            case R.id.item_measure_area:
                                displayView.setStrategy(measureStrategy);
                                measureStrategy.addShapeMaker(new CircleMaker());
                                break;
                            case R.id.item_oval:
                                displayView.setStrategy(measureStrategy);
                                measureStrategy.addShapeMaker(new OvalMaker());
                                break;
                            case R.id.item_clear:
                                measureStrategy.removeAllShapeMaker();
                                break;
                            case R.id.item_clear_curr:
                                measureStrategy.removeCurrent();
                                displayView.setStrategy(measureStrategy);
                                break;
                        }
                        return false;
                    }
                });
                break;
            case R.id.btn_exit_measure:
                measureStrategy.removeAllShapeMaker();
                displayView.setStrategy(basicStrategy);
                break;
        }
    }

}
