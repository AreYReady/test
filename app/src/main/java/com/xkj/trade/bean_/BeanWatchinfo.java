package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-02-28.
 * TODO:
 */

public class BeanWatchInfo {

    /**
     * status : 1
     * response : [{"status":0,"face_url":"","copynumber":10,"copymoney":5115,"name":"123123","focusid":"88079553","profitper":5.31,"huiceper":-20.15},{"status":0,"face_url":"","copynumber":0,"copymoney":0,"name":"mingjie","focusid":"88051194","profitper":0,"huiceper":0},{"status":1,"face_url":"","copynumber":6,"copymoney":200,"name":"喵碧咪mt4-01","focusid":"88002253","profitper":-130.01,"huiceper":-103.97},{"status":0,"face_url":"","copynumber":1,"copymoney":100,"name":"喵碧咪","focusid":"88056792","profitper":-103.39,"huiceper":-99.15}]
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
         * status : 0
         * face_url :
         * copynumber : 10
         * copymoney : 5115
         * name : 123123
         * focusid : 88079553
         * profitper : 5.31
         * huiceper : -20.15
         * ui_states://false或者null,正常，ture 改变状态
         */
        public ResponseBean(){
            init();
        }

        private void init() {
            uiStatues=false;
        }

        public ResponseBean(int status,String face_url,int copynumber,String focusid,String name){
            this.status=status;
            this.face_url=face_url;
            this.copynumber=copynumber;
            this.focusid=focusid;
            this.name=name;
            init();
        }

        public boolean isUiStatues() {
            return uiStatues;
        }

        public void setUiStatues(boolean uiStatues) {
            this.uiStatues = uiStatues;
        }

        private int status;
        private String face_url;
        private int copynumber;
        private long copymoney;
        private String name;
        private String focusid;
        private double profitper;
        private double huiceper;
        private boolean uiStatues=false;

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
