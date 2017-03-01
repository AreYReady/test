package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-03-01.
 * TODO:获取高手复制的用户信息
 */

public class BeanMasterRelationCopy {

    /**
     * status : 1
     * response : [{"face_url":"","name":"10001","accountid":"10001","profitper":-2.26,"huiceper":-0.6},{"face_url":"/Uploads/faces/face1_20170222133458_101_MK1QDV8G.jpg","name":"f?f?f?f","accountid":"88006914","profitper":0,"huiceper":0},{"face_url":"","name":"ABC","accountid":"88010432","profitper":-9.94,"huiceper":-5},{"face_url":"","name":"陈","accountid":"88018892","profitper":0,"huiceper":0},{"face_url":"","name":"lihao","accountid":"88030588","profitper":0,"huiceper":0}]
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
         * face_url :
         * name : 10001
         * accountid : 10001
         * profitper : -2.26
         * huiceper : -0.6
         */

        private String face_url;
        private String name;
        private String accountid;
        private double profitper;
        private double huiceper;

        public String getFace_url() {
            return face_url;
        }

        public void setFace_url(String face_url) {
            this.face_url = face_url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAccountid() {
            return accountid;
        }

        public void setAccountid(String accountid) {
            this.accountid = accountid;
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
