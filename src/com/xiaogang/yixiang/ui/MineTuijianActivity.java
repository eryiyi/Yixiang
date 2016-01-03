package com.xiaogang.yixiang.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.UniversityApplication;
import com.xiaogang.yixiang.adapter.AnimateFirstDisplayListener;
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.base.InternetURL;

/**
 * Created by Administrator on 2015/12/23.
 */
public class MineTuijianActivity extends BaseActivity implements View.OnClickListener {
    private TextView mine_name;
    private ImageView mine_head;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuijian_activity);

        mine_name = (TextView) this.findViewById(R.id.mine_name);
        mine_head = (ImageView) this.findViewById(R.id.mine_head);


        mine_name.setText("我是"+ getGson().fromJson(getSp().getString("nick_name", ""), String.class));
        imageLoader.displayImage( UniversityApplication.member.getCover(), mine_head, UniversityApplication.txOptions, animateFirstListener);
    }

    @Override
    public void onClick(View view) {

    }

}
