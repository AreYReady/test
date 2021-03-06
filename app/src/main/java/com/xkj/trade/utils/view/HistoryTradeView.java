package com.xkj.trade.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xkj.trade.R;
import com.xkj.trade.bean.BeanDrawPriceData;
import com.xkj.trade.bean.BeanDrawRealTimePriceData;
import com.xkj.trade.bean.RealTimeDataList;
import com.xkj.trade.bean_.BeanHistory;
import com.xkj.trade.constant.TradeDateConstant;
import com.xkj.trade.utils.DataUtil;
import com.xkj.trade.utils.DateUtils;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ROUND_DOWN;

/**
 * Created by admin on 2016-11-23.
 */

public class HistoryTradeView extends View {
    private String TAG = SystemUtil.getTAG(this);
    private Boolean isReady = false;
    private Paint mfallPaint;
    private Context mContext;
    private Paint mRisePaint;
    private Paint mGarkPaint;
    private Paint mSilverPaint;

    private List<BeanDrawPriceData> mDrawPriceDataList;
    /**
     * @param data
     */
    private long dataBeginTime;
    private long dataStopTime;
    /**
     * 0是最小,1是最大
     */
    private double[] price;
    /**
     * 单位dip的价格差
     */
    private double unit;
    //每条数据占得px大小
    private float unitDataIndex;
    private BeanHistory.BeanHistoryData data;
    private BeanHistory showData;
    //两个数据绘图的空隙,默认3dip
    private float dataViewSpace = 3;
    //单个数据所占的空间
    private float simpleDataSpace = 0;
    private float showDataLeftX;
    private float showDataRightX;
    private double openPrice;
    private double closePrice;
    private double maxPrice;
    private double minPrice;
    private float yTop;
    private float yBottom;
    private float yMaxTop;
    private float yMinBottom;
    private float startX;
    private float stopX;
    private int digits;
    //放大倍数,默认1为正常
    private float scaleSize = 1;
    private long showBeginTime;
    private int indexCount = TradeDateConstant.count;
    private int period;
    private Paint mDashedPaint;
    private int realPriceColor=R.color.text_color_price_fall;
    /**
     * 每单位px的秒数
     */
    private float unitSpace;
    /**
     * 展示数据的区域高度
     */
    private float showDataHeight;
    /**
     * 最高价和最低价的差价
     */
    private double blance;
    private DrawPriceListener mDrawPriceListener;
    private BeanDrawPriceData mBeanDrawPriceData;
    public HistoryTradeView(Context context) {
        this(context, null);
    }

    public HistoryTradeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setDrawPriceListener(DrawPriceListener drawPriceListener) {
        mDrawPriceListener = drawPriceListener;
    }


    public interface DrawPriceListener {
        void drawPriceData(List<BeanDrawPriceData> drawPriceData);
    }


    public HistoryTradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mfallPaint = new Paint();
        mfallPaint.setStyle(Paint.Style.FILL);
        mfallPaint.setColor(getResources().getColor(R.color.text_color_price_fall));
        mfallPaint.setStrokeWidth(3);
        mRisePaint = new Paint();
        mRisePaint.setStyle(Paint.Style.FILL);
        mRisePaint.setColor(getResources().getColor(R.color.text_color_price_rise));
        mRisePaint.setStrokeWidth(3);
        mSilverPaint = new Paint();
        mSilverPaint.setStyle(Paint.Style.FILL);
        mSilverPaint.setColor(getResources().getColor(R.color.text_color_primary_disabled_dark));
        mSilverPaint.setStrokeWidth(3);

