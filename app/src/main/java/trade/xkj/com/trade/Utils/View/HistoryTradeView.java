package trade.xkj.com.trade.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.bean.BeanDrawPriceData;
import trade.xkj.com.trade.bean.BeanDrawRealTimePriceData;
import trade.xkj.com.trade.bean.RealTimeDataList;
import trade.xkj.com.trade.bean_.BeanHistory;
import trade.xkj.com.trade.constant.TradeDateConstant;
import trade.xkj.com.trade.utils.DataUtil;
import trade.xkj.com.trade.utils.DateUtils;
import trade.xkj.com.trade.utils.MoneyUtil;
import trade.xkj.com.trade.utils.SystemUtil;

import static java.math.BigDecimal.ROUND_DOWN;

/**
 * Created by admin on 2016-11-23.
 */

public class HistoryTradeView extends View {
    private String TAG = SystemUtil.getTAG(this);
    private Boolean isReady = false;
    private Paint mRedPaint;
    private Context mContext;
    private Paint mBluePaint;
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
    /**
     * 每单位px的秒数
     */
    private float unitSecond;
    /**
     * 展示数据的区域高度
     */
    private float showDataHeight;
    /**
     * 最高价和最低价的差价
     */
    private double blance;
    private double lastItemOpenPrice;
    private DrawPriceListener mDrawPriceListener;

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

    private BeanDrawPriceData mBeanDrawPriceData;

