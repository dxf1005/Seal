package com.dfjy.seal.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import java.util.Calendar;
import java.util.Locale;

public class SealPictureUploadActivity extends Activity implements View.OnClickListener {

    ImageView img;
    Button imgPicBtn;
    Button imgUploadBtn;
    String fileName;
    String fileId;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seal_picture_upload);
        Intent intent = getIntent();
        fileId = intent.getStringExtra("fileId");
        img = (ImageView) findViewById(R.id.seal_img_view);
        imgPicBtn = (Button) findViewById(R.id.seal_img_pic);
        imgUploadBtn = (Button) findViewById(R.id.seal_img_upload);
        imgPicBtn.setOnClickListener(this);
        imgUploadBtn.setOnClickListener(this);
        imgUploadBtn.setEnabled(false);

    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
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
        switch (v.getId()) {
            case R.id.seal_img_pic:
                // 利用系统自带的相机应用:拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri=Uri.fromFile(getOutputMediaFile());
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, 1);
                break;
            case R.id.seal_img_upload:
                UploadImgTask uploadImg = new UploadImgTask();
                uploadImg.execute();

                break;
        }

    }

    class UploadImgTask extends AsyncTask {

        private ProgressDialog progressDialog;

        public UploadImgTask() {
            progressDialog= ProgressDialog.show(SealPictureUploadActivity.this,"","正在上传图片，请耐心等候...");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            File file = new File(fileName);
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(SPUtils.get(SealPictureUploadActivity.this, "url", "").toString());
            urlStr.append("/SealServer/FileImageUploadServlet");
            return UploadUtils.uploadFile(file, urlStr.toString(), fileId, "img");

        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
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

    /**
     * 获得图片保存路径
     * @return
     */
    private  File getOutputMediaFile() {
        String LOG_TAG = "getOutputMediaFile";
        File mediaStorageDir = null;
        File mediaFile;
        try {
            String sdStatus = Environment.getExternalStorageState();
            if (sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                mediaStorageDir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "sealImage");
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d(LOG_TAG,
                                "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }
        new DateFormat();
        String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        fileName =mediaStorageDir.getPath() + File.separator
                + "IMG_" + name + ".jpg";
        mediaFile = new File(fileName);


        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            img.setVisibility(View.VISIBLE);
            if (data != null)
            {
                Toast.makeText(this, "Image saved to:\n" + data.getData(),
                        Toast.LENGTH_LONG).show();

                if (data.hasExtra("data"))
                {
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    img.setImageBitmap(thumbnail);
                }
            }else{
                int width = img.getWidth();
                int height = img.getHeight();
                BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
                factoryOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);
                int imageWidth = factoryOptions.outWidth;
                int imageHeight = factoryOptions.outHeight;
                int scaleFactor = Math.min(imageWidth / width, imageHeight
                        / height);
                factoryOptions.inJustDecodeBounds = false;
                factoryOptions.inSampleSize = scaleFactor;
                factoryOptions.inPurgeable = true;
                Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                        factoryOptions);
                img.setImageBitmap(bitmap);
            }
            try {
                imgPicBtn.setEnabled(false);
                imgUploadBtn.setEnabled(true);
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }

        }
    }
}
