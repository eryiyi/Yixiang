package com.xiaogang.yixiang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.xiaogang.yixiang.base.BaseFragment;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.data.MemberData;
import com.xiaogang.yixiang.data.TalentsData;
import com.xiaogang.yixiang.module.Member;
import com.xiaogang.yixiang.ui.*;
import com.xiaogang.yixiang.util.StringUtil;
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
        initView(view);
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
        //
        imageLoader.displayImage(InternetURL.INTERNAL_PIC+ member.getCover(), mine_head, UniversityApplication.txOptions, animateFirstListener);
        mine_name.setText(member.getNick_name());
        mine_zhiye.setText(member.getMajorBusinesses());


        save("user_id", member.getUser_id());
        save("cover", member.getCover());
        save("nick_name", member.getNick_name());
        save("sex", member.getSex());
        save("birth_year", member.getBirth_year());
        save("birth_month", member.getBirth_month());
        save("hobby", member.getHobby());
        save("address", member.getAddress());
        save("employment", member.getEmployment());
        save("jobHunting", member.getJobHunting());
        save("majorBusinesses", member.getMajorBusinesses());
        save("recentDemand", member.getRecentDemand());

        UniversityApplication.member = member;
    }


}
