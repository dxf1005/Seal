package com.dfjy.seal.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dfjy.seal.R;
import com.dfjy.seal.bean.FileInfoTable;
import com.dfjy.seal.util.Constances;
import com.dfjy.seal.util.SPUtils;
import com.dfjy.seal.util.StreamTool;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuditDetailActivity extends Activity implements View.OnClickListener {
    private FileInfoTable fileInfoTable;
    private ProgressDialog mProgress;
    private String TAG="AuditDetailActivity";
    private  Button notPassBtn;
    private  Button oKPassBtn;
    private String stateId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_detail);
        fileInfoTable = (FileInfoTable) getIntent().getSerializableExtra("fileInfo");
        TextView fileName = (TextView) findViewById(R.id.detail_tv_file_name);
        TextView sealNum = (TextView) findViewById(R.id.detail_tv_file_seal_num);
        TextView seal = (TextView) findViewById(R.id.detail_tv_file_seal);
        TextView fileType = (TextView) findViewById(R.id.detail_tv_file_type);
        TextView dec = (TextView) findViewById(R.id.detail_tv_file_dec);
        TextView writeTime = (TextView) findViewById(R.id.detail_tv_file_write_time);
        TextView desc = (TextView) findViewById(R.id.detail_tv_file_dec);
        fileName.setText(fileInfoTable.getFileName());
        seal.setText(fileInfoTable.getSealName());
        sealNum.setText(fileInfoTable.getPageNum() + "");
        fileType.setText(fileInfoTable.getFileTypeName());
        dec.setText(fileInfoTable.getDescription());
        writeTime.setText(fileInfoTable.getWriteTime());
        desc.setText(fileInfoTable.getDescription());
        oKPassBtn =(Button)findViewById(R.id.audit_ok_btn);
        notPassBtn =(Button)findViewById(R.id.audit_erro_btn);
        oKPassBtn.setOnClickListener(this);
        notPassBtn.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick" + v.getId());
        UpdateFIleState updateFIleState = new UpdateFIleState();
        switch (v.getId()) {

            case R.id.audit_ok_btn:
                stateId ="80";

                updateFIleState.execute();
                break;
            case R.id.audit_erro_btn:
                stateId ="30";
                updateFIleState.execute();
                break;


        }

    }

    public class UpdateFIleState extends AsyncTask {

        //public List<FileInfoTable> list;

        @Override
        protected String doInBackground(Object[] params) {
            sealState();
            AuditDetailActivity.this.finish();
            return "";
        }

        @Override
        protected void onPostExecute(Object o) {

        }


        public String sealState() {
            String jsonStr="";
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(SPUtils.get(AuditDetailActivity.this, "url", "").toString());
            urlStr.append("/SealServer/ServletFileInfo?flag="+ Constances.UPDATE_SEAL_STATE_FLAG);
            urlStr.append("&stateId="+stateId);
            urlStr.append("&fileId="+fileInfoTable.getFileId());
            try {
                URL url = new URL(urlStr.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5 * 1000);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inStream = conn.getInputStream();
                    byte[] data = StreamTool.readInputStream(inStream);
                    jsonStr = new String(data);
                    Log.i("jsonStr", jsonStr);
                    System.out.print(jsonStr);
                    return jsonStr;
                }
                conn.disconnect();
            } catch (Exception e) {
                //showDialog(e.getMessage());
            }

            return jsonStr;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.menu_audit_detail, menu);
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
            Intent intent = new Intent(AuditDetailActivity.this, DownLoadImgActivity.class);
            intent.putExtra("fileId", String.valueOf(fileInfoTable.getFileId()));
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
