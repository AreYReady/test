package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-02-28.
 * TODO:账户复制的信息列表
 */

public class BeanCopyInfo {

    /**
     * status : 1
     * response : [{"fstatus":1,"face_url":"","copynumber":6,"copymoney":200,"name":"喵碧咪mt4-01","masterid":"88002253","profitper":-130.01,"huiceper":-103.97}]
     */

    private int status;
    private List<ResponseBean> response;

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
         * fstatus : 1
         * face_url :
         * copynumber : 6
         * copymoney : 200
         * name : 喵碧咪mt4-01
         * masterid : 88002253
         * profitper : -130.01
         * huiceper : -103.97
         */

        private int fstatus;
        private String face_url;
        private int copynumber;
        private int copymoney;
        private String name;
        private String masterid;
        private double profitper;
        private double huiceper;

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
