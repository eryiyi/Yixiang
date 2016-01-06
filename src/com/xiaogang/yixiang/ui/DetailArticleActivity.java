package com.xiaogang.yixiang.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.UniversityApplication;
import com.xiaogang.yixiang.adapter.AnimateFirstDisplayListener;
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.module.ArticleObj;

/**
 * Created by Administrator on 2016/1/6.
 */
public class DetailArticleActivity extends BaseActivity implements View.OnClickListener {
    private ArticleObj articleObj;
    private ImageView pic;
    private TextView title;
    private TextView dateline;
    private TextView hit;


    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);
        articleObj = (ArticleObj) getIntent().getExtras().get("articleObj");

        pic = (ImageView) this.findViewById(R.id.pic);
        title = (TextView) this.findViewById(R.id.title);
        dateline = (TextView) this.findViewById(R.id.dateline);
        hit = (TextView) this.findViewById(R.id.hit);

        imageLoader.displayImage(articleObj.getPicture(), pic, UniversityApplication.options, animateFirstListener);
        title.setText(articleObj.getTitle());
        dateline.setText(articleObj.getDateline());
//        hit.setText("点击量:"+articleObj.getHit());

        WebView wv = (WebView) findViewById(R.id.wvHtml);
        String htmlData = articleObj.getContent()==null?"":articleObj.getContent();
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        wv.getSettings().setBuiltInZoomControls(true);

        htmlData = htmlData.replaceAll("&amp;", "");

        htmlData = htmlData.replaceAll("quot;", "\"");

        htmlData = htmlData.replaceAll("lt;", "<");

        htmlData = htmlData.replaceAll("gt;", ">");
        wv.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);
    }

    @Override
    public void onClick(View v) {

    }

    public void back(View view){
        finish();
    }
}
