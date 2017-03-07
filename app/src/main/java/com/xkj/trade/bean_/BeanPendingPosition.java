package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-02-16.
 * TODO:挂单的实体类
 */

public class BeanPendingPosition {

    /**
     * data : {"list":[{"closeprice":"87.793","cmd":"buy","commission":"0.0","login":"10001","openprice":"87.128","opentime":"2017-02-14 09:26:54","order":4524101,"profit":"58.41","sl":"0.0","swap":"0.83","symbol":"AUDJPY","tp":"0.0","volume":"0.10"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"121.133","opentime":"2017-02-13 17:17:51","order":4524098,"profit":"-212.58","sl":"0.0","swap":"-10.06","symbol":"EURJPY","tp":"0.0","volume":"1.00"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"121.077","opentime":"2017-02-13 17:25:16","order":4524099,"profit":"-326.79","sl":"0.0","swap":"-20.10","symbol":"EURJPY","tp":"0.0","volume":"2.00"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"121.071","opentime":"2017-02-13 17:32:28","order":4524100,"profit":"-94.87","sl":"0.0","swap":"-6.03","symbol":"EURJPY","tp":"0.0","volume":"0.60"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.445","opentime":"2017-02-14 11:57:59","order":4524104,"profit":"-17.14","sl":"0.0","swap":"-0.12","symbol":"EURJPY","tp":"0.0","volume":"0.04"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.336","opentime":"2017-02-14 14:26:48","order":4524105,"profit":"-20.97","sl":"0.0","swap":"-0.12","symbol":"EURJPY","tp":"0.0","volume":"0.04"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"120.963","opentime":"2017-02-15 10:13:56","order":4524114,"profit":"-0.64","sl":"0.0","swap":"-0.06","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"120.963","opentime":"2017-02-15 10:13:59","order":4524115,"profit":"-0.64","sl":"0.0","swap":"-0.06","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.926","opentime":"2017-02-15 10:14:07","order":4524116,"profit":"-0.06","sl":"0.0","swap":"-0.02","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.926","opentime":"2017-02-15 10:14:08","order":4524117,"profit":"-0.06","sl":"0.0","swap":"-0.02","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.926","opentime":"2017-02-15 10:14:09","order":4524118,"profit":"-0.06","sl":"0.0","swap":"-0.02","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.926","opentime":"2017-02-15 10:14:10","order":4524119,"profit":"-0.06","sl":"0.0","swap":"-0.02","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.926","opentime":"2017-02-15 10:14:11","order":4524120,"profit":"-0.06","sl":"0.0","swap":"-0.02","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"120.967","opentime":"2017-02-15 10:14:12","order":4524121,"profit":"-0.67","sl":"0.0","swap":"-0.06","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"120.967","opentime":"2017-02-15 10:14:13","order":4524122,"profit":"-0.67","sl":"0.0","swap":"-0.06","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"120.967","opentime":"2017-02-15 10:14:13","order":4524123,"profit":"-0.67","sl":"0.0","swap":"-0.06","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"120.970","opentime":"2017-02-15 10:14:14","order":4524124,"profit":"-0.70","sl":"0.0","swap":"-0.06","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"1.06218","cmd":"sell","commission":"0.0","login":"10001","openprice":"1.05803","opentime":"2017-02-15 14:56:23","order":4524149,"profit":"-12.45","sl":"0.0","swap":"-0.07","symbol":"EURUSD","tp":"0.0","volume":"0.03"},{"closeprice":"1.06196","cmd":"buy","commission":"0.0","login":"10001","openprice":"1.05792","opentime":"2017-02-15 15:45:39","order":4524150,"profit":"4.04","sl":"0.0","swap":"-0.05","symbol":"EURUSD","tp":"0.0","volume":"0.01"},{"closeprice":"1.62967","cmd":"buy","commission":"0.0","login":"10001","openprice":"1.63021","opentime":"2017-02-16 09:33:38","order":4524151,"profit":"-1.65","sl":"0.0","swap":"0.0","symbol":"GBPCAD","tp":"0.0","volume":"0.04"},{"closeprice":"1.24737","cmd":"sell","commission":"0.0","login":"10001","openprice":"1.24571","opentime":"2017-02-16 11:09:08","order":4524152,"profit":"-6.64","sl":"0.0","swap":"0.0","symbol":"GBPUSD","tp":"0.0","volume":"0.04"}],"total":39}
     * msg : success
     * status : 1
     */

