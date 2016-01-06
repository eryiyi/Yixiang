package com.xiaogang.yixiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.base.ActivityTack;
import com.xiaogang.yixiang.base.BaseActivity;

/**
 * Created by Administrator on 2015/12/23.
 */
public class MineSetActivity extends BaseActivity implements View.OnClickListener {
    private TextView mine_yixianghao;
    private TextView mine_mobile;
    private TextView mine_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_activity);

        mine_yixianghao = (TextView) this.findViewById(R.id.mine_yixianghao);
        mine_mobile = (TextView) this.findViewById(R.id.mine_mobile);
        mine_email = (TextView) this.findViewById(R.id.mine_email);

        mine_yixianghao.setText(getGson().fromJson(getSp().getString("user_id", ""), String.class));
        mine_mobile.setText(getGson().fromJson(getSp().getString("mobile", ""), String.class));
        mine_email.setText(getGson().fromJson(getSp().getString("email", ""), String.class));

        this.findViewById(R.id.mine_pwr).setOnClickListener(this);
        this.findViewById(R.id.quite).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mine_pwr:
                //
                Intent updatePwr = new Intent(MineSetActivity.this, UpdatePwrActivity.class);
                startActivity(updatePwr);
                break;
            case R.id.quite:
                //退出
                ActivityTack.getInstanse().popUntilActivity(LoginActivity.class);
                break;
        }
    }

}
