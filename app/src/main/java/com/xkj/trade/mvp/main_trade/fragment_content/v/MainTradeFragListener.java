package com.xkj.trade.mvp.main_trade.fragment_content.v;

import com.xkj.trade.bean.BeanIndicatorData;
import com.xkj.trade.bean_.BeanHistory;
import com.xkj.trade.bean_.BeanUserListInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangsc on 2017-01-10.
 * TODO:mvp各逻辑处理类的接口类
 */

public class MainTradeFragListener {

    /**
     * view的接口类
     */
    public interface MainTradeContentLFragListener {
        void refreshView(BeanHistory.BeanHistoryData data, boolean isCountDown);
        void refreshIndicator(List<BeanIndicatorData> mBeanIndicatorDataList);
        void refreshUserInfo(BeanUserListInfo info);
    }

    /**
     * persenter的接口类
     */
    public interface MainTradeContentPreListener {
        /**
         * 加载当前订阅数据
         */
        void loadingSubSymbols(ArrayList<String> symbolsName,boolean subOrCancel);
        /**
         * 从服务器获取数据完成,刷新分时图view
         */
        void refreshView(BeanHistory data);
        void loadingHistoryData(String symbol, String period, int count);
        void loadingUserList();
        void userListResponse(BeanUserListInfo beanUserListInfo);
    }


    /**
     * model的接口类
     */
    public interface MainTradeContentModelListener {
        void sendHistoryRequest(String symbol, String period, int count);
        void sendAllSymbolsRequest();
        void sendSubSymbolsRequest(ArrayList<String> symbolsName,boolean subOrCancel);
        void loadingUserList(String mt4Login);
        //         void initIndicator(ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol);
//        void sendSubSymbol(String Symbol);
    }

}
