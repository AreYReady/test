package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-03-01.
 * TODO:高手
 */

public class BeanMasterMyCopy {

    /**
     * status : 1
     * response : [{"fstatus":0,"face_url":"","copynumber":11,"copymoney":5238,"name":"123123","masterid":"88079553","profitper":4.87,"huiceper":-18.49}]
     */

    private int status;
    private List<ResponseBean> response;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ResponseBean> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseBean> response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * fstatus : 0
         * face_url :
         * copynumber : 11
         * copymoney : 5238
         * name : 123123
         * masterid : 88079553
         * profitper : 4.87
         * huiceper : -18.49
         * uiStatus//不是网路数据，是为了记录adapter的状态
         */

        private int fstatus;
        private String face_url;
        private int copynumber;
        private int copymoney;
        private String name;
        private String masterid;
        private double profitper;
        private double huiceper;

        public ResponseBean(int fstatus, String face_url, int copynumber, int copymoney, String name, String masterid, double profitper, double huiceper) {
            this.fstatus = fstatus;
            this.face_url = face_url;
            this.copynumber = copynumber;
            this.copymoney = copymoney;
            this.name = name;
            this.masterid = masterid;
            this.profitper = profitper;
            this.huiceper = huiceper;
            init();
        }

        public ResponseBean() {
init();
        }

        public Boolean getUiStatus() {
            return uiStatus;
        }

        public void setUiStatus(Boolean uiStatus) {
            this.uiStatus = uiStatus;
        }

        private void init(){
            this.uiStatus=false;

        }

        private Boolean uiStatus;

        public int getFstatus() {
            return fstatus;
        }

        public void setFstatus(int fstatus) {
            this.fstatus = fstatus;
        }

        public String getFace_url() {
            return face_url;
        }

        public void setFace_url(String face_url) {
            this.face_url = face_url;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMasterid() {
            return masterid;
        }

        public void setMasterid(String masterid) {
            this.masterid = masterid;
        }

        public double getProfitper() {
            return profitper;
        }

        public void setProfitper(double profitper) {
            this.profitper = profitper;
        }

        public double getHuiceper() {
            return huiceper;
        }

        public void setHuiceper(double huiceper) {
            this.huiceper = huiceper;
        }
    }
}