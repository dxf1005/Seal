package com.dfjy.seal.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * 第一个为doInBackground接受的参数，第二个为显示进度的参数，第第三个为doInBackground返回和onPostExecute传入的参数。
 */
public class UploadFileTask extends AsyncTask<String[], Void, String> {

    /**
     * 可变长的输入参数，与AsyncTask.exucute()对应
     */
    private ProgressDialog pDialog;
    private Activity context = null;

    public UploadFileTask(Activity ctx) {
        this.context = ctx;
        pDialog = ProgressDialog.show(context, "正在加载...", "系统正在处理您的请求");
    }

    @Override
    protected void onPostExecute(String result) {
        // 返回HTML页面的内容
        pDialog.dismiss();
        if (UploadUtils.SUCCESS.equalsIgnoreCase(result)) {
            Toast.makeText(context, "上传成功!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "上传失败!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected String doInBackground(String[]... params) {
        String fileId = "0";


        fileId = params[0][0].toString();

        File file = new File(params[0][1].toString());
        StringBuffer urlStr = new StringBuffer();
        urlStr.append("http://");
        urlStr.append(SPUtils.get(context, "url", "").toString());
        urlStr.append("/SealServer/FileImageUploadServlet");
        Log.i("urlStr", urlStr.toString());
        return UploadUtils.uploadFile(file, urlStr.toString(), fileId);

    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }


}