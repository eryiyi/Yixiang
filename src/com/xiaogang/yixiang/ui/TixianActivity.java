package com.xiaogang.yixiang.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.util.StringUtil;
import com.xiaogang.yixiang.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/5.
 */
public class TixianActivity extends BaseActivity implements View.OnClickListener {
    private EditText name;
    private EditText jine;
    private EditText zhanghao;
    private EditText kaihuhang;
    private String type= "0";

    private TextView zhifubao;
    private TextView yinhangka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tixian_activity);

        name = (EditText) this.findViewById(R.id.name);
        jine = (EditText) this.findViewById(R.id.jine);
        zhanghao = (EditText) this.findViewById(R.id.zhanghao);
        kaihuhang = (EditText) this.findViewById(R.id.kaihuhang);

        this.findViewById(R.id.zhifubao).setOnClickListener(this);
        this.findViewById(R.id.yinhangka).setOnClickListener(this);
        kaihuhang.setVisibility(View.GONE);

        zhifubao = (TextView) this.findViewById(R.id.zhifubao);
        yinhangka = (TextView) this.findViewById(R.id.yinhangka);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zhifubao:
                type = "0";
                kaihuhang.setVisibility(View.GONE);
                zhifubao.setTextColor(getResources().getColor(R.color.red));
                yinhangka.setTextColor(getResources().getColor(R.color.dark_gray));
                break;
            case R.id.yinhangka:
                type = "1";
                kaihuhang.setVisibility(View.VISIBLE);
                zhifubao.setTextColor(getResources().getColor(R.color.dark_gray));
                yinhangka.setTextColor(getResources().getColor(R.color.red));
                break;
        }

    }

    public void back(View view){finish();}

    public void addSure(View view){
        //
        if(StringUtil.isNullOrEmpty(name.getText().toString())){
            showMsg(TixianActivity.this, "请输入真实姓名");
            return;
        }
        if(StringUtil.isNullOrEmpty(jine.getText().toString()) || "0".equals(jine.getText().toString())){
            showMsg(TixianActivity.this, "请输入金额,金额必须大于0");
            return;
        }
        if(StringUtil.isNullOrEmpty(zhanghao.getText().toString())){
            showMsg(TixianActivity.this, "请输入账号");
            return;
        }
        if("1".equals(type)){
            if(StringUtil.isNullOrEmpty(kaihuhang.getText().toString())){
                showMsg(TixianActivity.this, "请输入开户行");
                return;
            }

        }

        progressDialog = new CustomProgressDialog(TixianActivity.this , "请稍后", R.anim.frame_paopao);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();

    }

    void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.DRAW_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    Toast.makeText(TixianActivity.this, "提现申请成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    Toast.makeText(TixianActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(TixianActivity.this, "获得数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                params.put("user_id", getGson().fromJson(getSp().getString("user_id", ""), String.class));
                params.put("type", type);
                params.put("true_name", name.getText().toString());
                params.put("money", jine.getText().toString());
                params.put("bank", kaihuhang.getText().toString());
                params.put("account", zhanghao.getText().toString());
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
