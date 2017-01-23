package trade.xkj.com.trade.utils.view;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by huangsc on 2016-12-09.
 * TODO:
 */

public class MyScrollView extends ScrollView {
    private ScrollListener scrollListener;

    View view;
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        view=findViewById(R.id.space);
        view=((ViewGroup)this.getChildAt(0)).getChildAt(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float x = ev.getRawX(); // 获取相对于屏幕左上角的 x 坐标值
        float y = ev.getRawY(); // 获取相对于屏幕左上角的 y 坐标值
        RectF rectF = calcViewScreenLocation(view);
        if(rectF.contains(x,y)){
            return false;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public static RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollListener != null) {
            scrollListener.onScroll(t, l);
        }
    }

    public void registerListener(ScrollListener scrollListener){
        this.scrollListener = scrollListener;
    }
    public interface  ScrollListener{
        void onScroll(int top,int left);
    }
}
