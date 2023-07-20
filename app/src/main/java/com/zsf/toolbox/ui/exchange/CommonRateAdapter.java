package com.zsf.toolbox.ui.exchange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zsf.toolbox.R;
import com.zsf.toolbox.bean.CommonExchangeRateBean;

import java.util.List;

/**
 * Created by EWorld
 * 2022/6/14
 */
public class CommonRateAdapter extends BaseAdapter {
    private List<List<String>> mDataList;

    public CommonRateAdapter(List<List<String>> dataList) {
        mDataList = dataList;
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
        RateHolder holder = null;
        if (holder == null) {
            holder = new RateHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common_exchange_rate, null);
            holder.currencyName = convertView.findViewById(R.id.tv_currency_name);
            holder.unit = convertView.findViewById(R.id.tv_unit);
            holder.buyingRate = convertView.findViewById(R.id.tv_buying_rate);
            holder.cashBuyingRate = convertView.findViewById(R.id.tv_cash_buying_rate);
            holder.sellingRate = convertView.findViewById(R.id.tv_selling_rate);
            holder.bOEConversionRate = convertView.findViewById(R.id.tv_boe_conversion_rate);
            convertView.setTag(holder);

        } else {
            holder = (RateHolder) convertView.getTag();
        }
        List<String> commonRate = mDataList.get(position);

        holder.currencyName.setText(commonRate.get(0));
        holder.unit.setText("单位：" + commonRate.get(1));
        holder.buyingRate.setText("现汇买入价：" + commonRate.get(2));
        holder.cashBuyingRate.setText("现钞买入价：" + commonRate.get(3));
        holder.sellingRate.setText("现钞卖出价：" + commonRate.get(4));
        holder.bOEConversionRate.setText("中行折算价：" + commonRate.get(5));
        return convertView;
    }

    class RateHolder {
        TextView date;
        TextView currencyName;//货币名称
        TextView unit;//单位
        TextView buyingRate;//现汇买入价
        TextView cashBuyingRate;//现钞买入价
        TextView sellingRate;//现钞卖出价
        TextView bOEConversionRate;//中行折算价
    }
}
