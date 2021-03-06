package com.zhou.baoan.base;

/**
 * Created by zhou on 2018/1/5.
 *
 */

public class Constant {

    /**
     * 宝安区  http://gcjg.baoan.gov.cn:9250  序列号：tKB1F69J4TgRTM7QRN1+NxDaURCluPAAYFaWJfMEdhryuqvuoRIA7sF7CzKsSngLPPpy5gmaOu4=
     *
     * 勤智资本 http://121.15.203.82:9120  序列号：ysmi8nF7R3L/64UB2oGK4d7kx3kgvJ4PF2Uwk7k3jLeN1U1O+clGj7Jm0EFZLzYPaJ512P+SE3I=
     *
     * 深圳蓝田 序列号：ysmi8nF7R3L/64UB2oGK4d7kx3kgvJ4PF2Uwk7k3jLeN1U1O+clGj7Jm0EFZLzYPaJ512P+SE3I=    IP地址：http://121.15.203.82:9130
     *
     * 南山工务局 序列号：8PDuOH1Qql6d0t3CpZGKjmZv3cVzOviVfg+TRCIeJ2wt54AO0hBUcJk49db2CFY2QOOMyv2ih0E=   地址：183.62.232.185:8011
     */
    public static final String BASE_URL = "http://121.15.203.82:9130";
    public static final String NEW_URL = "new_url";
    public static final String OPTYE_TIME = "+\"&OpenType=capital_index\"";
    //登录地址
    public static final String LOGIN_URL = BASE_URL + "/wan_mpda_pic//WebService/WebService.asmx";
    public static final String PSW = "ysmi8nF7R3L/64UB2oGK4d7kx3kgvJ4PF2Uwk7k3jLeN1U1O+clGj7Jm0EFZLzYPaJ512P+SE3I=";
    public static final String UserBean = "userbean";
    public static  String ssoUrl = BASE_URL + "/WAN_MPDA_PIC/Handlers/SingleSignOnHandler.ashx?Action=SingleSignOnByXML";//首次跳转，进行验证合法性.
    public static String iniUrl = BASE_URL + "/WAN_MPDA_PIC/Handlers/SingleSignOnHandler.ashx?Action=Redirect&SessionId=";//如果验证通过后，第二次跳转，携带验证后的SessionId执行页面跳转.
    public static String UserName = "username";//保存当前用户名称
}
