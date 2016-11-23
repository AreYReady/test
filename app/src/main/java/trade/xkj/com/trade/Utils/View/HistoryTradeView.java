package trade.xkj.com.trade.Utils.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.mvp.login.UserLoginActivity;

/**
 * Created by admin on 2016-11-23.
 */

public class HistoryTradeView extends View {
    //屏幕的宽度
    private int screenWidth;
    private String TAG= SystemUtil.getTAG(this);
    //数据条数
    private int dataSize =1000;
    //同一时间显示的数据条数
    private int showDatasize=60;

    private int childViewWidth;
    private Paint mRedPaint;
    private Context mContext;
    private Paint mBluePaint;

    public HistoryTradeView(Context context) {
        this(context,null);
    }

    public HistoryTradeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public HistoryTradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        mRedPaint=new Paint();
        mRedPaint.setColor(Color.BLACK);
        mBluePaint=new Paint();
        mBluePaint.setTextSize(100);
        mBluePaint.setColor(Color.BLUE);
        screenWidth= UserLoginActivity.scrren[0];
        screenWidth= (int)SystemUtil.px2dp(mContext,screenWidth);

    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        Log.i(TAG, "onMeasure:screenWidth "+screenWidth+"heightSize"+SystemUtil.px2dp(mContext,heightSize));
        widthSize = (dataSize / showDatasize) * screenWidth;
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRedPaint.setTextSize(SystemUtil.px2sp(mContext,100));
        canvas.drawText("黄顺超",5000,100,mRedPaint);
    }
}
