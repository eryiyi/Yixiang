package com.xiaogang.yixiang.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.data.MemberData;
import com.xiaogang.yixiang.upload.CommonUtil;
import com.xiaogang.yixiang.util.CompressPhotoUtil;
import com.xiaogang.yixiang.util.FileUtils;
import com.xiaogang.yixiang.util.StringUtil;
import com.xiaogang.yixiang.util.TimeUtils;
import com.xiaogang.yixiang.widget.DateTimePickDialogUtil;
import com.xiaogang.yixiang.widget.SelectPhoPopWindow;
import com.xiaogang.yixiang.widget.SexPopWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/23.
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mine_head;
    private ImageView mine_erweima;
    private EditText mine_name;
    private TextView mine_zhiye;
    private TextView mine_sex;
    private TextView mine_birth;
    private EditText mine_sign;
    private EditText mine_address;
    private EditText mine_yewu;
    private EditText mine_xq;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private SexPopWindow sexPopWindow;
    private SelectPhoPopWindow deleteWindow;

    private String pics = "";
    private String sex = "-1";
    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/liangxun/PhotoCache");
    Bitmap photo;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        initView();
        initData();
    }
    void initView(){
        //
        mine_head = (ImageView) this.findViewById(R.id.mine_head);
        mine_erweima = (ImageView) this.findViewById(R.id.mine_erweima);
        mine_name = (EditText) this.findViewById(R.id.mine_name);
        mine_zhiye = (TextView) this.findViewById(R.id.mine_zhiye);
        mine_sex = (TextView) this.findViewById(R.id.mine_sex);
        mine_birth = (TextView) this.findViewById(R.id.mine_birth);
        mine_sign = (EditText) this.findViewById(R.id.mine_sign);
        mine_address = (EditText) this.findViewById(R.id.mine_address);
        mine_yewu = (EditText) this.findViewById(R.id.mine_yewu);
        mine_xq = (EditText) this.findViewById(R.id.mine_xq);

        this.findViewById(R.id.liner_sex).setOnClickListener(this);
        this.findViewById(R.id.liner_birth).setOnClickListener(this);
        this.findViewById(R.id.mine_head).setOnClickListener(this);
        this.findViewById(R.id.mineCompany).setOnClickListener(this);

    }

    void initData(){
        imageLoader.displayImage(InternetURL.INTERNAL_PIC+ getGson().fromJson(getSp().getString("cover", ""), String.class), mine_head, UniversityApplication.txOptions, animateFirstListener);
        mine_name.setText(getGson().fromJson(getSp().getString("nick_name", ""), String.class));
        mine_birth.setText(getGson().fromJson(getSp().getString("birthday", ""), String.class));
        mine_sign.setText(getGson().fromJson(getSp().getString("sign", ""), String.class));
        mine_address.setText(getGson().fromJson(getSp().getString("address", ""), String.class));
        mine_yewu.setText(getGson().fromJson(getSp().getString("major", ""), String.class));
        mine_xq.setText(getGson().fromJson(getSp().getString("requirement", ""), String.class));

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("sex", ""), String.class))){
            switch (Integer.parseInt(getGson().fromJson(getSp().getString("sex", ""), String.class))){
                case -1:
                    mine_sex.setText("未设置");
                    break;
                case 0:
                    mine_sex.setText("男");
                    break;
                case 1:
                    mine_sex.setText("女");
                    break;
            }
        }

//        try {
//            dateline.setText(StringUtil.getFrontBackStrDate(String.valueOf(df.format(new Date())),"yyyy年MM月dd日 HH:mm",-1));
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        mine_birth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DateTimePickDialogUtil dateTimePicKDialog = null;
                try {
                    dateTimePicKDialog = new DateTimePickDialogUtil(
                            ProfileActivity.this,  StringUtil.getFrontBackStrDate(String.valueOf(mine_birth.getText().toString()), "yyyy-MM-dd",0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dateTimePicKDialog.dateTimePicKDialog(mine_birth);

            }
        });
