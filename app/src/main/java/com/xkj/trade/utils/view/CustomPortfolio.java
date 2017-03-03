package com.xkj.trade.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xkj.trade.R;
import com.xkj.trade.bean.BeanPortfolioData;
import com.xkj.trade.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangsc on 2016-12-30.
 * TODO:
 */

public class CustomPortfolio extends View {
    String TAG= SystemUtil.getTAG(this);
    Paint basePaint;
    Paint portfolioPaint;
    int strokeWidth = 30;
    List<BeanPortfolioData> data;
    List<Integer> selectColors ;
    Paint textPaint;
private Context context;

    public CustomPortfolio(Context context) {
        this(context, null);
    }

    public CustomPortfolio(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPortfolio(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }
    private void init() {

        selectColors=  new ArrayList<>();
        selectColors.add(R.color.buy_action_button_edge_color_dark);
        selectColors.add(R.color.link_text_material_light);
        selectColors.add(R.color.chart_down_dark);
        selectColors.add(R.color.abc_search_url_text_normal);
        basePaint = new Paint();
        basePaint.setColor(getResources().getColor(R.color.buy_action_button_unpressed_light));
        portfolioPaint = new Paint();
        portfolioPaint.setColor(getResources().getColor(R.color.buy_action_button_edge_color_dark));
        basePaint.setStrokeWidth(strokeWidth);
        basePaint.setStyle(Paint.Style.STROKE);
        basePaint.setAntiAlias(true);
        portfolioPaint.setStrokeWidth(strokeWidth+10);
        portfolioPaint.setStyle(Paint.Style.STROKE);
        portfolioPaint.setAntiAlias(true);
        textPaint=new Paint();
        textPaint.setStrokeWidth(5);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);
        linkLendth=SystemUtil.dp2px(context,50);
    }

    //    /**
//     /*java已知圆点坐标和半径，得到圆上的点
//     /*CIRCLE_CENTER_X ，CIRCLE_CENTER_Y  圆心坐标
//     /*CIRCLE_R  圆半径
//     **/
//    List points = new LinkedList<Point>();
//    private void initPointsCircular() {
//        for (int i = 0; i < 360; i += 1) {
//            int x = (int) (Configs.CIRCLE_CENTER_X - Configs.CIRCLE_R * Math.sin(Math.PI * (i - 90) / 180));
//            int y = (int) (Configs.CIRCLE_CENTER_Y - Configs.CIRCLE_R - 10
//                    + Configs.CIRCLE_R * Math.cos(Math.PI * (i - 90) / 180));
//            points.add(new Point(x, y));
//        }
//    }
    int radius;
    Point centerPoint;
    @Override
    protected void onDraw(Canvas canvas) {
        if (data != null) {
            if (data.size() != 0) {
                textPaint.setTextSize(40);
                centerPoint = new Point();
                centerPoint.x = getWidth() / 2;
                centerPoint.y = getHeight() / 2;
                radius = (int) SystemUtil.dp2px(context, 100);
                RectF rectf = new RectF(centerPoint.x - radius, centerPoint.y - radius, centerPoint.x + radius, centerPoint.y + radius);
                RectF rectf2 = new RectF(centerPoint.x - radius, centerPoint.y - radius, centerPoint.x + radius, centerPoint.y + radius);
//            canvas.drawCircle(centerPoint.x , centerPoint.y, radius, basePaint);
                for (int i = 0; i < data.size(); i++) {
                    BeanPortfolioData portfolioData = data.get(i);
                    portfolioPaint.setColor(getResources().getColor(selectColors.get(i % selectColors.size())));
                    textPaint.setColor(getResources().getColor(selectColors.get(i % selectColors.size())));
                    if (data.get(i).getSymbol().contains("其他")) {
                        canvas.drawArc(rectf, portfolioData.getBeginAngle(), portfolioData.getSweepAngle(), false, basePaint);
                    } else {
                        canvas.drawArc(rectf, portfolioData.getBeginAngle(), portfolioData.getSweepAngle(), false, portfolioPaint);
                    }
                    drawText(canvas, getXY((portfolioData.getBeginAngle() * 2 + portfolioData.getSweepAngle()) / 2), portfolioData);
                }
            }else{
                textPaint.setTextSize(SystemUtil.dp2px(context,30));
                textPaint.setColor(Color.WHITE);
                canvas.drawText("没有数据",getWidth()/2,getHeight()/2,textPaint);
            }
        }
    }

