package com.dfjy.seal.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dfjy.seal.R;
import com.dfjy.seal.util.SPUtils;
import com.dfjy.seal.util.UploadUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class SealPictureUploadActivity extends Activity implements View.OnClickListener {

    ImageView img;
    Button imgPicBtn;
    Button imgUploadBtn;
    String fileName;


   String fileId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seal_picture_upload);
        Intent intent = getIntent();
        fileId=intent.getStringExtra("fileId");
        img=(ImageView)findViewById(R.id.seal_img_view);
        imgPicBtn=(Button)findViewById(R.id.seal_img_pic);
        imgUploadBtn=(Button)findViewById(R.id.seal_img_upload);
        imgPicBtn.setOnClickListener(this);
        imgUploadBtn.setOnClickListener(this);
        imgUploadBtn.setEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seal_picture_upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.seal_img_pic:
                // 利用系统自带的相机应用:拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);

                break;
            case R.id.seal_img_upload:
                UploadImgTask uploadImg = new UploadImgTask();
                uploadImg.execute();

                break;
        }

    }

    class UploadImgTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            File file = new File(fileName);
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(SPUtils.get(SealPictureUploadActivity.this, "url", "").toString());
            urlStr.append("/SealServer/FileImageUploadServlet");
            return UploadUtils.uploadFile(file, urlStr.toString(), fileId,"img");

        }

        @Override
        protected void onPostExecute(Object o) {

            if (UploadUtils.SUCCESS.equalsIgnoreCase(o.toString())) {

                Toast.makeText(SealPictureUploadActivity.this, "上传成功!", Toast.LENGTH_LONG).show();
                imgUploadBtn.setEnabled(false);
                img.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(SealPictureUploadActivity.this, "上传失败!", Toast.LENGTH_LONG).show();
            }
            imgPicBtn.setEnabled(true);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();

            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            //Toast.makeText(this, name, Toast.LENGTH_LONG).show();
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            FileOutputStream b = null;

            File file = new File("/sdcard/Image/");
            file.mkdirs();// 创建文件夹
            fileName = "/sdcard/Image/"+name;

            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try
            {img.setVisibility(View.VISIBLE);

                img.setImageBitmap(bitmap);// 将图片显示在ImageView里
                imgPicBtn.setEnabled(false);
                imgUploadBtn.setEnabled(true);
            }catch(Exception e)
            {
                Log.e("error", e.getMessage());
            }

        }
    }
}
