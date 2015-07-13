package com.dfjy.seal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
                intent = new Intent(MainActivity.this,ApproveListActivity01.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
