package trade.xkj.com.trade.mvp.main_trade.v;

import java.util.ArrayList;

import trade.xkj.com.trade.bean.BeanIndicatorData;
import trade.xkj.com.trade.bean.HistoryDataList;

/**
 * Created by admin on 2016-11-23.
 */

public interface MainTradeContentLFragListener  {
    public void freshView(HistoryDataList data);
    public void refreshIndicator(ArrayList <BeanIndicatorData> mBeanIndicatorDataList);
}
