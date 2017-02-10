package trade.xkj.com.trade.mvp.main_trade.v;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bingoogolapple.badgeview.BGABadgeImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import trade.xkj.com.trade.R;
import trade.xkj.com.trade.adapter.MyFavoritesAdapter;
import trade.xkj.com.trade.adapter.MyViewPagerAdapterItem;
import trade.xkj.com.trade.adapter.OpenAdapter;
import trade.xkj.com.trade.base.BaseFragment;
import trade.xkj.com.trade.bean.BeanDrawPriceData;
import trade.xkj.com.trade.bean.BeanDrawRealTimePriceData;
import trade.xkj.com.trade.bean.BeanIndicatorData;
import trade.xkj.com.trade.bean.BeanOpenPositionData;
import trade.xkj.com.trade.bean.RealTimeDataList;
import trade.xkj.com.trade.bean_.BeanAllSymbols;
import trade.xkj.com.trade.bean_.BeanHistory;
import trade.xkj.com.trade.constant.CacheKeyConstant;
import trade.xkj.com.trade.constant.TradeDateConstant;
import trade.xkj.com.trade.diffcallback.MyFavoritesDiffCallBack;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPreListenerImpl;
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
import trade.xkj.com.trade.utils.view.WrapContentLinearLayoutManager;
import trade.xkj.com.trade.utils.view.ZoomOutPageTransformer;

import static android.R.attr.x;
import static android.os.Build.VERSION_CODES.M;
import static trade.xkj.com.trade.R.string.search;

/**
 * Created by hsc on 2016-11-22.
 * TODO:展示k线图的fragment;
 */

public class MainTradeContentFrag extends BaseFragment implements MainTradeListener.MainTradeContentLFragListener {
    private MainTradeListener.MainTradeContentPreListener mMainTradeContentPre;
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
    private List<BeanOpenPositionData> mBeanOpenList;
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
    private int firstItemPosition;
    private int lastItemPosition;


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
                wChild = (TradeDateConstant.count) * (SystemUtil.dp2px(context, TradeDateConstant.juli + TradeDateConstant.jianju));
                //太过要求精确计算，最后一个item显示半个。所以宽度加10个像素，修正这个算法缺失
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(wChild+10, h);
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
        mTradeContent.setLayoutManager(new LinearLayoutManager(context));
        mTradeContent.setAdapter(new OpenAdapter(context, mBeanOpenList));
        mTradeContent.setFocusable(false);

