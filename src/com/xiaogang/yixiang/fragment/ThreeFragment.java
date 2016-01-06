package com.xiaogang.yixiang.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.UniversityApplication;
import com.xiaogang.yixiang.adapter.AnimateFirstDisplayListener;
import com.xiaogang.yixiang.base.BaseFragment;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.data.TalentsData;
import com.xiaogang.yixiang.module.Talents;
import com.xiaogang.yixiang.ui.GXActivtiy;
import com.xiaogang.yixiang.ui.QiehuanListActivity;
import com.xiaogang.yixiang.util.StringUtil;
import com.xiaogang.yixiang.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索
 */
public class ThreeFragment extends BaseFragment implements View.OnClickListener,Runnable {
    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private UiSettings mUiSettings;

    private List<Talents> talentses = new ArrayList<Talents>();


    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public LinearLayout bottomLiner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.three_fragment, null);

        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();

        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.");
        //设定中心点坐标
        LatLng cenpt = new LatLng(Double.valueOf(df.format(UniversityApplication.lat)), Double.valueOf(df.format(UniversityApplication.lng)));
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(6)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(mMapStatusUpdate, 50000);

        view.findViewById(R.id.mine_qiehuan).setOnClickListener(this);
        view.findViewById(R.id.mine_location).setOnClickListener(this);

        getData();

        bottomLiner = (LinearLayout) view.findViewById(R.id.bottomLiner);
        bottomLiner.setVisibility(View.GONE);

        view.findViewById(R.id.gongying).setOnClickListener(this);
        view.findViewById(R.id.xuqiu).setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_qiehuan:
                //
                Intent mineQiehuan = new Intent(getActivity(), QiehuanListActivity.class);
                ArrayList<Talents> arrayList = new ArrayList<Talents>();
                for(Talents talents:talentses){
                    arrayList.add(talents);
                }
                mineQiehuan.putExtra("talentses",arrayList);
                startActivity(mineQiehuan);
                break;
            case R.id.mine_location:
                //
                progressDialog = new CustomProgressDialog(getActivity() , "正在切换，请稍后", R.anim.frame_paopao);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();

                java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
                //设定中心点坐标
                LatLng cenpt = new LatLng(Double.valueOf(df.format(UniversityApplication.lat)), Double.valueOf(df.format(UniversityApplication.lng)));
                //定义地图状态
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(cenpt)
                        .zoom(16)
                        .build();
                //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                mBaiduMap.animateMapStatus(mMapStatusUpdate, 50000);
                // 启动一个线程
                new Thread(this).start();
                break;
            case R.id.gongying:
                Intent gy = new Intent(getActivity(), GXActivtiy.class);
                gy.putExtra("titleStr", "供应");
                startActivity(gy);
                break;
            case R.id.xuqiu:
                Intent xq = new Intent(getActivity(), GXActivtiy.class);
                xq.putExtra("titleStr", "需求");
                startActivity(xq);
                break;
        }
    }

    public void run() {
        try {
            // 15秒后跳转到登录界面
            Thread.sleep(45000);
            progressDialog.dismiss();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        super.onDestroy();
    }

    void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.AROUND_TALENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    TalentsData data = getGson().fromJson(s, TalentsData.class);
                                    talentses.clear();
                                    talentses.addAll(data.getData());
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
                params.put("lat", String.valueOf(UniversityApplication.lat));
                params.put("lng", String.valueOf(UniversityApplication.lng));
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
        if(talentses != null && talentses.size()>0){
            for(Talents talents:talentses){
                if(!StringUtil.isNullOrEmpty(talents.getLat()) && !StringUtil.isNullOrEmpty(talents.getLng())){
                    MydrawPointCurrentLocation(Double.valueOf(talents.getLat()), Double.valueOf(talents.getLng()) , talents );
                }

            }
        }
    }

    public void MydrawPointCurrentLocation(Double lat, Double lng, Talents talents){
        //定义Maker坐标点
        LatLng point = new LatLng(lat,lng);
        String pic = talents.getCover();

//        if(!StringUtil.isNullOrEmpty(pic)){
            ImageView imageView = new ImageView(getActivity());

            imageView.setMaxWidth(StringUtil.px2dp(getActivity(), 30));
            imageView.setMaxHeight(StringUtil.px2dp(getActivity(), 30));

            imageLoader.displayImage(InternetURL.INTERNAL_PIC+pic, imageView, UniversityApplication.txOptions, animateFirstListener);

            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromView(imageView);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
//        }
    }



}
