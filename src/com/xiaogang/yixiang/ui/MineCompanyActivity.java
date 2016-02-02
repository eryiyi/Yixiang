package com.xiaogang.yixiang.ui;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
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
import com.xiaogang.yixiang.data.CompanyObjData;
import com.xiaogang.yixiang.module.CompanyImage;
import com.xiaogang.yixiang.module.CompanyObj;
import com.xiaogang.yixiang.upload.CommonUtil;
import com.xiaogang.yixiang.util.CompressPhotoUtil;
import com.xiaogang.yixiang.util.FileUtils;
import com.xiaogang.yixiang.util.StringUtil;
import com.xiaogang.yixiang.widget.CustomProgressDialog;
import com.xiaogang.yixiang.widget.SelectPhoPopWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/5.
 */
public class MineCompanyActivity extends BaseActivity implements View.OnClickListener {
    private EditText name;
    private EditText tel;
    private EditText address;
    private EditText jieshao;
    private TextView textOne;
    private TextView textTwo;
    private TextView textThree;
    private TextView textFour;
    private TextView textFive;
    private int tmpPosition;
    private String picurl1;
    private String picurl2;
    private String picurl3;
    private String picurl4;
    private String picurl5;

    private String is_add;
    private CompanyObj companyObj;
    private LinearLayout line_one;
    private LinearLayout line_two;
    private LinearLayout line_three;
    private LinearLayout line_four;
    private LinearLayout line_five;

    private String prod_image_id_1;
    private String prod_image_id_2;
    private String prod_image_id_3;
    private String prod_image_id_4;
    private String prod_image_id_5;

    List<CompanyImage> company_image = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_company);

        name = (EditText) this.findViewById(R.id.name);
        tel = (EditText) this.findViewById(R.id.tel);
        address = (EditText) this.findViewById(R.id.address);
        jieshao = (EditText) this.findViewById(R.id.jieshao);
        textOne = (TextView) this.findViewById(R.id.textOne);
        textTwo = (TextView) this.findViewById(R.id.textTwo);
        textThree = (TextView) this.findViewById(R.id.textThree);
        textFour = (TextView) this.findViewById(R.id.textFour);
        textFive = (TextView) this.findViewById(R.id.textFive);

        this.findViewById(R.id.btnOne).setOnClickListener(this);
        this.findViewById(R.id.btnTwo).setOnClickListener(this);
        this.findViewById(R.id.btnThree).setOnClickListener(this);
        this.findViewById(R.id.btnFour).setOnClickListener(this);
        this.findViewById(R.id.btnFive).setOnClickListener(this);

        line_one = (LinearLayout) this.findViewById(R.id.line_one);
        line_two = (LinearLayout) this.findViewById(R.id.line_two);
        line_three = (LinearLayout) this.findViewById(R.id.line_three);
        line_four = (LinearLayout) this.findViewById(R.id.line_four);
        line_five = (LinearLayout) this.findViewById(R.id.line_five);
        progressDialog = new CustomProgressDialog(MineCompanyActivity.this , "请稍后", R.anim.frame_paopao);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getVa();
    }

    void getVa(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.COMPANY_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                is_add = code;
                                if("200".equals(code)){
                                    //如果成功
                                    CompanyObjData data = getGson().fromJson(s, CompanyObjData.class);
                                    companyObj = data.getData();
                                    name.setText(companyObj.getCompany_name());
                                    tel.setText(companyObj.getCompany_phone());
                                    address.setText(companyObj.getCompany_address());
                                    jieshao.setText(companyObj.getCompany_introduce());
                                    company_image = companyObj.getCompany_image();
                                    if(company_image != null && company_image.size()>0){
                                        CompanyImage companyImage1 = company_image.get(0);
                                        if(companyImage1 != null ){
                                            textOne.setText(companyImage1.getProd_image());
                                            prod_image_id_1 = companyImage1.getImage_id();
                                        }

                                        CompanyImage companyImage2 = company_image.get(1);
                                        if(companyImage2 != null ){
                                            textTwo.setText(companyImage2.getProd_image());
                                            prod_image_id_2 = companyImage2.getImage_id();
                                        }

                                        CompanyImage companyImage3 = company_image.get(2);
                                        if(companyImage3 != null ){
                                            textThree.setText(companyImage3.getProd_image());
                                            prod_image_id_3 = companyImage3.getImage_id();
                                        }

                                        CompanyImage companyImage4 = company_image.get(3);
                                        if(companyImage4 != null ){
                                            textFour.setText(companyImage4.getProd_image());
                                            prod_image_id_4 = companyImage4.getImage_id();
                                        }

                                        CompanyImage companyImage5 = company_image.get(4);
                                        if(companyImage5 != null ){
                                            textFive.setText(companyImage5.getProd_image());
                                            prod_image_id_5 = companyImage5.getImage_id();
                                        }
                                    }

                                    //说明已经添加过了
//                                    line_one.setVisibility(View.GONE);
//                                    line_two.setVisibility(View.GONE);
//                                    line_three.setVisibility(View.GONE);
//                                    line_four.setVisibility(View.GONE);
//                                    line_five.setVisibility(View.GONE);
                                }
                                else{
                                    Toast.makeText(MineCompanyActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOne:
                tmpPosition = 1;
                ShowPickDialog();
                break;
            case R.id.btnTwo:
                tmpPosition = 2;
                ShowPickDialog();
                break;
            case R.id.btnThree:
                tmpPosition = 3;
                ShowPickDialog();
                break;
            case R.id.btnFour:
                tmpPosition = 4;
                ShowPickDialog();
                break;
            case R.id.btnFive:
                tmpPosition = 5;
                ShowPickDialog();
                break;
        }
    }
    private SelectPhoPopWindow deleteWindow;
    // 选择相册，相机
    private void ShowPickDialog() {
        deleteWindow = new SelectPhoPopWindow(MineCompanyActivity.this, itemsOnClickPic);
        //显示窗口
        deleteWindow.showAtLocation(MineCompanyActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

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

    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/liangxun/PhotoCache");
    Bitmap photo;
    private String pics1 = "";
    private String pics2 = "";
    private String pics3 = "";
    private String pics4 = "";
    private String pics5 = "";


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
                switch (tmpPosition){
                    case 1:
                        pics1 = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);

                        sendPicOne(pics1);
                        break;
                    case 2:
                        pics2 = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);

                        sendPicOne(pics2);
                        break;
                    case 3:
                        pics3 = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);

                        sendPicOne(pics3);
                        break;
                    case 4:
                        pics4 = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);

                        sendPicOne(pics4);
                        break;
                    case 5:
                        pics5 = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);

                        sendPicOne(pics5);
                        break;
                }

