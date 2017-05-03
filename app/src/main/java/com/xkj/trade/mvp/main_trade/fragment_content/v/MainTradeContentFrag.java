package com.xkj.trade.mvp.main_trade.fragment_content.v;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.R;
import com.xkj.trade.adapter.MyFavoritesAdapter;
import com.xkj.trade.adapter.MyViewPagerAdapterItem;
import com.xkj.trade.adapter.OpenAdapter;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean.BeanDrawPriceData;
import com.xkj.trade.bean.BeanDrawRealTimePriceData;
import com.xkj.trade.bean.BeanIndicatorData;
import com.xkj.trade.bean.RealTimeDataList;
import com.xkj.trade.bean_.BeanAllSymbols;
import com.xkj.trade.bean_.BeanAppConfig;
import com.xkj.trade.bean_.BeanHistory;
import com.xkj.trade.bean_.BeanOpenPosition;
import com.xkj.trade.bean_.BeanUserListInfo;
import com.xkj.trade.bean_notification.NotificationClosePosition;
import com.xkj.trade.bean_notification.NotificationEditPosition;
import com.xkj.trade.bean_notification.NotificationFloat;
import com.xkj.trade.constant.CacheKeyConstant;
import com.xkj.trade.constant.TradeDateConstant;
import com.xkj.trade.diffcallback.MyFavoritesDiffCallBack;
import com.xkj.trade.diffcallback.OpenPositionDiff;
import com.xkj.trade.mvp.main_trade.fragment_content.p.MainTradeContentPreListenerImpl;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.DataUtil;
import com.xkj.trade.utils.DateUtils;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.RoundImageView;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.ThreadHelper;
import com.xkj.trade.utils.ToashUtil;
import com.xkj.trade.utils.view.CircleIndicator;
import com.xkj.trade.utils.view.CustomDashedLinkView;
import com.xkj.trade.utils.view.CustomPeriodButtons;
import com.xkj.trade.utils.view.CustomViewPager;
import com.xkj.trade.utils.view.DrawPriceView;
import com.xkj.trade.utils.view.FixedSpeedScroller;
import com.xkj.trade.utils.view.FullyLinearLayoutManager;
import com.xkj.trade.utils.view.HistoryTradeView;
import com.xkj.trade.utils.view.MyHorizontalScrollView;
import com.xkj.trade.utils.view.MyScrollView;
import com.xkj.trade.utils.view.WrapContentLinearLayoutManager;
import com.xkj.trade.utils.view.ZoomOutPageTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bingoogolapple.badgeview.BGABadgeImageView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.Build.VERSION_CODES.M;
import static com.xkj.trade.base.MyApplication.beanIndicatorData;

/**
 * Created by hsc on 2016-11-22.
 * TODO:展示k线图的fragment;
 */

public class MainTradeContentFrag extends BaseFragment implements MainTradeFragListener.MainTradeContentLFragListener {
    private MainTradeFragListener.MainTradeContentPreListener mMainTradeContentPre;
    private MyHorizontalScrollView mHScrollView;
    private HistoryTradeView mHistoryTradeView;
    private RelativeLayout rl;
    private Context context;
    private LinearLayout mKLinkLayout;
    private LinearLayout mMyFavorites;
    private CustomViewPager mMyFavoritesIndicator;
    private LinearLayout mShowSymbolInfo;
    private TextView mShowSymbolAsk;
    private TextView mShowSymbolBid;
    private int h;
    private int w;
    private float currentScaleSize = 1f;
    private float previousScaleSize = 1f;
    private int wChild;
    private CustomViewPager mHeaderCustomViewPager;
    private DrawPriceView mDrawPriceView;
    private MyPagerAdapter mViewPagerAdapter;
    private FixedSpeedScroller scroller;
    private int mPosition;
    private RecyclerView mTradeContent;
    private List<BeanOpenPosition.DataBean.ListBean> mBeanOpenList;
    private List<BeanOpenPosition.DataBean.ListBean> mBeanDupOpenList;
    private String mPeriod = "m1";
    private String symbol;
    private CustomPeriodButtons mCustomPeriodButtons;
    private CustomDashedLinkView mCustomDashedLinkView;
    private LinkedList<BeanIndicatorData> subSymbols;
    private LinkedList<BeanIndicatorData> dupSubSymbols = new LinkedList<>();
    private List<String> mMyFavoritesTitle;
    private RecyclerView mMyFavoritesContent;
    private MyFavoritesAdapter mMyFavoritesAdapter;
    private List<BeanAllSymbols.SymbolPrices> mDatas = new ArrayList<BeanAllSymbols.SymbolPrices>();
    private List<BeanAllSymbols.SymbolPrices> mFilterDatas = new ArrayList<BeanAllSymbols.SymbolPrices>();
    private List<BeanAllSymbols.SymbolPrices> mDupFilterDatas = new ArrayList<BeanAllSymbols.SymbolPrices>();
    private SearchView mSearchView;
    private OpenAdapter mOpenAdapter;
    private int firstItemPosition;
    private int lastItemPosition;
    public static Map<String, BeanAllSymbols.SymbolPrices> realTimeMap = new ArrayMap<>();
    public static Map<String,String> mDigitsList=new TreeMap<>();
    private TextView mOpenPositionCount;
    private TextView mProfitCount;
    private String mProfitCountMoney;
    private ImageView mIvLoading;
    private LinearLayout mllcontent;
    private MyScrollView mMyScrollView;
    private LinearLayout mllPrompt;
    private TextView mTvPromptSymbol;
    private TextView mTvPromptAsk;
    private TextView mTvPromptBid;
    private ImageView mIvPrompt;
    AnimationDrawable animationDrawable;
    CircleIndicator mCircleIndicator;
    HorizontalScrollView mHorizontalScrollView;
    BeanAppConfig beanAppConfig;
    /**
     * 储存所有历史数据
     */
    private Map<String ,BeanHistory.BeanHistoryData> mHistoryDataMap=new ArrayMap<>();

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
        subSymbols = new Gson().fromJson(aCache.getAsString(CacheKeyConstant.SUB_SYMBOLS), new TypeToken<LinkedList<BeanIndicatorData>>() {
        }.getType());
        clearAndAddAll(dupSubSymbols, subSymbols);
        initIndicator();
        mHistoryTradeView = new HistoryTradeView(context);
        ViewTreeObserver vto = mHScrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                h = mHScrollView.getHeight();
                w = mHScrollView.getWidth();
                Log.i(TAG, "Height=" + h); // 得到正确结果
                wChild = (TradeDateConstant.count) * (SystemUtil.dp2px(context, TradeDateConstant.juli + TradeDateConstant.jianju))+15;
                //太过要求精确计算，最后一个item显示半个。所以宽度加10个像素，修正这个算法缺失
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
                                                   mX = x;
                                                   mHistoryTradeView.postInvalidate(x, 0, x + w, h, currentScaleSize);
                                                   mY = y;
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
        if(subSymbols.size()!=1)
        mMainTradeContentPre.requestOpenPosition();
        mTradeContent.setLayoutManager(new FullyLinearLayoutManager(context));
//        mBeanOpenList=new ArrayList<>();
//        mTradeContent.setAdapter(mOpenAdapter=new OpenAdapter(context, mBeanOpenList));
        mTradeContent.setFocusable(false);

