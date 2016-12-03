package trade.xkj.com.trade.mvp.main_trade.v;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.Utils.ToashUtil;
import trade.xkj.com.trade.Utils.view.DrawPriceView;
import trade.xkj.com.trade.Utils.view.HistoryTradeView;
import trade.xkj.com.trade.Utils.view.MyHorizontalScrollView;
import trade.xkj.com.trade.adapter.GalleryAdapter;
import trade.xkj.com.trade.base.BaseFragment;
import trade.xkj.com.trade.bean.BeanDrawPriceData;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.constant.KLineChartConstant;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPre;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPreImpl;

/**
 * Created by admin on 2016-11-22.
 */

public class MainTradeContentFrag extends BaseFragment implements MainTradeContentLFragListener {
    private View view;
    private MainTradeContentPre mMainTradeContentPre;
    private MyHorizontalScrollView mHScrollView;
    private HistoryTradeView mHistoryTradeView;
    private LinearLayout ll;
    private LinearLayout llDrawTrade;
    private Context context;
    private int h;
    private int w;
    private int wChild;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<Integer> mDatas;
    private DrawPriceView mDrawPriceView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_trade_context, container, false);
        return view;
    }

    private float initF=1f;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHScrollView = (MyHorizontalScrollView) view.findViewById(R.id.hsv_trade);
        mDrawPriceView=(DrawPriceView)view.findViewById(R.id.dp_draw_price);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        context = this.getActivity();
        mHistoryTradeView = new HistoryTradeView(context);
        ViewTreeObserver vto = mHScrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                h = mHScrollView.getHeight();
                w = mHScrollView.getWidth();
                Log.i(TAG, "Height=" + h); // 得到正确结果
                 wChild=KLineChartConstant.count*(SystemUtil.dp2px(context,KLineChartConstant.juli+KLineChartConstant.jianju));
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(wChild, h);
                mHistoryTradeView.setLayoutParams(layoutParams);
                ll.addView(mHistoryTradeView);
                mHScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mHistoryTradeView.setDrawPriceListeren(new HistoryTradeView.DrawPriceListeren() {
            @Override
            public void drawPriceData(List<BeanDrawPriceData> drawPriceData) {
                mDrawPriceView.refresh(drawPriceData);
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new GalleryAdapter(context, mDatas);
        mAdapter.setOnItemClickListener(new GalleryAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onClick(View v, String s) {
                ToashUtil.showShort(context,s+"  "+v.getX());

            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition != 0) {
                    Log.i(TAG, "onScrolled: firstVisibleItemPosition" + firstVisibleItemPosition + "  lastVisibleItemPosition" + lastVisibleItemPosition);
                    View childAt = layoutManager.getChildAt(firstVisibleItemPosition+2);
                    int halfWidth = recyclerView.getWidth()/2;
                    float x = childAt.getX();
                    float v = halfWidth - x;
                    float v1 = x / halfWidth;
                    ScaleAnimation scaleAnimation = new ScaleAnimation(initF, v1, initF, v1);
                    initF=v1;
                    Log.i(TAG, "onScrolled:initF "+initF+"    v1"+v1);
                    scaleAnimation.setFillAfter(true);
                    childAt.setAnimation(scaleAnimation);
                    scaleAnimation.startNow();
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mHScrollView.setScrollViewListener(new MyHorizontalScrollView.ScrollViewListener() {
            int mX=0;
            int mY=0;
            int z=SystemUtil.dp2px(context,KLineChartConstant.juli+KLineChartConstant.jianju);
            @Override
            public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                if(x!=oldx){
                    if(Math.abs(x-mX)>=z){
                        mX=x;
                        mHistoryTradeView.postInvalidate(x,0,x+w,h);
                    }
                }else{
                    mX=x;
                    mY=y;
                }
            }
        });
        decodeRecycle();
    }

    private void decodeRecycle() {
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
        mMainTradeContentPre = new MainTradeContentPreImpl(this, mHandler);
        mMainTradeContentPre.loading();
        mDatas = new ArrayList<Integer>(Arrays.asList(R.mipmap.ic_instrument_au,
                R.mipmap.ic_instrument_audcad, R.mipmap.ic_instrument_audchf, R.mipmap.ic_instrument_audjpy, R.mipmap.ic_instrument_audnzd,
                R.mipmap.ic_instrument_beans, R.mipmap.ic_instrument_cadchf, R.mipmap.ic_instrument_cadjpy, R.mipmap.ic_instrument_be));
    }
    HistoryDataList data;
    @Override
    public void freshView(final HistoryDataList data) {
        Log.i(TAG, "freshView: ");
        this.data = data;

        initScrollView();
    }

    private void initScrollView() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHScrollView.scrollTo(wChild, 0);
                mHistoryTradeView.setHistoryData(data,wChild-w,wChild);
            }
        }, 1000);
    }

}
