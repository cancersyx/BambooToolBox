package com.zsf.toolbox.ui.appmanage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsf.toolbox.R;

import java.util.List;

/**
 * Created by EWorld
 * 2022/6/9
 */
public class AppAdapter extends BaseAdapter {
    private Context mContext;
    private List<AppInfo> mAppInfoList;

    public AppAdapter(Context context, List<AppInfo> appInfoList) {
        mContext = context;
        mAppInfoList = appInfoList;
    }

    @Override
    public int getCount() {
        return mAppInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppHolder holder = null;
        if (holder == null) {
            holder = new AppHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, null);
            holder.icon = convertView.findViewById(R.id.iv_logo);
            holder.name = convertView.findViewById(R.id.tv_app_label);
            holder.pkg = convertView.findViewById(R.id.tv_pkg_str);
            holder.version = convertView.findViewById(R.id.tv_version);
            holder.size = convertView.findViewById(R.id.tv_size);
            convertView.setTag(holder);
        } else {
            holder = (AppHolder) convertView.getTag();
        }
        holder.icon.setBackground(mAppInfoList.get(position).icon);
        holder.name.setText(mAppInfoList.get(position).label);
        holder.version.setText("版本：" + mAppInfoList.get(position).version);
        holder.pkg.setText("包名：" + mAppInfoList.get(position).package_name);
        holder.size.setText("大小：" + mAppInfoList.get(position).size + "M");
        return convertView;
    }

    class AppHolder {
        ImageView icon;
        TextView name;
        TextView pkg;
        TextView version;
        TextView size;
    }
}
