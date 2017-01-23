package trade.xkj.com.trade.mvp.main_trade.v;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeImageView;
import trade.xkj.com.trade.R;
import trade.xkj.com.trade.utils.ACache;
import trade.xkj.com.trade.utils.DataUtil;
import trade.xkj.com.trade.utils.MoneyUtil;
import trade.xkj.com.trade.utils.SystemUtil;
import trade.xkj.com.trade.utils.ToashUtil;
import trade.xkj.com.trade.utils.view.CustomDashedLinkView;
import trade.xkj.com.trade.utils.view.CustomPeriodButtons;
import trade.xkj.com.trade.utils.view.CustomViewPager;
import trade.xkj.com.trade.utils.view.DrawPriceView;
import trade.xkj.com.trade.utils.view.FixedSpeedScroller;
import trade.xkj.com.trade.utils.view.HistoryTradeView;
import trade.xkj.com.trade.utils.view.MyHorizontalScrollView;
import trade.xkj.com.trade.utils.view.MyScrollView;
import trade.xkj.com.trade.utils.view.ZoomOutPageTransformer;
import trade.xkj.com.trade.adapter.OpenAdapter;
import trade.xkj.com.trade.base.BaseFragment;
import trade.xkj.com.trade.bean.BeanDrawPriceData;
import trade.xkj.com.trade.bean.BeanDrawRealTimePriceData;
import trade.xkj.com.trade.bean.BeanIndicatorData;
import trade.xkj.com.trade.bean.BeanOpenPositionData;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.bean.RealTimeDataList;
import trade.xkj.com.trade.constant.TradeDateConstant;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPreListenerImpl;

import static android.R.attr.x;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by admin on 2016-11-22.
 * TODO:
 */

