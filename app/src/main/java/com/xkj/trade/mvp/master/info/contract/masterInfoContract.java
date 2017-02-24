package com.xkj.trade.mvp.master.info.contract;

import com.xkj.trade.bean_.BeanBaseResponse;
import com.xkj.trade.bean_.BeanMasterClosePosition;
import com.xkj.trade.bean_.BeanMasterOpenPosition;

/**
 * Created by huangsc on 2017-02-24.
 * TODO:控制
 */

public class MasterInfoContract {

    public interface View {
        void responseMasterOpenPosition(BeanMasterOpenPosition beanMasterOpenPosition);
        void responseMasterClosePosition(BeanMasterClosePosition beanMasterClosePosition);
        void responseCopyFollow(BeanBaseResponse beanBaseResponse);
        void responseUnCopyFollow(BeanBaseResponse beanBaseResponse);
        void responseNoFocus(BeanBaseResponse beanBaseResponse);
        void responseFocus(BeanBaseResponse beanBaseResponse);
    }

    public interface Presenter {
        void requestMasterOpenPosition(String masterid, String followid);
        void responseMasterOpenPosition(BeanMasterOpenPosition beanMasterOpenPosition);

        void requestMasterClosePosition(String masterid, String followid, String pageNo);
        void responseMasterClosePosition(BeanMasterClosePosition beanMasterClosePosition);

        void requestCopyFollow(String masterid,String copyvolume, String followid);
        void responseCopyFollow(BeanBaseResponse beanBaseResponse);

        void requestUnCopyFollow(String masterid, String followid);
        void responseUnCopyFollow(BeanBaseResponse beanBaseResponse);

        void requestFocus(String masterid, String followid);
        void responseFocus(BeanBaseResponse beanBaseResponse);

        void requestNoFocus(String masterid, String followid);
        void responseNoFocus(BeanBaseResponse beanBaseResponse);

    }

    public interface Model {
        void requestMasterOpenPosition(String masterid, String followid);
        void requestMasterClosePosition(String masterid, String followid, String pageNo);
        void requestCopyFollow(String masterid,String copyvolume,String followid);
        void requestUnCopyFollow(String masterid, String followid);
        void requestFocus(String masterid, String followid);
        void requestNoFocus(String masterid, String followid);
    }

}