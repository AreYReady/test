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

public class CustomPeriodButtons extends RelativeLayout implements View.OnClickListener {
//    private RadioButton mRbMinute1;
//    private RadioButton getmRbMinute5;
//    private RadioButton mRbMinute15;
//    private RadioButton mRbMinute30;
//    private RadioButton mRbhour1;
//    private RadioButton mRbhour4;
//    private RadioButton mRbday1;
//    private RadioButton mRbWeek1;
    private Paint p;
    private RadioButton mRbPeriod;
    private RadioGroup mRgPeriodButtons;
    private View inflate;
    private CheckChangeListener listener;

    public CustomPeriodButtons(Context context) {
       this(context,null);
    }

    public CustomPeriodButtons(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomPeriodButtons(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate = LayoutInflater.from(context).inflate(R.layout.v_period_buttons_draw, null);
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

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        canvas.drawColor(Color.RED);
//        int sc = canvas.saveLayer(0,0,300,300, null, Canvas.ALL_SAVE_FLAG);
//        canvas.drawText("画圆角矩形:", 10, 260, p);
//        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        p.setStyle(Paint.Style.FILL);//充满
//        p.setColor(Color.LTGRAY);
//        p.setAntiAlias(true);// 设置画笔的锯齿效果
//        canvas.drawText("画圆角矩形:", 10, 260, p);
//        RectF oval3 = new RectF(80, 260, 200, 300);// 设置个新的长方形
//        canvas.drawRoundRect(oval3, 20, 15, p);//第二个参数是x半径，第三个参数是y半径
//        p.setXfermode(null);
//        canvas.restoreToCount(sc);
//    }
}
