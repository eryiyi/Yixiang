package com.xiaogang.yixiang.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.WriterException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.UniversityApplication;
import com.xiaogang.yixiang.adapter.AnimateFirstDisplayListener;
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.base.InternetURL;
import com.xiaogang.yixiang.util.StringUtil;

/**
 * Created by Administrator on 2015/12/23.
 */
public class MineTuijianActivity extends BaseActivity implements View.OnClickListener {
    private TextView mine_name;
    private ImageView mine_head;
    private ImageView mineImg;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    UMSocialService mController;
    String shareCont = "";//内容
    String shareUrl = InternetURL.SHARE_URL;

    String shareParams = "";
    String appID = "wx198fc23a0fae697a";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuijian_activity);

        mine_name = (TextView) this.findViewById(R.id.mine_name);
        mine_head = (ImageView) this.findViewById(R.id.mine_head);
        mineImg = (ImageView) this.findViewById(R.id.mineImg);


        mine_name.setText("我是"+ getGson().fromJson(getSp().getString("nick_name", ""), String.class));
        imageLoader.displayImage( UniversityApplication.member.getCover(), mine_head, UniversityApplication.txOptions, animateFirstListener);
        try {
            mineImg.setImageBitmap(StringUtil.Create2DCode(getGson().fromJson(getSp().getString("download_code", ""), String.class)));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        this.findViewById(R.id.shareBtnOne).setOnClickListener(this);
        this.findViewById(R.id.shareBtnTwo).setOnClickListener(this);


        mController = UMServiceFactory.getUMSocialService(MineTuijianActivity.class.getName(), RequestType.SOCIAL);
        Bitmap bitmap = null;
        try {
             bitmap  = StringUtil.Create2DCode(getGson().fromJson(getSp().getString("download_code", ""), String.class));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        mController.setShareMedia(new UMImage(this, bitmap));//设置分享图片
        shareParams = "?action=" ;//设置分享链接
        mController.setShareContent(shareCont + "," + shareUrl + shareParams);//设置分享内容

//        //新浪微博
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        //腾讯微博
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        //1.添加QQ空间分享
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "1104297339", "pbodVVpCHwKwm7W9");
        qZoneSsoHandler.setTargetUrl(shareUrl + shareParams);
        qZoneSsoHandler.addToSocialSDK();
        //2.添加QQ好友分享
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104297339", "pbodVVpCHwKwm7W9");
        qqSsoHandler.setTargetUrl(shareUrl + shareParams);
        qqSsoHandler.addToSocialSDK();
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, appID);
        wxHandler.addToSocialSDK();
        //支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, appID);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        //单独设置微信分享
        WeiXinShareContent xinShareContent = new WeiXinShareContent();
        xinShareContent.setShareContent(shareCont);
        xinShareContent.setTitle(shareCont);
        xinShareContent.setShareImage(new UMImage(this, bitmap));
        xinShareContent.setTargetUrl(shareUrl + shareParams);
        mController.setShareMedia(xinShareContent);
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(shareCont);
        circleMedia.setTitle(shareCont);
        circleMedia.setShareImage(new UMImage(this, bitmap));
        circleMedia.setTargetUrl(shareUrl + shareParams);
        mController.setShareMedia(circleMedia);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shareBtnOne:
            case R.id.shareBtnTwo:
                mController.openShare(this, false);
                break;
        }
    }

}