    private DataBean data;
    private String msg;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * list : [{"closeprice":"87.793","cmd":"buy","commission":"0.0","login":"10001","openprice":"87.128","opentime":"2017-02-14 09:26:54","order":4524101,"profit":"58.41","sl":"0.0","swap":"0.83","symbol":"AUDJPY","tp":"0.0","volume":"0.10"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"121.133","opentime":"2017-02-13 17:17:51","order":4524098,"profit":"-212.58","sl":"0.0","swap":"-10.06","symbol":"EURJPY","tp":"0.0","volume":"1.00"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"121.077","opentime":"2017-02-13 17:25:16","order":4524099,"profit":"-326.79","sl":"0.0","swap":"-20.10","symbol":"EURJPY","tp":"0.0","volume":"2.00"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"121.071","opentime":"2017-02-13 17:32:28","order":4524100,"profit":"-94.87","sl":"0.0","swap":"-6.03","symbol":"EURJPY","tp":"0.0","volume":"0.60"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.445","opentime":"2017-02-14 11:57:59","order":4524104,"profit":"-17.14","sl":"0.0","swap":"-0.12","symbol":"EURJPY","tp":"0.0","volume":"0.04"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.336","opentime":"2017-02-14 14:26:48","order":4524105,"profit":"-20.97","sl":"0.0","swap":"-0.12","symbol":"EURJPY","tp":"0.0","volume":"0.04"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"120.963","opentime":"2017-02-15 10:13:56","order":4524114,"profit":"-0.64","sl":"0.0","swap":"-0.06","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"120.963","opentime":"2017-02-15 10:13:59","order":4524115,"profit":"-0.64","sl":"0.0","swap":"-0.06","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.926","opentime":"2017-02-15 10:14:07","order":4524116,"profit":"-0.06","sl":"0.0","swap":"-0.02","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.926","opentime":"2017-02-15 10:14:08","order":4524117,"profit":"-0.06","sl":"0.0","swap":"-0.02","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.926","opentime":"2017-02-15 10:14:09","order":4524118,"profit":"-0.06","sl":"0.0","swap":"-0.02","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.926","opentime":"2017-02-15 10:14:10","order":4524119,"profit":"-0.06","sl":"0.0","swap":"-0.02","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.933","cmd":"sell","commission":"0.0","login":"10001","openprice":"120.926","opentime":"2017-02-15 10:14:11","order":4524120,"profit":"-0.06","sl":"0.0","swap":"-0.02","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"120.967","opentime":"2017-02-15 10:14:12","order":4524121,"profit":"-0.67","sl":"0.0","swap":"-0.06","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"120.967","opentime":"2017-02-15 10:14:13","order":4524122,"profit":"-0.67","sl":"0.0","swap":"-0.06","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"120.967","opentime":"2017-02-15 10:14:13","order":4524123,"profit":"-0.67","sl":"0.0","swap":"-0.06","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"120.891","cmd":"buy","commission":"0.0","login":"10001","openprice":"120.970","opentime":"2017-02-15 10:14:14","order":4524124,"profit":"-0.70","sl":"0.0","swap":"-0.06","symbol":"EURJPY","tp":"0.0","volume":"0.01"},{"closeprice":"1.06218","cmd":"sell","commission":"0.0","login":"10001","openprice":"1.05803","opentime":"2017-02-15 14:56:23","order":4524149,"profit":"-12.45","sl":"0.0","swap":"-0.07","symbol":"EURUSD","tp":"0.0","volume":"0.03"},{"closeprice":"1.06196","cmd":"buy","commission":"0.0","login":"10001","openprice":"1.05792","opentime":"2017-02-15 15:45:39","order":4524150,"profit":"4.04","sl":"0.0","swap":"-0.05","symbol":"EURUSD","tp":"0.0","volume":"0.01"},{"closeprice":"1.62967","cmd":"buy","commission":"0.0","login":"10001","openprice":"1.63021","opentime":"2017-02-16 09:33:38","order":4524151,"profit":"-1.65","sl":"0.0","swap":"0.0","symbol":"GBPCAD","tp":"0.0","volume":"0.04"},{"closeprice":"1.24737","cmd":"sell","commission":"0.0","login":"10001","openprice":"1.24571","opentime":"2017-02-16 11:09:08","order":4524152,"profit":"-6.64","sl":"0.0","swap":"0.0","symbol":"GBPUSD","tp":"0.0","volume":"0.04"}]
         * total : 39
         */

        private int total;
        private List<ListBean> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * closeprice : 87.793
             * cmd : buy
             * commission : 0.0
             * login : 10001
             * openprice : 87.128
             * opentime : 2017-02-14 09:26:54
             * order : 4524101
             * profit : 58.41
             * sl : 0.0
             * swap : 0.83
             * symbol : AUDJPY
             * tp : 0.0
             * volume : 0.10
             * price:不是网络的数据，后期加入。表示当前价格
             * status:不是网咯数据，后期加入，表示当前状态，0：正常，1点击
             */

            private String closeprice;
            private String cmd;
            private String commission;
            private String login;
            private String openprice;
            private String opentime;
            private int order;
            private String price;
            private String profit;
            private String sl;
            private String swap;
            private String symbol;
            private String tp;
            private String volume;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            private  int status;

            public String getCloseprice() {
                return closeprice;
            }

            public void setCloseprice(String closeprice) {
                this.closeprice = closeprice;
            }

            public String getCmd() {
                return cmd;
            }

            public void setCmd(String cmd) {
                this.cmd = cmd;
            }

            public String getCommission() {
                return commission;
            }

            public void setCommission(String commission) {
                this.commission = commission;
            }

            public String getLogin() {
                return login;
            }

            public void setLogin(String login) {
                this.login = login;
            }

            public String getOpenprice() {
                return openprice;
            }

            public void setOpenprice(String openprice) {
                this.openprice = openprice;
            }

            public String getOpentime() {
                return opentime;
            }

            public void setOpentime(String opentime) {
                this.opentime = opentime;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public String getProfit() {
                return profit;
            }

            public void setProfit(String profit) {
                this.profit = profit;
            }

            public String getSl() {
                return sl;
            }

            public void setSl(String sl) {
                this.sl = sl;
            }

            public String getSwap() {
                return swap;
            }

            public void setSwap(String swap) {
                this.swap = swap;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getTp() {
                return tp;
            }

            public void setTp(String tp) {
                this.tp = tp;
            }

            public String getVolume() {
                return volume;
            }

            public void setVolume(String volume) {
                this.volume = volume;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }
}
