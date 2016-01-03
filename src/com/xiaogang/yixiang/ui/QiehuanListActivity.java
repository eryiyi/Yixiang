package com.xiaogang.yixiang.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.adapter.ItemCityAdapter;
import com.xiaogang.yixiang.base.BaseActivity;
import com.xiaogang.yixiang.module.Talents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/14.
 */
public class QiehuanListActivity extends BaseActivity implements View.OnClickListener {
    private TextView mine_title;
    private List<Talents> arrayList = new ArrayList<Talents>();
    private ListView lstv;
    private ItemCityAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qiehuan_acitivty);
        arrayList = (List<Talents>) getIntent().getExtras().get("talentses");

        mine_title = (TextView) this.findViewById(R.id.mine_title);
        mine_title.setOnClickListener(this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemCityAdapter(arrayList, QiehuanListActivity.this);
        lstv.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mine_title:
                finish();
                break;
        }
    }
}
