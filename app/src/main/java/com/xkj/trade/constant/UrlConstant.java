package com.xkj.trade.constant;

/**
 * Created by huangsc on 2017-01-24.
 * TODO:
 */

public class UrlConstant {
    public static  String API_URL="crmv2.bbryt.cn";
    public static String URL_SERVICE_TIME = "http://"+API_URL+"/WebAPI/Public/servertime";
    public static String URL_LOGIN = "http://"+API_URL+"/WebAPI/Public/login";
    public static String URL_MT4_PRICE = "http://"+API_URL+"/WebAPI/Public/mt4price";
    public static String URL_MT4_USERLIST = "http://"+API_URL+"/WebAPI/Account/mt4userlist";
    public static String URL_MT4_RANKING = "http://"+API_URL+"/WebAPI/Master/ranking";
    public static   String WS_URL="ws://115.29.50.147:9984";
    public static String URL_TRADE_ORDER_EXE ="http://"+API_URL+"/WebAPI/Trades/OrderExe";
    public static String URL_TRADE_MAKET_LIST ="http://"+API_URL+"/WebAPI/Trades/maketlist";
    public static String URL_TRADE_PENDING_LIST ="http://"+API_URL+"/WebAPI/Trades/pendinglist";
    public static String URL_TRADE_HISTORY_LIST ="http://"+API_URL+"/WebAPI/Trades/historylist";
    public static String URL_MASTER_FOLLOW_TICKETS ="http://"+API_URL+"/WebAPI/Master/followtickets";//获取跟随高手当前订单/历史订单
    public static String URL_MASTER_FOLLOW ="http://"+API_URL+"/WebAPI/Master/follow";//跟随复制高手
    public static String URL_MASTER_NOFOLLOW ="http://"+API_URL+"/WebAPI/Master/nofollow";//取消跟随复制高手
    public static String URL_MASTER_FOLLOW_INFO ="http://"+API_URL+"/WebAPI/Master/info";//获取高手基本信息
    public static String URL_MASTER_FOLLOW_FOCUS ="http://"+API_URL+"/WebAPI/Master/focus";//关注高手
    public static String URL_MASTER_FOLLOW_NOFOCUS ="http://"+API_URL+"/WebAPI/Master/nofocus";//取消关注高手


}
