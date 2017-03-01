package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-02-27.
 * TODO:
 */

public class BeanMasterProtfolio {

    /**
     * status : 1
     * response : {"orders":[{"total":"119","winnum":"37"}],"sumorders":[{"orders":"27","symbol":"AUDUSD"},{"orders":"6","symbol":"EURAUD"},{"orders":"2","symbol":"EURCHF"},{"orders":"2","symbol":"EURGBP"},{"orders":"14","symbol":"EURUSD"},{"orders":"4","symbol":"GBPUSD"},{"orders":"4","symbol":"USDCAD"},{"orders":"2","symbol":"USDCHF"},{"orders":"2","symbol":"USDJPY"}],"list":{"login":"88024509","name":"12233","country":"中国","leverage":"300","balance":"120301.43","tradeexperience":"2","followfunds":"1000.00","tradeway":"长线交易","tradedesc":"","moredesc":"","ini_cmoney":"0","ini_cpeople":"0","face_url":"","nickname":"","totalprofit":"17834.230000000007","minprofit":"-4704.35","totalvolume":"15852","maxvolume":3,"copynumber":3,"copymoney":3000,"profitper":"14.82%","winper":"31.09%","huiceper":"-3.91%"}}
     */

    private int status;
    private ResponseBean response;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * orders : [{"total":"119","winnum":"37"}]
         * sumorders : [{"orders":"27","symbol":"AUDUSD"},{"orders":"6","symbol":"EURAUD"},{"orders":"2","symbol":"EURCHF"},{"orders":"2","symbol":"EURGBP"},{"orders":"14","symbol":"EURUSD"},{"orders":"4","symbol":"GBPUSD"},{"orders":"4","symbol":"USDCAD"},{"orders":"2","symbol":"USDCHF"},{"orders":"2","symbol":"USDJPY"}]
         * list : {"login":"88024509","name":"12233","country":"中国","leverage":"300","balance":"120301.43","tradeexperience":"2","followfunds":"1000.00","tradeway":"长线交易","tradedesc":"","moredesc":"","ini_cmoney":"0","ini_cpeople":"0","face_url":"","nickname":"","totalprofit":"17834.230000000007","minprofit":"-4704.35","totalvolume":"15852","maxvolume":3,"copynumber":3,"copymoney":3000,"profitper":"14.82%","winper":"31.09%","huiceper":"-3.91%"}
         */

        private ListBean list;
        private List<OrdersBean> orders;
        private List<SumordersBean> sumorders;

        public ListBean getList() {
            return list;
        }

        public void setList(ListBean list) {
            this.list = list;
        }

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public List<SumordersBean> getSumorders() {
            return sumorders;
        }

        public void setSumorders(List<SumordersBean> sumorders) {
            this.sumorders = sumorders;
        }

        public static class ListBean {
            /**
             * login : 88024509
             * name : 12233
             * country : 中国
             * leverage : 300
             * balance : 120301.43
             * tradeexperience : 2
             * followfunds : 1000.00
             * tradeway : 长线交易
             * tradedesc :
             * moredesc :
             * ini_cmoney : 0
             * ini_cpeople : 0
             * face_url :
             * nickname :
             * totalprofit : 17834.230000000007
             * minprofit : -4704.35
             * totalvolume : 15852
             * maxvolume : 3
             * copynumber : 3
             * copymoney : 3000
             * profitper : 14.82%
             * winper : 31.09%
             * huiceper : -3.91%
             */

            private String login;
            private String name;
            private String country;
            private String leverage;
            private String balance;
            private String tradeexperience;
            private String followfunds;
            private String tradeway;
            private String tradedesc;
            private String moredesc;
            private String ini_cmoney;
            private String ini_cpeople;
            private String face_url;
            private String nickname;
            private String totalprofit;
            private String minprofit;
            private String totalvolume;
            private float maxvolume;
            private int copynumber;
            private int copymoney;
            private String profitper;
            private String winper;
            private String huiceper;

            public String getLogin() {
                return login;
            }

            public void setLogin(String login) {
                this.login = login;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getLeverage() {
                return leverage;
            }

            public void setLeverage(String leverage) {
                this.leverage = leverage;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public String getTradeexperience() {
                return tradeexperience;
            }

            public void setTradeexperience(String tradeexperience) {
                this.tradeexperience = tradeexperience;
            }

            public String getFollowfunds() {
                return followfunds;
            }

            public void setFollowfunds(String followfunds) {
                this.followfunds = followfunds;
            }

            public String getTradeway() {
                return tradeway;
            }

            public void setTradeway(String tradeway) {
                this.tradeway = tradeway;
            }

            public String getTradedesc() {
                return tradedesc;
            }

            public void setTradedesc(String tradedesc) {
                this.tradedesc = tradedesc;
            }

            public String getMoredesc() {
                return moredesc;
            }

            public void setMoredesc(String moredesc) {
                this.moredesc = moredesc;
            }

            public String getIni_cmoney() {
                return ini_cmoney;
            }

            public void setIni_cmoney(String ini_cmoney) {
                this.ini_cmoney = ini_cmoney;
            }

            public String getIni_cpeople() {
                return ini_cpeople;
            }

            public void setIni_cpeople(String ini_cpeople) {
                this.ini_cpeople = ini_cpeople;
            }

            public String getFace_url() {
                return face_url;
            }

            public void setFace_url(String face_url) {
                this.face_url = face_url;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getTotalprofit() {
                return totalprofit;
            }

            public void setTotalprofit(String totalprofit) {
                this.totalprofit = totalprofit;
            }

            public String getMinprofit() {
                return minprofit;
            }

            public void setMinprofit(String minprofit) {
                this.minprofit = minprofit;
            }

            public String getTotalvolume() {
                return totalvolume;
            }

            public void setTotalvolume(String totalvolume) {
                this.totalvolume = totalvolume;
            }

            public float getMaxvolume() {
                return maxvolume;
            }

            public void setMaxvolume(float maxvolume) {
                this.maxvolume = maxvolume;
            }

            public int getCopynumber() {
                return copynumber;
            }

            public void setCopynumber(int copynumber) {
                this.copynumber = copynumber;
            }

            public int getCopymoney() {
                return copymoney;
            }

            public void setCopymoney(int copymoney) {
                this.copymoney = copymoney;
            }

            public String getProfitper() {
                return profitper;
            }

            public void setProfitper(String profitper) {
                this.profitper = profitper;
            }

            public String getWinper() {
                return winper;
            }

            public void setWinper(String winper) {
                this.winper = winper;
            }

            public String getHuiceper() {
                return huiceper;
            }

            public void setHuiceper(String huiceper) {
                this.huiceper = huiceper;
            }
        }

        public static class OrdersBean {
            /**
             * total : 119
             * winnum : 37
             */

            private String total;
            private String winnum;

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getWinnum() {
                return winnum;
            }

            public void setWinnum(String winnum) {
                this.winnum = winnum;
            }
        }

        public static class SumordersBean {
            /**
             * orders : 27
             * symbol : AUDUSD
             */

            private String orders;
            private String symbol;

            public String getOrders() {
                return orders;
            }

            public void setOrders(String orders) {
                this.orders = orders;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }
        }
    }
}
