package trade.xkj.com.trade.mvp.main_trade.v;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import trade.xkj.com.trade.Base.BaseFragment;
import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.View.HistoryTradeView;
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
    private HistoryTradeView mHistoryTradeView;
    private LinearLayout ll;
    private Context context;
    private View imView;
    private int h;
    private int w;
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
        imView=view.findViewById(R.id.imageVi);
        ll=(LinearLayout)view.findViewById(R.id.ll);
        context=this.getActivity();

//        ViewGroup childAt = (ViewGroup)mHScrollView.getChildAt(0);
//        View childAt1 = childAt.getChildAt(0);
//        initScrollView();
        ViewTreeObserver vto = mHScrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                 h = mHScrollView.getHeight();
                 w=mHScrollView.getWidth();
                Log.i(TAG, "Height=" + h); // 得到正确结果
                mHistoryTradeView=new HistoryTradeView(context);
                // 成功调用一次后，移除 Hook 方法，防止被反复调用
                // removeGlobalOnLayoutListener() 方法在 API 16 后不再使用
                // 使用新方法 removeOnGlobalLayoutListener() 代替

                ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(w,h);
                mHistoryTradeView.setLayoutParams(new ViewGroup.LayoutParams(w,h));
                mHistoryTradeView.setBackgroundColor(getResources().getColor(R.color.brand_color_dark));
                ll.addView(mHistoryTradeView);
                mHScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void freshView(final HistoryDataList data) {
                Log.i(TAG, "freshView: ");

        mHistoryTradeView.setHistoryData(data);
    }
//    private void initScrollView(){
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mHScrollView.smoothScrollTo(5000,0);
//            }
//        },1000);
//    }
}
