package trade.xkj.com.trade.mvp.main_trade.v;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.bean.BeanIndicatorData;
import trade.xkj.com.trade.bean_.BeanHistory;

/**
 * Created by huangsc on 2017-01-10.
 * TODO:mvp各逻辑处理类的接口类
 */

public class MainTradeListener {

    /**
     * view的接口类
     */
    public interface MainTradeContentLFragListener {
        void freshView(BeanHistory.BeanHistoryData data, boolean isCountDown);

        void refreshIndicator(List<BeanIndicatorData> mBeanIndicatorDataList);
    }

    /**
     * persenter的接口类
     */
    public interface MainTradeContentPreListener {
        /**
         * 加载数据
         */
        void loadingSubSymbols(ArrayList<String> symbolsName,boolean subOrCancel);

        /**
         * 从服务器获取数据完成,刷新分时图view
         */
        void refreshView(BeanHistory data);

//        /**
//         * 刷新事实数据
//         *
//         * @param realTimeDataList
//         */
//        void refreshRealTimeView(RealTimeDataList realTimeDataList);
//
//        void initIndicator(ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol);

        void loadingHistoryData(String symbol, String period, int count);
    }


    /**
     * model的接口类
     */
    public interface MainTradeContentModelListener {
        void sendHistoryRequest(String symbol, String period, int count);
        void sendAllSymbolsRequest();
        void sendSubSymbolsRequest(ArrayList<String> symbolsName,boolean subOrCancel);
        //         void initIndicator(ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol);
//        void sendSubSymbol(String Symbol);
    }

}
