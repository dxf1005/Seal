package com.dfjy.seal.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dfjy.seal.R;
import com.dfjy.seal.util.SPUtils;
import com.dfjy.seal.util.StreamTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DownLoadImgActivity extends Activity implements View.OnClickListener {
    private String fileId;
    private List<String> listImg;
    private Button imgUpBtn;
    private Button imgNextBtn;
    private ImageView imgView;
    private List<String> listPicId;
    private int currentIndex;
    private InputStream inStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileId = getIntent().getStringExtra("fileId");
        setContentView(R.layout.activity_down_load_img);
        imgView = (ImageView) findViewById(R.id.down_img);
        imgUpBtn = (Button) findViewById(R.id.down_up_btn);
        imgNextBtn = (Button) findViewById(R.id.down_next_btn);
        imgNextBtn.setOnClickListener(this);
        imgUpBtn.setOnClickListener(this);
        currentIndex = 0;
        imgUpBtn.setEnabled(false);
        DownImg downImgn = new DownImg();
        downImgn.execute("getPicIdList");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.down_up_btn:
                currentIndex--;
                imgNextBtn.setEnabled(true);
                if (currentIndex == 0) {
                    imgUpBtn.setEnabled(false);
                }

                DownImg downImgn = new DownImg();
                downImgn.execute("getImg");
                break;

            case R.id.down_next_btn:
                imgUpBtn.setEnabled(true);
                currentIndex++;
                if ((listPicId.size() - 1) == currentIndex) {
                    imgNextBtn.setEnabled(false);

                }
                DownImg downImgn1 = new DownImg();
                downImgn1.execute("getImg");

                break;

        }

    }

    public class DownImg extends AsyncTask {
        private ProgressDialog pDialog;

        public DownImg() {
            pDialog = ProgressDialog.show(DownLoadImgActivity.this, "正在加载...", "正在加载图片，请耐心等候...");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            if (params[0].equals("getPicIdList")) {
                getPictureInfoIDList();
                if(listPicId!=null&&listPicId.size()>0){

                    return getImgByteData();
                }

            } else if (params[0].equals("getImg")) {

                return getImgByteData();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Object o) {
            if(o!=null){
                byte[] data = (byte[]) o;
                Log.i("data.length", "" + data.length);
                if(listPicId.size()==1){
                    imgNextBtn.setEnabled(false);
                }
                int width = imgView.getWidth();
                int height = imgView.getHeight();
                BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
                factoryOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(data, 0, data.length,factoryOptions);// bitmap
                int imageWidth = factoryOptions.outWidth;
                int imageHeight = factoryOptions.outHeight;
                int scaleFactor = Math.min(imageWidth / width, imageHeight
                        / height);
                factoryOptions.inJustDecodeBounds = false;
                factoryOptions.inSampleSize = scaleFactor;
                factoryOptions.inPurgeable = true;
                Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length,factoryOptions);// bitmap
                imgView.setImageBitmap(mBitmap);

            }else{
                Toast.makeText(DownLoadImgActivity.this,"没有用印图片",Toast.LENGTH_SHORT);
                imgNextBtn.setEnabled(false);
            }

            pDialog.dismiss();

        }

        public byte[] getImgByteData() {
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(SPUtils.get(DownLoadImgActivity.this, "url", "").toString());
            urlStr.append("/SealServer/ServletImgDown?flag=img");
            urlStr.append("&picId=" + listPicId.get(currentIndex));
            Log.i("picId", listPicId.get(currentIndex));
            byte[] base64 = null;
            try {
                URL url = new URL(urlStr.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20 * 1000);
                conn.setConnectTimeout(10 * 1000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inStream = conn.getInputStream();
                    byte[] data = StreamTool.readInputStream(inStream);
                    base64 = Base64.decode(data, Base64.DEFAULT);
                    //  Log.i("base64", base64.toString());
                    return base64;

                }
                conn.disconnect();
            } catch (Exception e) {
            }


            return base64;

        }

        public List<String> getPictureInfoIDList() {
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(SPUtils.get(DownLoadImgActivity.this, "url", "").toString());
            urlStr.append("/SealServer/ServletFileInfo?flag=getImgId");
            urlStr.append("&fileId=" + fileId);
            Log.i("fileId", fileId);
            try {
                URL url = new URL(urlStr.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20 * 1000);
                conn.setConnectTimeout(10 * 1000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inStream = conn.getInputStream();
                    byte[] data = StreamTool.readInputStream(inStream);
                    String jsonStr = new String(data);
                    Log.i("jsonStr", jsonStr);
                    System.out.print(jsonStr);
                    Gson gson = new Gson();
                    listPicId = gson.fromJson(jsonStr, new TypeToken<List<String>>() {
                    }.getType());

                    return listPicId;

                }
                conn.disconnect();
            } catch (Exception e) {
            }


            return null;

        }

    }


}