    /**
     * 画线和文字
     * @param canvas
     * @param xy
     */
    private int linkLendth;
    private void drawText(Canvas canvas, Point point,BeanPortfolioData data) {
        Point endPoint=new Point();
        if(Math.abs(centerPoint.x-point.x)>Math.abs(centerPoint.y-point.y)){
            //先画横线在画竖线
            if(point.y>centerPoint.y){
                if(point.x>centerPoint.x){
                    canvas.drawLine(point.x,point.y,point.x+linkLendth,point.y,textPaint);
                    canvas.drawLine(endPoint.x=point.x+linkLendth,point.y,point.x+linkLendth,endPoint.y=point.y+linkLendth,textPaint);

                }else{
                    canvas.drawLine(point.x,point.y,point.x-linkLendth,point.y,textPaint);
                    canvas.drawLine(endPoint.x=point.x-linkLendth,point.y,point.x-linkLendth,endPoint.y=point.y+linkLendth,textPaint);
                }
                canvas.drawText(data.getSymbol(),endPoint.x,endPoint.y+textPaint.getTextSize(),textPaint);
            }else{
                if(point.x>centerPoint.x){
                    canvas.drawLine(point.x,point.y,point.x+linkLendth,point.y,textPaint);
                    canvas.drawLine(endPoint.x=point.x+linkLendth,point.y,point.x+linkLendth,endPoint.y=point.y-linkLendth,textPaint);
                }else{
                    canvas.drawLine(point.x,point.y,point.x-linkLendth,point.y,textPaint);
                    canvas.drawLine(endPoint.x=point.x-linkLendth,point.y,point.x-linkLendth,endPoint.y=point.y-linkLendth,textPaint);
                }
                canvas.drawText(data.getSymbol(),endPoint.x,endPoint.y,textPaint);
            }

        }else{
            //先画竖线在画横线
            if(point.y>centerPoint.y){
                canvas.drawLine(endPoint.x=point.x,point.y,point.x,endPoint.y=point.y+linkLendth,textPaint);
                canvas.drawText(data.getSymbol(),endPoint.x,endPoint.y+textPaint.getTextSize(),textPaint);
            }else{
                canvas.drawLine(endPoint.x=point.x,point.y,point.x,endPoint.y=point.y-linkLendth,textPaint);
                canvas.drawText(data.getSymbol(),endPoint.x,endPoint.y,textPaint);
            }

        }

    }
//    圆点坐标：(x0,y0)
//    半径：r
//    角度：a0
//
//    则圆上任一点为：（x1,y1）
//    x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
//    y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )
    /**
     * 获取圆上任一点的坐标
     *
     * @param angle
     * @return
     */
    public Point getXY(int angle) {
        int x = (int) (centerPoint.x +radius * Math.cos(Math.PI * (angle) / 180));
        int y = (int) (centerPoint.y + radius * Math.sin(Math.PI * (angle) / 180));
        return new Point(x, y);
    }

    private int evaluateColor(int startValue, int endValue, float fraction) {
        if (fraction <= 0) {
            return startValue;
        }
        if (fraction >= 1) {
            return endValue;
        }
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) | ((startR + (int) (fraction * (endR - startR))) << 16) | ((startG + (int) (fraction * (endG - startG))) << 8) | ((startB + (int) (fraction * (endB - startB))));
    }

    public void setData(List<BeanPortfolioData> data) {
        this.data = data;
    }
}
