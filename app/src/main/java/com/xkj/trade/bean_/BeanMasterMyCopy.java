package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-03-01.
 * TODO:高手
 */

public class BeanMasterMyCopy {


    /**
     * response : [{"copymoney":1200,"copynumber":12,"face_url":"","fstatus":1,"huiceper":0,"masterid":"88047196","name":"孙老师","profitper":0},{"copymoney":200,"copynumber":101,"face_url":"","fstatus":1,"huiceper":-1.33,"masterid":"88091551","name":"赵老师","profitper":0.76},{"copymoney":200,"copynumber":101,"face_url":"","fstatus":1,"huiceper":-0.54,"masterid":"88041947","name":"钱老师","profitper":-1.06},{"copymoney":300,"copynumber":102,"face_url":"","fstatus":1,"huiceper":-0.05,"masterid":"88071238","name":"王老师","profitper":-0.1},{"copymoney":2147493647,"copynumber":5002,"face_url":"/Uploads/faces/face1_20170304092932_655_HJ5WYRNV.jpg","fstatus":1,"huiceper":-0.57,"masterid":"88018011","name":"cp3","profitper":69.69}]
     * status : 1
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
        private long copymoney;
        private String name;
        private String masterid;
        private double profitper;
        private double huiceper;
        private Boolean uiStatus;
        public ResponseBean(int fstatus, String face_url, int copynumber, long copymoney, String name, String masterid, double profitper, double huiceper) {
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

        public long getCopymoney() {
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
