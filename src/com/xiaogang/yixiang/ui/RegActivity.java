package com.xiaogang.yixiang.ui;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.UniversityApplication;
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.data.EmpData;
import com.xiaogang.yixiang.module.Emp;
import com.xiaogang.yixiang.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/27.
 */
public class RegActivity extends BaseActivity implements View.OnClickListener {
    Resources res;
    private EditText mobile;
    private EditText code;
    private EditText password;
    private Button btn_code;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_activity);
        res = getResources();

        mobile = (EditText) this.findViewById(R.id.mobile);
        code = (EditText) this.findViewById(R.id.code);
        password = (EditText) this.findViewById(R.id.password);
        btn_code = (Button) this.findViewById(R.id.btn_code);
        btn = (Button) this.findViewById(R.id.btn);

        btn_code.setOnClickListener(this);
        btn.setOnClickListener(this);
        this.findViewById(R.id.back).setOnClickListener(this);

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
                    showMsg(RegActivity.this, "请输入手机号码");
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
                    showMsg(RegActivity.this, "请输入手机号码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(code.getText().toString())){
                    showMsg(RegActivity.this, "请输入验证码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(password.getText().toString())){
                    showMsg(RegActivity.this, "请输入密码");
                    return;
                }

                setpwr();
                break;
        }
    }


    void getCard(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.REG_SEND_CODE_URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    Toast.makeText(RegActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(RegActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RegActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RegActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
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
                InternetURL.REG_CODE_URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    EmpData data = getGson().fromJson(s, EmpData.class);

                                    Toast.makeText(RegActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                    //huanxin
                                    register(data.getData());
                                }else {
                                    Toast.makeText(RegActivity.this, jo.getString("msg") , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RegActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RegActivity.this, R.string.get_cart_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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


    /**
     * 注册
     *
     */
    public void register(Emp emp) {
        final String username = emp.getUser_id();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
            mobile.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(username)) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage(getResources().getString(R.string.Is_the_registered));
            pd.show();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        // 调用sdk注册方法
                        EMChatManager.getInstance().createAccountOnServer(username, "123456");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegActivity.this.isFinishing())
                                    pd.dismiss();
                                // 保存用户名
                                UniversityApplication.getInstance().setUserName(username);
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } catch (final EaseMobException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegActivity.this.isFinishing())
                                    pd.dismiss();
                                int errorCode=e.getErrorCode();
                                if(errorCode== EMError.NONETWORK_ERROR){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.USER_ALREADY_EXISTS){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.UNAUTHORIZED){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.ILLEGAL_USER_NAME){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();

        }
    }

}
