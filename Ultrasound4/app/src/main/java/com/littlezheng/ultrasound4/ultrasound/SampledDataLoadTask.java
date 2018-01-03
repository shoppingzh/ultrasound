package com.littlezheng.ultrasound4.ultrasound;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


/**
 * Created by zxp on 2017/7/18.
 * 配置文件加载任务，该异步任务在应用启动时即加载
 */

public class SampledDataLoadTask extends AsyncTask<SampledData, Integer, Boolean> {

    private Context mContext;
    private ProgressDialog progressDialog;

    public SampledDataLoadTask(Context context) {
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
    protected Boolean doInBackground(SampledData... params) {
        try {
            SampledData data = params[0];
            data.load();
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
