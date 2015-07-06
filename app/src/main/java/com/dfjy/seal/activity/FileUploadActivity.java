package com.dfjy.seal.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dfjy.seal.R;
import com.dfjy.seal.util.FileUtils;
import com.dfjy.seal.util.UploadFileTask;

import java.util.ArrayList;
import java.util.List;

public class FileUploadActivity extends Activity implements View.OnClickListener {

    String fileId;
    String filePath;
    ListView imgPathListView;
    List<String> imgList;
    Button selectBtn;
    Button uploadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);
        fileId = getIntent().getStringExtra("fileId");
        selectBtn = (Button) findViewById(R.id.select_btn);
        uploadBtn = (Button) findViewById(R.id.upload_btn);
        imgPathListView = (ListView) findViewById(R.id.upload_img_list);
        selectBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
        imgList = new ArrayList<String>();
        imgPathListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, imgList));

    }

    /**
     * 回调执行的方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();
            filePath = FileUtils.getPath(this, uri);
            String fileExt = FileUtils.getExtension(filePath).toLowerCase();

            if (fileExt.equals("jpg") || fileExt.equals("png")) {


            }
            imgList.add(filePath);
            imgPathListView.invalidateViews();


            Log.i("path:", filePath);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_upload, menu);
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
            case R.id.select_btn:
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 1);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),
                            "Please install a File Manager ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.upload_btn:
                String[] str = new String[2];
                str[0] = fileId;
                for (int i = 0; i < imgList.size(); i++) {
                    if (imgList.get(i) != null && imgList.get(i).length() > 0) {
                        str[1]=imgList.get(i);
                        UploadFileTask uploadFileTask = new UploadFileTask(FileUploadActivity.this);
                        uploadFileTask.execute(str);
                    }

                }
                break;
        }
    }
}
