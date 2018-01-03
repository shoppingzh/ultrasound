package com.littlezheng.newultrasound.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


/**
 * 配置文件加载任务，该异步任务在应用启动时即加载
 *
 * Created by zxp on 2017/7/18.
 */

public class SampleDataLoadTask extends AsyncTask<Void, Integer, Boolean> {

    private Context mContext;
    private ProgressDialog progressDialog;

    public SampleDataLoadTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("请稍候..");
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... param) {
        try {
            SampleData data = SampleData.getInstance();
            data.loadThirdSampleData(mContext.getAssets().open("thirdsample.data"));
            if (mContext != null) progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (!aBoolean) {
            Toast.makeText(mContext, "采样数据加载失败，请重新启动应用！", Toast.LENGTH_SHORT).show();
        }
    }


}
