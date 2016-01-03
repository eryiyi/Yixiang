package com.xiaogang.yixiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.xiaogang.yixiang.MainActivity;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.base.BaseActivity;

/**
 * Created by Administrator on 2015/8/19.
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener,Runnable {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        // 启动一个线程
        new Thread(WelcomeActivity.this).start();
    }

    @Override
    public void onClick(View view) {}

    @Override
    public void run() {
        try {
            // 3秒后跳转到登录界面
            Thread.sleep(3000);
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
