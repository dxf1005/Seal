package com.dfjy.seal.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

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

public class ApprovedTabActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_approved_tab);
        FragmentManager fm = getFragmentManager();

        // Create the list fragment and add it as our sole content.
        if (fm.findFragmentById(android.R.id.content) == null) {
            ApprovedListFragment list = new ApprovedListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }
    }

    public static class ApprovedListFragment extends ListFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

        // This is the Adapter being used to display the list's data.
        //SimpleCursorAdapter mAdapter;

        // The SearchView for doing filtering.
        SearchView mSearchView;

        // If non-null, this is the current filter the user has provided.
        String mCurFilter;
        private static List<FileInfoTable> list;
        private GetDataList getDataList;

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


        public class GetDataList extends AsyncTask {


            @Override
            protected List<FileInfoTable> doInBackground(Object[] params) {
                return getApplyListData();
            }

            @Override
            protected void onPostExecute(Object o) {
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), getData(list),
                        R.layout.apply_list, new String[]{"name", "writeTime", "fileType", "sealId"}, new int[]{
                        R.id.name, R.id.writeTime, R.id.fileType, R.id.sealId});
                setListAdapter(adapter);
            }


            public List<FileInfoTable> getApplyListData() {
                list = new ArrayList<FileInfoTable>();
                StringBuffer urlStr = new StringBuffer();
                urlStr.append("http://");
                urlStr.append(SPUtils.get(getActivity(), "url", "").toString());
                urlStr.append("/SealServer/ServletFileInfo?flag=20");
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
                    //showDialog(e.getMessage());
                }

                return list;
            }

        }
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Give some text to display if there is no data.  In a real
            // application this would come from a resource.
            setEmptyText("没有已审批记录");

            // We have a menu item to show in action bar.
            setHasOptionsMenu(true);

            // Create an empty adapter we will use to display the loaded data.

            //list= (List<FileInfoTable>) o;

            getDataList = new GetDataList();
            getDataList.execute();



//            mAdapter = new SimpleCursorAdapter(getActivity(),
//                    R.layout.apply_list, null,
//                    new String[] { "name", "writeTime", "fileType", "sealId"},
//                    new int[] { android.R.id.text1, android.R.id.text2 }, 0);
//            setListAdapter(mAdapter);

            // Start out with a progress indicator.
            setListShown(false);

            // Prepare the loader.  Either re-connect with an existing one,
            // or start a new one.
            //getLoaderManager().initLoader(0, null, this);
        }

        public static class MySearchView extends SearchView {
            public MySearchView(Context context) {
                super(context);
            }

            // The normal SearchView doesn't clear its search text when
            // collapsed, so we will do this for it.
            @Override
            public void onActionViewCollapsed() {
                setQuery("", false);
                super.onActionViewCollapsed();
            }
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Place an action bar item for searching.
            MenuItem item = menu.add("Search");
            item.setIcon(android.R.drawable.ic_menu_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            mSearchView = new MySearchView(getActivity());
            mSearchView.setOnQueryTextListener(this);
            mSearchView.setOnCloseListener(this);
            mSearchView.setIconifiedByDefault(true);
            item.setActionView(mSearchView);
        }

        public boolean onQueryTextChange(String newText) {
            // Called when the action bar search text has changed.  Update
            // the search filter, and restart the loader to do a new query
            // with this filter.
            String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
            // Don't do anything if the filter hasn't actually changed.
            // Prevents restarting the loader when restoring state.
            if (mCurFilter == null && newFilter == null) {
                return true;
            }
            if (mCurFilter != null && mCurFilter.equals(newFilter)) {
                return true;
            }
            mCurFilter = newFilter;
            //getLoaderManager().restartLoader(0, null, this);
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            // Don't care about this.
            return true;
        }

        @Override
        public boolean onClose() {
            if (!TextUtils.isEmpty(mSearchView.getQuery())) {
                mSearchView.setQuery(null, true);
            }
            return true;
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            // Insert desired behavior here.
            Log.i("FragmentComplexList", "Item clicked: " + id);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_approved_tab, menu);
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
