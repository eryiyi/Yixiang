package com.xiaogang.yixiang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.UniversityApplication;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.module.ArticleObj;
import com.xiaogang.yixiang.module.Talents;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/9
 * Time: 8:42
 * 文章
 */
public class ItemArticleAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<ArticleObj> findEmps;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public ItemArticleAdapter(List<ArticleObj> findEmps, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_article, null);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.dateline = (TextView) convertView.findViewById(R.id.dateline);
            holder.share = (LinearLayout) convertView.findViewById(R.id.share);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ArticleObj cell = findEmps.get(position);
        if (findEmps != null) {
            imageLoader.displayImage(InternetURL.INTERNAL_PIC + cell.getPicture(), holder.pic, UniversityApplication.options, animateFirstListener);
            holder.title.setText(cell.getTitle());
            holder.content.setText(cell.getSummary());
            holder.dateline.setText((cell.getHit()==null?"0":cell.getHit())+"阅读");
        }
        return convertView;
    }

    class ViewHolder {
        ImageView pic;
        TextView title;
        TextView content;
        TextView dateline;
        LinearLayout share;
    }
}
