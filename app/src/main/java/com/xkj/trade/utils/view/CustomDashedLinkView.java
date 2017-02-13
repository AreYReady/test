package com.xkj.trade.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.xkj.trade.bean.BeanDrawRealTimePriceData;

/**
 * Created by huangsc on 2017-01-12.
 * TODO:实时报价虚线
 */

public class CustomDashedLinkView extends View {
    private Paint paint;
    private Path path;
    private PathEffect effects = null;
    private float y = 0;

    public CustomDashedLinkView(Context context) {
        this(context, null);
    }

    public CustomDashedLinkView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomDashedLinkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        path = new Path();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        effects = new DashPathEffect(new float[]{60, 20, 60, 20}, 1);
        paint.setPathEffect(effects);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (y > 0) {
            path.reset();
            path.moveTo(0, y);
            path.lineTo(getWidth(), y);
            int width = this.getWidth();
            // PathEffect是用来控制绘制轮廓(线条)的方式
            // 代码中的float数组,必须是偶数长度,且>=2,指定了多少长度的实线之后再画多少长度的空白.如本代码中,绘制长度5的实线,再绘制长度5的空白,再绘制长度5的实线,再绘制长度5的空白,依次重复.1是偏移量,可以不用理会.
            canvas.drawPath(path, paint);
        }
    }

    public void refresh(BeanDrawRealTimePriceData beanDrawRealTimePriceData) {
        this.y = beanDrawRealTimePriceData.getY();
        paint.setColor(beanDrawRealTimePriceData.getColor());
        postInvalidate();
    }
}
