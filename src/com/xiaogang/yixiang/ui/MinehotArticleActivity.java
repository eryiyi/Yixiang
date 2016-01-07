package com.xiaogang.yixiang.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.adapter.ItemArticleAdapter;
import com.xiaogang.yixiang.adapter.OnClickContentItemListener;
import com.xiaogang.yixiang.adapter.ViewPagerAdapter;
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.data.AdSlideData;
import com.xiaogang.yixiang.data.ArticleObjData;
import com.xiaogang.yixiang.module.AdSlide;
import com.xiaogang.yixiang.module.ArticleObj;
import com.xiaogang.yixiang.util.StringUtil;
import com.xiaogang.yixiang.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/23.
 */
public class MinehotArticleActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {

    //导航
    private ViewPager viewpager;
    private ViewPagerAdapter adapter;
    private LinearLayout viewGroup;
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    private List<AdSlide> lists = new ArrayList<AdSlide>();


    private ListView lstv;
    private ItemArticleAdapter adapterHot;
    List<ArticleObj> articleObjs = new ArrayList<ArticleObj>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_activity);
        initViewPager();
        initView();
        progressDialog = new CustomProgressDialog(MinehotArticleActivity.this , "请稍后", R.anim.frame_paopao);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getAd();
        getdata();
    }

    void initView(){
        //
        adapterHot = new ItemArticleAdapter(articleObjs, MinehotArticleActivity.this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        lstv.setAdapter(adapterHot);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //
                Intent detailView = new Intent(MinehotArticleActivity.this, DetailArticleActivity.class);
                ArticleObj articleObj = articleObjs.get(i);
                detailView.putExtra("articleObj", articleObj);
                startActivity(detailView);
            }
        });
    }

    void getAd(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.AD_LUNBO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    AdSlideData data = getGson().fromJson(s, AdSlideData.class);
                                    lists.clear();
                                    lists.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(MinehotArticleActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MinehotArticleActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MinehotArticleActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    @Override
    public void onClick(View view) {

    }

    private void initViewPager() {
        adapter = new ViewPagerAdapter(this);
        adapter.change(lists);
        adapter.setOnClickContentItemListener(this);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(myOnPageChangeListener);
        initDot();
        runnable = new Runnable() {
            @Override
            public void run() {
                int next = viewpager.getCurrentItem() + 1;
                if (next >= adapter.getCount()) {
                    next = 0;
                }
                viewHandler.sendEmptyMessage(next);
            }
        };
        viewHandler.postDelayed(runnable, autoChangeTime);
    }

    // 初始化dot视图
    private void initDot() {
        viewGroup = (LinearLayout) findViewById(R.id.viewGroup);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                20, 20);
        layoutParams.setMargins(4, 3, 4, 3);

        dots = new ImageView[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {

            dot = new ImageView(this);
            dot.setLayoutParams(layoutParams);
            dots[i] = dot;
            dots[i].setTag(i);
            dots[i].setOnClickListener(onClick);

            if (i == 0) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }

            viewGroup.addView(dots[i]);
        }
    }

    ViewPager.OnPageChangeListener myOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            setCurDot(arg0);
            viewHandler.removeCallbacks(runnable);
            viewHandler.postDelayed(runnable, autoChangeTime);
        }

    };
    // 实现dot点击响应功能,通过点击事件更换页面
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            setCurView(position);
        }

    };

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position > adapter.getCount()) {
            return;
        }
        viewpager.setCurrentItem(position);
    }

    /**
     * 选中当前引导小点
     */
    private void setCurDot(int position) {
        for (int i = 0; i < dots.length; i++) {
            if (position == i) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }
        }
    }

    /**
     * 每隔固定时间切换广告栏图片
     */
    @SuppressLint("HandlerLeak")
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setCurView(msg.what);
        }
    };

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        AdSlide slide = lists.get(position);
        switch (flag){
            case 1:
                 if(slide != null){
                     //
                     Intent webViewActivity = new Intent(MinehotArticleActivity.this, WebViewActivity.class);
                     webViewActivity.putExtra("url", slide.getUrl());
                     startActivity(webViewActivity);
                 }
                break;
        }
    }

    void getdata(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_NEWS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    ArticleObjData data = getGson().fromJson(s, ArticleObjData.class);
                                    articleObjs.clear();
                                    articleObjs.addAll(data.getData());
                                    adapterHot.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(MinehotArticleActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MinehotArticleActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MinehotArticleActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
//                params.put("type_id", "1");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    public void back(View view){finish();}

}
