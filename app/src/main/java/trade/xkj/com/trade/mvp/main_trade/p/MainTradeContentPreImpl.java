package trade.xkj.com.trade.mvp.main_trade.p;

import android.os.Handler;
import android.util.Log;

import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.mvp.main_trade.m.MainTradeContentModel;
import trade.xkj.com.trade.mvp.main_trade.m.MainTradeContentModelmpl;
import trade.xkj.com.trade.mvp.main_trade.v.MainTradeContentLFragListener;

/**
 * Created by admin on 2016-11-23.
 */

public class MainTradeContentPreImpl implements MainTradeContentPre {
    public MainTradeContentLFragListener mMainTradeContentLFragListener;
    public MainTradeContentModel mMainTradeContentModel;
    private final String TAG= SystemUtil.getTAG(this);
    public MainTradeContentPreImpl(MainTradeContentLFragListener mListener,Handler mHandler){
        mMainTradeContentLFragListener=mListener;
        mMainTradeContentModel=new MainTradeContentModelmpl(this,mHandler);
    }

    @Override
    public void loading() {
        mMainTradeContentModel.sendHistoryRequest("AUDCAD",1000);
    }


    @Override
    public void refreshView(HistoryDataList data) {
        mMainTradeContentLFragListener.freshView(data);
    }

}
