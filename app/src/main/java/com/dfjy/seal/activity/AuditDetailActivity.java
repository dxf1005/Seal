package com.dfjy.seal.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dfjy.seal.R;
import com.dfjy.seal.bean.FileInfoTable;

public class AuditDetailActivity extends Activity implements View.OnClickListener {
    private FileInfoTable fileInfoTable;
    private ProgressDialog mProgress;
    private String TAG="AuditDetailActivity";
   // private Button imgLookBtn;
    private  Button notPassBtn;
    private  Button oKPassBtn;


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
        //imgLookBtn =(Button)findViewById(R.id.audit_look_image_btn);
        oKPassBtn =(Button)findViewById(R.id.audit_ok_btn);
        notPassBtn =(Button)findViewById(R.id.audit_erro_btn);
        //imgLookBtn.setOnClickListener(this);
        oKPassBtn.setOnClickListener(this);
        notPassBtn.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick" + v.getId());
        switch (v.getId()) {

            case R.id.audit_ok_btn:

                break;
            case R.id.audit_erro_btn:

                break;


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
