package com.dfjy.seal.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dfjy.seal.R;
import com.dfjy.seal.bean.FileInfoTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Project：SealCop
 * User: dongxf(dongxf@orient-it.com)
 * Date: 2015-07-07
 * Time: 10:28
 */
public class FileInfoListAdapter extends BaseAdapter {
    private List<FileInfoTable> mList = new ArrayList<FileInfoTable>();
    private Context mContext;

    public FileInfoListAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    /**
     * 刷新调用setmList
     *
     * @param mList
     */
    public void setmList(List<FileInfoTable> mList) {
        this.mList = mList;
    }


    /**
     * 加载调用addmList
     *
     * @param mList
     */
    public void addmList(List<FileInfoTable> mList) {
        this.mList.addAll(mList);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public FileInfoTable getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        FileInfoViewHolder holder;
        FileInfoTable outBean = mList.get(position);
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.apply_list, null);
            holder = new FileInfoViewHolder();
            holder.tv_name = (TextView) view.findViewById(R.id.name);
            holder.tv_seal = (TextView) view.findViewById(R.id.sealId);
            holder.tv_fileType = (TextView) view.findViewById(R.id.fileType);
            holder.tv_time = (TextView) view.findViewById(R.id.writeTime);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (FileInfoViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(outBean.getFileName());
        holder.tv_seal.setText(outBean.getSealName());
        holder.tv_fileType.setText(outBean.getFileTypeName());
        holder.tv_time.setText(outBean.getWriteTime());
        return view;
    }

    private static class FileInfoViewHolder {
        TextView tv_name;
        TextView tv_seal;
        TextView tv_fileType;
        TextView tv_time;

    }
}
