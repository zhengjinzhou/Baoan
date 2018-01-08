package com.zhou.baoan.base;

/**
 * Created by zhou on 2018/1/5.
 *
 */

public class Constant {

    public static final String BASE_URL = "http://gcjg.baoan.gov.cn:9250";
    public static final String NEW_URL = "new_url";
    //登录地址
    public static final String LOGIN_URL = BASE_URL + "/wan_mpda_pic//WebService/WebService.asmx";
    public static final String PSW = "tKB1F69J4TgRTM7QRN1+NxDaURCluPAAYFaWJfMEdhryuqvuoRIA7sF7CzKsSngLPPpy5gmaOu4=";//宝安工务局
    public static final String UserBean = "userbean";
    public static  String ssoUrl = BASE_URL + "/WAN_MPDA_PIC/Handlers/SingleSignOnHandler.ashx?Action=SingleSignOnByXML";//首次跳转，进行验证合法性.
    public static String iniUrl = BASE_URL + "/WAN_MPDA_PIC/Handlers/SingleSignOnHandler.ashx?Action=Redirect&SessionId=";//如果验证通过后，第二次跳转，携带验证后的SessionId执行页面跳转.
    public static String UserName = "username";//保存当前用户名称
}
