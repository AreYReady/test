package com.xkj.trade.constant;

/**
 * @author huangsc
 * @date 2016-12-24
 * TODO:接口参数的key
 */
public interface RequestConstant {
    //账号
    String ACCOUNT = "account";
    //mt4账号
    String LOGIN = "login";

    String API_ID = "apiid";
    //服务器时间
    String API_TIME = "apitime";
    //mt4账号密码
    String LOGIN_PASSWORD = "password";
    //标志
    String API_SIGN = "apisign";
    //交易名
    String SYMBOL = "symbol";
    //时间段
    String PERIOD = "period";
    //数量
    String BARNUM = "barnum";
    //是否分页
    String IS_PAGE = "ispage";

    String RANK_TYPE = "rankType";
    //操作动作
    String ACTION = "action";
    String EXC = "exc";
    String VOLUME = "volume";
    String PRICE = "price";
    String SL = "sl";
    String TP = "tp";
    String ORDERNO = "orderno";
    String IS_FILTER = "isfilter";

     enum Exc {
         BUY("BUY"),//买
         SELL("SELL"),//卖
         BUY_LIMIT("BUYLIMIT"),//买限制
         SELL_LIMIT("SELLLIMIT"),//卖限制
         BUY_STOP("BUYSTOP"),//买止损
         SELL_STOP("SELLSTOP");//买止损
        private final String text;

         Exc(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
    enum Action {
        MAKET("maket"),//开市场单
        PENDING ("pending"),//挂单交易
        CLOSE("close"),//③平仓交易
        DELETE("delete"),//删除取消
        EDIT("edit");//订单修改
        private final String text;

        Action(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    String PROFIT="profit";
    String CURRENT_PRICE="CURRENT_PRICE";

}