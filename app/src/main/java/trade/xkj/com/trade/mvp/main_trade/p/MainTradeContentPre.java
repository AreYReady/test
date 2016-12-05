package trade.xkj.com.trade.mvp.main_trade.p;

import java.util.ArrayList;

import trade.xkj.com.trade.bean.BeanSymbolConfig;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.bean.RealTimeDataList;

/**
 * Created by admin on 2016-11-23.
 */

public interface MainTradeContentPre {
    /**
     * 加载数据
     */
    public void loading();
    /**
     *从服务器获取数据完成,刷新分时图view
     */
    public void refreshView(HistoryDataList data);

    /**
     * 刷新事实数据
     * @param realTimeDataList
     */
    public void refreshRealTimeView(RealTimeDataList realTimeDataList);
    public void refreshIndicator(ArrayList <BeanSymbolConfig.SymbolsBean> subTradeSymbol);
}
