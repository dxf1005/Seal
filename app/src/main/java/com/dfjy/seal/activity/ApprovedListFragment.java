package com.dfjy.seal.activity;

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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;

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
import java.util.List;

public class ApprovedListFragment extends ListFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    SearchView mSearchView;

    // If non-null, this is the current filter the user has provided.
    String mCurFilter;
    private static List<FileInfoTable> list;
    private GetDataList getDataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("ApproveListFragment--->onCreate");
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("没有待审批记录");
        setHasOptionsMenu(true);
        getDataList = new GetDataList();
        getDataList.execute();
        setListShown(false);


    }


    public class GetDataList extends AsyncTask {


        @Override
        protected List<FileInfoTable> doInBackground(Object[] params) {
            return getApplyListData();
        }

        @Override
        protected void onPostExecute(Object o) {

            BaseAdapter baseAdapter = (BaseAdapter) getListAdapter();
            FileInfoListAdapter adapter = (FileInfoListAdapter) baseAdapter;
            if (adapter != null) {

                adapter.setmList(list);
                setListAdapter(adapter);
                // adapter.notifyDataSetChanged();
            } else {

                adapter = new FileInfoListAdapter(getActivity());
                adapter.setmList(list);
                setListAdapter(adapter);
            }


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
            }

            return list;
        }

    }


    public static class MySearchView extends SearchView {
        public MySearchView(Context context) {
            super(context);
        }


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
