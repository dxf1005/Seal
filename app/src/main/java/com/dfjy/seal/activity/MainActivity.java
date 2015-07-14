package com.dfjy.seal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dfjy.seal.R;


public class MainActivity extends Activity implements View.OnClickListener {
    private Button applyBtn;
    private Button approveBtn;
    private Button auditBtn;
    private Button pictureBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applyBtn = (Button) findViewById(R.id.apply_btn);
        approveBtn = (Button) findViewById(R.id.approve_btn);
        auditBtn = (Button) findViewById(R.id.audit_btn);
        pictureBtn = (Button) findViewById(R.id.picture_btn);
        applyBtn.setOnClickListener(this);
        approveBtn.setOnClickListener(this);
        auditBtn.setOnClickListener(this);
        pictureBtn.setOnClickListener(this);
    }

    public class mainPopeCheck extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.apply_btn:
                intent = new Intent(MainActivity.this,ApplyListActivity.class);
                startActivity(intent);
                break;
            case R.id.approve_btn:
                //intent = new Intent(MainActivity.this,ApproveListActivity.class);
                intent = new Intent(MainActivity.this,ApproveListActivity.class);
                startActivity(intent);
                break;
            case  R.id.audit_btn:
                intent = new Intent(MainActivity.this,AuditListActivity.class);
                startActivity(intent);
                break;
            case  R.id.picture_btn:
                intent = new Intent(MainActivity.this,SealPictureActivity.class);
                startActivity(intent);
                break;

        }

    }


}
