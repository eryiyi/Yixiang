package com.xiaogang.yixiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.base.BaseActivity;

/**
 * Created by Administrator on 2015/12/23.
 */
public class MineQianbaoActivity extends BaseActivity implements View.OnClickListener {
    private TextView yue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qianbao_activity);

        yue = (TextView) this.findViewById(R.id.yue);
        yue.setText(getGson().fromJson(getSp().getString("money", ""), String.class));

        this.findViewById(R.id.chongzhi).setOnClickListener(this);
        this.findViewById(R.id.hongbao).setOnClickListener(this);
        this.findViewById(R.id.tixian).setOnClickListener(this);
        this.findViewById(R.id.yinhangka_one).setOnClickListener(this);
        this.findViewById(R.id.yinhangka_two).setOnClickListener(this);
        this.findViewById(R.id.yinhangka_three).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chongzhi:
                break;
            case R.id.hongbao:
                Intent redbag = new Intent(MineQianbaoActivity.this, MineRedBagActivity.class);
                startActivity(redbag);
                break;
            case R.id.tixian:
                Intent tixianView = new Intent(MineQianbaoActivity.this, TixianActivity.class);
                startActivity(tixianView);
                break;
            case R.id.yinhangka_one:
            case R.id.yinhangka_two:
            case R.id.yinhangka_three:
                break;
        }
    }

}
