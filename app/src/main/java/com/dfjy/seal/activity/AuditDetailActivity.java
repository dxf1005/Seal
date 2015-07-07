package com.dfjy.seal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dfjy.seal.R;
import com.dfjy.seal.bean.FileInfoTable;
import com.dfjy.seal.util.SPUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuditDetailActivity extends Activity implements View.OnClickListener {
    private FileInfoTable fileInfoTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_detail);
        fileInfoTable = (FileInfoTable) getIntent().getSerializableExtra("fileInfo");
        TextView fileName = (TextView) findViewById(R.id.detail_tv_file_name);
        //TextView fileNo=(TextView)findViewById(R.id.detail_tv_file_no);
        TextView sealNum = (TextView) findViewById(R.id.detail_tv_file_seal_num);
        TextView seal = (TextView) findViewById(R.id.detail_tv_file_seal);
        TextView fileType = (TextView) findViewById(R.id.detail_tv_file_type);
        TextView dec = (TextView) findViewById(R.id.detail_tv_file_dec);
        TextView writeTime = (TextView) findViewById(R.id.detail_tv_file_write_time);
        TextView desc = (TextView) findViewById(R.id.detail_tv_file_dec);
        fileName.setText(fileInfoTable.getFileName());
        // fileNo.setText(fileInfoTable.getFileNo());
        seal.setText(fileInfoTable.getSealName());
        sealNum.setText(fileInfoTable.getPageNum() + "");
        fileType.setText(fileInfoTable.getFileTypeName());
        dec.setText(fileInfoTable.getDescription());
        writeTime.setText(fileInfoTable.getWriteTime());
        desc.setText(fileInfoTable.getDescription());
    }


    public class downImag extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
           return getImgListData();
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

        public byte[] getImgListData() {
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(SPUtils.get(AuditDetailActivity.this, "url", "").toString());
            urlStr.append("/SealServer/ServletFileInfo?flag=20");
            try {
                URL url = new URL(urlStr.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inStream = conn.getInputStream();
                    return readStream(inStream);
                }
                conn.disconnect();
            } catch (Exception e) {
            }


           return null;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_apply_detail, menu);
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
            Intent intent = new Intent(AuditDetailActivity.this, FileUploadActivity.class);
            intent.putExtra("fileId", fileInfoTable.getFileId());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audit_look_image_btn:

                break;
            case R.id.audit_ok_btn:

                break;
            case R.id.audit_erro_btn:

                break;


        }

    }
}
