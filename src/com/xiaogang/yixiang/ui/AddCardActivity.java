package com.xiaogang.yixiang.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
 * Created by Administrator on 2016/1/6.
 */
public class AddCardActivity extends BaseActivity implements View.OnClickListener {
    private EditText name;
    private EditText card;
    private EditText kaihuhang;
    private EditText mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_activity);

        name = (EditText) this.findViewById(R.id.name);
        card = (EditText) this.findViewById(R.id.card);
        kaihuhang = (EditText) this.findViewById(R.id.kaihuhang);
        mobile = (EditText) this.findViewById(R.id.mobile);
        this.findViewById(R.id.add).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                add();
                break;
        }
    }

    public void back(View view){finish();}

    public void add(){
        if(StringUtil.isNullOrEmpty(name.getText().toString())){
            showMsg(AddCardActivity.this, "输入姓名");
            return;
        }
        if(StringUtil.isNullOrEmpty(card.getText().toString())){
            showMsg(AddCardActivity.this, "输入卡号");
            return;
        }
        if(StringUtil.isNullOrEmpty(kaihuhang.getText().toString())){
            showMsg(AddCardActivity.this, "输入开户行");
            return;
        }
        if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
            showMsg(AddCardActivity.this, "输入手机号");
            return;
        }
        progressDialog = new CustomProgressDialog(AddCardActivity.this , "请稍后", R.anim.frame_paopao);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getdata();
    }
    void getdata(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.YINHANG_SET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    Toast.makeText(AddCardActivity.this, "添加银行卡成功", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent("add_card_bank");
                                    sendBroadcast(intent1);
                                    finish();
                                } else {
                                    Toast.makeText(AddCardActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(AddCardActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddCardActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                params.put("user_id", getGson().fromJson(getSp().getString("user_id", ""), String.class));
                params.put("true_name",  name.getText().toString());
                params.put("card_num",  card.getText().toString());
                params.put("card_bank",  kaihuhang.getText().toString());
                params.put("rev_phone",  mobile.getText().toString());
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
