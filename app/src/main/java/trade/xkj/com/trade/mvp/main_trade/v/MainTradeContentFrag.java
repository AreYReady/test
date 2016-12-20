package trade.xkj.com.trade.mvp.main_trade.v;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.MoneyUtil;
import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.Utils.ToashUtil;
import trade.xkj.com.trade.Utils.view.CustomViewPager;
import trade.xkj.com.trade.Utils.view.DrawPriceView;
import trade.xkj.com.trade.Utils.view.FixedSpeedScroller;
import trade.xkj.com.trade.Utils.view.HistoryTradeView;
import trade.xkj.com.trade.Utils.view.MyHorizontalScrollView2;
import trade.xkj.com.trade.Utils.view.ZoomOutPageTransformer;
import trade.xkj.com.trade.adapter.OpenAdapter;
import trade.xkj.com.trade.base.BaseFragment;
import trade.xkj.com.trade.bean.BeanDrawPriceData;
import trade.xkj.com.trade.bean.BeanIndicatorData;
import trade.xkj.com.trade.bean.BeanOpenPositionData;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.constant.TradeDateConstant;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPre;
import trade.xkj.com.trade.mvp.main_trade.p.MainTradeContentPreImpl;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by admin on 2016-11-22.
 */

public class MainTradeContentFrag extends BaseFragment implements MainTradeContentLFragListener {
    private MainTradeContentPre mMainTradeContentPre;
    private MyHorizontalScrollView2 mHScrollView;
    private HistoryTradeView mHistoryTradeView;
    private LinearLayout ll;
    private Context context;
    private int h;
    private int w;
    private float currentScaleSize=1f;
    private float previousScaleSize=1f;
    private int wChild;
    private CustomViewPager mHeaderCustomViewPager;
    private List<BeanIndicatorData> mIndicatorDatas;
    private DrawPriceView mDrawPriceView;
    private ViewpagerAdapter mViewPagerAdapter;
    private FixedSpeedScroller scroller;
    private int mPosition;
    private RecyclerView mTradeContent;
    private List<BeanOpenPositionData> mBeanOpenList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_trade_content, container, false);
        return view;
    }


    @RequiresApi(api = M)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        mHScrollView.setScrollViewListener(new MyHorizontalScrollView2.ScrollViewListener()

                                           {
                                               int mX = 0;
                                               int mY = 0;
                                               int z = SystemUtil.dp2px(context, TradeDateConstant.juli + TradeDateConstant.jianju);

                                               @Override
                                               public void onScrollChanged(MyHorizontalScrollView2 scrollView, int x, int y,
                                                                           int oldx, int oldy) {
                                                   if (x != oldx) {
                                                       if (Math.abs(x - mX) >= z) {
                                                           mX = x;
                                                           mHistoryTradeView.postInvalidate(x, 0, x + w, h,currentScaleSize);
                                                       }
                                                   } else {
                                                       mX = x;
                                                       mY = y;
                                                   }
                                               }

                                               @Override
                                               public void onScaleDraw(float scaleSize) {
                                                   currentScaleSize=scaleSize;
                                                   Log.i(TAG, "onScaleDraw: previousScaleSize "+previousScaleSize +"   scaleSize "+scaleSize);
                                                   LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)(wChild*scaleSize), h);
                                                   mHScrollView.smoothScrollTo((int)(mX*(scaleSize/previousScaleSize)),0);
                                                   mHistoryTradeView.setLayoutParams(layoutParams);
                                                   previousScaleSize=scaleSize;
                                                   mHistoryTradeView.postInvalidate((int)(mX*(scaleSize/previousScaleSize)), 0, (int)(mX*(scaleSize/previousScaleSize)) + w, h,currentScaleSize);
                                               }
                                           }

        );

        mTradeContent=(RecyclerView)view.findViewById(R.id.rv_trade_content);
        mTradeContent.setLayoutManager(new LinearLayoutManager(context));
        mTradeContent.setAdapter(new OpenAdapter(context,mBeanOpenList));
        mTradeContent.setFocusable(false);
    }

    @Override
    protected void initView() {
        mHeaderCustomViewPager = (CustomViewPager) view.findViewById(R.id.vp_indicator_trade_content);
        mHScrollView = (MyHorizontalScrollView2) view.findViewById(R.id.hsv_trade);
        mDrawPriceView = (DrawPriceView) view.findViewById(R.id.dp_draw_price);
        ll = (LinearLayout) view.findViewById(R.id.ll);

    }

    @Override
    protected void initData() {
        context = this.getActivity();
        mMainTradeContentPre = new MainTradeContentPreImpl(this, mHandler);
        mMainTradeContentPre.loading();
        mBeanOpenList=new ArrayList<>();
        for(int i=0;i<=20;i++){
            mBeanOpenList.add(new BeanOpenPositionData());
        }
    }

    HistoryDataList data;

    @Override
    public void freshView(final HistoryDataList data) {
        Log.i(TAG, "freshView: ");
        this.data = data;
        initScrollView();
    }

    @Override
    public void refreshIndicator(ArrayList<BeanIndicatorData> mBeanIndicatorDataArrayList) {
        mIndicatorDatas = mBeanIndicatorDataArrayList;
        mViewPagerAdapter = new ViewpagerAdapter();
        mHeaderCustomViewPager.setAdapter(mViewPagerAdapter);
        mHeaderCustomViewPager.setOffscreenPageLimit(mIndicatorDatas.size());
        mHeaderCustomViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
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

            }
        });
        setViewPagerSpeed(250);
    }

private Handler handler=new Handler(){};
    private void initScrollView() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHScrollView.scrollTo(wChild, 0);
                mHistoryTradeView.setHistoryData(data, wChild - w, wChild);
            }
        }, 1000);
    }

    class ViewpagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final BeanIndicatorData info = mIndicatorDatas.get(position);
            View view = LayoutInflater.from(context).inflate(R.layout.vp_indicator_head, null);
            ImageView isSymbol = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
            view.setTag(info.getImageResource());
            TextView tvLeft = (TextView) view.findViewById(R.id.id_index_gallery_item_text_left);
            TextView tvRight = (TextView) view.findViewById(R.id.id_index_gallery_item_text_right);
            TextView tvName =(TextView)view.findViewById(R.id.id_index_gallery_symbol_name);
//            tvLeft.setText(info.getLeftString());
//            tvRight.setText(info.getRightString());
            tvLeft.setText(MoneyUtil.getRealTimePriceTextBig(context,"1.90031"));
            tvRight.setText(MoneyUtil.getRealTimePriceTextBig(context,"1.90031"));
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
}
