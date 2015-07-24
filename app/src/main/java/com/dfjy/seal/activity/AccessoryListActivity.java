package com.dfjy.seal.activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.dfjy.seal.R;
import com.dfjy.seal.util.FileUtils;
import com.dfjy.seal.util.SPUtils;
import com.dfjy.seal.util.StreamTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
public class AccessoryListActivity extends ListActivity {

    String fileId;
    List<String> accessoryList;
    String accessoryName;
    String pathName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        fileId = intent.getStringExtra("fileId");
        GetAccessoryList getAccessoryList = new GetAccessoryList();
        getAccessoryList.execute("list");
    }

    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        // 先创建对话框构造器
        AlertDialog.Builder builder = new AlertDialog.Builder(AccessoryListActivity.this);
        // 创建完后设置对话框的属性
        // 标题
        builder.setMessage("查看附件?")
                // 设置第一个按钮的标签及其事件监听器
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                accessoryName = accessoryList.get(position);
                                GetAccessoryList getAccessoryList = new GetAccessoryList();
                                getAccessoryList.execute("file");

                            }
                        })
                        // 设置第二个按钮的标签及其事件监听器
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        // 用对话框构造器创建对话框
        AlertDialog alert = builder.create();
        // 显示对话框
        alert.show();


    }

    public class GetAccessoryList extends AsyncTask {

        private ProgressDialog progressDialog;

        public GetAccessoryList() {
            progressDialog=ProgressDialog.show(AccessoryListActivity.this,"","正在加载数据......");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            if (params[0].equals("list")) {
                getAccessList();
                return "list";
            } else if (params[0].equals("file")) {
                try {
                    getFileByteData();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return "file";
            }
            return "";

        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            if (o.equals("list")) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AccessoryListActivity.this, android.R.layout.simple_expandable_list_item_1, accessoryList);
                setListAdapter(adapter);
            } else if (o.equals("file")) {
                startActivity(FileUtils.openFile(pathName));

            }

        }

        public String  getFileByteData() throws UnsupportedEncodingException {
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(SPUtils.get(AccessoryListActivity.this, "url", "").toString());
            urlStr.append("/SealServer/ServletImgDown?flag=accessory");
            urlStr.append("&fileId=" + fileId);
            urlStr.append("&accessoryName=" + URLEncoder.encode(accessoryName, "UTF-8"));
            byte[] base64 = null;
            OutputStream output = null;
            try {
                URL url = new URL(urlStr.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20 * 1000);
                conn.setConnectTimeout(10 * 1000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inStream = conn.getInputStream();
                    byte[] data = StreamTool.readInputStream(inStream);
                    base64 = Base64.decode(data, Base64.DEFAULT);
                    String SDCard = Environment.getExternalStorageDirectory() + "";
                    pathName = SDCard + "/sealFile/"+fileId+ "/" + accessoryName;//文件存储路径
                    File file = new File(pathName);
                    if (file.exists()) {
                        System.out.println("exits");
                        return "";
                    } else {
                        String dir = SDCard + "/sealFile/"+fileId;
                        new File(dir).mkdirs();//新建文件夹
                        file.createNewFile();//新建文件
                        output = new FileOutputStream(file);
                        output.write(base64);
                    }
                    output.flush();
                    //读取大文件
                    return "SUCCESS";

                }
                conn.disconnect();
            } catch (Exception e) {
                Log.e("getFileByteData",e.getMessage());
            }


            return "FAIL";

        }

        public List<String> getAccessList() {
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(SPUtils.get(AccessoryListActivity.this, "url", "").toString());
            urlStr.append("/SealServer/ServletFileInfo?flag=getAccessory");
            urlStr.append("&fileId=" + fileId);
            Log.i("fileId", fileId);
            try {
                URL url = new URL(urlStr.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5 * 1000);
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inStream = conn.getInputStream();
                    byte[] data = StreamTool.readInputStream(inStream);
                    String jsonStr = new String(data);
                    Log.i("jsonStr", jsonStr);
                    System.out.print(jsonStr);
                    Gson gson = new Gson();
                    accessoryList = gson.fromJson(jsonStr, new TypeToken<List<String>>() {
                    }.getType());

                    return accessoryList;

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
        getMenuInflater().inflate(R.menu.menu_accessory_list, menu);
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