        mGarkPaint = new Paint();
        mGarkPaint.setColor(getResources().getColor(R.color.text_color_primary_dark_with_opacity));
        mGarkPaint.setStrokeWidth(3);
        mGarkPaint.setTextAlign(Paint.Align.CENTER);
        mGarkPaint.setTextSize(SystemUtil.dp2pxFloat(mContext, 10));
        mDashedPaint=new Paint();
        mDashedPaint.setStyle(Paint.Style.FILL);
        mDashedPaint.setStrokeWidth(3);
        mDrawPriceDataList = new ArrayList<>();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data != null) {
            decodeHistoryData();
            drawVertical(canvas);
            drawLine(canvas);
            drawRect(canvas);
        }
    }

    private void drawVertical(Canvas canvas) {
        //每13个data的时间单位的px
        int size = (int) (13 / scaleSize);
        float l = (int) ( size *unitDataIndex);
        float i = l - ((int) showDataLeftX) % l;
        for (int x = 0; x < 5; x++) {
            if (showDataLeftX + i + l * x <= showDataRightX) {
                canvas.drawLine(showDataLeftX + i + l * x, 0, showDataLeftX + i + l * x, showDataHeight, mGarkPaint);
                //下标从0开始,所有有可能为1000,实际上是999
                int showTimeIndex = (int) ((showDataLeftX + i + l * x) / unitDataIndex);
                if (showTimeIndex >= indexCount) {
                    showTimeIndex = data.getBarnum() - 1;
                }
                canvas.drawText((data.getList().get(showTimeIndex).getTime()), showDataLeftX + i + l * x, showDataHeight + TradeDateConstant.showTimeSpace / 2 + SystemUtil.dp2pxFloat(mContext, 10), mGarkPaint);
            }
        }
    }

    //画矩形
    private void drawRect(Canvas canvas) {
        if (data != null) {
            BeanHistory.BeanHistoryData.HistoryItem historyData;
            for (int i = startIndex; i < endIndex; i++) {

                historyData = data.getList().get(i);
                openPrice = historyData.getOpen();
                closePrice = historyData.getClose();
                maxPrice = historyData.getHigh();
                minPrice = historyData.getLow();
                try {
                    yTop = (float) MoneyUtil.div(Math.abs(price[1] - openPrice), unit);
                    yBottom = (float) MoneyUtil.div(Math.abs(price[1] - closePrice), unit);
                    yMaxTop = (float) MoneyUtil.div(Math.abs(price[1] - maxPrice), unit);
                    yMinBottom = (float) MoneyUtil.div(Math.abs(price[1] - minPrice), unit);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                startX = showDataLeftX + (i - startIndex) * (simpleDataSpace + dataViewSpace);
                stopX = startX + simpleDataSpace;
                if(i==999) {
                    Log.i(TAG, "drawRect: " + endIndex+" showDataRightX "+showDataRightX+" stopX "+stopX);
                }
                //说明打开价和关闭价相等或者太多相近，人工画出值
                if (historyData.getClose() > historyData.getOpen()) {
                    canvas.drawRect(startX, yBottom, stopX, yTop, mRisePaint);
                    canvas.drawLine(startX + (stopX - startX) / 2, yMinBottom, startX + (stopX - startX) / 2, yMaxTop, mRisePaint);
                } else if (historyData.getClose() < historyData.getOpen()) {
                    canvas.drawRect(startX, yTop, stopX, yBottom, mfallPaint);
                    canvas.drawLine(startX + (stopX - startX) / 2, yMaxTop, startX + (stopX - startX) / 2, yMinBottom, mfallPaint);
                } else {
                    if (yMaxTop > yMinBottom) {
                        canvas.drawLine(startX + (stopX - startX) / 2, yMaxTop, startX + (stopX - startX) / 2, yMinBottom, mSilverPaint);
                    } else {
                        canvas.drawLine(startX + (stopX - startX) / 2, yMinBottom, startX + (stopX - startX) / 2, yMaxTop, mSilverPaint);
                    }
                    canvas.drawRect(startX, yTop, stopX, yBottom + 3, mSilverPaint);
                }
            }
        }
    }

    //画线
    private void drawLine(Canvas canvas) {
        if (data != null) {
            int[] ints = DataUtil.drawLineCount(digits, price[1], price[0]);
            //注意,这里还缺少划线数都是整数,所以需要计算.曲余数,+100;
            int wholeNumber = (int) (price[1] * Math.pow(10, digits));
            Log.i(TAG, "drawLine: " + data.getBarnum() + " " + ints[1] + " " + ints[0]);

            if(Math.pow(10,digits)==0){
                Log.i(TAG, "drawLine: ");
            }
            double   remainder = ((wholeNumber % ints[0]) / Math.pow(10, digits));

            Log.i(TAG, "drawLine:显示出来的数据最大最小值 " + price[1] + "  " + price[0]);

            canvas.drawLine(showDataLeftX, 0, showDataRightX, 0, mGarkPaint);
            canvas.drawLine(showDataLeftX, getMeasuredHeight() - SystemUtil.dp2pxFloat(mContext, TradeDateConstant.showTimeSpace), showDataRightX, getMeasuredHeight() - SystemUtil.dp2pxFloat(mContext, TradeDateConstant.showTimeSpace), mGarkPaint);
            Log.i(TAG, "drawLine: " + digits);
            for (int i = 0; i < ints[1]; i++) {
                if ((int) ((remainder + ints[0] / Math.pow(10, digits) * i) / unit) < showDataHeight) {
                    int i1 = (int) ((remainder + ints[0] / Math.pow(10, digits) * i) / unit);
                    canvas.drawLine(showDataLeftX, i1, showDataRightX, i1, mGarkPaint);
                    mBeanDrawPriceData = new BeanDrawPriceData();
                    mBeanDrawPriceData.setPriceY(i1);
                    try {
                        mBeanDrawPriceData.setPriceString(String.valueOf(MoneyUtil.div((price[1] - remainder - ints[0] / Math.pow(10, digits) * i), 1, digits, ROUND_DOWN)));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    mDrawPriceDataList.add(mBeanDrawPriceData);
                }
            }

//            计算虚线的位置和传实时值：
            if (realTimePrice != -1 && price[1] - realTimePrice > 0) {
                try {
                    double y = MoneyUtil.div((price[1] - realTimePrice), unit);
                    if(y<showDataHeight) {
                        mBeanDrawPriceData = new BeanDrawPriceData();
                        mBeanDrawPriceData.setPriceY((int) ((price[1] - realTimePrice) / unit));
                        mBeanDrawPriceData.setPriceString(String.valueOf(realTimePrice));
                        mDrawPriceDataList.add(mBeanDrawPriceData);

                        mDashedPaint.setColor(getResources().getColor(realPriceColor));
                        mBeanDrawPriceData.setColor(getResources().getColor(realPriceColor));

                        canvas.drawLine(showDataLeftX, (float) y, showDataRightX, (float) y, mDashedPaint);
                    }
                } catch (IllegalAccessException e) {
                    Log.i(TAG, "drawLine: 异常");
                    e.printStackTrace();
                }
            }
            if (mDrawPriceListener != null)
                mDrawPriceListener.drawPriceData(mDrawPriceDataList);

        }
    }



    public void setHistoryData(BeanHistory.BeanHistoryData data, int left, int right) {
        if (data != null) {
            this.data = data;
            indexCount = data.getBarnum();
            realTimePrice = -1;
        }
        postInvalidate(left, 0, right, getMeasuredHeight());
    }

    public void postInvalidate(int left, int top, int right, int bottom, float scaleSize) {
        this.scaleSize = scaleSize;
        showDataLeftX = left;
        showDataRightX = right;
        super.postInvalidate(left, top, right, bottom);
    }


    /**
     * 数据处理,我们要拿到最高价,最低价price[],每单元dp代表的差价unit,数据的开始时间时间dataBeginTime,
     */
    private int startIndex;
    private int endIndex;

    private void decodeHistoryData() {
        mDrawPriceDataList.clear();
        digits = data.getDigits();
        dataViewSpace = (SystemUtil.dp2pxFloat(mContext, TradeDateConstant.jianju) * scaleSize);
        simpleDataSpace =(SystemUtil.dp2pxFloat(mContext, TradeDateConstant.juli) * scaleSize);
        try {
            endIndex=(int)Math.abs(MoneyUtil.div((double) showDataRightX,(double) (simpleDataSpace + dataViewSpace)));
            if(endIndex>=data.getBarnum()){
                endIndex=data.getBarnum();
            }
            startIndex=(int)Math.abs(MoneyUtil.div((double) showDataLeftX,(double) (simpleDataSpace + dataViewSpace)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        price = DataUtil.calcMaxMinPrice(data, digits, startIndex, endIndex);
        if(price[0]==0){
            Log.i(TAG, "decodeHistoryData: 最大值最小值相等，出错");
        }
        blance = MoneyUtil.subPrice(price[1], price[0]);
        showDataHeight = (getMeasuredHeight() - SystemUtil.dp2pxFloat(mContext, TradeDateConstant.showTimeSpace));
        try {
            unit = MoneyUtil.div(blance, (double) this.showDataHeight);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(unit==0){
            Log.i(TAG, "decodeHistoryData: 单位价值空间为0");
        }
        unitDataIndex = ((float) getWidth() / (float) data.getBarnum());
        dataBeginTime = data.getList().get(0).getQuoteTime();
        period = data.getPeriod();
        if (!isReady)
            isReady = true;
    }
    //组装实时数据。
    BeanDrawRealTimePriceData beanDrawRealTimePriceData;
    double realTimePrice = -1;

    public BeanDrawRealTimePriceData refreshRealTimePrice(RealTimeDataList.BeanRealTime beanRealTime) {
        //两种保存方式。1：当前显示的。如果实时值对最后一个值有所更改，则实时更改。
        if(data==null){
            return null;
        }
        BeanHistory.BeanHistoryData.HistoryItem historyItem = data.getList().get(data.getBarnum() - 1);
        if (DateUtils.getOrderStartTime(beanRealTime.getTime()) < DateUtils.getOrderStartTime(historyItem.getTime()) + period * 60 * 1000) {
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
            data.getList().remove(0);
            BeanHistory.BeanHistoryData.HistoryItem lastItem = new BeanHistory().new BeanHistoryData().new HistoryItem();
            lastItem.setOpen(beanRealTime.getBid());
            lastItem.setClose(beanRealTime.getBid());
            lastItem.setHigh(beanRealTime.getBid());
            lastItem.setLow(beanRealTime.getBid());
            lastItem.setTime(beanRealTime.getTime());
            lastItem.setQuoteTime((int) DateUtils.getOrderStartTime(beanRealTime.getTime()));
            data.getList().add(lastItem);
        }
        if(beanRealTime.getBid()>=realTimePrice){
            realPriceColor=R.color.text_color_price_rise;
        }else{
            realPriceColor=R.color.text_color_price_fall;
        }
        this.realTimePrice = beanRealTime.getBid();
        postInvalidate((int) showDataLeftX, 0, (int) showDataRightX, getHeight(), scaleSize);
        return beanDrawRealTimePriceData;
    }
}
