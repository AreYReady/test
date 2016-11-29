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
import trade.xkj.com.trade.mvp.login.UserLoginActivity;

/**
 * Created by admin on 2016-11-23.
 */

public class HistoryTradeView extends View {
    //屏幕的宽度
    private int screenWidth;
    private String TAG = SystemUtil.getTAG(this);
    //数据条数
    private int dataSize = 1000;
    //同一时间显示的数据条数
    private int showDatasize = 60;

    private int childViewWidth;
    private Paint mRedPaint;
    private Context mContext;
    private Paint mBluePaint;
    private Paint mGarkPaint;
    //两个数据绘图的空隙,默认1dip
    private int dataViewSpace=1;

    //右部价格空间
    private int rightPriceSpace = 200;

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
        screenWidth = UserLoginActivity.scrren[0];
        dataViewSpace=SystemUtil.dp2px(mContext,2);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout: width" + getMeasuredWidth() + "hergh" + getMeasuredHeight());

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int width;
//        int height ;
//        Log.i(TAG, "onMeasure:screenWidth "+screenWidth+"heightSize"+heightSize+"rightPriceSpace:"+rightPriceSpace);
//        //根据数据多少和一屏幕显示多少数据,计算view的宽度,这里扣除右侧的价格空间
//        widthSize = (int)(dataSize / showDatasize) * (screenWidth-SystemUtil.dp2px(mContext,rightPriceSpace));
//        setMeasuredDimension(widthSize, heightSize);
//        Log.i(TAG, "onLayout: width" + widthMeasureSpec + "hergh" + heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawRect(canvas);
        canvas.drawRect(60, 60, 80, 80, mRedPaint);// 正方形
    }

    private int moveSpace;

    //画矩形
    private void drawRect(Canvas canvas) {
        if (data != null) {
            price = DataUtil.calcMaxMinPrice(data, data.getDigits(), data.getItems().size() - showDatasize, data.getItems().size());
            double blance = price[1] - price[0];
            unit = blance / SystemUtil.px2dp(mContext, getMeasuredHeight());
            int beginDataIndex = data.getItems().size() - showDatasize - moveSpace;
            HistoryData historyData;
            long t;
            for (int i = 0; i < showDatasize; i++) {
                historyData = data.getItems().get(beginDataIndex + i);
                if(i==0){
                     t = historyData.getT();
                }
                String oPrice[] = historyData.getO().split("\\|");
                double minPrice = Double.valueOf(oPrice[0]) + Double.valueOf(oPrice[2]);
                double maxPrice = Double.valueOf(oPrice[0]) + Double.valueOf(oPrice[1]);

                int yTop=SystemUtil.dp2px(mContext, (float) ((maxPrice - price[0]) / unit));
                int yBottom=SystemUtil.dp2px(mContext, (float) ((minPrice - price[0]) / unit));
                int x=(5 * getMeasuredWidth() / 6)/showDatasize;
                if(Double.valueOf(oPrice[3])>0){
                    canvas.drawRect(i*x,yBottom,i*x+x-dataViewSpace,yTop,mBluePaint);
                }else{
                    canvas.drawRect(i*x,yBottom,i*x+x-dataViewSpace,yTop,mRedPaint);
                }
            }
        }

    }



    //画线
    private void drawLine(Canvas canvas) {
        if (data != null) {
            int i = getMeasuredHeight() / 4;
            int y = 5 * getMeasuredWidth() / 6;
            if (unit == 0.00) {

            }
            canvas.drawLine(0, 0, y, 0, mGarkPaint);
            canvas.drawLine(0, i, y, i, mGarkPaint);
            canvas.drawLine(0, i * 2, y, i * 2, mGarkPaint);
            canvas.drawLine(0, i * 3, y, i * 3, mGarkPaint);
            canvas.drawLine(0, i * 4, y, i * 4, mGarkPaint);
        }
    }

    /**
     * @param data
     */
    private long dataBeginTime;
    //0是最小,1是最大
    private double[] price;
    /**
     * 单位dip的价格差
     */
    private double unit;
    private HistoryDataList data;

    public void setHistoryData(HistoryDataList data) {
        if (data != null) {
            this.data = data;
            dataBeginTime = data.getItems().get(0).getT();
        }
        postInvalidate();
    }

    /**
     * 数据处理,我们要拿到最高价,最低价price[],每单元dp代表的差价unit,数据的开始时间时间dataBeginTime,
     */
    private void devodeHistoryData() {
        if (data != null) {
            price = DataUtil.calcMaxMinPrice(data, data.getDigits(), data.getItems().size() - showDatasize, data.getItems().size());
            double blance = price[1] - price[0];
            unit = blance / SystemUtil.px2dp(mContext, getMeasuredHeight());
        }
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
