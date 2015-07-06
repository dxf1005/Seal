package com.dfjy.seal.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dfjy.seal.R;
import com.dfjy.seal.bean.FileTypeInfoBean;
import com.dfjy.seal.bean.MachInfoBean;
import com.dfjy.seal.bean.SealInfoBean;
import com.dfjy.seal.util.SPUtils;
import com.dfjy.seal.util.StreamTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

;

public class ApplyNewFileActivity extends Activity {
    private List<SealInfoBean> sealList;
    private List<MachInfoBean> machList;
    private List<FileTypeInfoBean> typeList;
    private GetSealAndTypeInfo getInfo;
    private Spinner machSpinner;
    private Spinner sealSpinner;
    private Spinner fileTypeSpinner;
    private EditText fileNameEdit;
    private EditText sealNumEdit;
    private EditText fileDecEdit;
    private Button newBtn;
    private Button cancelBtn;
    private ArrayAdapter<String> machAdapter = null;
    private ArrayAdapter<String> sealAdapter = null;
    private ArrayAdapter<String> typeAdapter = null;
    private String orgaID;
    private String machID;
    private String fileName;
    private String sealNum;
    private String sealId;
    private String fileTypeID;
    private String fileDec;
    private String filePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_new_file);
        fileNameEdit = (EditText) findViewById(R.id.new_edit_file_name);
        // fileNoEdit = (EditText) findViewById(R.id.new_edit_file_no);
        sealNumEdit = (EditText) findViewById(R.id.new_edit_file_seal_num);
        fileDecEdit = (EditText) findViewById(R.id.new_edit_file_dec);
        machSpinner = (Spinner) findViewById(R.id.new_spinner_mach);
        sealSpinner = (Spinner) findViewById(R.id.new_spinner_file_seal);
        fileTypeSpinner = (Spinner) findViewById(R.id.new_spinner_file_type);
        newBtn = (Button) findViewById(R.id.new_btn);
        //cancelBtn = (Button) findViewById(R.id.new_cancel_btn);
        machSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                machID = machList.get(position).getMachID();
                getInfo = new GetSealAndTypeInfo();
                getInfo.execute("seal");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = fileNameEdit.getText().toString().trim();
                //fileNo = fileNoEdit.getText().toString().trim();
                fileDec = fileDecEdit.getText().toString().trim();
                sealNum = sealNumEdit.getText().toString().trim();
                sealId = sealList.get((int) sealSpinner.getSelectedItemId()).getSealId();
                fileTypeID = typeList.get((int) fileTypeSpinner.getSelectedItemId()).getFileTypeId();
                getInfo = new GetSealAndTypeInfo();
                getInfo.execute("new");


            }
        });
        if (getInfo != null) {
            if (getInfo.getStatus() != AsyncTask.Status.FINISHED) {
                return;
            }
        }
        getInfo = new GetSealAndTypeInfo();
        getInfo.execute("mach");


    }


    private String[] getData(List<SealInfoBean> listSealInfo) {
        String[] items = new String[listSealInfo.size()];
        for (int i = 0; i < listSealInfo.size(); i++) {
            items[i] = listSealInfo.get(i).getSealName();
        }
        return items;
    }

    private String[] getTypeData(List<FileTypeInfoBean> listInfo) {
        String[] items = new String[listInfo.size()];
        for (int i = 0; i < listInfo.size(); i++) {
            items[i] = listInfo.get(i).getFileTypeName();
        }
        return items;
    }

    private String[] getMachData(List<MachInfoBean> listMachInfo) {
        String[] items = new String[listMachInfo.size()];
        for (int i = 0; i < listMachInfo.size(); i++) {
            items[i] = listMachInfo.get(i).getMachName();
        }
        return items;
    }

    public class GetSealAndTypeInfo extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            String flag = "";
            if (params[0].equals("new")) {
                try {
                    if (insertFileInfo()) {
                        flag = "new";
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else if (params[0].equals("mach")) {
                getMachList();
                getTypeList();
                flag = "mach";

            } else if (params[0].equals("seal")) {
                getSealList();
                flag = "seal";
            }
            return flag;

        }

        @Override
        protected void onPostExecute(Object o) {
            if (o.equals("new")) {

                // Intent intent = new Intent(ApplyNewFileActivity.this, ApplyListActivity.class);
                // startActivity(intent);
                ApplyNewFileActivity.this.finish();

            } else if (o.equals("mach")) {
                machAdapter = new ArrayAdapter<String>(ApplyNewFileActivity.this, android.R.layout.simple_spinner_item, getMachData(machList));
                machAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                machSpinner.setAdapter(machAdapter);

                typeAdapter = new ArrayAdapter<String>(ApplyNewFileActivity.this, android.R.layout.simple_spinner_item, getTypeData(typeList));
                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fileTypeSpinner.setAdapter(typeAdapter);
            } else if (o.equals("seal")) {

                sealAdapter = new ArrayAdapter<String>(ApplyNewFileActivity.this, android.R.layout.simple_spinner_item, getData(sealList));
                sealAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sealSpinner.setAdapter(sealAdapter);

            }


        }

        private boolean insertFileInfo() throws UnsupportedEncodingException {
            String str = "http://" + SPUtils.get(ApplyNewFileActivity.this, "url", "").toString() + "/SealServer/ServletFileInfo?";
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("flag=newInsert");
            urlStr.append("&orgaId=" + SPUtils.get(ApplyNewFileActivity.this, "orgaId", "").toString());
            urlStr.append("&writeid=" + SPUtils.get(ApplyNewFileActivity.this, "user", "").toString());
            urlStr.append("&machId=" + machID);
            urlStr.append("&fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            //urlStr.append("&fileNo=" +  URLEncoder.encode(fileNo,"UTF-8"));
            urlStr.append("&sealNum=" + sealNum);
            urlStr.append("&sealId=" + sealId);
            urlStr.append("&fileTypeId=" + fileTypeID);
            urlStr.append("&fileDec=" + URLEncoder.encode(fileDec, "UTF-8"));
            try {
                //str =str+ URLEncoder.encode(urlStr.toString(), "UTF-8");
                URL url = new URL(str + urlStr.toString());
                Log.i("jsonStr", str + urlStr.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(5 * 1000);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inStream = conn.getInputStream();
                    byte[] data = StreamTool.readInputStream(inStream);
                    String jsonStr = new String(data);
                    Log.i("jsonStr", jsonStr);
                    System.out.print(jsonStr);
                    if (jsonStr.equals("0")) {
                        return true;
                    }

                }
                conn.disconnect();
            } catch (Exception e) {

            }

            return false;
        }

        private List<MachInfoBean> getMachList() {
            machList = new ArrayList<MachInfoBean>();
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(SPUtils.get(ApplyNewFileActivity.this, "url", "").toString());
            urlStr.append("/SealServer/ServletFileInfo?flag=newMach");
            urlStr.append("&orgaId=" + SPUtils.get(ApplyNewFileActivity.this, "orgaId", "").toString());
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
                    machList = gson.fromJson(jsonStr, new TypeToken<List<MachInfoBean>>() {
                    }.getType());
                }
                conn.disconnect();
            } catch (Exception e) {

            }

            return machList;
        }

        private List<SealInfoBean> getSealList() {
            sealList = new ArrayList<SealInfoBean>();
            StringBuffer urlStr = new StringBuffer();
            urlStr.append("http://");
            urlStr.append(SPUtils.get(ApplyNewFileActivity.this, "url", "").toString());
            urlStr.append("/SealServer/ServletFileInfo?flag=newSeal");
            urlStr.append("&orgaId=" + SPUtils.get(ApplyNewFileActivity.this, "orgaId", "").toString());
            urlStr.append("&machId=" + machID);
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
                    sealList = gson.fromJson(jsonStr, new TypeToken<List<SealInfoBean>>() {
                    }.getType());
                }
                conn.disconnect();
            } catch (Exception e) {

            }

            return sealList;
        }

        private boolean uploadFile() {
            typeList = new ArrayList<FileTypeInfoBean>();
            StringBuffer urlStr = new StringBuffer();

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
                }
                conn.disconnect();
            } catch (Exception e) {

            }

            return false;
        }

    }


    private List<FileTypeInfoBean> getTypeList() {
        typeList = new ArrayList<FileTypeInfoBean>();
        StringBuffer urlStr = new StringBuffer();
        urlStr.append("http://");
        urlStr.append(SPUtils.get(ApplyNewFileActivity.this, "url", "").toString());
        urlStr.append("/SealServer/ServletFileInfo?flag=newType");
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
                typeList = gson.fromJson(jsonStr, new TypeToken<List<FileTypeInfoBean>>() {
                }.getType());
            }
            conn.disconnect();
        } catch (Exception e) {

        }

        return typeList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_apply_new_file, menu);
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
