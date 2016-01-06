package com.xiaogang.yixiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.adapter.ItemRankGqAdapter;
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.data.RankGqObjData;
import com.xiaogang.yixiang.module.RankGqObj;
import com.xiaogang.yixiang.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/5.
 */
public class RankGuquanActivity extends BaseActivity implements View.OnClickListener {
    private ListView lstv;
    private ItemRankGqAdapter adapterHot;
    List<RankGqObj> articleObjs = new ArrayList<RankGqObj>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_activity);

        initView();
        getdata();

    }

    void initView(){
        //
        adapterHot = new ItemRankGqAdapter(articleObjs, RankGuquanActivity.this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        lstv.setAdapter(adapterHot);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //
                RankGqObj rankGqObj = articleObjs.get(i);
                Intent detailView = new Intent(RankGuquanActivity.this, DetailMemberActivity.class);
                detailView.putExtra("userid", rankGqObj.getUser_id());
                startActivity(detailView);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public void back(View view){finish();}

    void getdata(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GUQUAN_ORDER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    RankGqObjData data = getGson().fromJson(s, RankGqObjData.class);
                                    articleObjs.clear();
                                    articleObjs.addAll(data.getData());
                                    adapterHot.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(RankGuquanActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RankGuquanActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RankGuquanActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
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
