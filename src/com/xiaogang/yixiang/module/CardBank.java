package com.xiaogang.yixiang.module;

/**
 * Created by Administrator on 2016/1/6.
 *  "id": "8",
 "user_id": "815862327",
 "true_name": "张三",
 "card_num": "11158995",
 "card_bank": "工商",
 "rev_phone": "110"
 */
public class CardBank {
    private String id;
    private String user_id;
    private String true_name;
    private String card_num;
    private String card_bank;
    private String rev_phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTrue_name() {
        return true_name;
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getCard_bank() {
        return card_bank;
    }

    public void setCard_bank(String card_bank) {
        this.card_bank = card_bank;
    }

    public String getRev_phone() {
        return rev_phone;
    }

    public void setRev_phone(String rev_phone) {
        this.rev_phone = rev_phone;
    }
}
