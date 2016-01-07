package com.xiaogang.yixiang.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.UniversityApplication;
import com.xiaogang.yixiang.adapter.AnimateFirstDisplayListener;
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.data.ArticleObjData;
import com.xiaogang.yixiang.module.ArticleObj;
import com.xiaogang.yixiang.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        hit.setText("点击量:"+articleObj.getHit());

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
        getdata();
    }

    @Override
    public void onClick(View v) {

    }

    public void back(View view){
        finish();
    }


    void getdata(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_NEWS_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {

                                } else {
                                    Toast.makeText(DetailArticleActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DetailArticleActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DetailArticleActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                params.put("uid", articleObj.getUid());
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

}
