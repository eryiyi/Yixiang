package com.xiaogang.yixiang.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.WriterException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.UniversityApplication;
import com.xiaogang.yixiang.adapter.AnimateFirstDisplayListener;
import com.xiaogang.yixiang.base.BaseFragment;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.data.MemberData;
import com.xiaogang.yixiang.module.Member;
import com.xiaogang.yixiang.ui.*;
import com.xiaogang.yixiang.util.StringUtil;
import com.xiaogang.yixiang.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class FourFragment extends BaseFragment implements View.OnClickListener {
    private Member member;

    private ImageView mine_head;
    private ImageView mine_erweima;
    private ImageView mine_lingqu;
    private TextView mine_name;
    private TextView mine_zhiye;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.four_fragment, null);
        registerBoradcastReceiver();
        initView(view);
        progressDialog = new CustomProgressDialog(getActivity() , "请稍后", R.anim.frame_paopao);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.liner_article).setOnClickListener(this);
        view.findViewById(R.id.liner_msg).setOnClickListener(this);
        view.findViewById(R.id.liner_goods).setOnClickListener(this);
        view.findViewById(R.id.liner_set).setOnClickListener(this);
        view.findViewById(R.id.liner_qianbao).setOnClickListener(this);

        mine_head = (ImageView) view.findViewById(R.id.mine_head);
        mine_erweima = (ImageView) view.findViewById(R.id.mine_erweima);
        mine_lingqu = (ImageView) view.findViewById(R.id.mine_lingqu);
        mine_lingqu.setOnClickListener(this);
        mine_name = (TextView) view.findViewById(R.id.mine_name);
        mine_zhiye = (TextView) view.findViewById(R.id.mine_zhiye);
        mine_head.setOnClickListener(this);
        view.findViewById(R.id.mineGuquan).setOnClickListener(this);
        view.findViewById(R.id.profile).setOnClickListener(this);
        view.findViewById(R.id.liner_tuijian).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.liner_article:
                Intent hotView = new Intent(getActivity(), MinehotArticleActivity.class);
                startActivity(hotView);
                break;
            case R.id.liner_msg:
                break;
            case R.id.liner_goods:
                break;
            case R.id.liner_set:
                Intent mineSet = new Intent(getActivity(), MineSetActivity.class);
                startActivity(mineSet);
                break;
            case R.id.liner_qianbao:
                Intent qianbaoView = new Intent(getActivity(), MineQianbaoActivity.class);
                startActivity(qianbaoView);
                break;
            case R.id.mine_head:
                break;
            case R.id.profile:
                Intent profileView = new Intent(getActivity(), ProfileActivity.class);
                startActivity(profileView);
                break;
            case R.id.mineGuquan:
                Intent guquanView = new Intent(getActivity(), MineGuquanActivity.class);
                startActivity(guquanView);
                break;
            case R.id.liner_tuijian:
                Intent tuijianView = new Intent(getActivity(), MineTuijianActivity.class);
                startActivity(tuijianView);
                break;
            case R.id.mine_lingqu:
                //领取
                lq();
                break;
        }
    }


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
                                    initData();
                                }
                                else{
                                    Toast.makeText(getActivity(), jo.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "获得数据失败", Toast.LENGTH_SHORT).show();
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

    void initData(){
        imageLoader.displayImage(InternetURL.INTERNAL_PIC + member.getCover(), mine_head, UniversityApplication.txOptions, animateFirstListener);
        mine_name.setText(member.getNick_name());
        mine_zhiye.setText(member.getIdentity_name());

        try {
            mine_erweima.setImageBitmap(StringUtil.Create2DCode(member.getDownload_code()));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        save("user_id", member.getUser_id());
        save("uid", member.getUid());
        save("nick_name", member.getNick_name());
        save("sex", member.getSex());
        save("salt", member.getSalt());
        save("money", member.getMoney());
        save("money_freeze", member.getMoney_freeze());
        save("gq_identity", member.getGq_identity());
        save("gq_nature", member.getGq_nature());
        save("gq_number", member.getGq_number());
        save("truename", member.getTruename());
        save("cover", member.getCover());
        save("id_card_num", member.getId_card_num());
        save("mobile", member.getMobile());
        save("email", member.getEmail());
        save("is_get_gufen", member.getIs_get_gufen());
        save("download_code", member.getDownload_code());
        save("birthday", member.getBirthday());
        save("sign", member.getSign());
        save("address", member.getAddress());
        save("major", member.getMajor());
        save("requirement", member.getRequirement());
        save("identity_name", member.getIdentity_name());

        UniversityApplication.member = member;
    }
    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("updateSuccess")) {
                //编辑信息成功
                getData();
//                imageLoader.displayImage(getGson().fromJson(getSp().getString("cover", ""), String.class), mine_head, UniversityApplication.txOptions, animateFirstListener);
//                mine_name.setText(getGson().fromJson(getSp().getString("nick_name", ""), String.class));
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("updateSuccess");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    void lq(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_EQUYLITY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    Toast.makeText(getActivity(), "领取股份成功", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getActivity(), jo.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "获得数据失败", Toast.LENGTH_SHORT).show();
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
