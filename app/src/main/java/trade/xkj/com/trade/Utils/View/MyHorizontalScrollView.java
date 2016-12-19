package trade.xkj.com.trade.Utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.SystemUtil;

/**
 * Created by huangsc on 2016-11-30.
 * TODO:
 */

public class MyHorizontalScrollView extends HorizontalScrollView {
    private ScrollViewListener mScrollViewListener;
    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyHorizontalScrollView(Context context) {
        this(context,null);
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

        void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy);

    }

}
