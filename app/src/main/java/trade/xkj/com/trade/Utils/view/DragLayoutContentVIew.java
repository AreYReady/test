package trade.xkj.com.trade.utils.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.utils.SystemUtil;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class DragLayoutContentVIew extends ViewGroup {
    private final ViewDragHelper mViewDragHelper;
    private View mFirstView;
    private String TAG= SystemUtil.getTAG(this);

    public DragLayoutContentVIew(Context context) {
        this(context,null);
    }

    public DragLayoutContentVIew(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragLayoutContentVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewDragHelper=ViewDragHelper.create(this,1.0f,new MyCallBack());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mFirstView =findViewById(R.id.ll_draw_trade_context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(TAG, "onLayout:mFirstView.getHeight() "+mFirstView.getHeight());
        mFirstView.layout(0,0,0,150);
    }
    private class  MyCallBack extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {

            int PaddingTop=getPaddingTop();
            int paddingBottom = mFirstView.getPaddingBottom();
            if(PaddingTop>top){
                return PaddingTop;
            }
            if(getHeight()-mFirstView.getHeight()-300<top){
                return getHeight()-mFirstView.getHeight()-300;
            }
            return top;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
