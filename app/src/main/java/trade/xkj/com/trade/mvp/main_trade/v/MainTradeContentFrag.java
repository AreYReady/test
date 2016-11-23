package trade.xkj.com.trade.mvp.main_trade.v;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import org.greenrobot.eventbus.EventBus;

import trade.xkj.com.trade.Base.BaseFragment;
import trade.xkj.com.trade.R;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPre;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPreImpl;

/**
 * Created by admin on 2016-11-22.
 */

public class MainTradeContentFrag extends BaseFragment implements MainTradeContentLFragListener {
    private View view;
    private MainTradeContentPre mMainTradeContentPre;
    private HorizontalScrollView mHScrollView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_main_trade_context,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHScrollView=(HorizontalScrollView)view.findViewById(R.id.hsv_trade);
        ViewGroup childAt = (ViewGroup)mHScrollView.getChildAt(0);
        View childAt1 = childAt.getChildAt(0);
        initScrollView();

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        mMainTradeContentPre=new MainTradeContentPreImpl(this,mHandler);
        mMainTradeContentPre.loading();

    }



    @Override
    public void freshView(HistoryDataList data) {

    }
    private void initScrollView(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHScrollView.smoothScrollTo(5000,0);
            }
        },1000);
    }
}
