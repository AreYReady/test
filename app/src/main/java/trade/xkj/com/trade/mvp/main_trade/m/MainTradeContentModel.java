package trade.xkj.com.trade.mvp.main_trade.m;

import java.util.ArrayList;

import trade.xkj.com.trade.bean.BeanSymbolConfig;

/**
 * Created by admin on 2016-11-23.
 */

public interface MainTradeContentModel {
    public void sendHistoryRequest(String symbol,int count);
    public void refreshIndicator(ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol);
}
