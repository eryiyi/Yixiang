package com.xiaogang.yixiang.ui;

import android.app.ProgressDialog;
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
import com.easemob.chatuidemo.activity.HxAlertDialog;
import com.easemob.chatuidemo.activity.HxAlertYanzheng;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.UniversityApplication;
import com.xiaogang.yixiang.adapter.AnimateFirstDisplayListener;
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.data.MemberData;
import com.xiaogang.yixiang.data.RedBagObjData;
import com.xiaogang.yixiang.module.Member;
import com.xiaogang.yixiang.module.RedBagObj;
import com.xiaogang.yixiang.util.StringUtil;
import com.xiaogang.yixiang.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/5.
 */
public class DetailMemberActivity extends BaseActivity implements View.OnClickListener {
    private String userid;
    private ImageView cover;
    private TextView name;
    private TextView location;
    private TextView yixiangnum;
    private TextView mobile;
    private TextView address;
    private TextView cont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_member_activity);
        userid =  getIntent().getExtras().getString("userid");

        this.findViewById(R.id.back).setOnClickListener(this);
        cover = (ImageView) this.findViewById(R.id.cover);
        name = (TextView) this.findViewById(R.id.name);
        location = (TextView) this.findViewById(R.id.location);
        yixiangnum = (TextView) this.findViewById(R.id.yixiangnum);
        mobile = (TextView) this.findViewById(R.id.mobile);
        address = (TextView) this.findViewById(R.id.address);
        cont = (TextView) this.findViewById(R.id.cont);

        progressDialog = new CustomProgressDialog(DetailMemberActivity.this , "请稍后", R.anim.frame_paopao);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
        getred();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    Member member;
    void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MEMBER_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    MemberData data = getGson().fromJson(s, MemberData.class);
                                    member = data.getData();
                                    initData(member);
                                }
                                else{
                                    Toast.makeText(DetailMemberActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                    finish();
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
                        Toast.makeText(DetailMemberActivity.this, "获得数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                params.put("user_id", userid);
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

    void initData(Member member){
        //
        imageLoader.displayImage(InternetURL.INTERNAL_PIC + member.getCover(), cover, UniversityApplication.txOptions, animateFirstListener);
        name.setText(member.getNick_name());
        yixiangnum.setText("移享号："+member.getUser_id());
        if(StringUtil.isNullOrEmpty(member.getLat()) && StringUtil.isNullOrEmpty(member.getLng()) && UniversityApplication.lng != null && UniversityApplication.lat != null){
            location.setText(String.valueOf(StringUtil.GetShortDistance(Double.valueOf(member.getLng()), Double.valueOf(member.getLat()), UniversityApplication.lng, UniversityApplication.lat)));
        }
        mobile.setText("手机号："+member.getMobile());
        address.setText("地址："+member.getAddress());
        cont.setText("需求："+member.getRequirement());
    }

    public void addHongbao(View view){
        //
        RedBagObj redBagObj = null ;
        if(listsRedBags != null && listsRedBags.size() > 0){
            for(RedBagObj redBagObj1:listsRedBags){
                if(redBagObj1.getUser_id().equals(member.getUser_id())){
                    //是对方的红包
                    redBagObj = redBagObj1;
                    break;
                }
            }
        }else {
            showMsg(DetailMemberActivity.this, "暂无红包");
        }
        if(redBagObj != null){
            //说明对方有红包
            lingqu(redBagObj);
        }else {
            showMsg(DetailMemberActivity.this, "对方暂无红包");
        }
    }

    List<RedBagObj> listsRedBags = new ArrayList<>();
    void getred(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.LIST_RED_SEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    RedBagObjData data = getGson().fromJson(s, RedBagObjData.class);
                                    listsRedBags.clear();
                                    listsRedBags.addAll(data.getData());
                                }
                                else{
                                    Toast.makeText(DetailMemberActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DetailMemberActivity.this, "获得数据失败", Toast.LENGTH_SHORT).show();
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

    void lingqu(final RedBagObj redBagObj){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.PACK_RED_SEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    Toast.makeText(DetailMemberActivity.this, "领取红包成功", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(DetailMemberActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DetailMemberActivity.this, "获得数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                params.put("user_id", getGson().fromJson(getSp().getString("user_id", ""), String.class));
                params.put("pack_id", redBagObj.getId());
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



    public void addFriend(View v) {
        if ((getGson().fromJson(getSp().getString("user_id", ""), String.class)).equals(member.getUser_id())) {
            String str = getResources().getString(R.string.not_add_myself);
            startActivity(new Intent(this, HxAlertDialog.class).putExtra("msg", str));
            return;
        }

        if (getMyApp().getContactList().containsKey(member.getUser_id())) {
            String strin = getResources().getString(R.string.This_user_is_already_your_friend);
            startActivity(new Intent(this, HxAlertDialog.class).putExtra("msg", strin));
            return;
        }

        Intent intent = new Intent(this, HxAlertYanzheng.class);
        intent.putExtra("hxUserName", member.getUser_id());
        startActivity(intent);
    }

}
