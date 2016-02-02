package com.xiaogang.yixiang.module;

import java.util.List;

/**
 * Created by Administrator on 2016/1/6.
 *  "uid": "16",
 "company_name": "hru",
 "company_phone": "",
 "company_address": "gbbb",
 "company_introduce": "ggghbg",
 "user_id": "45"
 */
public class CompanyObj {
    private String uid;
    private String company_name;
    private String company_phone;
    private String company_address;
    private String company_introduce;
    private String user_id;
    private List<CompanyImage> company_image;

    public List<CompanyImage> getCompany_image() {
        return company_image;
    }

    public void setCompany_image(List<CompanyImage> company_image) {
        this.company_image = company_image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_phone() {
        return company_phone;
    }

    public void setCompany_phone(String company_phone) {
        this.company_phone = company_phone;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getCompany_introduce() {
        return company_introduce;
    }

    public void setCompany_introduce(String company_introduce) {
        this.company_introduce = company_introduce;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
