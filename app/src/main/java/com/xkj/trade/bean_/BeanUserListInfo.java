package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-02-13.
 * TODO:
 */

public class BeanUserListInfo extends BeanBaseResponse {

    /**
     * data : {"list":[{"agent_account":"9900","balance":"21690.2","credit":"0","enable":"1","equity":"21690.2","group":"demo2","leverage":"100","login":"10001","margin":"0","margin_free":"21690.2","name":"10001","taxes":"0"}],"total":"1"}
     * msg : success
     * status : 1
     */
    private BeanUserList data;

    public BeanUserList getData() {
        return data;
    }

    public void setData(BeanUserList data) {
        this.data = data;
    }
    public static class BeanUserList {
        /**
         * list : [{"agent_account":"9900","balance":"21690.2","credit":"0","enable":"1","equity":"21690.2","group":"demo2","leverage":"100","login":"10001","margin":"0","margin_free":"21690.2","name":"10001","taxes":"0"}]
         * total : 1
         */

        private String total;
        private List<BeanUserInfo> list;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<BeanUserInfo> getList() {
            return list;
        }

        public void setList(List<BeanUserInfo> list) {
            this.list = list;
        }

        public static class BeanUserInfo {
            /**
             * agent_account : 9900
             * balance : 21690.2//余额
             * credit : 0//信用
             * enable : 1
             * equity : 21690.2//净值
             * group : demo2
             * leverage : 100//杠杆
             * login : 10001
             * margin : 0//已用保证金  保证金水平是：余额/已用保证金
             * margin_free : 21690.2//可用保证金
             * name : 10001
             * taxes : 0
             * 浮动盈亏：净值-（余额+信用）
             */

            private String agent_account;
            private String balance;
            private String credit;
            private String enable;
            private String equity;
            private String group;
            private String leverage;
            private String login;
            private String margin;
            private String margin_free;
            private String name;
            private String taxes;

            public String getAgent_account() {
                return agent_account;
            }

            public void setAgent_account(String agent_account) {
                this.agent_account = agent_account;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public String getCredit() {
                return credit;
            }

            public void setCredit(String credit) {
                this.credit = credit;
            }

            public String getEnable() {
                return enable;
            }

            public void setEnable(String enable) {
                this.enable = enable;
            }

            public String getEquity() {
                return equity;
            }

            public void setEquity(String equity) {
                this.equity = equity;
            }

            public String getGroup() {
                return group;
            }

            public void setGroup(String group) {
                this.group = group;
            }

            public String getLeverage() {
                return leverage;
            }

            public void setLeverage(String leverage) {
                this.leverage = leverage;
            }

            public String getLogin() {
                return login;
            }

            public void setLogin(String login) {
                this.login = login;
            }

            public String getMargin() {
                return margin;
            }

            public void setMargin(String margin) {
                this.margin = margin;
            }

            public String getMargin_free() {
                return margin_free;
            }

            public void setMargin_free(String margin_free) {
                this.margin_free = margin_free;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTaxes() {
                return taxes;
            }

            public void setTaxes(String taxes) {
                this.taxes = taxes;
            }
        }
    }
}
