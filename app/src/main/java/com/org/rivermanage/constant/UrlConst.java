package com.org.rivermanage.constant;



public class UrlConst {
    public static final String UP_RECORD = GlobalConst.SERVER_IP +   "/warn/phoneList";
    public static String LOGIN        =     GlobalConst.SERVER_IP +   "/user_phone_Login";   //登录
    public static String LOGOUT = GlobalConst.SERVER_IP+"/user_phoneLogout";  //注销
    public static String FILE_UPLOAD   =  GlobalConst.SERVER_IP +"/warning_phone_Post";//文件上传
    public static String SELRCT_WRNING   =  GlobalConst.SERVER_IP +"/warning_showAllData";//查询所有警告点
    public static String SELRCTByLoginId_WRNING   =  GlobalConst.SERVER_IP +"/warning_showDataByName";//查询所有警告点

}
