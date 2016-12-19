package trade.xkj.com.trade.Utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import trade.xkj.com.trade.Utils.MoneyUtil;
import trade.xkj.com.trade.Utils.SystemUtil;

/**
 * Created by huangsc on 2016-11-30.
 * TODO:
 */

public class MyHorizontalScrollView2 extends HorizontalScrollView {
    private ScrollViewListener mScrollViewListener;
    private String TAG = SystemUtil.getTAG(this);
    private Context context;
    private View view;

    public MyHorizontalScrollView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public MyHorizontalScrollView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHorizontalScrollView2(Context context) {
        this(context, null);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.mScrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (mScrollViewListener != null) {
            mScrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface ScrollViewListener {

        void onScrollChanged(MyHorizontalScrollView2 scrollView, int x, int y, int oldx, int oldy);

        void onScaleDraw(float scaleSize);
    }


    private HistoryTradeView.MODE mode = HistoryTradeView.MODE.NONE;
    private float scaleSize = 1f;
    private double downDistance;
    private double moveDistance;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /** 处理单点、多点触摸 **/
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: MotionEvent.ACTION_DOWN");
                if (mode == HistoryTradeView.MODE.YES) {
                    mode = HistoryTradeView.MODE.NONE;
                }
//                onTouchDown(event);
                break;
            // 多点触摸
            case MotionEvent.ACTION_POINTER_DOWN:
                if (mode == HistoryTradeView.MODE.NONE) {
                    mode = HistoryTradeView.MODE.YES;
                    downDistance = getDistance(event);
                }
//                onPointerDown(event);

                Log.i(TAG, "onTouchEvent: MotionEvent.ACTION_POINTER_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == HistoryTradeView.MODE.YES) {
                    moveDistance = getDistance(event);
                    scaleSize = Float.valueOf(MoneyUtil.mulPriceToString(scaleSize, 1 + (MoneyUtil.subPrice(moveDistance, downDistance) / getWidth())));
                    if (scaleSize >= 2) {
                        scaleSize = 2;
                    } else if (scaleSize <= 0.5f) {
                        scaleSize = 0.5f;
                    }
                    downDistance = moveDistance;
                    onScaleDraw();
                }
//                onTouchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent: MotionEvent.ACTION_UP");
                break;

            // 多点松开
            case MotionEvent.ACTION_POINTER_UP:
                if (mode == HistoryTradeView.MODE.YES) {
                    mode = HistoryTradeView.MODE.NONE;
                }

                Log.i(TAG, "onTouchEvent: MotionEvent.ACTION_POINTER_UP");
//                mode = MODE.NONE;
                /** 执行缩放还原 **/
//                if (isScaleAnim) {
//                    doScaleAnim();
//                }
                break;
        }
        if (mode == HistoryTradeView.MODE.NONE)
            return super.onTouchEvent(event);
        return true;
    }

    private void onScaleDraw() {
        if (mScrollViewListener != null) {
            mScrollViewListener.onScaleDraw(scaleSize);
            Log.i(TAG, "onScaleDraw: scaleSize " + scaleSize);
        }
    }

    /**
     * 获取两点的距离
     **/
    double getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return Math.sqrt(x * x + y * y);
    }

}
