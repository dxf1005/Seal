package com.dfjy.seal.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.dfjy.seal.R;
import com.dfjy.seal.util.SPUtils;
import com.dfjy.seal.util.StreamTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DownLoadImgActivity extends Activity {
    private String fileId;
    private List<String> listImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileId=getIntent().getStringExtra("fileId");
        setContentView(R.layout.activity_down_load_img);
        DownImg downImgn = new DownImg();
        downImgn.execute();
    }

    public class DownImg extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {

             getImgListData();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {


        }

        public byte[] readStream(InputStream inStream) throws Exception {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            inStream.close();
            return outStream.toByteArray();
        }

        public List<String> getImgListData() {
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(SPUtils.get(DownLoadImgActivity.this, "url", "").toString());
            urlStr.append("/SealServer/ServletImgDown?");
            urlStr.append("fileId="+fileId);
            try {
                URL url = new URL(urlStr.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inStream = conn.getInputStream();
                    byte[] data = StreamTool.readInputStream(inStream);
                    String jsonStr = new String(data);
                    Log.i("jsonStr", jsonStr);
                    System.out.print(jsonStr);
                    Gson gson = new Gson();
                    listImg = gson.fromJson(jsonStr, new TypeToken<List<String>>(){}.getType());

                    return listImg;
                }
                conn.disconnect();
            } catch (Exception e) {
            }


            return null;

        }

    }


}
