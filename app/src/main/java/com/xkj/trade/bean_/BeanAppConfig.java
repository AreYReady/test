package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-04-20.
 * TODO:app的配置信息
 */

public class BeanAppConfig {

    /**
     * status : 1
     * msg : {"is_open_master":1,"symbol":[{"id":"1","symbol":"XAUUSDbo"},{"id":"2","symbol":"EURJPYbo"},{"id":"3","symbol":"EURUSDbo"},{"id":"4","symbol":"GBPUSDbo"},{"id":"5","symbol":"USDJPYbo"},{"id":"6","symbol":"AUDUSDbo"},{"id":"7","symbol":"EURGBPbo"},{"id":"8","symbol":"UKOilbo"},{"id":"9","symbol":"USOilbo"},{"id":"10","symbol":"HK50bo"}]}
     */

    private int status;
    private MsgBean msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * is_open_master : 1
         * symbol : [{"id":"1","symbol":"XAUUSDbo"},{"id":"2","symbol":"EURJPYbo"},{"id":"3","symbol":"EURUSDbo"},{"id":"4","symbol":"GBPUSDbo"},{"id":"5","symbol":"USDJPYbo"},{"id":"6","symbol":"AUDUSDbo"},{"id":"7","symbol":"EURGBPbo"},{"id":"8","symbol":"UKOilbo"},{"id":"9","symbol":"USOilbo"},{"id":"10","symbol":"HK50bo"}]
         */

        private int is_open_master;
        private List<SymbolBean> symbol;

        public int getIs_open_master() {
            return is_open_master;
        }

        public void setIs_open_master(int is_open_master) {
            this.is_open_master = is_open_master;
        }

        public List<SymbolBean> getSymbol() {
            return symbol;
        }

        public void setSymbol(List<SymbolBean> symbol) {
            this.symbol = symbol;
        }

        public static class SymbolBean {
            /**
             * id : 1
             * symbol : XAUUSDbo
             */

            private String id;
            private String symbol;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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
