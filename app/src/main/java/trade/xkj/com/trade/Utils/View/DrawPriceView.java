package trade.xkj.com.trade.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import trade.xkj.com.trade.base.MyApplication;
import trade.xkj.com.trade.R;
import trade.xkj.com.trade.utils.SystemUtil;
import trade.xkj.com.trade.bean.BeanDrawPriceData;
import trade.xkj.com.trade.bean.BeanDrawRealTimePriceData;

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
            canvas.drawText(beanDrawPriceData.getPriceString(), getResources().getDimension(R.dimen.space_big), beanDrawPriceData.getPriceY(), mGarkPaint);
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
