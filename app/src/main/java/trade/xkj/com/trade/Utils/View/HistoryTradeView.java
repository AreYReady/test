package trade.xkj.com.trade.Utils.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.DataUtil;
import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.bean.HistoryData;
import trade.xkj.com.trade.bean.HistoryDataList;
import trade.xkj.com.trade.constant.KLineChartConstant;

/**
 * Created by admin on 2016-11-23.
 */

public class HistoryTradeView extends View {

    private String TAG = SystemUtil.getTAG(this);
    private Paint mRedPaint;
    private Context mContext;
    private Paint mBluePaint;
    private Paint mGarkPaint;
    /**
     * @param data
     */
    private long dataBeginTime;
    /**
     *     0是最小,1是最大
     */
    private double[] price;
    /**
     * 单位dip的价格差
     */
    private double unit;
    private HistoryDataList data;
    private HistoryDataList showData;
    //两个数据绘图的空隙,默认3dip
    private float dataViewSpace = 3;
    private int width = 0;
    private int height = 0;
    private int showLeftX;
    private int showRightX;
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

    public HistoryTradeView(Context context) {
        this(context, null);
    }

    public HistoryTradeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


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
        dataViewSpace = SystemUtil.dp2pxFloat(mContext, KLineChartConstant.jianju);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout: width" + getMeasuredWidth() + "hergh" + getMeasuredHeight());

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMySize(100, widthMeasureSpec);
        height = getMySize(100, heightMeasureSpec);
        Log.i(TAG, "onMeasure: width" + width + " height" + height);
        setMeasuredDimension(width, height);
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
            devodeHistoryData();
            drawLine(canvas);
            drawRect(canvas);
        }
    }

    private int moveSpace;

    //画矩形
    private void drawRect(Canvas canvas) {
        if (data != null) {
            HistoryData historyData;
            long t;
            for (int i = startIndex; i < endIndex; i++) {
                historyData = data.getItems().get(i);
                if (i == 0) {
                    t = historyData.getT();
                }
                String oPrice[] = historyData.getO().split("\\|");
                openPrice = Double.valueOf(oPrice[0]);
                closePrice = Double.valueOf(oPrice[0]) + Double.valueOf(oPrice[3]);
                maxPrice = Double.valueOf(oPrice[0])+Double.valueOf(oPrice[1]);
                minPrice = Double.valueOf(oPrice[0])+Double.valueOf(oPrice[2]);

                yTop = (float) (Math.abs(price[1] - openPrice) / unit);
                yBottom = (float)(Math.abs(price[1] - closePrice) / unit);
                yMaxTop = (float) (Math.abs(price[1] - maxPrice) / unit);
                yMinBottom = (float) (Math.abs(price[1] - minPrice) / unit);
//                float x = SystemUtil.dp2pxFloat(mContext, KLineChartConstant.juli+KLineChartConstant.jianju);
//                float i1 =showLeftX+(i-startIndex)*SystemUtil.dp2pxFloat(mContext, KLineChartConstant.juli+KLineChartConstant.jianju);
//                float i2 = showLeftX+(i-startIndex)*SystemUtil.dp2pxFloat(mContext, KLineChartConstant.juli+KLineChartConstant.jianju)-dataViewSpace;
                startX = showLeftX + (i - startIndex) * SystemUtil.dp2pxFloat(mContext, KLineChartConstant.juli+KLineChartConstant.jianju);
                stopX = startX + SystemUtil.dp2pxFloat(mContext, KLineChartConstant.juli+KLineChartConstant.jianju) - dataViewSpace;
                if (Double.valueOf(oPrice[3]) > 0) {
                    canvas.drawRect(startX, yBottom, stopX, yTop, mBluePaint);
                    canvas.drawLine(startX +(stopX - startX)/2, yMinBottom, startX +(stopX - startX)/2, yMaxTop,mBluePaint);
                } else {
                    canvas.drawRect(startX, yTop, stopX, yBottom, mRedPaint);
                    canvas.drawLine(startX +(stopX - startX)/2, yMaxTop, startX +(stopX - startX)/2, yMinBottom,mRedPaint);
                }
            }
        }
    }


    //画线
    private void drawLine(Canvas canvas) {
        if (data != null) {
            int[] ints = DataUtil.drawLineCount(digits, price[1], price[0]);
            int h = getMeasuredHeight() / ints[0];
            for(int i=0;i<ints[0];i++){
                canvas.drawLine(0,h*i,getMeasuredWidth()*5,h*i,mGarkPaint);
            }
//            canvas.drawLine(0, 0, getMeasuredWidth() * 5, 0, mGarkPaint);
//            canvas.drawLine(0, 0, getMeasuredWidth() * 5, 0, mGarkPaint);
//            canvas.drawLine(0, 0, getMeasuredWidth() * 5, 0, mGarkPaint);
//            canvas.drawLine(0, h, getMeasuredWidth() * 5, h, mGarkPaint);
//            canvas.drawLine(0, h * 2, getMeasuredWidth() * 5, h * 2, mGarkPaint);
//            canvas.drawLine(0, h * 3, getMeasuredWidth() * 5, h * 3, mGarkPaint);
//            canvas.drawLine(0, h * 4, getMeasuredWidth() * 5, h * 4, mGarkPaint);
        }
    }




    public void setHistoryData(HistoryDataList data, int left, int right) {
        if (data != null) {
            this.data = data;
            dataBeginTime = data.getItems().get(0).getT();
            digits=data.getDigits();
        }
        postInvalidate(left, 0, right, getMeasuredHeight());
    }

    @Override
    public void postInvalidate(int left, int top, int right, int bottom) {
        showLeftX = left;
        showRightX = right;
        super.postInvalidate(left, top, right, bottom);
    }


    /**
     * 数据处理,我们要拿到最高价,最低价price[],每单元dp代表的差价unit,数据的开始时间时间dataBeginTime,
     */
    private int startIndex;
    private int endIndex;

    private void devodeHistoryData() {
        int i = SystemUtil.dp2px(mContext, KLineChartConstant.jianju + KLineChartConstant.juli);
        int i1 = SystemUtil.dp2px(mContext, KLineChartConstant.jianju + KLineChartConstant.juli);
        startIndex = Math.abs(showLeftX / SystemUtil.dp2px(mContext, KLineChartConstant.jianju + KLineChartConstant.juli));
        endIndex = Math.abs(showRightX / SystemUtil.dp2px(mContext, KLineChartConstant.jianju + KLineChartConstant.juli));
        price = DataUtil.calcMaxMinPrice(data, data.getDigits(), startIndex, endIndex);
        Log.i(TAG, "devodeHistoryData: statIndex"+startIndex+"   endIndex"+endIndex);
        double blance = price[1] - price[0];
        unit = blance / (getMeasuredHeight()-SystemUtil.dp2pxFloat(mContext,KLineChartConstant.kongbaiqu));
    }

    private void postInvalidate(int x, int y) {
        devodeHistoryData();
        postInvalidate();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.i(TAG, "onScrollChanged: " + l + "   " + t + "       " + oldl + "     " + oldt);
    }
//    float lastX;
//    float lastY;
//    float rawX;
//    float rawY;
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Log.i(TAG, "onTouchEvent: ");
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                Log.i(TAG, "onTouchEvent: ACTION_DOWN");
//                lastX=event.getRawX();
//                lastY=event.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG, "onTouchEvent: ACTION_MOVE");
//                rawX=event.getRawX();
//                rawY=event.getRawY();
//                //通过View.layout来设置左上右下坐标位置
//                //获得当前的left等坐标并加上相应偏移量
////                layout(getLeft() + (int)offsetX,
////                        getTop() + offsetY,
////                        getRight() + offsetX,
////                        getBottom() + offsetY);
//                //移动过后，更新lastX与lastY
//                Log.i(TAG, "onTouchEvent: "+(rawX-lastX));
//                if(rawX-lastX>0){
//                    lastX = rawX;
//                    lastY = rawY;
//                    dataViewSpace++;
//                    postInvalidate();
//                }
//                break;
//        }
//
//
//        return true;
//    }


}
