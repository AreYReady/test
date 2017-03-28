package com.xkj.trade.mvp.main_trade.fragment_content.v;

import com.xkj.trade.bean.BeanIndicatorData;
import com.xkj.trade.bean_.BeanHistory;
import com.xkj.trade.bean_.BeanOpenPosition;
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
        void refreshUserInfo(BeanUserListInfo info);
        void refreshOpenPosition(BeanOpenPosition info);
        void responseAllSymbolsData(String response);
    }

    /**
     * persenter的接口类
     */
    public interface MainTradeContentPreListener {
        /**
         * 加载当前订阅数据
         */
        void requestSubSymbols(ArrayList<String> symbolsName, boolean subOrCancel);
        /**
         * 从服务器获取数据完成,刷新分时图view
         */
        void responseHistoryData(BeanHistory data);
        void requestHistoryData(String symbol, String period, int count);
        void requestUserList();
        void ResponseUserList(BeanUserListInfo info);
        //获取单个持仓数据
        void requestOpenPosition();
        void responseOpenPosition(BeanOpenPosition beanOpenPosition);
        void responseAllSymbolsData(String response);
    }


    /**
     * model的接口类
     */
    public interface MainTradeContentModelListener {
        void requestHistoryData(String symbol, String period, int count);
        void requestAllSymbolsData();
        void requestSubSymbolsData(ArrayList<String> symbolsName, boolean subOrCancel);
        void requestUserListData(String mt4Login);
        //         void initIndicator(ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol);
//        void sendSubSymbol(String Symbol);
        void requestOpenPositionData();
    }

}
