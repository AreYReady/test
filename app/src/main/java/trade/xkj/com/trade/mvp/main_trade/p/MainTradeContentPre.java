package trade.xkj.com.trade.mvp.main_trade.p;

import trade.xkj.com.trade.bean.HistoryDataList;

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
}
