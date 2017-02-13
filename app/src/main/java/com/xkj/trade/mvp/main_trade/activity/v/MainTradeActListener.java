package com.xkj.trade.mvp.main_trade.activity.v;

import com.xkj.trade.bean_.BeanUserListInfo;

/**
 * Created by huangsc on 2017-02-13.
 * TODO:
 */

public class MainTradeActListener {
    public interface ViewListener {
        void refreshUserInfo(BeanUserListInfo beanUserListInfo);
    }
    public interface ModelListener {
        void onDestroy();
        void sendUserInfo();

    }
    public interface PreListener {
        void onDestroy();
        void sendUserInfo();
        void refreshUserInfo(BeanUserListInfo beanUserListInfo);
    }
}
