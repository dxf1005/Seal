package com.dfjy.seal.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dfjy.seal.R;
import com.dfjy.seal.bean.FileInfoTable;
import com.dfjy.seal.util.SPUtils;
import com.dfjy.seal.util.StreamTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApproveListActivity01 extends ListActivity {
    private List<FileInfoTable> list;
    private GetDataList getDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_approve_list_activity01);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getDataList != null) {
            if (getDataList.getStatus() != AsyncTask.Status.FINISHED) {
                return;
            }
        }
        getDataList = new GetDataList();
        getDataList.execute();
    }

    private List<Map<String, String>> getData(List<FileInfoTable> listFileInfo) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < listFileInfo.size(); i++) {
            map = new HashMap<String, String>();
            map.put("name", "文件名:" + listFileInfo.get(i).getFileName());
            map.put("writeTime", listFileInfo.get(i).getWriteTime());
            map.put("fileType", "文件类型:" + String.valueOf(listFileInfo.get(i).getFileTypeName()));
            map.put("sealId", "印章:" + String.valueOf(listFileInfo.get(i).getSealName()));
            list.add(map);
        }
        return list;
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getApplicationContext(),
                "查看详细信息 ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(ApproveListActivity01.this, ApproveDetailActivity.class);
        Bundle data = new Bundle();
        data.putSerializable("fileInfo", list.get(position));
        intent.putExtras(data);
        intent.putExtra("upload", "file");
        startActivity(intent);

    }

    public class GetDataList extends AsyncTask {


        @Override
        protected List<FileInfoTable> doInBackground(Object[] params) {
            return getApplyListData();
        }

        @Override
        protected void onPostExecute(Object o) {
            //list= (List<FileInfoTable>) o;
            SimpleAdapter adapter = new SimpleAdapter(ApproveListActivity01.this, getData(list),
                    R.layout.apply_list, new String[]{"name", "writeTime", "fileType", "sealId"}, new int[]{
                    R.id.name, R.id.writeTime, R.id.fileType, R.id.sealId});
            setListAdapter(adapter);
        }


        public List<FileInfoTable> getApplyListData() {
            list = new ArrayList<FileInfoTable>();
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(SPUtils.get(ApproveListActivity01.this, "url", "").toString());
            urlStr.append("/SealServer/ServletFileInfo?flag=01");
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
                    Gson gson = new Gson();
                    list = gson.fromJson(jsonStr, new TypeToken<List<FileInfoTable>>() {
                    }.getType());
                }
                conn.disconnect();
            } catch (Exception e) {
            }

            return list;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_approve_list_activity01, menu);
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
