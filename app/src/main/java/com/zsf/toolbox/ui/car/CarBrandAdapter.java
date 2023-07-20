package com.zsf.toolbox.ui.car;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsf.toolbox.R;
import com.zsf.toolbox.bean.CarBrandBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EWorld
 * 2022/11/21
 */
public class CarBrandAdapter extends BaseAdapter {
    private static final String TAG = "CarBrandAdapter";
    private List<CarBrandBean.ResultDTO> mDataList;
    private Context mContext;

    public CarBrandAdapter(Context context, List<CarBrandBean.ResultDTO> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    public void refreshAll(List<CarBrandBean.ResultDTO> dataList) {
        if (mDataList.size() > 0) {
            mDataList.clear();
        }
        mDataList.addAll(dataList);
        Log.d(TAG, ">>>>>>> car brand size = " + mDataList.size());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CarBrandHolder brandHolder = null;
        if (brandHolder == null) {
            brandHolder = new CarBrandHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_brand, null);
            brandHolder.brandLogo = convertView.findViewById(R.id.iv_brand_logo);
            brandHolder.brandName = convertView.findViewById(R.id.tv_brand_name);
            convertView.setTag(brandHolder);
        } else {
            brandHolder = (CarBrandHolder) convertView.getTag();
        }
        Glide.with(mContext).load(mDataList.get(position).getBrand_logo()).into(brandHolder.brandLogo);
        brandHolder.brandName.setText(mDataList.get(position).getBrand_name());

        return convertView;
    }

    private class CarBrandHolder {
        ImageView brandLogo;
        TextView brandName;
    }
}
