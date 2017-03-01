package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-03-01.
 * TODO:用户关注的高手信息
 */

public class BeanMasterFocusInfo {

    /**
     * status : 1
     * response : [{"status":1,"face_url":"","copynumber":11,"copymoney":5238,"name":"123123","focusid":"88079553","profitper":4.87,"huiceper":-18.49},{"status":1,"face_url":"","copynumber":5,"copymoney":5000,"name":"12233","focusid":"88024509","profitper":14.82,"huiceper":-3.91},{"status":1,"face_url":"/Uploads/faces/face1_20161121172051_154_TS1Y3ZOU.jpg","copynumber":3,"copymoney":300,"name":"陈玲玲","focusid":"88059448","profitper":-2.57,"huiceper":-1.43},{"status":1,"face_url":"","copynumber":6,"copymoney":200,"name":"喵碧咪mt4-01","focusid":"88002253","profitper":-130.01,"huiceper":-103.97},{"status":0,"face_url":"","copynumber":1,"copymoney":100,"name":"喵碧咪","focusid":"88056792","profitper":-103.39,"huiceper":-99.15}]
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
         * status : 1
         * face_url :
         * copynumber : 11
         * copymoney : 5238
         * name : 123123
         * focusid : 88079553
         * profitper : 4.87
         * huiceper : -18.49
         */

        private int status;
        private String face_url;
        private int copynumber;
        private int copymoney;
        private String name;
        private String focusid;
        private double profitper;
        private double huiceper;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public String getFocusid() {
            return focusid;
        }

        public void setFocusid(String focusid) {
            this.focusid = focusid;
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
