package com.xkj.trade.utils.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.xkj.trade.R;


/**
 * Created by huangsc on 2016-12-20.
 * TODO:
 */

public class CustomPeriodButtons2 extends RelativeLayout implements View.OnClickListener {

    private Paint p;
    private RadioButton mRbPeriod;
    private RadioGroup mRgPeriodButtons;
    private View inflate;
    private CheckChangeListener listener;

    public CustomPeriodButtons2(Context context) {
       this(context,null);
    }

    public CustomPeriodButtons2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomPeriodButtons2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate = LayoutInflater.from(context).inflate(R.layout.v_master_period_buttons_draw, null);
        addView(inflate);
        mRbPeriod=(RadioButton) inflate.findViewById(R.id.rb_period);
        mRbPeriod.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRbPeriod.setVisibility(View.GONE);
                mRgPeriodButtons.setVisibility(View.VISIBLE);
            }
        });
        mRbPeriod.setPressed(true);
        mRgPeriodButtons=(RadioGroup)inflate.findViewById(R.id.rg_period_buttons);
        for(int i=0;i<mRgPeriodButtons.getChildCount();i++){
            mRgPeriodButtons.getChildAt(i).setOnClickListener(this);
        }
        p=new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        mRbPeriod.setVisibility(VISIBLE);
        mRgPeriodButtons.setVisibility(GONE);
        mRbPeriod.setText(((RadioButton)v).getText()+" >");
        if(listener!=null){
            listener.CheckChange(inflate.findViewById(mRgPeriodButtons.getCheckedRadioButtonId()).getTag().toString());
        }
    }
    public interface CheckChangeListener{
        void CheckChange(String period);
    }
    public void addCheckChangeListener(CheckChangeListener listener){
        this.listener=listener;
    }

}