public class MainTradeContentFrag extends BaseFragment implements MainTradeListener.MainTradeContentLFragListener {
    private MainTradeListener.MainTradeContentPreListener mMainTradeContentPre;
    private MyHorizontalScrollView mHScrollView;
    private HistoryTradeView mHistoryTradeView;
    private RelativeLayout rl;
    private Context context;
    private int h;
    private int w;
    private float currentScaleSize = 1f;
    private float previousScaleSize = 1f;
    private int wChild;
    private CustomViewPager mHeaderCustomViewPager;
    private List<BeanIndicatorData> mIndicatorDatas;
    private DrawPriceView mDrawPriceView;
    private MyPagerAdapter mViewPagerAdapter;
    private FixedSpeedScroller scroller;
    private int mPosition;
    private RecyclerView mTradeContent;
    private List<BeanOpenPositionData> mBeanOpenList;
    private String mPeriod = "m1";
    private String symbol;
    private CustomPeriodButtons mCustomPeriodButtons;
    private CustomDashedLinkView mCustomDashedLinkView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_trade_content, container, false);
        Log.i(TAG, "onCreateView: ");
        return view;
    }


    @RequiresApi(api = M)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated: ");
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
                rl.addView(mHistoryTradeView);
                mHScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mHistoryTradeView.setDrawPriceListener(new HistoryTradeView.DrawPriceListener() {
            @Override
            public void drawPriceData(List<BeanDrawPriceData> drawPriceData) {
                mDrawPriceView.refresh(drawPriceData);

            }
        });
        mHScrollView.setScrollViewListener(new MyHorizontalScrollView.ScrollViewListener()

                                           {
                                               int mX = 0;
                                               int mY = 0;
                                               int z = SystemUtil.dp2px(context, TradeDateConstant.juli + TradeDateConstant.jianju);

                                               @Override
                                               public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y,
                                                                           int oldx, int oldy) {
//                                                   if (x != oldx) {
//                                                       if (Math.abs(x - mX) >= z) {
                                                           mX = x;
                                                           mHistoryTradeView.postInvalidate(x, 0, x + w, h, currentScaleSize);
//                                                       }
//                                                   } else {
//                                                       mX = x;
                                                       mY = y;
//                                                   }
                                                   Log.i(TAG, "onScrollChanged: w");
                                               }

                                               @Override
                                               public void onScaleDraw(float scaleSize) {
                                                   currentScaleSize = scaleSize;
                                                   Log.i(TAG, "onScaleDraw: previousScaleSize " + previousScaleSize + "   scaleSize " + scaleSize);
                                                   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (wChild * scaleSize), h);
                                                   mHScrollView.smoothScrollTo((int) (mX * (scaleSize / previousScaleSize)), 0);
                                                   mHistoryTradeView.setLayoutParams(layoutParams);
                                                   previousScaleSize = scaleSize;
                                                   mHistoryTradeView.postInvalidate((int) (mX * (scaleSize / previousScaleSize)), 0, (int) (mX * (scaleSize / previousScaleSize)) + w, h, currentScaleSize);
                                               }
                                           }
        );
        mTradeContent = (RecyclerView) view.findViewById(R.id.rv_trade_content);
        mTradeContent.setLayoutManager(new LinearLayoutManager(context));
        mTradeContent.setAdapter(new OpenAdapter(context, mBeanOpenList));
        mTradeContent.setFocusable(false);

        final View viewTitle = view.findViewById(R.id.trade_title_hint);
        ((MyScrollView) view.findViewById(R.id.msv_my_scroll_view)).registerListener(new MyScrollView.ScrollListener() {
            @Override
            public void onScroll(int top, int left) {
                if (top - view.findViewById(R.id.rl_trade).getTop() > 0 && viewTitle.getVisibility() != View.VISIBLE) {
                    viewTitle.setVisibility(View.VISIBLE);
                    Log.i(TAG, "onScroll: visible");
                } else if (top - view.findViewById(R.id.rl_trade).getTop() <= 0 && viewTitle.getVisibility() == View.VISIBLE) {
                    viewTitle.setVisibility(View.GONE);
                }
            }
        });
        mCustomPeriodButtons.addCheckChangeListener(new CustomPeriodButtons.CheckChangeListener() {
            @Override
            public void CheckChange(String period) {
                if (!mPeriod.equals(period)) {
                    mPeriod = period;
//                    mMainTradeContentPre.loadingHistoryData(symbol, period, TradeDateConstant.count);
                    mMainTradeContentPre.loadingHistoryData(symbol, period, TradeDateConstant.count);
                }
            }
        });
    }


    @Override
    protected void initView() {
        mHeaderCustomViewPager = (CustomViewPager) view.findViewById(R.id.vp_indicator_trade_content);
        mHScrollView = (MyHorizontalScrollView) view.findViewById(R.id.hsv_trade);
        mDrawPriceView = (DrawPriceView) view.findViewById(R.id.dp_draw_price);
        rl = (RelativeLayout) view.findViewById(R.id.rl);
        mCustomPeriodButtons = (CustomPeriodButtons) view.findViewById(R.id.cpb_period);
        mCustomDashedLinkView = (CustomDashedLinkView) view.findViewById(R.id.cdlv_);
    }

    @Override
    protected void initData() {
        context = this.getActivity();
        mMainTradeContentPre = new MainTradeContentPreListenerImpl(this, mHandler);
        mMainTradeContentPre.loading();
        mBeanOpenList = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            mBeanOpenList.add(new BeanOpenPositionData());
        }
    }

    HistoryDataList data;

    @Override
    public void freshView(final HistoryDataList data, boolean isCountDown) {
        Log.i(TAG, "freshView: ");
        if (data.getSymbol().equals(symbol) && data.getPeriod() == DataUtil.selectPeriod(mPeriod)) {
//            //处理数据
//            List<HistoryData> items = this.data.getItems();
//            for (HistoryData historyData : data.getItems()) {
//                items.remove(0);
//                items.add(historyData);
//            }
//            this.data.setItems(items);
//        } else {
            this.data = data;
//        }
            Log.i(TAG, "freshView: data");
            initScrollView(isCountDown);
            saveCache();
        }
    }

    private ACache aCache;

    private void saveCache() {
//        if (aCache == null) {
//            aCache = ACache.get(context);
//        }
//        aCache.remove(symbol.concat(mPeriod));
//        aCache.put(symbol.concat(mPeriod), new Gson().toJson(data, HistoryDataList.class));
    }

    @Override
    public void refreshIndicator(final ArrayList<BeanIndicatorData> mBeanIndicatorDataArrayList) {

        Log.i(TAG, "refreshIndicator: ");
        mIndicatorDatas = mBeanIndicatorDataArrayList;
        mViewPagerAdapter = new MyPagerAdapter();
        mHeaderCustomViewPager.setAdapter(mViewPagerAdapter);
        mHeaderCustomViewPager.setOffscreenPageLimit(mIndicatorDatas.size());
        mHeaderCustomViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
//        mMainTradeContentPre.loadingHistoryData(symbol = mBeanIndicatorDataArrayList.get(0).getSymbolTag(), mPeriod, TradeDateConstant.count);
        mMainTradeContentPre.loadingHistoryData(symbol = mBeanIndicatorDataArrayList.get(0).getSymbolTag(), mPeriod, TradeDateConstant.count);
        mHeaderCustomViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                //当手指左滑速度大于2000时viewpager右滑（注意是item+2）
                if (mHeaderCustomViewPager.getSpeed() < -1800) {
                    mHeaderCustomViewPager.setCurrentItem(mPosition + 2);
                    mHeaderCustomViewPager.setSpeed(0);
                } else if (mHeaderCustomViewPager.getSpeed() > 1800 && mPosition > 0) {
                    //当手指右滑速度大于2000时viewpager左滑（注意item-1即可）
                    mHeaderCustomViewPager.setCurrentItem(mPosition - 1);
                    mHeaderCustomViewPager.setSpeed(0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && !symbol.equals(mBeanIndicatorDataArrayList.get(mPosition).getSymbolTag())) {
                    Log.i(TAG, "onPageScrollStateChanged:+SCROLL_STATE_IDLE " + mPosition);
//                    mMainTradeContentPre.loadingHistoryData(symbol = mBeanIndicatorDataArrayList.get(mPosition).getSymbolTag(), mPeriod, TradeDateConstant.count);
                    mMainTradeContentPre.loadingHistoryData(symbol = mBeanIndicatorDataArrayList.get(mPosition).getSymbolTag(), mPeriod, TradeDateConstant.count);

                }
            }
        });
        setViewPagerSpeed(250);
        mHeaderCustomViewPager.postInvalidate();
    }

    private Handler handler = new Handler() {
    };

    private void initScrollView(final boolean isCountDown) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (TradeDateConstant.count != data.getCount()) {
                    wChild = data.getCount() * (SystemUtil.dp2px(context, TradeDateConstant.juli + TradeDateConstant.jianju));
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(wChild, h);
                    mHistoryTradeView.setLayoutParams(layoutParams);
                }
                if (data.getSymbol().equals(symbol) && data.getPeriod() == DataUtil.selectPeriod(mPeriod)) {
                    if (isCountDown) {
                        mHistoryTradeView.setHistoryData(data, x, x + w);
                        Log.i(TAG, "run: 时间到，定时刷新");
                    } else {
                        Log.i(TAG, "run: 全数据刷新");
                        mHScrollView.scrollTo(wChild, 0);
                        mHistoryTradeView.setHistoryData(data, wChild - w, wChild);
                    }
                }
            }
        });
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final BeanIndicatorData info = mIndicatorDatas.get(position);
            View view = LayoutInflater.from(context).inflate(R.layout.vp_indicator_head, null);
            BGABadgeImageView isSymbol = (BGABadgeImageView) view.findViewById(R.id.id_index_gallery_item_image);
            isSymbol.showDrawableBadge(BitmapFactory.decodeResource(getResources(), R.drawable.toggle_switch));
            view.setTag(info.getImageResource());
            TextView tvLeft = (TextView) view.findViewById(R.id.id_index_gallery_item_text_left);
            TextView tvRight = (TextView) view.findViewById(R.id.id_index_gallery_item_text_right);
            TextView tvName = (TextView) view.findViewById(R.id.id_index_gallery_symbol_name);
            tvLeft.setText(MoneyUtil.getRealTimePriceTextBig(context, "1.90031"));
            tvRight.setText(MoneyUtil.getRealTimePriceTextBig(context, "1.90031"));
            tvName.setText(info.getSymbolTag());
            isSymbol.setImageResource(info.getImageResource());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToashUtil.showShort(context, info.getSymbolTag());
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mIndicatorDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    /**
     * 设置ViewPager切换速度
     *
     * @param duration
     */
    private void setViewPagerSpeed(int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            scroller = new FixedSpeedScroller(context, new AccelerateInterpolator());
            field.set(mHeaderCustomViewPager, scroller);
            scroller.setmDuration(duration);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    BeanDrawRealTimePriceData beanDrawRealTimePriceData;

    @Subscribe
    public void getRealTime(RealTimeDataList realTimeDataList) {
        Log.i(TAG, "getRealTime: ");
//        for (RealTimeDataList.BeanRealTime beanRealTime : realTimeDataList.getQuotes()) {
//            if (beanRealTime.getSymbol().equals(symbol)) {
//                beanDrawRealTimePriceData = mHistoryTradeView.refreshRealTimePrice(beanRealTime);
//                if (beanDrawRealTimePriceData != null) {
//                    mDrawPriceView.refreshRealTimePrice(beanDrawRealTimePriceData);
//                    mCustomDashedLinkView.refresh(beanDrawRealTimePriceData);
//                }
//            }
//        }
    }
}
