package com.xiaogang.yixiang.data;

import com.xiaogang.yixiang.module.ArticleObj;

import java.util.List;

/**
 * Created by Administrator on 2015/12/29.
 */
public class ArticleObjData extends Data {
    private List<ArticleObj> data;

    public List<ArticleObj> getData() {
        return data;
    }

    public void setData(List<ArticleObj> data) {
        this.data = data;
    }
}
