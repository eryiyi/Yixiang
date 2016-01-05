package com.xiaogang.yixiang;

import android.app.ProgressDialog;
import android.content.*;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.LoginActivity;
import com.easemob.util.EMLog;
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.data.EmpData;
import com.xiaogang.yixiang.fragment.FourFragment;
import com.xiaogang.yixiang.fragment.OneFragment;
import com.xiaogang.yixiang.fragment.ThreeFragment;
import com.xiaogang.yixiang.fragment.TwoFragment;
import com.xiaogang.yixiang.module.Emp;
import com.xiaogang.yixiang.util.StringUtil;
import com.xiaogang.yixiang.widget.MenuPopMenu;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener ,MenuPopMenu.OnItemClickListener,EMEventListener {
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;

    private OneFragment oneFragment;
    private TwoFragment twoFragment;
    private ThreeFragment threeFragment;
    private FourFragment fourFragment;

    private ImageView foot_one;
    private ImageView foot_two;
    private ImageView foot_three;
    private ImageView foot_four;

    private long waitTime = 2000;
    private long touchTime = 0;

    //设置底部图标
    Resources res;

    private int index;
    // 当前fragment的index
    private int currentTabIndex = 0;


    private LocationClient mLocationClient;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor="gcj02";

    //下拉菜单
    private MenuPopMenu menu;
    List<String> arrayMenu = new ArrayList<String>();

    protected static final String TAG = "MainActivity";
    // 账号在别处登录
    public boolean isConflict = false;

    public static  int tmpVisible = 0;


    @Override
    public void onStop() {
        mLocationClient.stop();
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mLocationClient = ((UniversityApplication)getApplication()).mLocationClient;
        initLocation();
        mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
        mLocationClient.requestLocation();

        res = getResources();
        fm = getSupportFragmentManager();
        initView();
        arrayMenu.add("人才供需");
        arrayMenu.add("业务供需");
        switchFragment(R.id.foot_three);

        //上传地理位置
        upLocation();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，

        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View v) {
        switchFragment(v.getId());
    }

    private void initView() {
        foot_one = (ImageView) this.findViewById(R.id.foot_one);
        foot_two = (ImageView) this.findViewById(R.id.foot_two);
        foot_three = (ImageView) this.findViewById(R.id.foot_three);
        foot_four = (ImageView) this.findViewById(R.id.foot_four);
        foot_one.setOnClickListener(this);
        foot_two.setOnClickListener(this);
        foot_three.setOnClickListener(this);
        foot_four.setOnClickListener(this);

//        login();
    }


    public void switchFragment(int id) {
        fragmentTransaction = fm.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (id) {
            case R.id.foot_one:
                if (oneFragment == null) {
                    oneFragment = new OneFragment();
                    fragmentTransaction.add(R.id.content_frame, oneFragment);
                } else {
                    fragmentTransaction.show(oneFragment);
                }
                currentTabIndex = 0;
                foot_one.setImageResource(R.drawable.foot_one);
                foot_two.setImageResource(R.drawable.foot_two);
                foot_three.setImageResource(R.drawable.foot_three);
                foot_four.setImageResource(R.drawable.foot_four);
                threeFragment.bottomLiner.setVisibility(View.GONE);
                break;
            case R.id.foot_two:
                if (twoFragment == null) {
                    twoFragment = new TwoFragment();
                    fragmentTransaction.add(R.id.content_frame, twoFragment);
                } else {
                    fragmentTransaction.show(twoFragment);
                }

                currentTabIndex = 1;
                foot_one.setImageResource(R.drawable.foot_one);
                foot_two.setImageResource(R.drawable.foot_two);
                foot_three.setImageResource(R.drawable.foot_three);
                foot_four.setImageResource(R.drawable.foot_four);
                threeFragment.bottomLiner.setVisibility(View.GONE);
                break;
            case R.id.foot_three:
                if (threeFragment == null) {
                    threeFragment = new ThreeFragment();
                    fragmentTransaction.add(R.id.content_frame, threeFragment);
                } else {
                    fragmentTransaction.show(threeFragment);
                }
                currentTabIndex = 3;
                foot_one.setImageResource(R.drawable.foot_one);
                foot_two.setImageResource(R.drawable.foot_two);
                foot_three.setImageResource(R.drawable.foot_three);
                foot_four.setImageResource(R.drawable.foot_four);
                if(tmpVisible == 0 && threeFragment.bottomLiner!= null){
                    tmpVisible = 1;
                    threeFragment.bottomLiner.setVisibility(View.GONE);
                }else if(tmpVisible == 1 &&threeFragment.bottomLiner!= null) {
                    tmpVisible = 0;
                    threeFragment.bottomLiner.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.foot_four:
                if (fourFragment == null) {
                    fourFragment = new FourFragment();
                    fragmentTransaction.add(R.id.content_frame, fourFragment);
                } else {
                    fragmentTransaction.show(fourFragment);
                }
                currentTabIndex = 4;
                foot_one.setImageResource(R.drawable.foot_one);
                foot_two.setImageResource(R.drawable.foot_two);
                foot_three.setImageResource(R.drawable.foot_three);
                foot_four.setImageResource(R.drawable.foot_four);
                threeFragment.bottomLiner.setVisibility(View.GONE);
                break;

        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (oneFragment != null) {
            ft.hide(oneFragment);
        }
        if (twoFragment != null) {
            ft.hide(twoFragment);
        }
        if (threeFragment != null) {
            ft.hide(threeFragment);
        }
        if (fourFragment != null) {
            ft.hide(fourFragment);
        }
    }



    //再摁退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo =
                pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
            startActivitySafely(startIntent);
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        }
    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        // 重新显示登陆页面
        finish();
    }

    void getToken(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
//                InternetURL.GET_TOKEN+"?user_id="+"13266816551" +"&password="+"123456",
                InternetURL.GET_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    String access_token =  jo.getString("access_token");
                                    save("access_token", access_token);
                                    login();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", "13266816551");
                params.put("password", "123456");
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


    void login(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    EmpData data = getGson().fromJson(s, EmpData.class);
                                    saveAccount(data.getData());
                                }
                                else{
                                    Toast.makeText(MainActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", "13266816551");
                params.put("password", "123456");
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

    public void saveAccount(Emp emp) {
        // 登陆成功，保存用户名密码
        save("uid", emp.getUid());
        save("access_token", emp.getAccess_token());
        save("user_id", emp.getUser_id());
        save("truename", emp.getTruename());
        save("mobile", emp.getMobile());
        save("salt", emp.getSalt());
//        save("password", emp.getPassword());
        save("lng", emp.getLng());
        save("lat", emp.getLat());
        save("reg_time", emp.getReg_time());
        save("birth", emp.getBirth());
        save("id_card_num", emp.getId_card_num());
        save("email", emp.getEmail());
        save("birth_place", emp.getBirth_place());
        save("interest", emp.getInterest());
        save("sex", emp.getSex());
        save("skill", emp.getSkill());
        save("job_intension", emp.getJob_intension());
        save("job_experience", emp.getJob_experience());
        save("edu_experience", emp.getEdu_experience());
        save("gq_nature", emp.getGq_nature());
        save("gq_identity", emp.getGq_identity());
        save("gq_number", emp.getGq_number());
        save("nick_name", emp.getNick_name());
        save("cover", emp.getCover());
        save("is_admin", emp.getIs_admin());
        save("is_superadmin", emp.getIs_superadmin());

        switchFragment(R.id.foot_three);
    }

    //弹出顶部主菜单
    public void onTopMenuPopupButtonClick(View view) {
        //顶部右侧按钮
        menu = new MenuPopMenu(MainActivity.this, arrayMenu);
        menu.setOnItemClickListener(this);
        menu.showAsDropDown(view);
    }


    @Override
    public void onItemClick(int index) {
        switch (index) {
            case 0:
                break;
            case 1:
                break;
        }
    }

    // 账号被移除
    private boolean isCurrentAccountRemoved = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }
    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }


    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    private BroadcastReceiver internalDebugReceiver;

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHXSDKHelper.getInstance().logout(false,null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHXSDKHelper.getInstance().logout(true,null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();

            } catch (Exception e) {
                EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // unregister this event listener when this activity enters the
        // background
        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
        sdkHelper.pushActivity(this);

        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(this,
                new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage ,EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged});
    }

    /**
     * 监听事件
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: // 普通消息
            {
                EMMessage message = (EMMessage) event.getData();
                // 提示新消息
                HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                break;
            }
            case EventOfflineMessage: {
                break;
            }
            case EventConversationListChanged: {
                break;
            }
            default:
                break;
        }
    }

    /**
     * button点击事件
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_conversation:
                TwoFragment.index = 0;
                break;
            case R.id.btn_address_list:
                TwoFragment.index = 1;
                break;
            case R.id.btn_setting:
                TwoFragment.index = 2;
                break;
        }
        if (TwoFragment.currentTabIndex != TwoFragment.index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(TwoFragment.fragments[TwoFragment.currentTabIndex]);
            if (!TwoFragment.fragments[TwoFragment.index].isAdded()) {
                trx.add(R.id.fragment_container, TwoFragment.fragments[TwoFragment.index]);
            }
            trx.show(TwoFragment.fragments[index]).commit();
        }
        TwoFragment.mTabs[TwoFragment.currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        TwoFragment.mTabs[TwoFragment.index].setSelected(true);
        TwoFragment.currentTabIndex = TwoFragment.index;
    }

    void upLocation(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UP_LOCATION_URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {

                        } else {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                params.put("user_id", getGson().fromJson(getSp().getString("user_id", ""), String.class));
                params.put("lng", String.valueOf(UniversityApplication.lng));
                params.put("lat", String.valueOf(UniversityApplication.lat));
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
