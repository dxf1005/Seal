package com.dfjy.seal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.dfjy.seal.R;
import com.dfjy.seal.util.SPUtils;
import com.dfjy.seal.util.StreamTool;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends Activity {
    private Button loginBtn;
    private EditText userNameEdit;
    private EditText pwdEdit;
    private EditText serviceURLEdit;
    private CheckBox rememberCheckBox;
    private String userName;
    private String pwd;
    private String serviceUrl;
    private LoginTo checkLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.login_btn);
        userNameEdit = (EditText) findViewById(R.id.login_name_edit);
        pwdEdit = (EditText) findViewById(R.id.login_pwd_edit);
        serviceURLEdit = (EditText) findViewById(R.id.login_server_edit);
        rememberCheckBox = (CheckBox) findViewById(R.id.check_pwd);
        boolean choseRemember = (Boolean) SPUtils.get(this, "remember", false);
        serviceURLEdit.setText(SPUtils.get(this, "url", "").toString());
        userNameEdit.setText(SPUtils.get(this, "user", "").toString());
        if (choseRemember) {
            pwdEdit.setText(SPUtils.get(this, "pwd", "").toString());
            rememberCheckBox.setChecked(true);
        }else{
            pwdEdit.setText("");
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName =userNameEdit.getText().toString().trim();
                serviceUrl = serviceURLEdit.getText().toString().trim();
                pwd = pwdEdit.getText().toString().trim();
                if (checkLogin != null) {
                    if (checkLogin.getStatus() != AsyncTask.Status.FINISHED) {
                        return;
                    }
                }
                checkLogin = new LoginTo();
                checkLogin.execute();


            }
        });
    }


    public class LoginTo extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            return loginCheck();
        }

        @Override
        protected void onPostExecute(Object o) {
            if((Boolean)o){
                SPUtils.put(LoginActivity.this,"user",userName);
                SPUtils.put(LoginActivity.this, "url", serviceUrl);
                if (rememberCheckBox.isChecked()) {
                    SPUtils.put(LoginActivity.this, "remember", true);
                    SPUtils.put(LoginActivity.this, "pwd", pwd);
                } else {
                    SPUtils.put(LoginActivity.this, "remember", false);
                    SPUtils.put(LoginActivity.this, "pwd", "");
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(),
                        "登录用户账号或密码错误！ " , Toast.LENGTH_SHORT).show();
            }

        }

        protected boolean loginCheck(){
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(serviceUrl);
            urlStr.append("/SealServer/ServletFileInfo?flag=login");
            urlStr.append("&writeid="+userName);
            urlStr.append("&pwd="+pwd);
            try {
                URL url = new URL(urlStr.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5 * 1000);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inStream = conn.getInputStream();
                    byte[] data = StreamTool.readInputStream(inStream);
                    String jsonStr = new String(data);
                    Log.i("jsonStr", jsonStr);
                    System.out.print(jsonStr);

                    if(jsonStr.equals("0")){
                        return  false;
                    }else{
                        SPUtils.put(LoginActivity.this, "orgaId", jsonStr);
                        return true;
                    }
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.i("Exception:",e.getMessage());
            }
            return false;
        }
    }


}
