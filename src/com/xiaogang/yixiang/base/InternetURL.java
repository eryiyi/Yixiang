package com.xiaogang.yixiang.base;

/**
 * Created by liuzwei on 2015/1/12.
 */
public class InternetURL {
    public static final String INTERNAL = "http://yixiang.apptech.space/mobile.php/";
    public static final String INTERNAL_PIC = "http://yixiang.apptech.space/";

    public static final String GET_TOKEN = INTERNAL + "user.api-authkey";
    //2 2、 发送验证码
    public static final String REG_SEND_CODE_URL = INTERNAL + "user.api-sendCode";
    //校验验证码
    public static final String REG_VAL_CODE_URL = INTERNAL + "user.api-verityCode";
    //注册
    public static final String REG_CODE_URL = INTERNAL + "user.api-register";
    //3 、登陆 (user.api- - login)
    public static final String LOGIN_URL = INTERNAL + "user.api-login";
    //修改密码发送验证码
    public static final String UPDATE_PWR_SEND_CARD_URL = INTERNAL + "user.api-sendCheckCode";
    //修改密码修改密码
    public static final String UPDATE_PWR_UPDATE_CARD_URL = INTERNAL + "user.api-updatePassword";
    // 、 查看 公司介绍 (user.api- -
    public static final String COMPANY_INFO_URL = INTERNAL + "user.api-companyInfo";
    // 、 编辑 公司介绍 (user.api- - companyInfoEdit)
    public static final String COMPANY_INFO_EDIT_URL = INTERNAL + "user.api-companyInfoEdit";
    // 、 新增 公司介绍 (user.api- -
    public static final String COMPANY_INFO_ADD_URL = INTERNAL + "user.api-companyInfoAdd";
    // 、 发布招聘 (user.api- -employmentInfoAdd) )
    public static final String ADD_EMPLOYMENT_INFO_URL = INTERNAL + "user.api-employmentInfoAd";
    //9 、 添加 应聘 (user.api- -jobHuntingAdd) )
    public static final String ADD_JOB_HUNTING_URL = INTERNAL + "user.api-jobHuntingAdd";
    // 、 修改 应聘 (user.api- - jobHuntingEdit) )
    public static final String UPDATE_JOB_HUNTING_URL = INTERNAL + "user.api-jobHuntingEdit";
    // 、 查看 我的股权 (user.api- - get MyGuQuan) )
    public static final String GET_GUQUAN_URL = INTERNAL + "user.api-getMyGuQuan";
    // 、 更新 我的股权 (user.api- -
    public static final String UPDATE_GUQUAN_URL = INTERNAL + "user.api-updateMemberInfo";
    // 、 查看 文章( ( article .api- -
    public static final String GET_NEWS_URL = INTERNAL + "article.api-news";
    public static final String GET_NEWS_DETAIL_URL = INTERNAL + "article.api-detail";
    // 、 上传地理位置( ( user .api- -
    public static final String UP_LOCATION_URL = INTERNAL + "user.api-saveLngLat";
    // 、 股权排名 (user.api- - guquanOrderBy
    public static final String GUQUAN_ORDER_URL = INTERNAL + "user.api-guquanOrderBy";
    // 、 附近人才 (user.api- -talentsAround) )
    public static final String AROUND_TALENT_URL = INTERNAL + "user.api-talentsAround";
//    / 、 筛选招聘与应聘 (user.api- -talentsAround) )
    public static final String LV_AROUND_TALENT_URL = INTERNAL + "user.api-talentsAround";
    // 、 查看 个人中心 (user.api- -memberInfo) )
    public static final String MEMBER_INFO_URL = INTERNAL + "user.api-memberInfo";
    // 、 编辑个人中心 (user.api- -updateMemberInfo) )
    public static final String UPDATE_MEMBER_INFO_URL = INTERNAL + "user.api-updateMemberInfo";
    //20 、 获取好友列表 (chat.api- -showFriend) )
    public static final String CHAT_FRIEND_URL = INTERNAL + "chat.api-showFriend";
//    、 获取好友列表 (chat.api- -getAllInfo) )
    public static final String GETALL_CHAT_FRIEND_URL = INTERNAL + "chat.api-getAllInfo";
    //22 、发 红包 (redPack.api- -send) )
    public static final String RED_SEND_URL = INTERNAL + "redPack.api-send";
    // 、拆 红包
    public static final String PACK_RED_SEND_URL = INTERNAL + "redPack.api-get";
    // 、 红包列表
    public static final String LIST_RED_SEND_URL = INTERNAL + "redPack.api-lists";
    // 、 领取股份 (equity.api- -ge
    public static final String GET_EQUYLITY_URL = INTERNAL + "equity.api-get";
//    、、26 、微信充值
    public static final String WEIXIN_CHONGZHI_URL = INTERNAL_PIC + "Weixin/unifiedorder";
    //27 、
    public static final String SHARE_URL = INTERNAL_PIC + "share/index";
    // 、提现
    public static final String DRAW_URL = INTERNAL + "withdraw.api-set";
    // 、上传图片
    public static final String UPLOAD_FILE_URL = INTERNAL + "user.api-uploadfile";
    // 、发布需求
    public static final String ADD_REQUIRE_URL = INTERNAL + "require.api-add";
    //需求列表
    public static final String LIST_REQUIRE_URL = INTERNAL + "require.api-lists";
    //轮播图
    public static final String AD_LUNBO_URL = INTERNAL + "adv.api-lists";
    // 、设置银行卡
    public static final String YINHANG_SET_URL = INTERNAL + "bank.api-set";
    //获取银行卡
    public static final String YINHANG_GET_URL = INTERNAL + "bank.api-get";
    //删除银行卡
    public static final String YINHANG_DELETE_URL = INTERNAL + "bank.api-del";
    //查询用户 根据昵称和手机号
    public static final String FIND_MEMBER_URL = INTERNAL + "user.api-findMember";
}
