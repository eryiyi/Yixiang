package com.xiaogang.yixiang.module;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/14.
 */
public class Talents implements Serializable{
    private String reg_time;
    private String user_id;
    private String truename;
    private String cover;
    private String sex;
    private String lng;
    private String lat;
    private String companyNameOrCareer;
    private String identity;
    private String positonOrHobby;
    private String distance;

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCompanyNameOrCareer() {
        return companyNameOrCareer;
    }

    public void setCompanyNameOrCareer(String companyNameOrCareer) {
        this.companyNameOrCareer = companyNameOrCareer;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPositonOrHobby() {
        return positonOrHobby;
    }

    public void setPositonOrHobby(String positonOrHobby) {
        this.positonOrHobby = positonOrHobby;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
