package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-02-20.
 * TODO:
 */

public class BeanClosePosition {

    /**
     * status : 1
     * msg : success
     * data : {"total":{"count":"13","totalmyprofit":"-340.76","totalvolume":3.89},"list":[{"login":"10001","name":"10001","digits":"3","symbol":"EURJPY","profit":"-112.92","cmd":"buy","volume":"100","sl":"0","tp":"0","commission":"0","swap":"-12.09","order":"4524098","opentime":"2017-02-13 17:17:51","closetime":"2017-02-17 11:16:43","openprice":"121.133","closeprice":"121.005"},{"login":"10001","name":"10001","digits":"3","symbol":"EURJPY","profit":"-127.04","cmd":"buy","volume":"200","sl":"0","tp":"0","commission":"0","swap":"-24.15","order":"4524099","opentime":"2017-02-13 17:25:16","closetime":"2017-02-17 11:16:44","openprice":"121.077","closeprice":"121.005"},{"login":"10001","name":"10001","digits":"3","symbol":"EURJPY","profit":"-34.93","cmd":"buy","volume":"60","sl":"0","tp":"0","commission":"0","swap":"-7.25","order":"4524100","opentime":"2017-02-13 17:32:28","closetime":"2017-02-17 11:16:44","openprice":"121.071","closeprice":"121.005"},{"login":"10001","name":"10001","digits":"3","symbol":"AUDJPY","profit":"16.06","cmd":"buy","volume":"10","sl":"0","tp":"0","commission":"0","swap":"1.04","order":"4524101","opentime":"2017-02-14 09:26:54","closetime":"2017-02-17 11:17:10","openprice":"87.128","closeprice":"87.310"},{"login":"10001","name":"10001","digits":"5","symbol":"EURUSD","profit":"-8.15","cmd":"sell","volume":"1","sl":"0","tp":"0","commission":"0","swap":"-0.04","order":"4524102","opentime":"2017-02-14 09:49:02","closetime":"2017-02-17 11:17:11","openprice":"1.05960","closeprice":"1.06775"},{"login":"10001","name":"10001","digits":"5","symbol":"EURUSD","profit":"-16.3","cmd":"sell","volume":"2","sl":"0","tp":"0","commission":"0","swap":"-0.06","order":"4524103","opentime":"2017-02-14 09:49:07","closetime":"2017-02-17 11:17:12","openprice":"1.05960","closeprice":"1.06775"},{"login":"10001","name":"10001","digits":"3","symbol":"EURJPY","profit":"-21.2","cmd":"sell","volume":"4","sl":"0","tp":"0","commission":"0","swap":"-0.15","order":"4524104","opentime":"2017-02-14 11:57:59","closetime":"2017-02-17 11:17:13","openprice":"120.445","closeprice":"121.046"},{"login":"10001","name":"10001","digits":"3","symbol":"EURJPY","profit":"-25.25","cmd":"sell","volume":"4","sl":"0","tp":"0","commission":"0","swap":"-0.15","order":"4524105","opentime":"2017-02-14 14:26:48","closetime":"2017-02-17 11:20:55","openprice":"120.336","closeprice":"121.052"},{"login":"10001","name":"10001","digits":"5","symbol":"EURUSD","profit":"-4.72","cmd":"sell","volume":"1","sl":"0","tp":"0","commission":"0","swap":"-0.04","order":"4524111","opentime":"2017-02-14 17:05:15","closetime":"2017-02-17 11:20:56","openprice":"1.06285","closeprice":"1.06757"},{"login":"10001","name":"10001","digits":"5","symbol":"EURUSD","profit":"4.33","cmd":"buy","volume":"1","sl":"0","tp":"0","commission":"0","swap":"-0.09","order":"4524112","opentime":"2017-02-14 17:06:43","closetime":"2017-02-17 11:20:56","openprice":"1.06303","closeprice":"1.06736"},{"login":"10001","name":"10001","digits":"5","symbol":"EURUSD","profit":"-4.73","cmd":"sell","volume":"1","sl":"0","tp":"0","commission":"0","swap":"-0.04","order":"4524113","opentime":"2017-02-14 17:06:53","closetime":"2017-02-17 11:20:57","openprice":"1.06285","closeprice":"1.06758"},{"login":"10001","name":"10001","digits":"3","symbol":"EURJPY","profit":"0.41","cmd":"buy","volume":"1","sl":"0","tp":"0","commission":"0","swap":"-0.08","order":"4524114","opentime":"2017-02-15 10:13:56","closetime":"2017-02-17 11:20:57","openprice":"120.963","closeprice":"121.010"},{"login":"10001","name":"10001","digits":"5","symbol":"GBPUSD","profit":"-6.32","cmd":"sell","volume":"4","sl":"0","tp":"0","commission":"0","swap":"-0.1","order":"4524152","opentime":"2017-02-16 11:09:08","closetime":"2017-02-17 17:21:05","openprice":"1.24571","closeprice":"1.24729"}]}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total : {"count":"13","totalmyprofit":"-340.76","totalvolume":3.89}
         * list : [{"login":"10001","name":"10001","digits":"3","symbol":"EURJPY","profit":"-112.92","cmd":"buy","volume":"100","sl":"0","tp":"0","commission":"0","swap":"-12.09","order":"4524098","opentime":"2017-02-13 17:17:51","closetime":"2017-02-17 11:16:43","openprice":"121.133","closeprice":"121.005"},{"login":"10001","name":"10001","digits":"3","symbol":"EURJPY","profit":"-127.04","cmd":"buy","volume":"200","sl":"0","tp":"0","commission":"0","swap":"-24.15","order":"4524099","opentime":"2017-02-13 17:25:16","closetime":"2017-02-17 11:16:44","openprice":"121.077","closeprice":"121.005"},{"login":"10001","name":"10001","digits":"3","symbol":"EURJPY","profit":"-34.93","cmd":"buy","volume":"60","sl":"0","tp":"0","commission":"0","swap":"-7.25","order":"4524100","opentime":"2017-02-13 17:32:28","closetime":"2017-02-17 11:16:44","openprice":"121.071","closeprice":"121.005"},{"login":"10001","name":"10001","digits":"3","symbol":"AUDJPY","profit":"16.06","cmd":"buy","volume":"10","sl":"0","tp":"0","commission":"0","swap":"1.04","order":"4524101","opentime":"2017-02-14 09:26:54","closetime":"2017-02-17 11:17:10","openprice":"87.128","closeprice":"87.310"},{"login":"10001","name":"10001","digits":"5","symbol":"EURUSD","profit":"-8.15","cmd":"sell","volume":"1","sl":"0","tp":"0","commission":"0","swap":"-0.04","order":"4524102","opentime":"2017-02-14 09:49:02","closetime":"2017-02-17 11:17:11","openprice":"1.05960","closeprice":"1.06775"},{"login":"10001","name":"10001","digits":"5","symbol":"EURUSD","profit":"-16.3","cmd":"sell","volume":"2","sl":"0","tp":"0","commission":"0","swap":"-0.06","order":"4524103","opentime":"2017-02-14 09:49:07","closetime":"2017-02-17 11:17:12","openprice":"1.05960","closeprice":"1.06775"},{"login":"10001","name":"10001","digits":"3","symbol":"EURJPY","profit":"-21.2","cmd":"sell","volume":"4","sl":"0","tp":"0","commission":"0","swap":"-0.15","order":"4524104","opentime":"2017-02-14 11:57:59","closetime":"2017-02-17 11:17:13","openprice":"120.445","closeprice":"121.046"},{"login":"10001","name":"10001","digits":"3","symbol":"EURJPY","profit":"-25.25","cmd":"sell","volume":"4","sl":"0","tp":"0","commission":"0","swap":"-0.15","order":"4524105","opentime":"2017-02-14 14:26:48","closetime":"2017-02-17 11:20:55","openprice":"120.336","closeprice":"121.052"},{"login":"10001","name":"10001","digits":"5","symbol":"EURUSD","profit":"-4.72","cmd":"sell","volume":"1","sl":"0","tp":"0","commission":"0","swap":"-0.04","order":"4524111","opentime":"2017-02-14 17:05:15","closetime":"2017-02-17 11:20:56","openprice":"1.06285","closeprice":"1.06757"},{"login":"10001","name":"10001","digits":"5","symbol":"EURUSD","profit":"4.33","cmd":"buy","volume":"1","sl":"0","tp":"0","commission":"0","swap":"-0.09","order":"4524112","opentime":"2017-02-14 17:06:43","closetime":"2017-02-17 11:20:56","openprice":"1.06303","closeprice":"1.06736"},{"login":"10001","name":"10001","digits":"5","symbol":"EURUSD","profit":"-4.73","cmd":"sell","volume":"1","sl":"0","tp":"0","commission":"0","swap":"-0.04","order":"4524113","opentime":"2017-02-14 17:06:53","closetime":"2017-02-17 11:20:57","openprice":"1.06285","closeprice":"1.06758"},{"login":"10001","name":"10001","digits":"3","symbol":"EURJPY","profit":"0.41","cmd":"buy","volume":"1","sl":"0","tp":"0","commission":"0","swap":"-0.08","order":"4524114","opentime":"2017-02-15 10:13:56","closetime":"2017-02-17 11:20:57","openprice":"120.963","closeprice":"121.010"},{"login":"10001","name":"10001","digits":"5","symbol":"GBPUSD","profit":"-6.32","cmd":"sell","volume":"4","sl":"0","tp":"0","commission":"0","swap":"-0.1","order":"4524152","opentime":"2017-02-16 11:09:08","closetime":"2017-02-17 17:21:05","openprice":"1.24571","closeprice":"1.24729"}]
         */

