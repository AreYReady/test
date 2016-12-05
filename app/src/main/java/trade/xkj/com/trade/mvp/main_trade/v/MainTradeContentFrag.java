package trade.xkj.com.trade.mvp.main_trade.v;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.Utils.ToashUtil;
import trade.xkj.com.trade.Utils.view.CustomRecycleView;
import trade.xkj.com.trade.Utils.view.DrawPriceView;
import trade.xkj.com.trade.Utils.view.HistoryTradeView;
import trade.xkj.com.trade.Utils.view.MyHorizontalScrollView;
import trade.xkj.com.trade.adapter.GalleryAdapter;
import trade.xkj.com.trade.base.BaseFragment;
import trade.xkj.com.trade.bean.BeanDrawPriceData;
import trade.xkj.com.trade.bean.BeanIndicatorData;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.constant.TradeDateConstant;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPre;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPreImpl;

import static android.os.Build.VERSION_CODES.M;

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
    private CustomRecycleView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<BeanIndicatorData> mIndicatorDatas;
    private DrawPriceView mDrawPriceView;
    private LinearLayoutManager linearLayoutManager;
    private int firstVisibleItemPosition;
    private int lastVisibleItemPosition;
    private View childAt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_trade_context, container, false);
        return view;
    }

    private float initF = 1f;

    @RequiresApi(api = M)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHScrollView = (MyHorizontalScrollView) view.findViewById(R.id.hsv_trade);
        mDrawPriceView = (DrawPriceView) view.findViewById(R.id.dp_draw_price);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        context = this.getActivity();
        mHistoryTradeView = new HistoryTradeView(context);
        ViewTreeObserver vto = mHScrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                h = mHScrollView.getHeight();
                w = mHScrollView.getWidth();
                Log.i(TAG, "Height=" + h); // 得到正确结果
                wChild = TradeDateConstant.count * (SystemUtil.dp2px(context, TradeDateConstant.juli + TradeDateConstant.jianju));
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
        mRecyclerView = (CustomRecycleView) view.findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器

        mHScrollView.setScrollViewListener(new MyHorizontalScrollView.ScrollViewListener()

                                           {
                                               int mX = 0;
                                               int mY = 0;
                                               int z = SystemUtil.dp2px(context, TradeDateConstant.juli + TradeDateConstant.jianju);

                                               @Override
                                               public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y,
                                                                           int oldx, int oldy) {
                                                   if (x != oldx) {
                                                       if (Math.abs(x - mX) >= z) {
                                                           mX = x;
                                                           mHistoryTradeView.postInvalidate(x, 0, x + w, h);
                                                       }
                                                   } else {
                                                       mX = x;
                                                       mY = y;
                                                   }
                                               }
                                           }

        );
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
//        EventBus.getDefault().register(this);
        mMainTradeContentPre = new MainTradeContentPreImpl(this, mHandler);
        mMainTradeContentPre.loading();
        if (mIndicatorDatas == null)
            mIndicatorDatas = new ArrayList<>();
    }

    HistoryDataList data;

    @Override
    public void freshView(final HistoryDataList data) {
        Log.i(TAG, "freshView: ");
        this.data = data;

        initScrollView();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    public void refreshIndicator(ArrayList<BeanIndicatorData> mBeanIndicatorDataArrayList) {
        mIndicatorDatas = mBeanIndicatorDataArrayList;
                mAdapter = new GalleryAdapter(context, mIndicatorDatas);
                mAdapter.setOnItemClickListener(new GalleryAdapter.OnRecyclerItemClickListener() {
                    @Override
                    public void onClick(View v, String s) {
                        ToashUtil.showShort(context, s + "  " + v.getX());
                    }
                });
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                      @Override
                                                      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                                          super.onScrollStateChanged(recyclerView, newState);
                                                          if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                                              mRecyclerView.smoothToCenter();
                                                          }
                                                      }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                Log.i(TAG, "smoothToCenter: onScrolled");
//                firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
//                lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//                for (; firstVisibleItemPosition <= lastVisibleItemPosition; firstVisibleItemPosition++) {
//                    Log.i(TAG, "onScrolled: firstVisibleItemPosition" + firstVisibleItemPosition + "  lastVisibleItemPosition" + lastVisibleItemPosition);
//                    childAt = linearLayoutManager.getChildAt(firstVisibleItemPosition);
//                    int parentWidth = recyclerView.getWidth();
//                    if (childAt == null)
//                        return;
//                    int childWidth = childAt.getWidth();
//                    int center = parentWidth / 2 - childWidth / 2;//计算子view居中后相对于父view的左边距
//                    float chlidLeft = childAt.getLeft();
//                    float v1 = chlidLeft / center;
//                    if (chlidLeft >= center) {
//                        int childAtRight = childAt.getRight();
//                        v1 = (parentWidth - childAtRight) / center;
//                    }
//                    if (v1 > 1) {
//                        v1 = 1;
//                    }
//                        Log.i(TAG, "onScrolled: v1" + v1);
//                        ScaleAnimation scaleAnimation = new ScaleAnimation(v1, v1, v1, v1);
//                        scaleAnimation.setFillAfter(true);
//                        childAt.setAnimation(scaleAnimation);
//                        scaleAnimation.startNow();
//                    }
//                }
//
                                                  }

                );
                mRecyclerView.setAdapter(mAdapter);
    }

    private void initScrollView() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHScrollView.scrollTo(wChild, 0);
                mHistoryTradeView.setHistoryData(data, wChild - w, wChild);
            }
        }, 1000);
    }
}
