package com.xiaogang.yixiang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.UniversityApplication;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.module.Talents;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/9
 * Time: 8:42
 * 类的功能、说明写在此处.
 */
public class ItemCityAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Talents> findEmps;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public ItemCityAdapter(List<Talents> findEmps, Context mContext) {
        this.findEmps = findEmps;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return findEmps.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_qiehuan, null);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            holder.location = (TextView) convertView.findViewById(R.id.location);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.zhiye = (TextView) convertView.findViewById(R.id.zhiye);
            holder.cont = (TextView) convertView.findViewById(R.id.cont);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Talents cell = findEmps.get(position);
        if (findEmps != null) {
            imageLoader.displayImage(InternetURL.INTERNAL_PIC+ cell.getCover(), holder.pic, UniversityApplication.txOptions, animateFirstListener);
            holder.name.setText(cell.getTruename());
            holder.location.setText(cell.getDistance()+"米以内");
            holder.zhiye.setText(cell.getPositonOrHobby());
            holder.cont.setText(cell.getCompanyNameOrCareer());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView pic;
        TextView name;
        TextView location;
        TextView zhiye;
        TextView cont;
    }
}