//                if(!StringUtil.isNullOrEmpty(pics)){
////                    sendCover();
////                    mine_head.setImageBitmap(photo);
//
//                }
            }
        }
    }


    public void sendPicOne(String picss){
        Bitmap bm = FileUtils.getSmallBitmap(picss);
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

                                    switch (tmpPosition){
                                        case 1:
                                            picurl1 = jo.getString("url");
                                            textOne.setText(picurl1);
                                            break;
                                        case 2:
                                            picurl2 = jo.getString("url");
                                            textTwo.setText(picurl2);
                                            break;
                                        case 3:
                                            picurl3 = jo.getString("url");
                                            textThree.setText(picurl3);
                                            break;
                                        case 4:
                                            picurl4 = jo.getString("url");
                                            textFour.setText(picurl4);
                                           break;
                                        case 5:
                                            picurl5 = jo.getString("url");
                                            textFive.setText(picurl5);
                                            break;
                                    }
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

    public void addSave(View view){

        if("200".equals(is_add)){
            //说明是编辑
            if(StringUtil.isNullOrEmpty(name.getText().toString())){
                showMsg(MineCompanyActivity.this, "请输入公司名称");
                return;
            }
            if(StringUtil.isNullOrEmpty(tel.getText().toString())){
                showMsg(MineCompanyActivity.this, "请输入公司电话");
                return;
            }
            if(StringUtil.isNullOrEmpty(address.getText().toString())){
                showMsg(MineCompanyActivity.this, "请输入公司地址");
                return;
            }
            if(StringUtil.isNullOrEmpty(jieshao.getText().toString())){
                showMsg(MineCompanyActivity.this, "请输入公司介绍");
                return;
            }
            progressDialog = new CustomProgressDialog(MineCompanyActivity.this , "请稍后", R.anim.frame_paopao);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            getDataEdit();
        }else {
            //说明是新增
            if(StringUtil.isNullOrEmpty(name.getText().toString())){
                showMsg(MineCompanyActivity.this, "请输入公司名称");
                return;
            }
            if(StringUtil.isNullOrEmpty(tel.getText().toString())){
                showMsg(MineCompanyActivity.this, "请输入公司电话");
                return;
            }
            if(StringUtil.isNullOrEmpty(address.getText().toString())){
                showMsg(MineCompanyActivity.this, "请输入公司地址");
                return;
            }
            if(StringUtil.isNullOrEmpty(jieshao.getText().toString())){
                showMsg(MineCompanyActivity.this, "请输入公司介绍");
                return;
            }
//            if(StringUtil.isNullOrEmpty(textOne.getText().toString())
//                    || StringUtil.isNullOrEmpty(textTwo.getText().toString())
//                    ||StringUtil.isNullOrEmpty(textThree.getText().toString()) ||
//                    StringUtil.isNullOrEmpty(textFour.getText().toString()) ||
//                    StringUtil.isNullOrEmpty(textFive.getText().toString()) ){
//                showMsg(MineCompanyActivity.this, "请输入图片介绍");
//                return;
//            }
            progressDialog = new CustomProgressDialog(MineCompanyActivity.this , "请稍后", R.anim.frame_paopao);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            getData();
        }

    }
    public void back(View view){
        finish();
    }

    void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.COMPANY_INFO_ADD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    Toast.makeText(MineCompanyActivity.this, "新增公司成立", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    Toast.makeText(MineCompanyActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("company_name", name.getText().toString());
                params.put("company_phone", tel.getText().toString());
                params.put("company_address", address.getText().toString());
                params.put("company_introduce", jieshao.getText().toString());
                if(!StringUtil.isNullOrEmpty(picurl1)){
                    params.put("file1", picurl1);
                }
                if(!StringUtil.isNullOrEmpty(picurl2)){
                    params.put("file2", picurl2);
                }
                if(!StringUtil.isNullOrEmpty(picurl3)){
                    params.put("file3", picurl3);
                }
                if(!StringUtil.isNullOrEmpty(picurl4)){
                    params.put("file4", picurl4);
                }
                if(!StringUtil.isNullOrEmpty(picurl2)){
                    params.put("file2", picurl2);
                }
                if(!StringUtil.isNullOrEmpty(picurl5)){
                    params.put("file5", picurl5);
                }

                if(!StringUtil.isNullOrEmpty(textOne.getText().toString())){
                    params.put("prod_name_1", textOne.getText().toString() );
                }
                if(!StringUtil.isNullOrEmpty(textTwo.getText().toString())){
                    params.put("prod_name_2", textTwo.getText().toString() );
                }
                if(!StringUtil.isNullOrEmpty(textThree.getText().toString())){
                    params.put("prod_name_3", textThree.getText().toString() );
                }
                if(!StringUtil.isNullOrEmpty(textFour.getText().toString())){
                    params.put("prod_name_4", textFour.getText().toString() );
                }
                if(!StringUtil.isNullOrEmpty(textFive.getText().toString())){
                    params.put("prod_name_5", textFive.getText().toString() );
                }
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

    void getDataEdit(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.COMPANY_INFO_EDIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    Toast.makeText(MineCompanyActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    Toast.makeText(MineCompanyActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("company_name", name.getText().toString());
                params.put("company_phone", tel.getText().toString());
                params.put("company_address", address.getText().toString());
                params.put("company_introduce", jieshao.getText().toString());

                if(!StringUtil.isNullOrEmpty(prod_image_id_1)){
                    params.put("prod_image_id_1", prod_image_id_1);
                }
                if(!StringUtil.isNullOrEmpty(prod_image_id_2)){
                    params.put("prod_image_id_2", prod_image_id_2);
                }
                if(!StringUtil.isNullOrEmpty(prod_image_id_3)){
                    params.put("prod_image_id_3", prod_image_id_3);
                }
                if(!StringUtil.isNullOrEmpty(prod_image_id_4)){
                    params.put("prod_image_id_4", prod_image_id_4);
                }
                if(!StringUtil.isNullOrEmpty(prod_image_id_5)){
                    params.put("prod_image_id_5", prod_image_id_5);
                }

                if(!StringUtil.isNullOrEmpty(picurl1)){
                    params.put("file1", picurl1);
                }
                if(!StringUtil.isNullOrEmpty(picurl2)){
                    params.put("file2", picurl2);
                }
                if(!StringUtil.isNullOrEmpty(picurl3)){
                    params.put("file3", picurl3);
                }
                if(!StringUtil.isNullOrEmpty(picurl4)){
                    params.put("file4", picurl4);
                }
                if(!StringUtil.isNullOrEmpty(picurl2)){
                    params.put("file2", picurl2);
                }
                if(!StringUtil.isNullOrEmpty(picurl5)){
                    params.put("file5", picurl5);
                }

                if(!StringUtil.isNullOrEmpty(textOne.getText().toString())){
                    params.put("prod_name_1", textOne.getText().toString() );
                }
                if(!StringUtil.isNullOrEmpty(textTwo.getText().toString())){
                    params.put("prod_name_2", textTwo.getText().toString() );
                }
                if(!StringUtil.isNullOrEmpty(textThree.getText().toString())){
                    params.put("prod_name_3", textThree.getText().toString() );
                }
                if(!StringUtil.isNullOrEmpty(textFour.getText().toString())){
                    params.put("prod_name_4", textFour.getText().toString() );
                }
                if(!StringUtil.isNullOrEmpty(textFive.getText().toString())){
                    params.put("prod_name_5", textFive.getText().toString() );
                }

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
