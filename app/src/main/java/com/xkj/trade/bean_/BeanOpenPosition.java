package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-02-15.
 * TODO:
 * {@link com.xkj.trade.mvp.main_trade.fragment_content.v.MainTradeContentFrag#enterOrder(BeanOpenPosition)}}
 */
public class BeanOpenPosition  {

    /**
     * status : 1
     * msg : success
     * data : {"total":6,"list":[{"closeprice":"1.05815","cmd":"sell","commission":"0.0","openprice":"1.05960","opentime":"2017-02-14 09:49:02","order":4524102,"profit":"1.45","sl":"0.0","swap":"-0.01","symbol":"EURUSD","tp":"0.0","volume":"0.01","login":"10001"},{"closeprice":"1.05815","cmd":"sell","commission":"0.0","openprice":"1.05960","opentime":"2017-02-14 09:49:07","order":4524103,"profit":"2.90","sl":"0.0","swap":"-0.01","symbol":"EURUSD","tp":"0.0","volume":"0.02","login":"10001"},{"closeprice":"1.05815","cmd":"sell","commission":"0.0","openprice":"1.06285","opentime":"2017-02-14 17:05:15","order":4524111,"profit":"4.70","sl":"0.0","swap":"-0.01","symbol":"EURUSD","tp":"0.0","volume":"0.01","login":"10001"},{"closeprice":"1.05793","cmd":"buy","commission":"0.0","openprice":"1.06303","opentime":"2017-02-14 17:06:43","order":4524112,"profit":"-5.10","sl":"0.0","swap":"-0.02","symbol":"EURUSD","tp":"0.0","volume":"0.01","login":"10001"},{"closeprice":"1.05815","cmd":"sell","commission":"0.0","openprice":"1.06285","opentime":"2017-02-14 17:06:53","order":4524113,"profit":"4.70","sl":"0.0","swap":"-0.01","symbol":"EURUSD","tp":"0.0","volume":"0.01","login":"10001"},{"closeprice":"1.05815","cmd":"sell","commission":"0.0","openprice":"1.05771","opentime":"2017-02-15 11:05:32","order":4524127,"profit":"-0.44","sl":"0.0","swap":"0.0","symbol":"EURUSD","tp":"0.0","volume":"0.01","login":"10001"}]}
     */
    int status;
    String msg;
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
         * total : 6
         * list : [{"closeprice":"1.05815","cmd":"sell","commission":"0.0","openprice":"1.05960","opentime":"2017-02-14 09:49:02","order":4524102,"profit":"1.45","sl":"0.0","swap":"-0.01","symbol":"EURUSD","tp":"0.0","volume":"0.01","login":"10001"},{"closeprice":"1.05815","cmd":"sell","commission":"0.0","openprice":"1.05960","opentime":"2017-02-14 09:49:07","order":4524103,"profit":"2.90","sl":"0.0","swap":"-0.01","symbol":"EURUSD","tp":"0.0","volume":"0.02","login":"10001"},{"closeprice":"1.05815","cmd":"sell","commission":"0.0","openprice":"1.06285","opentime":"2017-02-14 17:05:15","order":4524111,"profit":"4.70","sl":"0.0","swap":"-0.01","symbol":"EURUSD","tp":"0.0","volume":"0.01","login":"10001"},{"closeprice":"1.05793","cmd":"buy","commission":"0.0","openprice":"1.06303","opentime":"2017-02-14 17:06:43","order":4524112,"profit":"-5.10","sl":"0.0","swap":"-0.02","symbol":"EURUSD","tp":"0.0","volume":"0.01","login":"10001"},{"closeprice":"1.05815","cmd":"sell","commission":"0.0","openprice":"1.06285","opentime":"2017-02-14 17:06:53","order":4524113,"profit":"4.70","sl":"0.0","swap":"-0.01","symbol":"EURUSD","tp":"0.0","volume":"0.01","login":"10001"},{"closeprice":"1.05815","cmd":"sell","commission":"0.0","openprice":"1.05771","opentime":"2017-02-15 11:05:32","order":4524127,"profit":"-0.44","sl":"0.0","swap":"0.0","symbol":"EURUSD","tp":"0.0","volume":"0.01","login":"10001"}]
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
            public ListBean(String sl, String tp, int order) {
                this.sl = sl;
                this.tp = tp;
                this.order = order;
            }

            /**
             * closeprice : 1.05815
             * cmd : sell
             * commission : 0.0
             * openprice : 1.05960
             * opentime : 2017-02-14 09:49:02
             * order : 4524102
             * profit : 1.45
             * sl : 0.0
             * swap : -0.01
             * symbol : EURUSD
             * tp : 0.0
             * volume : 0.01
             * login : 10001
             * price:不是网络的数据，后期加入。表示当前价格
             * status: 不是网络数据，表示状态0或者1,0是正常，1是点击之后
             */

            public ListBean(){}
            private String closeprice;
            private String cmd;
            private String commission;
            private String openprice;
            private String opentime;
            private int order;
            private String profit;
            private String sl;
            private String swap;
            private String symbol;
            private String tp;
            private String volume;
            private String login;
            private String price;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            private int status;
            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

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

            public String getLogin() {
                return login;
            }

            public void setLogin(String login) {
                this.login = login;
            }
        }
    }
}
