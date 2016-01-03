package com.xiaogang.yixiang.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.adapter.ItemGoutongAdapter;
import com.xiaogang.yixiang.base.BaseFragment;
import com.xiaogang.yixiang.module.Talents;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class OneFragment extends BaseFragment implements View.OnClickListener {
    private ListView lstv;
    private ItemGoutongAdapter adapter;
    List<Talents> lists = new ArrayList<Talents>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_fragment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        lists.add(new Talents());
        lists.add(new Talents());
        lists.add(new Talents());
        lists.add(new Talents());
        lists.add(new Talents());
        lists.add(new Talents());
        lists.add(new Talents());
        lists.add(new Talents());
        lists.add(new Talents());
        lists.add(new Talents());
        lists.add(new Talents());
        lists.add(new Talents());
        lstv = (ListView) view.findViewById(R.id.lstv);
        adapter = new ItemGoutongAdapter(lists, getActivity());
        lstv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
