package com.xkj.trade.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xkj.trade.R;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangsc on 2017-02-23.
 * TODO:
 */

public class CustomMasterLink extends View {
    private List<Point> mPointList;
    private Paint mPaint;
    private BeanMasterRank.MasterRank rank;
    private String max;
    private String min;
    private double unit;
    private int distance;
    private List<String> profitList;
    private int height;
    private int width;
    private Paint mGaryPaint;
    private String TAG= SystemUtil.getTAG(this);

    public CustomMasterLink(Context context) {
        this(context, null);
    }

    public CustomMasterLink(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomMasterLink(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(getResources().getColor(R.color.link_master_info_));
        mPointList = new ArrayList<>();
        mGaryPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mGaryPaint.setStrokeWidth(3);
        mGaryPaint.setColor(getResources().getColor(R.color.background_master_head));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rank != null&&rank.getData()!=null) {
            decodeData();
            drawLink(canvas);
        }
    }

    private void drawLink(Canvas canvas) {
        Point firstPoint = null;
        Point lastPoint=null;

        if(unit==0){
            canvas.drawLine(0,getHeight()/2,width,getHeight()/2,mPaint);
        }
//        for(Point mPoint:mPointList){
        for(int i=0;i<mPointList.size();i++){
            Point mPoint=mPointList.get(i);
            if(firstPoint ==null){
                firstPoint =mPoint;
            }else if(i!=mPointList.size()){
                lastPoint=mPoint;
                canvas.drawLine(firstPoint.x, firstPoint.y,lastPoint.x,lastPoint.y,mPaint);
                firstPoint =lastPoint;
            }
        }
//        try {
//            canvas.drawLine(0,(float) MoneyUtil.div(MoneyUtil.subPrice(max,"0"),unit),getWidth(),(float) MoneyUtil.div(MoneyUtil.subPrice(max,"0"),unit),mGaryPaint);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        //画0元线
    }

    private void decodeData() {
        mPointList.clear();
        max=null;
        min=null;
        for (String profit : profitList = rank.getData()) {
            if (min == null) {
                max = profit;
                min = profit;
            } else {
                if (Double.valueOf(profit) > Double.valueOf(max)) {
                    max = profit;
                }
                if (Double.valueOf(profit) < Double.valueOf(min)) {
                    min = profit;
                }
            }
        }

        distance = getWidth()/ profitList.size();
        Log.i(TAG, "decodeData: "+getWidth()+"  "+getMeasuredWidth()+"  "+getHeight()+"  "+getMeasuredHeight()+"  "+rank.getName());
        try {
            unit = MoneyUtil.div(MoneyUtil.subPrice(max, min),getHeight());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(unit==0){
            return;
        }
        String profit;
        for (int i = 0; i < profitList.size(); i++) {
            profit=profitList.get(i);
            try {
                    mPointList.add(new Point(i*distance,(int) MoneyUtil.div(MoneyUtil.subPrice(max, profit), unit)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void postInvalidate(BeanMasterRank.MasterRank rank,int height,int width) {
        this.rank = rank;
        this.height=height;
        this.width=width;
        postInvalidate();
    }
    public void postInvalidate(BeanMasterRank.MasterRank rank) {
        this.rank = rank;
        postInvalidate();
    }
        }