        mCustomPeriodButtons.addCheckChangeListener(new CustomPeriodButtons.CheckChangeListener() {
            @Override
            public void CheckChange(String period) {
                if (!mPeriod.equals(period)) {
                    mPeriod = period;
//                    mMainTradeContentPre.requestHistoryData(symbol, period, TradeDateConstant.count);
                    requestHistoryData(symbol, period, TradeDateConstant.count);
                }
            }
        });
        mMyFavoritesIndicator.setAdapter(new MyViewPagerAdapterItem(context, mMyFavoritesTitle));
        mMyFavoritesIndicator.setOffscreenPageLimit(mMyFavoritesTitle.size());
        mMyFavoritesIndicator.setPageTransformer(true, new ZoomOutPageTransformer());
        mSearchView.setVisibility(View.GONE);
        mMyFavoritesIndicator.setCurrentItem(1);
        mMyFavoritesIndicator.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int mPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                clearAndAddAll(mDupFilterDatas, mFilterDatas);
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    switch (mPosition) {
                        case 0:
                            //根据搜索显示
                            mSearchView.setVisibility(View.VISIBLE);
                            mFilterDatas.clear();
                            SearchSymbol();
                            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyFavoritesDiffCallBack(mDupFilterDatas, mFilterDatas), true);
                            diffResult.dispatchUpdatesTo(mMyFavoritesAdapter);
                            break;
                        case 1:
                            //显示全部
                            mSearchView.setVisibility(View.GONE);
                            clearAndAddAll(mFilterDatas, mDatas);
                            mMyFavoritesAdapter.notifyDataSetChanged();
                            break;
                        case 2:
                            mSearchView.setVisibility(View.GONE);
                            mFilterDatas.clear();
                            for (BeanAllSymbols.SymbolPrices subSymbol : mDatas) {
                                if (subSymbol.getSign()) {
                                    mFilterDatas.add(subSymbol);
                                    Log.i(TAG, "onPageScrollStateChanged: ");
                                }
                            }
                            mMyFavoritesAdapter.notifyDataSetChanged();
                            //显示订阅的
                            break;
                    }
                    mMyFavoritesAdapter.setData(mFilterDatas);
                }
            }
        });
        mMyFavoritesContent.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit = " + query);

                if (mSearchView != null) {
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                    }
                    mSearchView.clearFocus(); // 不获取焦点
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mFilterDatas = checkFitData(mFilterDatas, mDatas, newText);
                clearAndAddAll(mDupFilterDatas, mFilterDatas);
                mMyFavoritesAdapter.notifyDataSetChanged();
                return true;
            }
        });
        ArrayList<String> symbolsName = new ArrayList<>();
        for (int i = 0; i < subSymbols.size() - 1; i++) {
            symbolsName.add(subSymbols.get(i).getSymbol());
        }
        mMainTradeContentPre.requestSubSymbols(symbolsName, true);
        mMainTradeContentPre.requestUserList();
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (view.getMeasuredHeight() != 0) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Log.i(TAG, "onlobalLayout: " + view.getMeasuredHeight());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(view.getWidth(), view.getMeasuredHeight() - view.findViewById(R.id.trade_title).getMeasuredHeight()-mllPrompt.getHeight());
                    mTradeContent.setLayoutParams(layoutParams);
                    view.findViewById(R.id.ll_my_favorites).setLayoutParams(new LinearLayout.LayoutParams(view.getWidth(), view.getHeight()));
                }
            }
        });
        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mllPrompt.getLayoutParams();
