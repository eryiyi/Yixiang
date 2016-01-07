package com.xiaogang.yixiang.data;

import com.xiaogang.yixiang.module.CardBank;

import java.util.List;

/**
 * Created by Administrator on 2016/1/6.
 */
public class CardBankData extends Data {
    private List<CardBank> data;

    public List<CardBank> getData() {
        return data;
    }

    public void setData(List<CardBank> data) {
        this.data = data;
    }
}
