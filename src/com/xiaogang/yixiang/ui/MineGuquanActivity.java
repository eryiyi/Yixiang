package com.xiaogang.yixiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.xiaogang.yixiang.data.AdSlideData;
import com.xiaogang.yixiang.data.MineGuquanObjData;
import com.xiaogang.yixiang.module.MineGuquanObj;
import com.xiaogang.yixiang.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/23.
 */
public class MineGuquanActivity extends BaseActivity implements View.OnClickListener {
    MineGuquanObj mineGuquanObj;
    private TextView gq_nature_name;
    private TextView number;
    private TextView mine_zhiye;
    private TextView mine_name;
    private ImageView mine_head;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guquan_activity);

        number = (TextView) this.findViewById(R.id.number);
        gq_nature_name = (TextView) this.findViewById(R.id.gq_nature_name);
        mine_zhiye = (TextView) this.findViewById(R.id.mine_zhiye);
        mine_name = (TextView) this.findViewById(R.id.mine_name);
        mine_head = (ImageView) this.findViewById(R.id.mine_head);

        mine_name.setText("我是"+ getGson().fromJson(getSp().getString("nick_name", ""), String.class));
        imageLoader.displayImage( UniversityApplication.member.getCover(), mine_head, UniversityApplication.txOptions, animateFirstListener);
        getdata();

        this.findViewById(R.id.liner_sign).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.liner_sign:
                //股权排名
                Intent rankView = new Intent(MineGuquanActivity.this, RankGuquanActivity.class);
                startActivity(rankView);
                break;
        }
    }


    void getdata(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_GUQUAN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    MineGuquanObjData data = getGson().fromJson(s, MineGuquanObjData.class);
                                    mineGuquanObj = data.getData();
                                    number.setText(mineGuquanObj.getGq_number()+"股");
                                    mine_zhiye.setText(mineGuquanObj.getGq_nature_name());
                                    gq_nature_name.setText(mineGuquanObj.getGq_identity_name());
                                } else {
                                    Toast.makeText(MineGuquanActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MineGuquanActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineGuquanActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                params.put("user_id", getGson().fromJson(getSp().getString("user_id", ""), String.class));
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
