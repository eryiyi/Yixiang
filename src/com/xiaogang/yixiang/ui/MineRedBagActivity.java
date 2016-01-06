package com.xiaogang.yixiang.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
 * Created by Administrator on 2015/12/23.
 */
public class MineRedBagActivity extends BaseActivity implements View.OnClickListener {
    private EditText redbag_num;
    private EditText jine;
    private EditText liuyan;
    private TextView money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redbag_activity);

        redbag_num = (EditText) this.findViewById(R.id.redbag_num);
        jine = (EditText) this.findViewById(R.id.jine);
        liuyan = (EditText) this.findViewById(R.id.liuyan);
        money = (TextView) this.findViewById(R.id.money);

        redbag_num.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable edt)
            {
                toCulamte();
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });

        jine.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable edt)
            {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0){
                    if(temp.length()<=4){
                        return;
                    }else{
                        edt.delete(4, 5);
                        return;
                    }
                }
                if (temp.length() - posDot - 1 > 2)
                {
                    edt.delete(posDot + 3, posDot + 4);
                }
                toCulamte();
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    public void back(View view){finish();}

    public void addSure(View view){
        //
        if(StringUtil.isNullOrEmpty(redbag_num.getText().toString())){
            showMsg(MineRedBagActivity.this, "红包数量不能为空");
            return;
        }if(StringUtil.isNullOrEmpty(jine.getText().toString())){
            showMsg(MineRedBagActivity.this, "单个红包金额不能为空");
            return;
        }
        if(StringUtil.isNullOrEmpty(liuyan.getText().toString())){
            showMsg(MineRedBagActivity.this, "留言不能为空");
            return;
        }
        if("￥0.0".equals(money.getText().toString())){
            showMsg(MineRedBagActivity.this, "红包不能为空");
            return;
        }
        progressDialog = new CustomProgressDialog(MineRedBagActivity.this , "请稍后", R.anim.frame_paopao);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    void toCulamte(){
        String str_one = redbag_num.getText().toString();
        String str_two = jine.getText().toString();
        if(!StringUtil.isNullOrEmpty(str_one) && !StringUtil.isNullOrEmpty(str_two)){
            money.setText("￥"+String.valueOf(Integer.parseInt(str_one)*Double.valueOf(str_two)));
        }
    }

    void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.RED_SEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    Toast.makeText(MineRedBagActivity.this, "发红包成功", Toast.LENGTH_SHORT).show();
                                   finish();
                                }
                                else{
                                    Toast.makeText(MineRedBagActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                params.put("user_id", getGson().fromJson(getSp().getString("user_id", ""), String.class));
                params.put("money", jine.getText().toString());
                params.put("number", redbag_num.getText().toString());
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