    public HistoryTradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mRedPaint = new Paint();
        mRedPaint.setStyle(Paint.Style.FILL);
        mRedPaint.setColor(getResources().getColor(R.color.risk_slippage_max_red_dark));
        mRedPaint.setStrokeWidth(3);
        mBluePaint = new Paint();
        mBluePaint.setStyle(Paint.Style.FILL);
        mBluePaint.setColor(getResources().getColor(R.color.risk_slippage_max_green_dark));
        mBluePaint.setStrokeWidth(3);
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

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
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
        float l = (int) (data.getPeriod() * 60 * size / unitSecond);
        float i = l - ((int) showDataLeftX) % l;
        for (int x = 0; x < 5; x++) {
            if (showDataLeftX + i + l * x <= showDataRightX) {
                canvas.drawLine(showDataLeftX + i + l * x, 0, showDataLeftX + i + l * x, showDataHeight, mGarkPaint);
                //下标从0开始,所有有可能为1000,实际上是999
                int showTimeIndex = (int) ((showDataLeftX + i + l * x) / unitDataIndex);
                if (showTimeIndex >= indexCount) {
                    showTimeIndex = data.getBarnum() - 1;
                }
                canvas.drawText(DateUtils.getShowTimeNoTimeZone((data.getList().get(showTimeIndex).getQuoteTime()) * indexCount + dataBeginTime * indexCount), showDataLeftX + i + l * x, showDataHeight + TradeDateConstant.showTimeSpace / 2 + SystemUtil.dp2pxFloat(mContext, 10), mGarkPaint);
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
                    canvas.drawRect(startX, yBottom, stopX, yTop, mBluePaint);
                    canvas.drawLine(startX + (stopX - startX) / 2, yMinBottom, startX + (stopX - startX) / 2, yMaxTop, mBluePaint);
                } else if (historyData.getClose() < historyData.getOpen()) {
                    canvas.drawRect(startX, yTop, stopX, yBottom, mRedPaint);
                    canvas.drawLine(startX + (stopX - startX) / 2, yMaxTop, startX + (stopX - startX) / 2, yMinBottom, mRedPaint);
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
            double remainder = 0.00000000;
            int i2 = wholeNumber % ints[0];
//            double v = 12.0 / 100000.0;
            double div = 0;
            try {
                div = MoneyUtil.div(12.0000, 10000.0, 5);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "drawLine: " + div + i2);
            remainder = ((wholeNumber % ints[0]) / Math.pow(10, digits));

            Log.i(TAG, "drawLine:显示出来的数据最大最小值 " + price[1] + "  " + price[0]);

            canvas.drawLine(showDataLeftX, 0, showDataRightX, 0, mGarkPaint);
            canvas.drawLine(showDataLeftX, getMeasuredHeight() - SystemUtil.dp2pxFloat(mContext, TradeDateConstant.showTimeSpace), showDataRightX, getMeasuredHeight() - SystemUtil.dp2pxFloat(mContext, TradeDateConstant.showTimeSpace), mGarkPaint);
            Log.i(TAG, "drawLine: " + digits);
            for (int i = 0; i < ints[1]; i++) {
                if ((int) ((remainder + ints[0] / Math.pow(10, digits) * i) / unit) < showDataHeight) {
                    int i1 = (int) ((remainder + ints[0] / Math.pow(10, digits) * i) / unit);
//                    String s = MoneyUtil.moneyFormat(((price[1] - remainder - ints[0] * i) / Math.pow(10, digits)), digits);
                    String s = String.valueOf((int) (price[1] - remainder - ints[0] / Math.pow(10, digits) * i) / Math.pow(10, digits));
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
                    mBeanDrawPriceData = new BeanDrawPriceData();
                    mBeanDrawPriceData.setPriceY((int) ((price[1] - realTimePrice) / unit));
                    String temp;
                    mBeanDrawPriceData.setPriceString(String.valueOf(realTimePrice));
                    mDrawPriceDataList.add(mBeanDrawPriceData);
                    if(realTimePrice<lastItemOpenPrice){
                        mDashedPaint.setColor(getResources().getColor(R.color.risk_slippage_max_red_dark));
                        mBeanDrawPriceData.setColor(getResources().getColor(R.color.risk_slippage_max_red_dark));
                    }else{
                        mDashedPaint.setColor(getResources().getColor(R.color.risk_slippage_max_green_dark));
                        mBeanDrawPriceData.setColor(getResources().getColor(R.color.risk_slippage_max_green_dark));
                    }
                    canvas.drawLine(showDataLeftX, (float) y, showDataRightX, (float) y,mDashedPaint);
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
//        startIndex = (int) Math.abs(showDataLeftX / (simpleDataSpace + dataViewSpace));
        //下标是从零开始
//        endIndex = (int) Math.abs(showDataRightX / (simpleDataSpace + dataViewSpace));
        try {
            endIndex=(int)Math.abs(MoneyUtil.div((double) showDataRightX,(double) (simpleDataSpace + dataViewSpace)));
            startIndex=(int)Math.abs(MoneyUtil.div((double) showDataLeftX,(double) (simpleDataSpace + dataViewSpace)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        price = DataUtil.calcMaxMinPrice(data, digits, startIndex, endIndex);
        blance = MoneyUtil.subPrice(price[1], price[0]);
        showDataHeight = (getMeasuredHeight() - SystemUtil.dp2pxFloat(mContext, TradeDateConstant.showTimeSpace));
//        unit = blance / showDataHeight;
        try {
            unit = MoneyUtil.div(blance, (double) this.showDataHeight);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        unitSecond = (float) (data.getPeriod() * 60 * data.getBarnum() / getWidth());
        unitDataIndex = ((float) getWidth() / (float) data.getBarnum());
        dataBeginTime = data.getList().get(0).getQuoteTime();
        dataBeginTime = data.getList().get(0).getQuoteTime() + TradeDateConstant.tz_delta * 60 * 60;
        dataStopTime = dataBeginTime + data.getList().get(data.getList().size() - 1).getQuoteTime();
        showBeginTime = dataBeginTime + data.getList().get(startIndex).getQuoteTime();
        period = data.getPeriod();
        lastItemOpenPrice=data.getList().get(data.getBarnum()-1).getOpen();
        if (!isReady)
            isReady = true;
    }
    //组装实时数据。
    BeanDrawRealTimePriceData beanDrawRealTimePriceData;
    BeanHistory.BeanHistoryData.HistoryItem lastHistoryData;
    double realTimePrice = -1;
    public BeanDrawRealTimePriceData refreshRealTimePrice(RealTimeDataList.BeanRealTime beanRealTime) {
        //两种保存方式。1：当前显示的。如果实时值对最后一个值有所更改，则实时更改。
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
        this.realTimePrice = beanRealTime.getBid();
        postInvalidate((int) showDataLeftX, 0, (int) showDataRightX, getHeight(), scaleSize);
        return beanDrawRealTimePriceData;
    }
}
