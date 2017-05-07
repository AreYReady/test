package com.xkj.trade.bean_;

import java.util.List;

/**
 * Created by huangsc on 2017-02-13.
 * TODO:
 */

public class BeanMasterRank {


    /**
     * response : [{"copymoney":1000,"copynumber":1,"country":"中国","data":["-38.94","-882.82","5062.7","1325.95","2152","-4297.51","8306","363.04","2324","222.36","267.73","1770.95","-4704.35","-1791.99","264.7","431.03","2768","-3070.15","114.15","-110","-67.8","326.97","189.63","-193.8","129.91","32.19","-168.74","-60.27","-502.51","-265.01","-135","-223.24","-141","-147","-135","-117","2583","2583","-111","-111","2625","2580","2580","2565","25.62","-0.52","-0.83","-0.63","-1.3","-1.3"],"face_url":"","fstatus":0,"huiceper":-3.77,"login":"88024509","minprofit":"-4704.35","name":"12233","profitper":17.82,"status":0,"totalprofit":"22228.68","totalvolume":103.59,"tradeexperience":"2","tradeway":"长线交易"},{"copymoney":2769,"copynumber":6,"country":"中国","data":["-38.48","-23.18","-17.33","-22","-22.77","-18","-12.5","-25.09","-33","-27","-94","-4","-18.43","-34","-52","-52.37","-44.62","18","16","-80.7","-57","-28.81","4.03","-95","-297","-148","-58.47","-20","-43.66","70.57","-153.01","-39.83","-59.76","2.98","-1087.84","-1149.86","12","-2256.51","1232.49","2297.45","1190.92","291.12","139.37","181","1153.82","941","38.98","139","-4.18","-4.48"],"face_url":"","fstatus":0,"huiceper":-19.96,"login":"88079553","minprofit":"-2256.51","name":"123123","profitper":5.26,"status":0,"totalprofit":"594.9000000000002","totalvolume":57.2,"tradeexperience":"0","tradeway":"长线交易"},{"copymoney":0,"copynumber":0,"country":"中国","face_url":"","fstatus":0,"huiceper":0,"login":"88051194","name":"mingjie","profitper":0,"status":0,"totalvolume":0,"tradeexperience":"2","tradeway":"长线交易"},{"copymoney":200,"copynumber":2,"country":"中国","data":["5.98","-38.86","-220","-555","-0.65","-0.69","-115.87","-6","-57","-37","-63","-3.34","-3.58","96.26"],"face_url":"/Uploads/faces/face1_20161121172051_154_TS1Y3ZOU.jpg","fstatus":0,"huiceper":-1.43,"login":"88059448","minprofit":"-555","name":"陈玲玲","profitper":-2.57,"status":0,"totalprofit":"-998.75","totalvolume":9.26,"tradeexperience":"0","tradeway":"超短线交易"},{"copymoney":100,"copynumber":1,"country":"中国","data":["0.71","-1.53","-0.39","-0.59","0.39","-0.33","-0.56","13.2","-293.32","160.23","18.12","23.53","150","122.33","5.07","13.9","-0.42","-0.45","-289.29","46.82","-174.8","-4853.2"],"face_url":"","fstatus":0,"huiceper":-99.15,"login":"88056792","minprofit":"-4853.2","name":"喵碧咪","profitper":-103.39,"status":0,"totalprofit":"-5060.58","totalvolume":6.09,"tradeexperience":"3","tradeway":"超短线交易"},{"copymoney":100,"copynumber":5,"country":"中国","data":["-224.58","-10465","70.57","-153.01","-39.83","-59.76","-1087.84","-1149.86","11","12"],"face_url":"","fstatus":0,"huiceper":-103.97,"login":"88002253","minprofit":"-10465","name":"喵碧咪mt4-01","profitper":-130.01,"status":0,"totalprofit":"-13086.310000000001","totalvolume":14,"tradeexperience":"4","tradeway":"短线交易"}]
     * status : 1
     */

    private int status;
    private List<MasterRank> response;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<MasterRank> getResponse() {
        return response;
    }

    public void setResponse(List<MasterRank> response) {
        this.response = response;
    }

    public static class MasterRank {
        /**
         * copymoney : 1000
         * copynumber : 1
         * country : 中国
         * data : ["-38.94","-882.82","5062.7","1325.95","2152","-4297.51","8306","363.04","2324","222.36","267.73","1770.95","-4704.35","-1791.99","264.7","431.03","2768","-3070.15","114.15","-110","-67.8","326.97","189.63","-193.8","129.91","32.19","-168.74","-60.27","-502.51","-265.01","-135","-223.24","-141","-147","-135","-117","2583","2583","-111","-111","2625","2580","2580","2565","25.62","-0.52","-0.83","-0.63","-1.3","-1.3"]
         * face_url :
         * fstatus : 0r
         * huiceper : -3.77
         * login : 88024509
         * minprofit : -4704.35
         * name : 12233
         * profitper : 17.82
         * status : 0
         * totalprofit : 22228.68
         * totalvolume : 103.59
         * tradeexperience : 2
         * tradeway : 长线交易
         */

        public MasterRank(){}
        public MasterRank(String login,int fstatus,int status){
            this.fstatus=fstatus;
            this.status=status;
            this.login=login;
        }
        private long copymoney;
        private int copynumber;
        private String country;
        private String face_url;
        private int fstatus;
        private double huiceper;
        private String login;
        private String minprofit;
        private String name;
        private double profitper;
        private int status;
        private String totalprofit;
        private double totalvolume;
        private String tradeexperience;
        private String tradeway;
        private List<String> data;

        public long getCopymoney() {
            return copymoney;
        }

        public void setCopymoney(int copymoney) {
            this.copymoney = copymoney;
        }

        public int getCopynumber() {
            return copynumber;
        }

        public void setCopynumber(int copynumber) {
            this.copynumber = copynumber;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getFace_url() {
            return face_url;
        }

        public void setFace_url(String face_url) {
            this.face_url = face_url;
        }

        public int getFstatus() {
            return fstatus;
        }

        public void setFstatus(int fstatus) {
            this.fstatus = fstatus;
        }

        public double getHuiceper() {
            return huiceper;
        }

        public void setHuiceper(double huiceper) {
            this.huiceper = huiceper;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getMinprofit() {
            return minprofit;
        }

        public void setMinprofit(String minprofit) {
            this.minprofit = minprofit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getProfitper() {
            return profitper;
        }

        public void setProfitper(double profitper) {
            this.profitper = profitper;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTotalprofit() {
            return totalprofit;
        }

        public void setTotalprofit(String totalprofit) {
            this.totalprofit = totalprofit;
        }

        public double getTotalvolume() {
            return totalvolume;
        }

        public void setTotalvolume(double totalvolume) {
            this.totalvolume = totalvolume;
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

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }
}
