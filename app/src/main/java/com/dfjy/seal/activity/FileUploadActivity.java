package com.dfjy.seal.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    List<String> imgListName;
    Button selectBtn;
    Button uploadBtn;
    String uploadFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);
        Intent intent =getIntent();
        fileId = intent.getStringExtra("fileId");
        uploadFlag =intent.getStringExtra("upload");
        selectBtn = (Button) findViewById(R.id.select_btn);
        uploadBtn = (Button) findViewById(R.id.upload_btn);
        imgPathListView = (ListView) findViewById(R.id.upload_img_list);
        selectBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
        imgList = new ArrayList<String>();
        imgListName =new ArrayList<String>();
        imgPathListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, imgListName));

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
            imgListName.add(FileUtils.getFileName(filePath));
            imgPathListView.invalidateViews();


            Log.i("path:", filePath);
        }

        super.onActivityResult(requestCode, resultCode, data);
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
                String[] str = new String[3+ imgList.size()];
                str[0] = fileId;
                str[1]=uploadFlag;
                for (int i = 0; i < imgList.size(); i++) {

                        str[2+i]=imgList.get(i);


                }

                UploadFileTask uploadFileTask = new UploadFileTask(FileUploadActivity.this);
                uploadFileTask.execute(str);
                imgListName.clear();
                imgList.clear();
                imgPathListView.invalidateViews();
                break;
        }
    }
}
