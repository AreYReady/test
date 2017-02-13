package com.xkj.trade.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xkj.trade.R;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean.BeanDrawPriceData;
import com.xkj.trade.bean.BeanDrawRealTimePriceData;
import com.xkj.trade.utils.SystemUtil;

import java.util.List;

/**
 * Created by huangsc on 2016-12-02.
 * TODO:
 */

public class DrawPriceView extends View {
    private Paint mGarkPaint;
    private Paint mRealTimePaint;
    private Context mContext;
    private String TAG = SystemUtil.getTAG(this);
    private List<BeanDrawPriceData> mDrawPrice;
    private BeanDrawRealTimePriceData mBeanDrawRealTimePriceData;


    public DrawPriceView(Context context) {
        super(context, null);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        mGarkPaint = new Paint();
        mRealTimePaint=new Paint();
        mGarkPaint.setColor(getResources().getColor(R.color.text_color_primary_dark_with_opacity));
        mGarkPaint.setStrokeWidth(3);
        mGarkPaint.setTextSize(SystemUtil.dp2pxFloat(MyApplication.getInstance().getApplicationContext(), 10));
        mRealTimePaint.setStrokeWidth(3);
        mRealTimePaint.setTextSize(SystemUtil.dp2pxFloat(MyApplication.getInstance().getApplicationContext(), 10));
    }

    public DrawPriceView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initView(context);
    }

    public DrawPriceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawPrice == null) {
            return;
        }
        for (BeanDrawPriceData beanDrawPriceData : mDrawPrice) {
            if(beanDrawPriceData.getColor()!=0){
                //实时数据
                mRealTimePaint.setColor(beanDrawPriceData.getColor());
                RectF oval3 = new RectF(20, beanDrawPriceData.getPriceY()-50, getWidth()-20, beanDrawPriceData.getPriceY()+20);// 设置个新的长方形
                canvas.drawRoundRect(oval3, 200, 200, mRealTimePaint);//第二个参数是x半径，第三个参数是y半径
                mRealTimePaint.setColor(Color.WHITE);
                canvas.drawText(beanDrawPriceData.getPriceString(), getResources().getDimension(R.dimen.space_big), beanDrawPriceData.getPriceY(), mRealTimePaint);
                mRealTimePaint.setStyle(Paint.Style.FILL);//充满
                mRealTimePaint.setColor(beanDrawPriceData.getColor());
                mRealTimePaint.setAntiAlias(true);// 设置画笔的锯齿效果
            }else{
                canvas.drawText(beanDrawPriceData.getPriceString(), getResources().getDimension(R.dimen.space_big), beanDrawPriceData.getPriceY(), mGarkPaint);
            }
        }

        if(mBeanDrawRealTimePriceData!=null){
            mRealTimePaint.setColor(mBeanDrawRealTimePriceData.getColor());
            canvas.drawText(mBeanDrawRealTimePriceData.getRealTimePrice(),getResources().getDimension(R.dimen.space_big),mBeanDrawRealTimePriceData.getY(),mRealTimePaint);
        }
    }

    public void refresh(List<BeanDrawPriceData> drawPrice) {
        mDrawPrice = drawPrice;
        postInvalidate();
    }
    public void refreshRealTimePrice(BeanDrawRealTimePriceData beanDrawRealTimePriceData){
        mBeanDrawRealTimePriceData=beanDrawRealTimePriceData;
        postInvalidate();
    }
}
