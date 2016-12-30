package trade.xkj.com.trade.Utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.bean.BeanPortfolioData;

/**
 * Created by huangsc on 2016-12-30.
 * TODO:
 */

public class CustomPortfolio extends View {
    Paint basePaint;
    Paint portfolioPaint;
    int strokeWidth = 30;
    List<BeanPortfolioData> data;

    List<Integer> selectColors ;


    public CustomPortfolio(Context context) {
        this(context, null);
    }

    public CustomPortfolio(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPortfolio(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        portfolioPaint.setStrokeWidth(strokeWidth);
        portfolioPaint.setStyle(Paint.Style.STROKE);
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
    int center;
    int radius;

    @Override
    protected void onDraw(Canvas canvas) {
        if (data != null) {
            center = getWidth() / 2;
            radius = center - strokeWidth / 2;
            RectF rectf = new RectF(center - radius, center - radius, center + radius, center + radius);
            canvas.drawCircle(center, center, radius, basePaint);
            for (int i = 0; i < data.size(); i++) {
                canvas.drawArc(rectf, data.get(i).getBeginAngle(), data.get(i).getEndAngle(), false, portfolioPaint);
                int x;
                portfolioPaint.setColor(getResources().getColor(selectColors.get(x=i%selectColors.size())));
                Log.i("hsc", "onDraw: "+portfolioPaint.getColor()+"  x  "+x);

            }
        }
    }

    /**
     * 获取圆上任一点的坐标
     *
     * @param angle
     * @return
     */
    public Point getXY(int angle) {
        int x = (int) (center - radius * Math.sin(Math.PI * (angle - 90) / 180));
        int y = (int) (center - radius - 10
                + radius * Math.cos(Math.PI * (angle - 90) / 180));
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
