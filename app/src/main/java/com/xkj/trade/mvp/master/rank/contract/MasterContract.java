package com.xkj.trade.mvp.master.rank.contract;

import com.xkj.trade.bean_.BeanMasterRank;

/**
 * Created by huangsc on 2017-02-13.
 * TODO:介绍高手排行
 */

public class MasterContract {

    public interface View {
        void responseMasterRank(BeanMasterRank beanMasterRank);
    }

    public interface Presenter {
        void requestMasterRank(String rankType);
        void responseMasterRank(BeanMasterRank beanMasterRank);
    }

    public interface Model {
        void requestMasterRank(String rankType);
    }


}