//        dateline.setText(df.format(new Date()));
        this.findViewById(R.id.mineCompany).setOnClickListener(this);
    }

    // 性别选择
    private void ShowSexDialog() {
        sexPopWindow = new SexPopWindow(ProfileActivity.this, itemsOnClick);
        //显示窗口
        sexPopWindow.showAtLocation(ProfileActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            sexPopWindow.dismiss();
            switch (v.getId()) {
                case R.id.sex_man: {
                    sex = "0";
                    mine_sex.setText("男");
                }
                break;
                case R.id.sex_woman: {
                    sex = "1";
                    mine_sex.setText("女");
                }
                break;
                default:
                    break;
            }
        }
    };



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mine_head:
                //头像
                ShowPickDialog();
                break;
            case R.id.liner_sex:
                //性别
                ShowSexDialog();
                break;
            case R.id.mineCompany:
                //我的公司
                Intent mineView = new Intent(ProfileActivity.this, MineCompanyActivity.class);
                startActivity(mineView);
                break;

        }
    }

    public void addSave (View view){
        //
        if(StringUtil.isNullOrEmpty(mine_name.getText().toString())){
            showMsg(ProfileActivity.this, "请输入昵称");
            return;
        }
        if(!StringUtil.isNullOrEmpty(pics)){
            //有头像
            sendCover();
        }else {
            //没有头像
            saveNoPic("");
        }
    }

    void saveNoPic(final String url){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_MEMBER_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    Toast.makeText(ProfileActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                    if(!StringUtil.isNullOrEmpty(pics)){
                                        save("cover", pics);
                                    }
                                    if(!StringUtil.isNullOrEmpty(sex)){
                                        save("sex", sex);
                                    }
                                    save("birthday", mine_birth.getText().toString());
                                    save("sign", mine_sign.getText().toString());
                                    save("address", mine_address.getText().toString());
                                    save("major", mine_yewu.getText().toString());
                                    save("nick_name", mine_name.getText().toString());
                                    save("requirement", mine_xq.getText().toString());
                                    Intent intent = new Intent("updateSuccess");
                                    sendBroadcast(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(ProfileActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity.this, "获得数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                params.put("user_id", getGson().fromJson(getSp().getString("user_id", ""), String.class));
                if(!StringUtil.isNullOrEmpty(url)){
                    params.put("cover",url );
                }
                params.put("nick_name ",mine_name.getText().toString() );
                if(!StringUtil.isNullOrEmpty(sex)){
                    params.put("sex ", sex );
                }
                if(!StringUtil.isNullOrEmpty(mine_birth.getText().toString())){
                    try {
                        params.put("birthday ", TimeUtils.getCurrentMillion(mine_birth.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                params.put("sign ", mine_sign.getText().toString() );
                params.put("address ", mine_address.getText().toString() );
                params.put("major ", mine_yewu.getText().toString());
                params.put("requirement ", mine_xq.getText().toString());
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

    public void back(View view){finish();}

    // 选择相册，相机
    private void ShowPickDialog() {
        deleteWindow = new SelectPhoPopWindow(ProfileActivity.this, itemsOnClickPic);
        //显示窗口
        deleteWindow.showAtLocation(ProfileActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClickPic = new View.OnClickListener() {

        public void onClick(View v) {
            deleteWindow.dismiss();
            switch (v.getId()) {
                case R.id.camera: {
                    Intent camera = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                            .fromFile(new File(Environment
                                    .getExternalStorageDirectory(),
                                    "ppCover.jpg")));
                    startActivityForResult(camera, 2);
                }
                break;
                case R.id.mapstorage: {
                    Intent mapstorage = new Intent(Intent.ACTION_PICK, null);
                    mapstorage.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(mapstorage, 1);
                }
                break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/ppCover.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case 3:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            if (photo != null) {
                pics = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);
                if(!StringUtil.isNullOrEmpty(pics)){
//                    sendCover();
                    mine_head.setImageBitmap(photo);
                }
            }
        }
    }


    public void sendCover(){
            Bitmap bm = FileUtils.getSmallBitmap(pics);
            String cameraImagePath = FileUtils.saveBitToSD(bm, System.currentTimeMillis() + ".jpg");
            File f = new File(cameraImagePath);
            Map<String, File> files = new HashMap<String, File>();
            files.put("file", f);
            Map<String, String> params = new HashMap<String, String>();
            CommonUtil.addPutUploadFileRequest(
                    this,
                    InternetURL.UPLOAD_FILE_URL,    //http://115.29.200.169/api/test/uploadfile
                    files,
                    params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (StringUtil.isJson(s)) {
                                try {
                                    JSONObject jo = new JSONObject(s);
                                    String code1 = jo.getString("code");
                                    if (Integer.parseInt(code1) == 200) {
                                        String url = jo.getString("url");
                                        saveNoPic(url);
                                    }
                                } catch (JSONException e) {
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                    e.printStackTrace();
                                }
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
                    },
                    null);
    }


}