        private TotalBean total;
        private List<ListBean> list;

        public TotalBean getTotal() {
            return total;
        }

        public void setTotal(TotalBean total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class TotalBean {
            /**
             * count : 13
             * totalmyprofit : -340.76
             * totalvolume : 3.89
             */

            private String count;
            private String totalmyprofit;
            private double totalvolume;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getTotalmyprofit() {
                return totalmyprofit;
            }

            public void setTotalmyprofit(String totalmyprofit) {
                this.totalmyprofit = totalmyprofit;
            }

            public double getTotalvolume() {
                return totalvolume;
            }

            public void setTotalvolume(double totalvolume) {
                this.totalvolume = totalvolume;
            }
        }

        public static class ListBean {
            /**
             * login : 10001
             * name : 10001
             * digits : 3
             * symbol : EURJPY
             * profit : -112.92
             * cmd : buy
             * volume : 100
             * sl : 0
             * tp : 0
             * commission : 0
             * swap : -12.09
             * order : 4524098
             * opentime : 2017-02-13 17:17:51
             * closetime : 2017-02-17 11:16:43
             * openprice : 121.133
             * closeprice : 121.005
             */

            private String login;
            private String name;
            private String digits;
            private String symbol;
            private String profit;
            private String cmd;
            private String volume;
            private String sl;
            private String tp;
            private String commission;
            private String swap;
            private String order;
            private String opentime;
            private String closetime;
            private String openprice;
            private String closeprice;

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

            public String getDigits() {
                return digits;
            }

            public void setDigits(String digits) {
                this.digits = digits;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getProfit() {
                return profit;
            }

            public void setProfit(String profit) {
                this.profit = profit;
            }

            public String getCmd() {
                return cmd;
            }

            public void setCmd(String cmd) {
                this.cmd = cmd;
            }

            public String getVolume() {
                return volume;
            }

            public void setVolume(String volume) {
                this.volume = volume;
            }

            public String getSl() {
                return sl;
            }

            public void setSl(String sl) {
                this.sl = sl;
            }

            public String getTp() {
                return tp;
            }

            public void setTp(String tp) {
                this.tp = tp;
            }

            public String getCommission() {
                return commission;
            }

            public void setCommission(String commission) {
                this.commission = commission;
            }

            public String getSwap() {
                return swap;
            }

            public void setSwap(String swap) {
                this.swap = swap;
            }

            public String getOrder() {
                return order;
            }

            public void setOrder(String order) {
                this.order = order;
            }

            public String getOpentime() {
                return opentime;
            }

            public void setOpentime(String opentime) {
                this.opentime = opentime;
            }

            public String getClosetime() {
                return closetime;
            }

            public void setClosetime(String closetime) {
                this.closetime = closetime;
            }

            public String getOpenprice() {
                return openprice;
            }

            public void setOpenprice(String openprice) {
                this.openprice = openprice;
            }

            public String getCloseprice() {
                return closeprice;
            }

            public void setCloseprice(String closeprice) {
                this.closeprice = closeprice;
            }
        }
    }
}