//        mllPrompt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mllPrompt.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                layoutParams.topMargin=-mllPrompt.getHeight();
//                Log.i(TAG, "onGlobalLayout: "+mllPrompt.getHeight());
//                mllPrompt.setLayoutParams(layoutParams);
//            }
//        });
        mMyScrollView.registerListener(new MyScrollView.ScrollListener() {
            @Override
            public void onScroll(final int top, int left) {
                if (mPosition != subSymbols.size() - 1) {
                    if (top < mllPrompt.getHeight()) {
                        layoutParams.topMargin = top - mllPrompt.getHeight();
                        mllPrompt.setLayoutParams(layoutParams);
                        if (mllPrompt.getVisibility() != View.VISIBLE) {
                            mllPrompt.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layoutParams.topMargin = 0;
                        mllPrompt.setLayoutParams(layoutParams);
                    }
                }
            }
        });
        view.findViewById(R.id.rll_trade_indicator).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mHeaderCustomViewPager.dispatchTouchEvent(event);
            }
        });
        view.findViewById(R.id.fl_my_favorites_indicator_parent).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mMyFavoritesIndicator.dispatchTouchEvent(event);
            }
        });
    }

    //筛选合适的数据
    private List<BeanAllSymbols.SymbolPrices> checkFitData(List<BeanAllSymbols.SymbolPrices> fitData, List<BeanAllSymbols.SymbolPrices> fullData, String searchText) {
        fitData.clear();
        for (BeanAllSymbols.SymbolPrices symbolPrices : fullData) {
            if (contain(symbolPrices.getSymbol(), searchText)) {
                fitData.add(symbolPrices);
            }
        }
        return fitData;
    }


    private void requestHistoryData(String symbol, String period, int count) {

        if(symbol!=null&&mHistoryDataMap.containsKey(DataUtil.symbolConnectPeriod(symbol,""+DataUtil.selectPeriod(period)))){
         refreshView(mHistoryDataMap.get(DataUtil.symbolConnectPeriod(symbol,""+DataUtil.selectPeriod(period)))) ;
        }else {
            mIvLoading.setVisibility(View.VISIBLE);
//        mHScrollView.setVisibility(View.INVISIBLE);
//        mDrawPriceView.setVisibility(View.INVISIBLE);
            mllcontent.setVisibility(View.INVISIBLE);
            if (animationDrawable == null) {
                animationDrawable = (AnimationDrawable) mIvLoading.getDrawable();
            }
            animationDrawable.start();
            mMainTradeContentPre.requestHistoryData(symbol, period, count);
        }
    }

    public boolean contain(String input, String regex) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(input);
        boolean result = m.find();
        return result;
    }


    public void clearAndAddAll(List<BeanAllSymbols.SymbolPrices> clearData, List<BeanAllSymbols.SymbolPrices> addAllData) {
        clearData.clear();
        //保证地址
        for (BeanAllSymbols.SymbolPrices symbolPrice : addAllData) {
            clearData.add(symbolPrice.clone());
        }
    }

    private void clearAndAddAll(LinkedList<BeanIndicatorData> clearData, LinkedList<BeanIndicatorData> addAllData) {
        clearData.clear();
        clearData.addAll(addAllData);
    }

    //处理搜索显示
    private void SearchSymbol() {
        /**点击弹出键盘，布局不上顶。
         * 在最开始的时候，计算父layout的大小。给父view赋值。点击键盘，自动移到顶点。
         * 1:布局先不管，考虑功能逻辑。布局后面在实现
         * 2：考虑键盘问题
         */
    }

    @Override
    protected void initView() {
        mHeaderCustomViewPager = (CustomViewPager) view.findViewById(R.id.vp_indicator_trade_content);
        mHScrollView = (MyHorizontalScrollView) view.findViewById(R.id.hsv_trade);
        mDrawPriceView = (DrawPriceView) view.findViewById(R.id.dp_draw_price);
        rl = (RelativeLayout) view.findViewById(R.id.rl);
        mCustomPeriodButtons = (CustomPeriodButtons) view.findViewById(R.id.cpb_period);
        mCustomDashedLinkView = (CustomDashedLinkView) view.findViewById(R.id.cdlv_);
        mKLinkLayout = (LinearLayout) view.findViewById(R.id.ll_k_link_content);
        mMyFavorites = (LinearLayout) view.findViewById(R.id.ll_my_favorites);
        mMyFavoritesIndicator = (CustomViewPager) view.findViewById(R.id.cvp_my_favorites_indicator);
        mMyFavoritesContent = (RecyclerView) view.findViewById(R.id.rv_my_favorites_content);
        mSearchView = (SearchView) view.findViewById(R.id.sv_my_favorites);
        mShowSymbolInfo = (LinearLayout) view.findViewById(R.id.ll_show_symbol_info);
        mShowSymbolAsk = (TextView) view.findViewById(R.id.tv_symbol_ask);
        mShowSymbolBid = (TextView) view.findViewById(R.id.tv_symbol_bid);
        mOpenPositionCount = (TextView) view.findViewById(R.id.tv_open_position_count);
        mProfitCount = (TextView) view.findViewById(R.id.tv_profit_count);
        mIvLoading = (ImageView) view.findViewById(R.id.iv_loading);
        animationDrawable = (AnimationDrawable) mIvLoading.getDrawable();
        mllcontent = (LinearLayout) view.findViewById(R.id.ll_draw_trade_context);
        mMyScrollView = (MyScrollView) view.findViewById(R.id.msv_my_scroll_view);
        mllPrompt = (LinearLayout) view.findViewById(R.id.ll_prompt);
        mTvPromptAsk=(TextView)view.findViewById(R.id.tv_price_ask_prompt);
        mTvPromptBid=(TextView)view.findViewById(R.id.tv_price_bid_prompt);
        mTvPromptSymbol=(TextView)view.findViewById(R.id.tv_symbol_name_prompt);
        mIvPrompt=(RoundImageView)view.findViewById(R.id.riv_trade_symbol);
        mCircleIndicator=(CircleIndicator)view.findViewById(R.id.ci_circle_indicator);
        mHorizontalScrollView  =(HorizontalScrollView)view.findViewById(R.id.hsv_circleIndicator_scrollView);
    }

    @Override
    protected void initData() {
        context = this.getActivity();
        mMainTradeContentPre = new MainTradeContentPreListenerImpl(this, mHandler, getContext());
        if (aCache == null) {
            aCache = ACache.get(context);
        }

        BeanIndicatorData beanIndicatorData;
        beanAppConfig = new Gson().fromJson(aCache.getAsString(CacheKeyConstant.CACLE_APP_CONFIG), BeanAppConfig.class);
        if (aCache.getAsString(CacheKeyConstant.SUB_SYMBOLS) == null) {
            LinkedList<BeanIndicatorData> symbols = new LinkedList<BeanIndicatorData>();
            if(beanAppConfig.getMsg().getSymbol().size()>4){
                    symbols.add(new BeanIndicatorData(beanAppConfig.getMsg().getSymbol().get(0).getSymbol(),"0.00","0.00"));
                    symbols.add(new BeanIndicatorData(beanAppConfig.getMsg().getSymbol().get(1).getSymbol(),"0.00","0.00"));
                    symbols.add(new BeanIndicatorData(beanAppConfig.getMsg().getSymbol().get(2).getSymbol(),"0.00","0.00"));
                    symbols.add(new BeanIndicatorData(beanAppConfig.getMsg().getSymbol().get(3).getSymbol(),"0.00","0.00"));
            }else{
                for(BeanAppConfig.MsgBean.SymbolBean symbolBean:beanAppConfig.getMsg().getSymbol()){
                    symbols.add(new BeanIndicatorData(symbolBean.getSymbol(),"0.00","0.00"));
                }
            }
            beanIndicatorData = new BeanIndicatorData(getResources().getString(R.string.my_favorites), "", "");
            symbols.add(beanIndicatorData);
            aCache.put(CacheKeyConstant.SUB_SYMBOLS, new Gson().toJson(symbols, new TypeToken<LinkedList<BeanIndicatorData>>() {
            }.getType()));
        }
        mMyFavoritesTitle = new ArrayList<>();
        mMyFavoritesTitle.add(getResources().getString(R.string.search));
        mMyFavoritesTitle.add(getResources().getString(R.string.all));
        mMyFavoritesTitle.add(getResources().getString(R.string.favorites_num));
    }

    BeanHistory.BeanHistoryData data;

    @Override
    public void refreshView(final BeanHistory.BeanHistoryData data) {

        if(data==null){
            return;
        }

        mHistoryDataMap.put(DataUtil.symbolConnectPeriod(data.getSymbol(),data.getPeriod()+""),data);
        Log.i(TAG, "responseHistoryData: ");
        if (mHeaderCustomViewPager.getCurrentItem() == subSymbols.size() - 1) {
            Log.i(TAG, "responseHistoryData: 我的收藏夹");
        } else if (data.getSymbol().equals(symbol) && data.getPeriod() == DataUtil.selectPeriod(mPeriod)) {
            this.data = data;
            Log.i(TAG, "responseHistoryData: data");
            handler.sendEmptyMessage(REFRESH_HISTORY_VIEW);
            saveCache();
        }
    }
    private int refreshIndicatorIndex;

    public void realTimeIndicator(Map<String,BeanIndicatorData> mBeanIndicatorDataList) {
//
        Bundle bundle;
        for(String symbol:mBeanIndicatorDataList.keySet()){
            BeanIndicatorData newData=mBeanIndicatorDataList.get(symbol);
            for(int i=0;i<subSymbols.size()-1;i++){
                        BeanIndicatorData subData=subSymbols.get(i);
                if (newData.getSymbol().equals(subData.getSymbol())&&!(subData.getAsk().equals(newData.getAsk())&&subData.getBid().equals(newData.getBid()))) {
                    subData.setAskColor(getResources().getColor(Double.valueOf(newData.getAsk()) >= Double.valueOf(subData.getAsk()) ? R.color.text_color_price_rise : R.color.text_color_price_fall));
                    subData.setBidColor(getResources().getColor(Double.valueOf(newData.getBid()) >= Double.valueOf(subData.getBid()) ? R.color.text_color_price_rise : R.color.text_color_price_fall));
                    subData.setAsk(newData.getAsk());
                    subData.setBid(newData.getBid());
//                    refreshIndicatorIndex = i;
                    Message message=new Message();
                    message.what=REFRESH_INDICATOR;
                    bundle=new Bundle();
                    bundle.putString("symbol",subData.getSymbol());
                    message.arg1=i;
                    message.setData(bundle);
                    handler.sendMessage(message);
//                    }
                    if (i == mHeaderCustomViewPager.getCurrentItem()) {
                        //说明是当前
                        EventBus.getDefault().post(subData);
                    }
                }
            }
        }
    }

    @Override
    public void refreshUserInfo(BeanUserListInfo info) {
        Log.i(TAG, "refreshUserInfo: ");

    }

    @Override
    public void refreshOpenPosition(BeanOpenPosition info) {
        mBeanOpenList = info.getData().getList();
        if (mBeanOpenList == null) {
            mBeanOpenList = new ArrayList<>();
        }
        mProfitCountMoney = "0";
        for (BeanOpenPosition.DataBean.ListBean bean : mBeanOpenList) {
            if (bean.getCmd().equals("buy")) {
                bean.setPrice(beanIndicatorData.getBid());
            } else {
                bean.setPrice(beanIndicatorData.getAsk());
            }
        }
        mProfitCountMoney = cacleProfitCount();
        handler.sendEmptyMessage(REFRESH_OPEN_POSITION);
    }

    @Override
    public void responseAllSymbolsData(String response) {
        ACache.get(context).put(CacheKeyConstant.ALL_SYMBOLS_PRICES, response);
        BeanAllSymbols beanAllSymbols = new Gson().fromJson(response, BeanAllSymbols.class);
        allSymbolsName.clear();
        for (int i = 0; i < beanAllSymbols.getData().size(); i++) {
            realTimeMap.put(beanAllSymbols.getData().get(i).getSymbol(), beanAllSymbols.getData().get(i));
            allSymbolsName.add(beanAllSymbols.getData().get(i).getSymbol());
        }
        mMainTradeContentPre.requestSubSymbols(allSymbolsName, true);
        BeanAllSymbols.SymbolPrices symbolPrices;
        mDatas.clear();
        for(BeanAppConfig.MsgBean.SymbolBean symbolBean:beanAppConfig.getMsg().getSymbol()){
            for(int i=0;i<beanAllSymbols.getData().size();i++){
                symbolPrices=beanAllSymbols.getData().get(i);
                if(symbolBean.getSymbol().equals(symbolPrices.getSymbol())){
                    mDatas.add(symbolPrices);
                    mDigitsList.put(symbolBean.getSymbol(),symbolPrices.getDigits());
                }
            }
        }
    }


    private boolean isScroll = false;
    private ACache aCache;
    private void saveCache() {
//        if (aCache == null) {
//            aCache = ACache.get(context);
//        }
//        aCache.remove(symbol.concat(mPeriod));
//        aCache.put(symbol.concat(mPeriod), new Gson().toJson(data, HistoryDataList.class));
    }

    private void initIndicator() {

        Log.i(TAG, "initIndicator: ");
        mViewPagerAdapter = new MyPagerAdapter();
        mHeaderCustomViewPager.setAdapter(mViewPagerAdapter);
        mCircleIndicator.setViewPager(mHeaderCustomViewPager);
        mCircleIndicator.setListener(new CircleIndicator.listen() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position>1)
                    mHorizontalScrollView.smoothScrollTo((int) ((position-2) * SystemUtil.dp2px(context,15) + positionOffset * SystemUtil.dp2px(context,15)), 0);
//                    }
            }
        });
        mViewPagerAdapter.registerDataSetObserver(mCircleIndicator.getDataSetObserver());
