package com.org.rivermanage.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.org.rivermanage.R;

import java.util.List;

/**
 * Created by zhiqiang.com on 2017/10/23.
 */

public class warningAdapter extends ArrayAdapter<UserWaring> {

    public warningAdapter(Context context, List<UserWaring> objects) {
        super(context, R.layout.list_item , objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserWaring wanring= getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            //任务 补充完整
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            //获取控件
            viewHolder.title = (TextView) view.findViewById(R.id.Tv_tite);
            viewHolder.address = (TextView) view.findViewById(R.id.Tv_adss);
            viewHolder.miaoshu = (TextView) view.findViewById(R.id.Tv_miaoshu);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(wanring.getName());
        viewHolder.miaoshu.setText(wanring.getDescription());
        viewHolder.address.setText(wanring.getCreateTime()+"");


        return view;
    }

    class ViewHolder {
        TextView title;
        TextView address;
        TextView miaoshu;

    }
}