        final View viewTitle = view.findViewById(R.id.trade_title_hint);
        ((MyScrollView) view.findViewById(R.id.msv_my_scroll_view)).registerListener(new MyScrollView.ScrollListener() {
            @Override
            public void onScroll(int top, int left) {
                if (top - view.findViewById(R.id.rl_trade).getTop() > 0 && viewTitle.getVisibility() != View.VISIBLE) {
                    viewTitle.setVisibility(View.VISIBLE);
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

        mMyFavoritesIndicator.setAdapter(new MyViewPagerAdapterItem(context, mMyFavoritesTitle));
        mMyFavoritesIndicator.setOffscreenPageLimit(mMyFavoritesTitle.size());
        mMyFavoritesIndicator.setPageTransformer(true, new ZoomOutPageTransformer());
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
        mMainTradeContentPre.loadingSubSymbols(symbolsName, true);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(view.getMeasuredHeight()!=0) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Log.i(TAG, "onGlobalLayout: " + view.getMeasuredHeight());

                }
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
    }

    @Override
    protected void initData() {
        context = this.getActivity();
        mMainTradeContentPre = new MainTradeContentPreListenerImpl(this, mHandler, getContext());
        mBeanOpenList = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            mBeanOpenList.add(new BeanOpenPositionData());
        }
        if (aCache == null) {
            aCache = ACache.get(context);
        }

        BeanIndicatorData beanIndicatorData;
        if (aCache.getAsString(CacheKeyConstant.SUB_SYMBOLS) == null) {
            LinkedList<BeanIndicatorData> symbols = new LinkedList<BeanIndicatorData>();
            beanIndicatorData = new BeanIndicatorData("EURUSD", "1.03561", "1.03151");
            symbols.add(beanIndicatorData);
            beanIndicatorData = new BeanIndicatorData("EURJPY", "1.03561", "1.03151");
            symbols.add(beanIndicatorData);
            beanIndicatorData = new BeanIndicatorData("GBPUSD", "1.03561", "1.03151");
            symbols.add(beanIndicatorData);
            beanIndicatorData = new BeanIndicatorData("XAUUSD", "1.03561", "1.03151");
            symbols.add(beanIndicatorData);
            beanIndicatorData = new BeanIndicatorData(getResources().getString(R.string.my_favorites), "", "");
            symbols.add(beanIndicatorData);
            aCache.put(CacheKeyConstant.SUB_SYMBOLS, new Gson().toJson(symbols, new TypeToken<LinkedList<BeanIndicatorData>>() {
            }.getType()));
        }
        mMyFavoritesTitle = new ArrayList<>();
        mMyFavoritesTitle.add(getResources().getString(search));
        mMyFavoritesTitle.add(getResources().getString(R.string.all));
        mMyFavoritesTitle.add(getResources().getString(R.string.favorites_num));
    }

    BeanHistory.BeanHistoryData data;

    @Override
    public void freshView(final BeanHistory.BeanHistoryData data, boolean isCountDown) {

        Log.i(TAG, "freshView: ");
        if (mHeaderCustomViewPager.getCurrentItem() == subSymbols.size() - 1) {
            Log.i(TAG, "freshView: 我的收藏夹");
        } else if (data.getSymbol().equals(symbol) && data.getPeriod() == DataUtil.selectPeriod(mPeriod)) {
            this.data = data;
            Log.i(TAG, "freshView: data");
            this.isCountDown = isCountDown;
            handler.sendEmptyMessage(REFRESH_HISTORY_VIEW);
            saveCache();
        }
    }

    private boolean isCountDown = false;

    private int refreshIndicatorIndex;

    @Override
    public void refreshIndicator(List<BeanIndicatorData> mBeanIndicatorDataList) {
        for (BeanIndicatorData newData : mBeanIndicatorDataList) {
            for (int i = mHeaderCustomViewPager.getCurrentItem() - 1; i < mHeaderCustomViewPager.getCurrentItem() + 1; i++) {
                if (i >= 0 && i < mHeaderCustomViewPager.getChildCount() - 1) {
                    BeanIndicatorData subData = subSymbols.get(i);
                    if (newData.getSymbol().equals(subData.getSymbol())) {
                        subData.setAskColor(getResources().getColor(Double.valueOf(newData.getAsk()) > Double.valueOf(subData.getAsk()) ? R.color.text_color_price_rise : R.color.text_color_price_fall));
                        subData.setBidColor(getResources().getColor(Double.valueOf(newData.getBid()) > Double.valueOf(subData.getBid()) ? R.color.text_color_price_rise : R.color.text_color_price_fall));
                        subData.setAsk(newData.getAsk());
                        subData.setBid(newData.getBid());
                        refreshIndicatorIndex = i;
                        handler.sendEmptyMessage(REFRESH_INDICATOR);
                    }
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
        mHeaderCustomViewPager.setOffscreenPageLimit(subSymbols.size());
        mHeaderCustomViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mMainTradeContentPre.loadingHistoryData(symbol = subSymbols.get(0).getSymbol(), mPeriod, TradeDateConstant.count);
        fillPromptSymbolInfo(0);
        //申请当前所有交易的当前报价单储存
        mMainTradeContentPre.loadingHistoryData(null, null, 0);
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
                        Log.i(TAG, "onPageScrollStateChanged:+SCROLL_STATE_IDLE " + mPosition);
                        if (mPosition == subSymbols.size() - 1) {
                            showMyFavorites();

                        } else {
                            Log.i(TAG, "onPageScrollStateChanged: 加载数据");
                            if (allSymbolsName.size() > 0) {
                                for (BeanIndicatorData subSymbol : subSymbols) {
                                    allSymbolsName.remove(subSymbol.getSymbol());
                                }
                                mMainTradeContentPre.loadingSubSymbols(allSymbolsName, false);
                            }
                            if (mKLinkLayout.getVisibility() != View.VISIBLE) {
                                mKLinkLayout.setVisibility(View.VISIBLE);
                            }
                            if (mMyFavorites.getVisibility() != View.GONE) {
                                mMyFavorites.setVisibility(View.GONE);
                            }
                            fillPromptSymbolInfo(mPosition);
                            mMainTradeContentPre.loadingHistoryData(symbol, mPeriod, TradeDateConstant.count);
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
        BeanAllSymbols beanAllSymbols = new Gson().fromJson(ACache.get(context).getAsString(CacheKeyConstant.ALL_SYMBOLS_PRICES), new TypeToken<BeanAllSymbols>() {
        }.getType());
        allSymbolsName.clear();
        for (int i = 0; i < beanAllSymbols.getData().size(); i++) {
            allSymbolsName.add(beanAllSymbols.getData().get(i).getSymbol());
        }
        mMainTradeContentPre.loadingSubSymbols(allSymbolsName, true);
        mKLinkLayout.setVisibility(View.GONE);
        mMyFavorites.setVisibility(View.VISIBLE);
        mDatas = beanAllSymbols.getData();

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
            mMyFavoritesContent.setAdapter(mMyFavoritesAdapter = new MyFavoritesAdapter(context, mFilterDatas));
            mMyFavoritesAdapter.addOnItemClickListener(new MyFavoritesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BeanAllSymbols.SymbolPrices symbolPrices) {
                    if (checkIndicatorExist(symbolPrices)) {
                        //已经存在就取消
                    } else {
                        //没有存在就加入
                    }
                    aCache.put(CacheKeyConstant.SUB_SYMBOLS, new Gson().toJson(subSymbols, new TypeToken<LinkedList<BeanIndicatorData>>() {
                    }.getType()));
                    mViewPagerAdapter.notifyDataSetChanged();
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

    private boolean checkIndicatorExist(BeanAllSymbols.SymbolPrices symbolPrices) {
        for (BeanIndicatorData beanIndicatorData : subSymbols) {
            if (symbolPrices.getSymbol().equals(beanIndicatorData.getSymbol())) {
                subSymbols.remove(beanIndicatorData);
                return true;
            }
        }
        BeanIndicatorData beanIndicatorData = new BeanIndicatorData(symbolPrices.getSymbol(), symbolPrices.getAsk(), symbolPrices.getBid());
        subSymbols.add(subSymbols.size() - 2, beanIndicatorData);
        Sort();
        return false;
    }

    //给数组排序
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
                        Log.i(TAG, "handleMessage: 0" + "  " + refreshIndicatorIndex);
                        View childAt = mHeaderCustomViewPager.getChildAt(refreshIndicatorIndex);
                        BeanIndicatorData beanIndicatorData = subSymbols.get(refreshIndicatorIndex);
                        SpannableString askTextBig = MoneyUtil.getRealTimePriceTextBig(context, beanIndicatorData.getBid());
                        SpannableString bidTextBig = MoneyUtil.getRealTimePriceTextBig(context, beanIndicatorData.getAsk());
                        if (beanIndicatorData.getBidColor() != 0) {
                            bidTextBig.setSpan(new ForegroundColorSpan(beanIndicatorData.getBidColor()), 0, bidTextBig.length(),
                                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                        if (beanIndicatorData.getAskColor() != 0) {
                            askTextBig.setSpan(new ForegroundColorSpan(beanIndicatorData.getAskColor()), 0, askTextBig.length(),
                                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                        ((TextView) childAt.findViewById(R.id.id_index_gallery_item_text_ask)).setText(askTextBig);
                        ((TextView) childAt.findViewById(R.id.id_index_gallery_item_text_bid)).setText(bidTextBig);
//                        mViewPagerAdapter.notifyDataSetChanged();
                    }
                    break;
                case REFRESH_MY_FAVORITES:
                    BeanAllSymbols.SymbolPrices symbolPrices = mFilterDatas.get(refreshPosition);
                    SpannableString askTextBig = MoneyUtil.getRealTimePriceTextBig(context, symbolPrices.getBid());
                    SpannableString bidTextBig = MoneyUtil.getRealTimePriceTextBig(context, symbolPrices.getAsk());
                    if (symbolPrices.getBidColor() != 0) {
                        bidTextBig.setSpan(new ForegroundColorSpan(symbolPrices.getBidColor()), 0, bidTextBig.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    if (symbolPrices.getAskColor() != 0) {
                        askTextBig.setSpan(new ForegroundColorSpan(symbolPrices.getAskColor()), 0, askTextBig.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    askTextView.setText(askTextBig);
                    bidTextView.setText(bidTextBig);
                    break;
                case REFRESH_HISTORY_VIEW://
                    if (TradeDateConstant.count != data.getBarnum()) {
                        wChild = data.getBarnum() * (SystemUtil.dp2px(context, TradeDateConstant.juli + TradeDateConstant.jianju));
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
                    break;
            }
        }
    };
    private final int REFRESH_INDICATOR = 0;
    private final int REFRESH_MY_FAVORITES = 1;
    private final int REFRESH_HISTORY_VIEW = 2;


    class MyPagerAdapter extends PagerAdapter {
        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {

            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final BeanIndicatorData info = subSymbols.get(position);
            View view = LayoutInflater.from(context).inflate(R.layout.vp_indicator_head, null);
            BGABadgeImageView isSymbol = (BGABadgeImageView) view.findViewById(R.id.id_index_gallery_item_image);
            isSymbol.showDrawableBadge(BitmapFactory.decodeResource(getResources(), R.drawable.toggle_switch));
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

    private ArrayList<BeanIndicatorData> realTimeIndicatorData = new ArrayList<>();

    @Subscribe
    public void getRealTimeData(RealTimeDataList realTimeDataList) {
        Log.i(TAG, "getRealTimeData: ");

        realTimeIndicatorData.clear();
        for (RealTimeDataList.BeanRealTime beanRealTime : realTimeDataList.getQuotes()) {
            //indicator数据收集
            for (BeanIndicatorData subSymbol : subSymbols) {
                if (beanRealTime.getSymbol().equals(subSymbol.getSymbol())) {
                    realTimeIndicatorData.add(new BeanIndicatorData(beanRealTime.getSymbol(), String.valueOf(beanRealTime.getAsk()), String.valueOf(beanRealTime.getBid())));
                }
            }
        }
        refreshIndicator(realTimeIndicatorData);
        if (mMyFavorites.getVisibility() == View.VISIBLE) {
            //刷新我的收藏
            refreshMyFavorites(realTimeDataList.getQuotes());
        }
        //刷新k线图
        realTimeHistoryData(realTimeDataList);

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

    /**
     * 处理实时数据对历史图的影响
     *
     * @param realTimeDataList
     */
    private void realTimeHistoryData(RealTimeDataList realTimeDataList) {
        int period = data.getPeriod();
        for (RealTimeDataList.BeanRealTime beanRealTime : realTimeDataList.getQuotes()) {
            //k线图数据
            if (data.getSymbol().equals(symbol) && beanRealTime.getSymbol().equals(symbol) && DataUtil.selectPeriod(mPeriod) == period) {
                mHistoryTradeView.refreshRealTimePrice(beanRealTime);
//
//                isCountDown=true;
//                handler.sendEmptyMessage(REFRESH_HISTORY_VIEW);
            } else {
                //2:非当前显示的。则获取最后一个修改缓存
                //不考虑流量，我们先做实时更新，后续再去完成缓存
            }
        }
    }

    /**
     * 判断数据是否有区别
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
                    bidTextView = (TextView) (mMyFavoritesContent.getLayoutManager().getChildAt(i).findViewById(R.id.bid));
                    askTextView = (TextView) (mMyFavoritesContent.getLayoutManager().getChildAt(i).findViewById(R.id.ask));
                    refreshPosition = i;
                    Log.i(TAG, "REFRESH_MY_FAVORITES: " + i);
                    handler.sendEmptyMessage(REFRESH_MY_FAVORITES);
                    break;
                }
            }
        }
//        diffResult = DiffUtil.calculateDiff(new MyFavoritesDiffCallBack(mDupFilterDatas, mFilterDatas), true);
    }

}
