package com.xkj.trade.bean_;

/**
 * Created by huangsc on 2017-02-23.
 * TODO:
 */

public class BeanMasterInfo {

    /**
     * status : 1
     * response : {"id":"7","account":"88024509","nickname":"","tradeexperience":"2","tradeway":"长线交易","followfunds":"1000.00","ini_cmoney":"0","ini_cpeople":"0","textfield":"","tradedesc":"","moredesc":"","state":"1","reason":"","file_url":"./Uploads/Apply/2016-12-06/584612a2edc8d.xls","country":"中国"}
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
         * id : 7
         * account : 88024509
         * nickname :
         * tradeexperience : 2
         * tradeway : 长线交易
         * followfunds : 1000.00
         * ini_cmoney : 0
         * ini_cpeople : 0
         * textfield :
         * tradedesc :
         * moredesc :
         * state : 1
         * reason :
         * file_url : ./Uploads/Apply/2016-12-06/584612a2edc8d.xls
         * country : 中国
         */

        private String id;
        private String account;
        private String nickname;
        private String tradeexperience;
        private String tradeway;
        private String followfunds;
        private String ini_cmoney;
        private String ini_cpeople;
        private String textfield;
        private String tradedesc;
        private String moredesc;
        private String state;
        private String reason;
        private String file_url;
        private String country;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTradeexperience() {
            return tradeexperience;
        }

        public void setTradeexperience(String tradeexperience) {
            this.tradeexperience = tradeexperience;
        }

        public String getTradeway() {
            return tradeway;
        }

        public void setTradeway(String tradeway) {
            this.tradeway = tradeway;
        }

        public String getFollowfunds() {
            return followfunds;
        }

        public void setFollowfunds(String followfunds) {
            this.followfunds = followfunds;
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

        public String getTextfield() {
            return textfield;
        }

        public void setTextfield(String textfield) {
            this.textfield = textfield;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}
