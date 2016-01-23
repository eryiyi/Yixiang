package com.xiaogang.yixiang.data;

import com.xiaogang.yixiang.module.Member;

import java.util.List;

/**
 * Created by Administrator on 2015/12/15.
 */
public class MembersData extends Data {
    private List<Member> data;

    public List<Member> getData() {
        return data;
    }

    public void setData(List<Member> data) {
        this.data = data;
    }
}