//        mHeaderCustomViewPager.setOffscreenPageLimit(subSymbols.size());
        mHeaderCustomViewPager.setOffscreenPageLimit(subSymbols.size());
        mHeaderCustomViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        if(subSymbols.size()==1){
            showMyFavorites();
        }else {
            requestHistoryData(symbol = subSymbols.get(0).getSymbol(), mPeriod, TradeDateConstant.count);
            MyApplication.getInstance().beanIndicatorData = subSymbols.get(0);
            MyApplication.getInstance().beanIndicatorData.setSymbol(symbol);
            mTvPromptSymbol.setText(MyApplication.getInstance().beanIndicatorData.getSymbol());
            mTvPromptAsk.setText(MyApplication.getInstance().beanIndicatorData.getAsk());
            mTvPromptBid.setText(MyApplication.getInstance().beanIndicatorData.getBid());
            mIvPrompt.setImageResource(MyApplication.getInstance().beanIndicatorData.getImageResource());
            fillPromptSymbolInfo(0);
        }
        //申请当前所有交易的当前报价单储存
        requestHistoryData(null, null, 0);
        mHeaderCustomViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
//                当手指左滑速度大于2000时viewpager右滑（注意是item+2）
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
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    isScroll = false;
                    if (!symbol.equals(subSymbols.get(mPosition).getSymbol())) {
                        symbol = subSymbols.get(mPosition).getSymbol();
                        MyApplication.getInstance().beanIndicatorData = subSymbols.get(mPosition);
                        mTvPromptSymbol.setText(MyApplication.getInstance().beanIndicatorData.getSymbol());
                        mTvPromptAsk.setText(MyApplication.getInstance().beanIndicatorData.getAsk());
                        mTvPromptBid.setText(MyApplication.getInstance().beanIndicatorData.getBid());
                        mIvPrompt.setImageResource(MyApplication.getInstance().beanIndicatorData.getImageResource());
                        Log.i(TAG, "onPageScrollStateChanged:+SCROLL_STATE_IDLE " + mPosition);
                        if (mPosition == subSymbols.size() - 1) {
                            showMyFavorites();
                        } else {
                            mCustomPeriodButtons.setButtonVisibility(View.VISIBLE);
                            EventBus.getDefault().post(new NotificationFloat(true));
                            Log.i(TAG, "onPageScrollStateChanged: 加载数据");
                            if(mllPrompt.getVisibility()!=View.VISIBLE){
                                mllPrompt.setVisibility(View.VISIBLE);
                            }
                            if (mKLinkLayout.getVisibility() != View.VISIBLE) {
                                mKLinkLayout.setVisibility(View.VISIBLE);
                            }
                            if (mMyFavorites.getVisibility() != View.GONE) {
                                mMyFavorites.setVisibility(View.GONE);
                            }
                            mMainTradeContentPre.requestOpenPosition();
                            fillPromptSymbolInfo(mPosition);
                            requestHistoryData(symbol, mPeriod, TradeDateConstant.count);
                        }

                    }
                } else {
                    isScroll = true;
                }
            }
        });
        setViewPagerSpeed(250);
    }

    //上啦是显示的当前交易的基本信息
    private void fillPromptSymbolInfo(int currentPosition) {
        BeanIndicatorData beanIndicatorData = subSymbols.get(currentPosition);
        ((CircleImageView) view.findViewById(R.id.civ_symbol_flag)).setImageResource(beanIndicatorData.getImageResource());
        mShowSymbolAsk.setText(MoneyUtil.getRealTimePriceTextBig(context, beanIndicatorData.getAsk()));
        mShowSymbolBid.setText(MoneyUtil.getRealTimePriceTextBig(context, beanIndicatorData.getBid()));
        ((TextView) view.findViewById(R.id.tv_symbol_name)).setText(beanIndicatorData.getSymbol());
    }

    ArrayList<String> allSymbolsName = new ArrayList<>();

    //显示我的收藏夹
    private void showMyFavorites() {
        Log.i(TAG, "showMyFavorites: ");
        EventBus.getDefault().post(new NotificationFloat(false));
        if(mllPrompt.getVisibility()==View.VISIBLE){
            mllPrompt.setVisibility(View.INVISIBLE);
        }
        mCustomPeriodButtons.setButtonVisibility(View.INVISIBLE);
        mKLinkLayout.setVisibility(View.GONE);
        mMyFavorites.setVisibility(View.VISIBLE);

//        beanAllSymbols.getData().clear();
//        beanAllSymbols.getData().addAll(symbolPricesList);

        for (int i = 0; i < mDatas.size(); i++) {
            for (BeanIndicatorData subSymbol : subSymbols) {
                if (mDatas.get(i).getSymbol().equals(subSymbol.getSymbol())) {
                    Log.i(TAG, "showMyFavorites: " + subSymbol.getSymbol() + " " + i);
                    mDatas.get(i).setSign(true);
                    break;
                }
                mDatas.get(i).setSign(false);
            }
        }
        if (mMyFavoritesAdapter == null) {
            mMyFavoritesContent.setAdapter(mMyFavoritesAdapter = new MyFavoritesAdapter(context, mDatas));
            mMyFavoritesAdapter.addOnItemClickListener(new MyFavoritesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BeanAllSymbols.SymbolPrices symbolPrices,int position) {
                    if (checkIndicatorExist(symbolPrices)) {
                        //已经存在就取消
                    } else {
                        //没有存在就加入
                    }
                    mViewPagerAdapter.notifyDataSetChanged();
                    mDatas.get(position).setSign(symbolPrices.getSign());
                    aCache.put(CacheKeyConstant.SUB_SYMBOLS, new Gson().toJson(subSymbols, new TypeToken<LinkedList<BeanIndicatorData>>() {
                    }.getType()));
//                    mHorizontalScrollView.postInvalidate();
                    mHeaderCustomViewPager.setCurrentItem(subSymbols.size());
                }

                @Override
                public void onItemClickGoto(String symbol) {
                    //点击了前往图表
                    for (int i = 0; i < subSymbols.size(); i++) {
                        if (subSymbols.get(i).getSymbol().equals(symbol)) {
                            mHeaderCustomViewPager.setCurrentItem(i);
                            return;
                        }
                    }
                }
            });
        }

    }

    /**
     * 检测增加减少订阅的indicator
     * @param symbolPrices
     * @return
     */
    private boolean checkIndicatorExist(BeanAllSymbols.SymbolPrices symbolPrices) {
        for (BeanIndicatorData beanIndicatorData : subSymbols) {
            if (symbolPrices.getSymbol().equals(beanIndicatorData.getSymbol())) {
                subSymbols.remove(beanIndicatorData);
                return true;
            }
        }
        BeanIndicatorData beanIndicatorData = new BeanIndicatorData(symbolPrices.getSymbol(), symbolPrices.getAsk(), symbolPrices.getBid());
        if(subSymbols.size()<=1) {
            subSymbols.add(subSymbols.size() - 1, beanIndicatorData);
        }
        subSymbols.add(subSymbols.size() - 2, beanIndicatorData);
        Sort();
        return false;
    }

    //给订阅indicator数组排序
    private void Sort() {
        clearAndAddAll(dupSubSymbols, subSymbols);
        subSymbols.clear();
        List<String> symbolsList = new LinkedList<>();
        for (int i = 0; i < dupSubSymbols.size() - 1; i++) {
            symbolsList.add(dupSubSymbols.get(i).getSymbol());
        }
        Collections.sort(symbolsList);
        for (String symbol : symbolsList) {
            for (BeanIndicatorData beanIndicatorData : dupSubSymbols) {
                if (beanIndicatorData.getSymbol().equals(symbol)) {
                    subSymbols.add(beanIndicatorData);
                    break;
                }
            }
        }
        subSymbols.add(dupSubSymbols.get(dupSubSymbols.size() - 1));
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_INDICATOR:
                    if (!isScroll) {
                        View childAt=null;
                        BeanIndicatorData beanIndicatorData=null;
                        for(int i=0;i<subSymbols.size();i++){
                            if(subSymbols.get(i).getSymbol().equals(msg.getData().getString("symbol"))){
                                beanIndicatorData = subSymbols.get(i);
                                break;
                            }
                        }
                        for(int i=0;i<mViewPagerAdapter.getCount();i++){
                            if(mHeaderCustomViewPager.getChildAt(i)!=null) {
                                if (((TextView) mHeaderCustomViewPager.getChildAt(i).findViewById(R.id.id_index_gallery_symbol_name)).getText().toString().equals(msg.getData().getString("symbol"))) {
                                    childAt = mHeaderCustomViewPager.getChildAt(i);
                                    break;
                                }
                            }
                        }
                        if(childAt==null||beanIndicatorData==null){
                            return ;
                        }

                        SpannableString askText = MoneyUtil.getRealTimePriceTextBig(context, beanIndicatorData.getAsk());
                        SpannableString bidText = MoneyUtil.getRealTimePriceTextBig(context, beanIndicatorData.getBid());
                        if (beanIndicatorData.getBidColor() != 0) {
                            bidText.setSpan(new ForegroundColorSpan(beanIndicatorData.getBidColor()), 0, bidText.length(),
                                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                        if (beanIndicatorData.getAskColor() != 0) {
                            askText.setSpan(new ForegroundColorSpan(beanIndicatorData.getAskColor()), 0, askText.length(),
                                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                        ((TextView) childAt.findViewById(R.id.id_index_gallery_item_text_ask)).setText(askText);
                        ((TextView) childAt.findViewById(R.id.id_index_gallery_item_text_bid)).setText(bidText);
                        if (beanIndicatorData.getAskColor() == 0) {
                        } else if (beanIndicatorData.getAskColor() == getResources().getColor(R.color.text_color_price_rise)) {
                            ((BGABadgeImageView) childAt.findViewById(R.id.id_index_gallery_item_image)).showDrawableBadge(BitmapFactory.decodeResource(getResources(), R.mipmap.rise));
                        } else if (beanIndicatorData.getAskColor() == getResources().getColor(R.color.text_color_price_fall)) {
                            ((BGABadgeImageView) childAt.findViewById(R.id.id_index_gallery_item_image)).showDrawableBadge(BitmapFactory.decodeResource(getResources(), R.mipmap.fall));
                        }
//                        mViewPagerAdapter.notifyDataSetChanged();
                    }
                    break;
                case REFRESH_MY_FAVORITES:
//                    BeanAllSymbols.SymbolPrices symbolPrices = mFilterDatas.get(refreshPosition);
//                    SpannableString askTextBig = MoneyUtil.getRealTimePriceTextBig(context, symbolPrices.getBid());
//                    SpannableString bidTextBig = MoneyUtil.getRealTimePriceTextBig(context, symbolPrices.getAsk());
//                    if (symbolPrices.getBidColor() != 0) {
//                        bidTextBig.setSpan(new ForegroundColorSpan(symbolPrices.getBidColor()), 0, bidTextBig.length(),
//                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    }
//                    if (symbolPrices.getAskColor() != 0) {
//                        askTextBig.setSpan(new ForegroundColorSpan(symbolPrices.getAskColor()), 0, askTextBig.length(),
//                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    }
//                    askTextView.setText(askTextBig);
//                    bidTextView.setText(bidTextBig);
                    mMyFavoritesAdapter.notifyItemChanged(msg.arg1,"");
                    break;
                case REFRESH_HISTORY_VIEW://
                    if (TradeDateConstant.count != data.getBarnum()) {
                        wChild = data.getBarnum() * (SystemUtil.dp2px(context, TradeDateConstant.juli + TradeDateConstant.jianju));
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(wChild, h);
                        mHistoryTradeView.setLayoutParams(layoutParams);
                    }
                    if (data.getSymbol().equals(symbol) && data.getPeriod() == DataUtil.selectPeriod(mPeriod)) {
                            Log.i(TAG, "run: 全数据刷新");
                            mHScrollView.scrollTo(wChild, 0);
                            mHistoryTradeView.setHistoryData(data, wChild - w, wChild);
                            animationDrawable.stop();
                            mIvLoading.setVisibility(View.GONE);
//                            mHScrollView.setVisibility(View.VISIBLE);
//                            mDrawPriceView.setVisibility(View.VISIBLE);
                            mllcontent.setVisibility(View.VISIBLE);
                    }
                    break;
                case REFRESH_OPEN_POSITION:

                    if (mOpenAdapter == null) {
                        mTradeContent.setAdapter(mOpenAdapter = new OpenAdapter(context, mBeanOpenList));
                    }
                    mOpenPositionCount.setText(String.format(getString(R.string.num_open_positions), "" + mBeanOpenList.size()));
                    mProfitCount.setText("$" + mProfitCountMoney);
                    mProfitCount.setTextColor(Double.valueOf(mProfitCountMoney) > 0 ? getResources().getColor(R.color.text_color_price_rise) : getResources().getColor(R.color.text_color_price_fall));
                    mOpenAdapter.setData(mBeanOpenList);
                    mOpenAdapter.notifyDataSetChanged();
                    break;
                case REFRESH_PROMPT: {
                    SpannableString ask = MoneyUtil.getRealTimePriceTextBig(context, subSymbols.get(mHeaderCustomViewPager.getCurrentItem()).getAsk());
                    SpannableString bid = MoneyUtil.getRealTimePriceTextBig(context, subSymbols.get(mHeaderCustomViewPager.getCurrentItem()).getBid());
                    if (beanIndicatorData.getBidColor() != 0) {
                        bid.setSpan(new ForegroundColorSpan(beanIndicatorData.getBidColor()), 0, bid.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    if (beanIndicatorData.getAskColor() != 0) {
                        ask.setSpan(new ForegroundColorSpan(beanIndicatorData.getAskColor()), 0, ask.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    mTvPromptAsk.setText(ask);
                    mTvPromptBid.setText(bid);
                }
                    break;
            }
        }
    };
    private final int REFRESH_INDICATOR = 0;
    private final int REFRESH_MY_FAVORITES = 1;
    private final int REFRESH_HISTORY_VIEW = 2;
    private final int REFRESH_OPEN_POSITION = 3;
    private final int REFRESH_PROMPT=4;


    class MyPagerAdapter extends PagerAdapter {


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final BeanIndicatorData info = subSymbols.get(position);
            View view = LayoutInflater.from(context).inflate(R.layout.vp_indicator_head, null);
            BGABadgeImageView isSymbol = (BGABadgeImageView) view.findViewById(R.id.id_index_gallery_item_image);
            view.setTag(info.getImageResource());
            TextView tvLeft = (TextView) view.findViewById(R.id.id_index_gallery_item_text_ask);
            TextView tvRight = (TextView) view.findViewById(R.id.id_index_gallery_item_text_bid);
            TextView tvName = (TextView) view.findViewById(R.id.id_index_gallery_symbol_name);
            SpannableString askTextBig = MoneyUtil.getRealTimePriceTextBig(context, info.getAsk());
            SpannableString bidTextBig = MoneyUtil.getRealTimePriceTextBig(context, info.getBid());
            if (info.getAskColor() != 0) {
                askTextBig.setSpan(new ForegroundColorSpan(info.getAskColor()), 0, askTextBig.length(),
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            if (info.getBidColor() != 0) {
                bidTextBig.setSpan(new ForegroundColorSpan(info.getBidColor()), 0, bidTextBig.length(),
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            tvLeft.setText(askTextBig);
            tvRight.setText(bidTextBig);
            tvName.setText(info.getSymbol());
            isSymbol.setImageResource(info.getImageResource());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToashUtil.showShort(context, info.getSymbol());
                }
            });
            container.addView(view);
            view.setTag(symbol);
            return view;
        }

        @Override
        public int getCount() {
            return subSymbols.size();
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

    private Map<String,BeanIndicatorData> realTimeIndicatorDataMap=new TreeMap<>();

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void getRealTimeData(RealTimeDataList realTimeDataList) {
        Log.i(TAG, "getRealTimeData: ");
        for (RealTimeDataList.BeanRealTime beanRealTime : realTimeDataList.getQuotes()) {
            //维护最新的实时数据列表
            saveRealTimeDataMap(beanRealTime);
            //indicator数据收集
            if(subSymbols!=null) {
                for (BeanIndicatorData subSymbol : subSymbols) {
                    if (beanRealTime.getSymbol().equals(subSymbol.getSymbol())) {
                        realTimeIndicatorDataMap.put(beanRealTime.getSymbol(), new BeanIndicatorData(beanRealTime.getSymbol(), String.valueOf(beanRealTime.getAsk()), String.valueOf(beanRealTime.getBid())));
                    }
                }
            }
            if (beanRealTime.getSymbol().equals(symbol)) {
                //说明是本标题的数据
                //刷新k线图
                realTimeHistoryData(beanRealTime);
                //刷新当前symbol的持有仓数据
                realTimeOpenPositionData(beanRealTime);
                //刷新当前顶部提示栏数据
                realTimePromptData(beanRealTime);
            }
        }
        //刷新tag数据
        realTimeIndicator(realTimeIndicatorDataMap);
        if (mMyFavorites.getVisibility() == View.VISIBLE) {
            //刷新我的收藏
            refreshMyFavorites(realTimeDataList.getQuotes());
        }
    }

    private void realTimePromptData(RealTimeDataList.BeanRealTime beanRealTime) {
//      MyApplication.getInstance().beanIndicatorData.setAskColor(Double.valueOf(MyApplication.getInstance().beanIndicatorData.getAsk())<beanRealTime.getAsk()?getResources().getColor(R.color.text_color_price_fall):getResources().getColor(R.color.text_color_price_rise));
//      MyApplication.getInstance().beanIndicatorData.setBidColor(Double.valueOf(MyApplication.getInstance().beanIndicatorData.getBid())<beanRealTime.getBid()?getResources().getColor(R.color.text_color_price_fall):getResources().getColor(R.color.text_color_price_rise));
//      MyApplication.getInstance().beanIndicatorData.setAsk(String.valueOf(beanRealTime.getAsk()));
//      MyApplication.getInstance().beanIndicatorData.setBid(String.valueOf(beanRealTime.getBid()));
        handler.sendEmptyMessage(REFRESH_PROMPT);
    }

    private void saveRealTimeDataMap(RealTimeDataList.BeanRealTime beanRealTime) {
        BeanAllSymbols.SymbolPrices symbolPrices = new BeanAllSymbols.SymbolPrices();
        symbolPrices.setSymbol(beanRealTime.getSymbol());
        symbolPrices.setAsk(String.valueOf(beanRealTime.getAsk()));
        symbolPrices.setBid(String.valueOf(beanRealTime.getBid()));
        realTimeMap.put(beanRealTime.getSymbol(), symbolPrices);
    }

    /**
     * 刷新当前持有仓的数据
     *
     * @param beanRealTime
     */
    DiffUtil.DiffResult diffResult;

    private void realTimeOpenPositionData(RealTimeDataList.BeanRealTime beanRealTime) {
        if (mBeanOpenList == null)
            return;
        //复制数据
        mBeanDupOpenList = (new Gson().fromJson(new Gson().toJson(mBeanOpenList), new TypeToken<List<BeanOpenPosition.DataBean.ListBean>>() {
        }.getType()));
        BeanOpenPosition.DataBean.ListBean bean;
        for (int i = 0; i < mBeanOpenList.size(); i++) {
            bean = mBeanOpenList.get(i);
            bean.setProfit(DataUtil.getProfit(beanRealTime, bean.getCmd(), bean.getOpenprice(), bean.getVolume()));
            if (bean.getCmd().equals("buy")) {
                bean.setPrice(String.valueOf(beanRealTime.getBid()));
            } else {
                bean.setPrice(String.valueOf(beanRealTime.getAsk()));
            }
        }
        diffResult = DiffUtil.calculateDiff(new OpenPositionDiff(mBeanDupOpenList, mBeanOpenList), true);
        ThreadHelper.instance().runOnUiThread(mRunnable);
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mProfitCount.setText("$" + cacleProfitCount());
            mProfitCount.setTextColor(Double.valueOf(mProfitCountMoney) > 0 ? getResources().getColor(R.color.text_color_price_rise) : getResources().getColor(R.color.text_color_price_fall));
            diffResult.dispatchUpdatesTo(mOpenAdapter);
        }
    };

    private String cacleProfitCount() {
        mProfitCountMoney = "0";
        for (BeanOpenPosition.DataBean.ListBean listBean : mBeanOpenList) {
            mProfitCountMoney = MoneyUtil.addPrice(mProfitCountMoney, listBean.getProfit()==""?"0":listBean.getProfit());
        }
        return mProfitCountMoney;
    }

    @Subscribe
    public void enterOrder(BeanOpenPosition beanOpenPosition) {
        Log.i(TAG, "enterOrder: 下单成功，通知更新开仓数据");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mMainTradeContentPre.requestOpenPosition();
    }
    @Subscribe
    public void notificationEditPostion(NotificationEditPosition notificationEditPosition){
            for (int i = 0; i < mBeanOpenList.size(); i++) {
                if (notificationEditPosition.getOrder() == mBeanOpenList.get(i).getOrder()) {
                    if(notificationEditPosition.getSl()!=null)
                    mBeanOpenList.get(i).setSl(notificationEditPosition.getSl());
                    if(notificationEditPosition.getTp()!=null)
                        mBeanOpenList.get(i).setTp(notificationEditPosition.getTp());
                    mOpenAdapter.notifyItemChanged(i);
                }
            }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notificationClosePosition(NotificationClosePosition notificationClosePosition){
        if(notificationClosePosition.getOrder()!=0){
            for (int i = 0; i < mBeanOpenList.size(); i++) {
                if (notificationClosePosition.getOrder() == mBeanOpenList.get(i).getOrder()) {
                    mBeanOpenList.remove(i);
                    mOpenAdapter.notifyItemRemoved(i);
                    mOpenAdapter.notifyItemRangeChanged(0,mBeanOpenList.size());
                }
            }
        }
    }


    /**
     * 处理实时数据对历史图的影响
     */
    String[] mPeriodStrings=new String[]{"1","5","15","30","60","240","1440"};
    private void realTimeHistoryData(final RealTimeDataList.BeanRealTime beanRealTime) {
        if (data != null) {
            int period = data.getPeriod();
            //k线图数据
            //由于java的复制只是赋予指针，所以，当View里面的data改变，map里面存的值也会改变
            if (data.getSymbol().equals(symbol) && beanRealTime.getSymbol().equals(symbol) && DataUtil.selectPeriod(mPeriod) == period) {
                        mHistoryTradeView.refreshRealTimePrice(beanRealTime);
                Log.i(TAG, "刷新K线图: ");
            }else{
                //遍历数据列表。根据事实值改变
                for(String mPeriod:mPeriodStrings){
                    if(mHistoryDataMap.containsKey(DataUtil.symbolConnectPeriod(beanRealTime.getSymbol(),mPeriod))){
                        BeanHistory.BeanHistoryData beanHistoryData = mHistoryDataMap.get(DataUtil.symbolConnectPeriod(beanRealTime.getSymbol(), mPeriod));
                        BeanHistory.BeanHistoryData.HistoryItem historyItem = beanHistoryData.getList().get(beanHistoryData.getBarnum()-1);
                        if (DateUtils.getOrderStartTime(beanRealTime.getTime()) < DateUtils.getOrderStartTime(historyItem.getTime()) + beanHistoryData.getPeriod() * 60 * 1000) {
                            //还属于最后一个item的时间范围
                            if (historyItem.getHigh() < beanRealTime.getBid()) {
                                historyItem.setHigh(beanRealTime.getBid());
                            }
                            if (beanRealTime.getBid() < historyItem.getLow()) {
                                historyItem.setLow(beanRealTime.getBid());
                            }
                            historyItem.setClose(beanRealTime.getBid());
                        } else {
                            //不属于最有一个item的时间范围。数据删除第一个时间点，在最后增加一个
                            beanHistoryData.getList().remove(0);
                            BeanHistory.BeanHistoryData.HistoryItem lastItem = new BeanHistory().new BeanHistoryData().new HistoryItem();
                            lastItem.setOpen(beanRealTime.getBid());
                            lastItem.setClose(beanRealTime.getBid());
                            lastItem.setHigh(beanRealTime.getBid());
                            lastItem.setLow(beanRealTime.getBid());
                            lastItem.setTime(beanRealTime.getTime());
                            lastItem.setQuoteTime((int) DateUtils.getOrderStartTime(beanRealTime.getTime()));
                            beanHistoryData.getList().add(lastItem);
                        }
                    }
                }
                //不是当前显示，则存起来
            }
        }
    }

    /**
     * 判断数据是否有区别
     *
     * @param historyItem
     * @param beanRealTime
     * @return
     */
    private void historyItemChange(BeanHistory.BeanHistoryData.HistoryItem historyItem, RealTimeDataList.BeanRealTime beanRealTime) {

    }

    private TextView bidTextView;
    private TextView askTextView;
    private int refreshPosition = -1;

    /**
     * 处理实时数据对收藏夹的更新
     *
     * @param quotes
     */
    private void refreshMyFavorites(List<RealTimeDataList.BeanRealTime> quotes) {
        for (RealTimeDataList.BeanRealTime quote : quotes) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) (mMyFavoritesContent.getLayoutManager());
            firstItemPosition = layoutManager.findFirstVisibleItemPosition();
            lastItemPosition = layoutManager.findLastVisibleItemPosition();
            for (int i = firstItemPosition; i < lastItemPosition; i++) {
                BeanAllSymbols.SymbolPrices symbolPrices = mFilterDatas.get(i);
                if (symbolPrices.getSymbol().equals(quote.getSymbol())) {
                    symbolPrices.setAskColor(getResources().getColor(Double.valueOf(quote.getAsk()) > Double.valueOf(symbolPrices.getAsk()) ? R.color.text_color_price_rise : R.color.text_color_price_fall));
                    symbolPrices.setBidColor(getResources().getColor(Double.valueOf(quote.getBid()) > Double.valueOf(symbolPrices.getBid()) ? R.color.text_color_price_rise : R.color.text_color_price_fall));
                    symbolPrices.setAsk(String.valueOf(quote.getAsk()));
                    symbolPrices.setBid(String.valueOf(quote.getBid()));
//                    View childAt = (MyFavoritesAdapter.MyFavoritesHolder)(mMyFavoritesContent.getLayoutManager().getChildAt(i));

//                    if(mMyFavoritesContent.getLayoutManager().getChildAt(i)!=null) {
//                        bidTextView = (TextView) (mMyFavoritesContent.getLayoutManager().getChildAt(i).findViewById(R.id.bid));
//                    }
//                    if(mMyFavoritesContent.getLayoutManager().getChildAt(i).findViewById(R.id.ask)!=null)
//                        askTextView = (TextView) (mMyFavoritesContent.getLayoutManager().getChildAt(i).findViewById(R.id.ask));
                    refreshPosition = i;
                    Log.i(TAG, "REFRESH_MY_FAVORITES: " + i);
                    Message message=new Message();
                    message.what=REFRESH_MY_FAVORITES;
                    message.arg1=i;
                    handler.sendMessage(message);
                    break;
                }
            }
        }
//        diffResult = DiffUtil.calculateDiff(new MyFavoritesDiffCallBack(mDupFilterDatas, mFilterDatas), true);
    }

}
