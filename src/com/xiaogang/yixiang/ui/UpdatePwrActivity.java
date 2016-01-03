package com.xiaogang.yixiang.ui;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
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
import com.xiaogang.yixiang.data.SuccessData;
import com.xiaogang.yixiang.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/30.
 */
public class UpdatePwrActivity extends BaseActivity implements View.OnClickListener {
    private EditText surepass;
    private EditText password;
    private EditText mobile;
    private EditText code;
    private Button btn_code;
    private Button btn;
    Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pwr_activity);
        res = getResources();
        this.findViewById(R.id.back).setOnClickListener(this);
        surepass = (EditText) this.findViewById(R.id.surepass);
        password = (EditText) this.findViewById(R.id.password);
        mobile = (EditText) this.findViewById(R.id.mobile);
        code = (EditText) this.findViewById(R.id.code);
        btn_code = (Button) this.findViewById(R.id.btn_code);
        btn = (Button) this.findViewById(R.id.btn);

        btn_code.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_code:
                //验证码
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入手机号码");
                    return;
                }
                btn_code.setClickable(false);//不可点击
                MyTimer myTimer = new MyTimer(60000,1000);
                myTimer.start();
                getCard();
                break;
            case R.id.btn:
                //确定
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入手机号码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(code.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入验证码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(password.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入密码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(surepass.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入确认密码");
                    return;
                }
                if(!password.getText().toString().equals(surepass.getText().toString())){
                    Toast.makeText(UpdatePwrActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                setpwr();
                break;
        }
    }

    void getCard(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_PWR_SEND_CARD_URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    Toast.makeText(UpdatePwrActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(UpdatePwrActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(UpdatePwrActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(UpdatePwrActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile.getText().toString());
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

    class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_code.setText(res.getString(R.string.daojishi_three));
            btn_code.setClickable(true);//可点击
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_code.setText(res.getString(R.string.daojishi_one) + millisUntilFinished / 1000 + res.getString(R.string.daojishi_two));
        }
    }


    void setpwr(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_PWR_UPDATE_CARD_URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    Toast.makeText(UpdatePwrActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(UpdatePwrActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(UpdatePwrActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(UpdatePwrActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                params.put("user_id", getGson().fromJson(getSp().getString("user_id", ""), String.class));
                params.put("mobile" , mobile.getText().toString());
                params.put("code" , code.getText().toString());
                params.put("password" , password.getText().toString());
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
