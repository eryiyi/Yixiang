package com.xiaogang.yixiang.ui;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.data.MineGuquanObjData;
import com.xiaogang.yixiang.data.PayData;
import com.xiaogang.yixiang.util.StringUtil;
import com.xiaogang.yixiang.weixinpay.Constants;
import com.xiaogang.yixiang.weixinpay.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayActivity extends BaseActivity {
	
	private IWXAPI api;
	private EditText jine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);

		jine = (EditText) this.findViewById(R.id.jine);


		api = WXAPIFactory.createWXAPI(this, "wx98992885feea801b");
	}

	public void addSure(View view){
		//
		if(StringUtil.isNullOrEmpty(jine.getText().toString())){
			showMsg(PayActivity.this, "请输入金额");
			return;
		}
		getData();
	}

	public void back(View view){finish();}


	void getData(){
		StringRequest request = new StringRequest(
				Request.Method.POST,
				InternetURL.WEIXIN_CHONGZHI_URL,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						if (StringUtil.isJson(s)) {
							try {
								JSONObject jo = new JSONObject(s);
								String code =  jo.getString("return_code");
								if("SUCCESS".equals(code)){
									PayData payData = getGson().fromJson(s, PayData.class);
									pay(payData);
								}
								else{
									Toast.makeText(PayActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
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
						Toast.makeText(PayActivity.this, "充值失败", Toast.LENGTH_SHORT).show();
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("user_id", getGson().fromJson(getSp().getString("user_id", ""), String.class));
				params.put("amount", jine.getText().toString());
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

	void pay(PayData payData){
//		String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
//		try{
//			byte[] buf = Util.httpGet(url);
//			if (buf != null && buf.length > 0) {
//				String content = new String(buf);
//				Log.e("get server pay params:",content);
//				JSONObject json = new JSONObject(content);
//				if(null != json && !json.has("retcode") ){
//
//				}else{
//					Toast.makeText(PayActivity.this, ""+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
//				}
//			}else{
//				Log.d("PAY_GET", "�������������");
//				Toast.makeText(PayActivity.this, "", Toast.LENGTH_SHORT).show();
//			}
//		}catch(Exception e){
//			Toast.makeText(PayActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//		}

//
//
//
//
//

//

		//毫秒值
		long hmz = System.currentTimeMillis();
		PayReq req = new PayReq();
		//req.appId = "wxf8b4f85f3a794e77";  // ������appId
		req.appId			= payData.getAppid();
		req.partnerId		= payData.getMch_id();
		req.prepayId		= payData.getPrepay_id();
		req.nonceStr		= payData.getNonce_str();
		req.timeStamp		= String.valueOf("1412000000");
		req.packageValue	= "Sign=WXPay";
		req.sign			= payData.getSign();
		req.extData			= payData.getTrade_type(); // optional
		Toast.makeText(PayActivity.this, "123", Toast.LENGTH_SHORT).show();
		api.sendReq(req);


	}
}
