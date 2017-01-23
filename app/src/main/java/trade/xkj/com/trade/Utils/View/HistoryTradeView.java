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
import trade.xkj.com.trade.utils.DataUtil;
import trade.xkj.com.trade.utils.DateUtils;
import trade.xkj.com.trade.utils.MoneyUtil;
import trade.xkj.com.trade.utils.SystemUtil;
import trade.xkj.com.trade.bean.BeanDrawPriceData;
import trade.xkj.com.trade.bean.BeanDrawRealTimePriceData;
import trade.xkj.com.trade.bean.HistoryData;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.bean.RealTimeDataList;
import trade.xkj.com.trade.constant.TradeDateConstant;

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
    private HistoryDataList data;
    private HistoryDataList showData;
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
        mBluePaint = new Paint();
        mBluePaint.setStyle(Paint.Style.FILL);
        mBluePaint.setColor(getResources().getColor(R.color.risk_slippage_max_green_dark));
        mGarkPaint = new Paint();
        mGarkPaint.setColor(getResources().getColor(R.color.text_color_primary_dark_with_opacity));
        mGarkPaint.setStrokeWidth(3);
        mGarkPaint.setTextAlign(Paint.Align.CENTER);
        mGarkPaint.setTextSize(SystemUtil.dp2pxFloat(mContext, 10));

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
                    showTimeIndex = data.getCount() - 1;
                }
                canvas.drawText(DateUtils.getShowTimeNoTimeZone((data.getItems().get(showTimeIndex).getT()) * indexCount + dataBeginTime * indexCount), showDataLeftX + i + l * x, showDataHeight + TradeDateConstant.showTimeSpace / 2 + SystemUtil.dp2pxFloat(mContext, 10), mGarkPaint);
            }
        }
    }

    //画矩形
    private void drawRect(Canvas canvas) {
        if (data != null) {
            HistoryData historyData;
            for (int i = startIndex; i < endIndex; i++) {
                historyData = data.getItems().get(i);
                String oPrice[] = historyData.getO().split("\\|");
                openPrice = Double.valueOf(oPrice[0]);
                closePrice = Double.valueOf(oPrice[0]) + Double.valueOf(oPrice[3]);
                maxPrice = Double.valueOf(oPrice[0]) + Double.valueOf(oPrice[1]);
                minPrice = Double.valueOf(oPrice[0]) + Double.valueOf(oPrice[2]);
                yTop = (float) (Math.abs(price[1] - openPrice) / unit);
                yBottom = (float) (Math.abs(price[1] - closePrice) / unit);
                yMaxTop = (float) (Math.abs(price[1] - maxPrice) / unit);
                yMinBottom = (float) (Math.abs(price[1] - minPrice) / unit);
                startX = showDataLeftX + (i - startIndex) * (simpleDataSpace + dataViewSpace);
                stopX = startX + simpleDataSpace;
                if (Double.valueOf(oPrice[3]) > 0) {
                    canvas.drawRect(startX, yBottom, stopX, yTop, mBluePaint);
                    canvas.drawLine(startX + (stopX - startX) / 2, yMinBottom, startX + (stopX - startX) / 2, yMaxTop, mBluePaint);
                } else {
                    canvas.drawRect(startX, yTop, stopX, yBottom, mRedPaint);
                    canvas.drawLine(startX + (stopX - startX) / 2, yMaxTop, startX + (stopX - startX) / 2, yMinBottom, mRedPaint);
                }
            }
        }
    }


    //画线
    private void drawLine(Canvas canvas) {
        if (data != null) {
            int[] ints = DataUtil.drawLineCount(digits, price[1], price[0]);
            //注意,这里还缺少划线数都是整数,所以需要计算.曲余数,+100;
            int wholeNumber = (int) price[1];
            Log.i(TAG, "drawLine: " + data.getCount() + " " + ints[1] + " " + ints[0]);
            int remainder = wholeNumber % ints[0];
            Log.i(TAG, "drawLine:显示出来的数据最大最小值 " + price[1] + "  " + price[0]);
            canvas.drawLine(showDataLeftX, 0, showDataRightX, 0, mGarkPaint);
            canvas.drawLine(showDataLeftX, getMeasuredHeight() - SystemUtil.dp2pxFloat(mContext, TradeDateConstant.showTimeSpace), showDataRightX, getMeasuredHeight() - SystemUtil.dp2pxFloat(mContext, TradeDateConstant.showTimeSpace), mGarkPaint);
            Log.i(TAG, "drawLine: " + digits);
            for (int i = 0; i < ints[1]; i++) {
                if ((int) ((remainder + ints[0] * i) / unit) < showDataHeight) {
                    int i1 = (int) ((remainder + ints[0] * i) / unit);
//                    String s = MoneyUtil.moneyFormat(((price[1] - remainder - ints[0] * i) / Math.pow(10, digits)), digits);
                    String s = String.valueOf((int) (price[1] - remainder - ints[0] * i) / Math.pow(10, digits));
                    canvas.drawLine(showDataLeftX, i1, showDataRightX, i1, mGarkPaint);
                    mBeanDrawPriceData = new BeanDrawPriceData();
                    mBeanDrawPriceData.setPriceY(i1);
                    mBeanDrawPriceData.setPriceString(s);
                    mDrawPriceDataList.add(mBeanDrawPriceData);
                }
            }

            //计算虚线的位置和传实时值：
            if(realTimePrice!=-1&&price[1] - realTimePrice>0){
                double y = (price[1] - realTimePrice) / unit;
                mBeanDrawPriceData=new BeanDrawPriceData();
                mBeanDrawPriceData.setPriceY((int)((price[1] - realTimePrice) / unit));
                mBeanDrawPriceData.setPriceString(String.valueOf(realTimePrice/Math.pow(10,digits)));
                mDrawPriceDataList.add(mBeanDrawPriceData);

            }
            if (mDrawPriceListener != null)
                mDrawPriceListener.drawPriceData(mDrawPriceDataList);
        }
    }


    public void setHistoryData(HistoryDataList data, int left, int right) {
        if (data != null) {
            this.data = data;
            indexCount = data.getCount();
            realTimePrice=-1;
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
        dataViewSpace = (float) (SystemUtil.dp2pxFloat(mContext, TradeDateConstant.jianju) * scaleSize);
        simpleDataSpace = (float) (SystemUtil.dp2pxFloat(mContext, TradeDateConstant.juli) * scaleSize);
        startIndex = (int) Math.abs(showDataLeftX / (simpleDataSpace + dataViewSpace));
        //下标是从零开始
        endIndex = (int) Math.abs(showDataRightX / (simpleDataSpace + dataViewSpace)) - 1;
        price = DataUtil.calcMaxMinPrice(data, digits, startIndex, endIndex);
        blance = MoneyUtil.subPrice(price[1], price[0]);
        showDataHeight = (getMeasuredHeight() - SystemUtil.dp2pxFloat(mContext, TradeDateConstant.showTimeSpace));
        unit = blance / showDataHeight;
        unitSecond = (float) (data.getPeriod() * 60 * data.getCount() / getWidth());
        unitDataIndex = ((float) getWidth() / (float) data.getCount());
        dataBeginTime = data.getItems().get(0).getT();
        dataBeginTime = data.getItems().get(0).getT() + TradeDateConstant.tz_delta * 60 * 60 ;
        dataStopTime = dataBeginTime + data.getItems().get(data.getItems().size() - 1).getT();
        showBeginTime = dataBeginTime + data.getItems().get(startIndex).getT();
        period=data.getPeriod();
        if (!isReady)
            isReady = true;
    }

    //组装实时数据。
    BeanDrawRealTimePriceData beanDrawRealTimePriceData;
    HistoryData lastHistoryData;

    double realTimePrice=-1;

    public BeanDrawRealTimePriceData refreshRealTimePrice(RealTimeDataList.BeanRealTime beanRealTime){
        /**
         * 接收实时数据，画虚线。（可以在画横线的时候画）
         * 传送实时数据的y轴（可以给drawPrice的时候,一起传送
         * 更新data，以便刷新最后一个item的值
         * 所以这个有刷新的功能，也就是更新data然后刷新
         */
        if(DateUtils.getOrderStartTime(beanRealTime.getTime())/1000>=dataStopTime+period*60){
            //新的时间阶段。
            Log.i(TAG, "refreshRealTimePrice: 新的最后一个item");
        }else{
            Log.i(TAG, "refreshRealTimePrice: 更新最后一个item");
            lastHistoryData  = data.getItems().get(data.getCount() - 1);
            String[] split = lastHistoryData.getO().split("\\|");
            realTimePrice = beanRealTime.getBid() * Math.pow(10, digits);
            StringBuffer stringBuffer=new StringBuffer();
//             Double.valueOf(split[0])+Double.valueOf(split[1]);
//            Double.valueOf(split[0])+Double.valueOf(split[2]);
            stringBuffer.append(split[0]).append("|");
            if(Double.valueOf(split[0])+Double.valueOf(split[1])>realTimePrice){
                //说明比最小值还小
                stringBuffer.append((int)(realTimePrice-Double.valueOf(split[0]))).append("|");
                stringBuffer.append(split[2]).append("|");
            }else if(Double.valueOf(0)+Double.valueOf(split[2])<realTimePrice){
                //说明比最大值还大
                stringBuffer.append(split[1]).append("|");
                stringBuffer.append((int)(realTimePrice-Double.valueOf(split[0]))).append("|");
            }else{
                stringBuffer.append(split[1]).append("|");
                stringBuffer.append(split[2]).append("|");
            }
            stringBuffer.append((int)(realTimePrice-Double.valueOf(split[0])));
            data.getItems().get(data.getCount()-1).setO(stringBuffer.toString());
            Log.i(TAG, "refreshRealTimePrice: 前后对比"+lastHistoryData.getO()+"  "+stringBuffer.toString());
            postInvalidate((int)showDataLeftX,0,(int)showDataRightX,getHeight());
        }
        return beanDrawRealTimePriceData;
    }
}
