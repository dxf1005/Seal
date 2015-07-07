package com.dfjy.seal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dfjy.seal.R;
import com.dfjy.seal.bean.FileInfoTable;

public class ApplyDetailActivity extends Activity {
    private FileInfoTable fileInfoTable;
    String uploadFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_detail);
        Intent intent =getIntent();
        fileInfoTable=(FileInfoTable)intent.getSerializableExtra("fileInfo");
        uploadFlag = intent.getStringExtra("upload");
        TextView fileName=(TextView)findViewById(R.id.detail_tv_file_name);
        //TextView fileNo=(TextView)findViewById(R.id.detail_tv_file_no);
        TextView sealNum=(TextView)findViewById(R.id.detail_tv_file_seal_num);
        TextView seal=(TextView)findViewById(R.id.detail_tv_file_seal);
        TextView fileType=(TextView)findViewById(R.id.detail_tv_file_type);
        TextView dec=(TextView)findViewById(R.id.detail_tv_file_dec);
        TextView writeTime=(TextView)findViewById(R.id.detail_tv_file_write_time);
        TextView desc=(TextView)findViewById(R.id.detail_tv_file_dec);
        fileName.setText(fileInfoTable.getFileName());
       // fileNo.setText(fileInfoTable.getFileNo());
        seal.setText(fileInfoTable.getSealName());
        sealNum.setText(fileInfoTable.getPageNum()+"");
        fileType.setText(fileInfoTable.getFileTypeName());
        dec.setText(fileInfoTable.getDescription());
        writeTime.setText(fileInfoTable.getWriteTime());
        desc.setText(fileInfoTable.getDescription());





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
            Intent intent = new Intent(ApplyDetailActivity.this,FileUploadActivity.class);
            String filedID =String.valueOf(fileInfoTable.getFileId());
            intent.putExtra("fileId",filedID);
            intent.putExtra("upload",uploadFlag);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
