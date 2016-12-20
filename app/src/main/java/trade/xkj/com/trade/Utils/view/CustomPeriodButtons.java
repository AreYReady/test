package trade.xkj.com.trade.Utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import trade.xkj.com.trade.R;

/**
 * Created by huangsc on 2016-12-20.
 * TODO:
 */

public class CustomPeriodButtons extends RelativeLayout implements View.OnClickListener {
//    private RadioButton mRbMinute1;
//    private RadioButton getmRbMinute5;
//    private RadioButton mRbMinute15;
//    private RadioButton mRbMinute30;
//    private RadioButton mRbhour1;
//    private RadioButton mRbhour4;
//    private RadioButton mRbday1;
//    private RadioButton mRbWeek1;
    private RadioButton mRbPeriod;
    private RadioGroup mRgPeriodButtons;

    public CustomPeriodButtons(Context context) {
       this(context,null);
    }

    public CustomPeriodButtons(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomPeriodButtons(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = LayoutInflater.from(context).inflate(R.layout.v_period_buttons_draw, null);
        addView(inflate);
        mRbPeriod=(RadioButton) inflate.findViewById(R.id.rb_period);
        mRbPeriod.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRbPeriod.setVisibility(View.GONE);
                mRgPeriodButtons.setVisibility(View.VISIBLE);
            }
        });
        mRgPeriodButtons=(RadioGroup)inflate.findViewById(R.id.rg_period_buttons);
        for(int i=0;i<mRgPeriodButtons.getChildCount();i++){
            mRgPeriodButtons.getChildAt(i).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        mRbPeriod.setVisibility(VISIBLE);
        mRgPeriodButtons.setVisibility(GONE);
        mRbPeriod.setText(((RadioButton)v).getText()+" >");
    }
}
