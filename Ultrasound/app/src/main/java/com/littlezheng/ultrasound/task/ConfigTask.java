package com.littlezheng.ultrasound.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.littlezheng.ultrasound.ultrasound.Configuration;


/**
 * Created by zxp on 2017/7/18.
 * 配置文件加载任务，该异步任务在应用启动时即加载
 */

public class ConfigTask extends AsyncTask<Configuration,Integer,Boolean> {

    private Context mContext;
    private ProgressDialog progressDialog;

    public ConfigTask(Context context){
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("配置信息加载中..");
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Configuration... params) {
        try {
            Configuration conf = params[0];
            conf.configure();
            if(mContext != null) progressDialog.dismiss();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(!aBoolean){
            Toast.makeText(mContext,"配置文件加载失败，请重新启动应用！",Toast.LENGTH_SHORT).show();
        }/*else{
//            Toast.makeText(mContext,"配置文件加载成功！",Toast.LENGTH_SHORT).show();
        }*/
    }


}
