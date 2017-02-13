package com.xkj.trade.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.utils.DateUtils;
import com.xkj.trade.utils.SystemUtil;

import java.util.Calendar;


/**
 * Created by huangsc on 2016-12-13.
 * TODO:
 */

public class CustomDateSelect extends FrameLayout {
    private NumberPicker mDateSpinner;
    private NumberPicker mHourSpinner;
    private NumberPicker mMinuteSpinner;
    private Calendar mDate;
    private int mHour, mMinute;
    private String[] mDateDisplayValues = new String[7];
    private OnDateTimeChangedListener mOnDateTimeChangedListener;
    private Context context;
    private String mDateString;
    private Boolean mIsVisibity;
    private TextView mTvdate;
    private TextView mTvdesc;
    private ImageView mIvState;
    private LinearLayout llDateSelect;
    private final String TAG= SystemUtil.getTAG(this);

    public CustomDateSelect(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomDateSelect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomGroup);
        mDateString = typedArray.getString(R.styleable.CustomGroup_desc_text);
        mIsVisibity = typedArray.getBoolean(R.styleable.CustomGroup_iamge_visibility, true);
        init();
    }

    public CustomDateSelect(Context context) {
        this(context,null);
    }

    private void init() {
          /*
         *獲取系統時間
         */
        mDate = Calendar.getInstance();
        mHour = mDate.get(Calendar.HOUR_OF_DAY);
        mMinute = mDate.get(Calendar.MINUTE);

        /**
         * 加载布局
         */
        inflate(context, R.layout.v_date_select, this);
        /**
         * 初始化控件
         */
        mDateSpinner = (NumberPicker) this.findViewById(R.id.np_date);
        mDateSpinner.setMinValue(0);
        mDateSpinner.setMaxValue(6);
        updateDateControl();
        mDateSpinner.setOnValueChangedListener(mOnDateChangedListener);

        mHourSpinner = (NumberPicker) this.findViewById(R.id.np_hour);
        mHourSpinner.setMaxValue(23);
        mHourSpinner.setMinValue(0);
        mHourSpinner.setValue(mHour);
        mHourSpinner.setOnValueChangedListener(mOnHourChangedListener);

        mMinuteSpinner = (NumberPicker) this.findViewById(R.id.np_minute);
        mMinuteSpinner.setMaxValue(59);
        mMinuteSpinner.setMinValue(0);
        mMinuteSpinner.setValue(mMinute);
        mMinuteSpinner.setOnValueChangedListener(mOnMinuteChangedListener);
        llDateSelect=(LinearLayout)this.findViewById(R.id.ll_date_select);
        mTvdate=(TextView)this.findViewById(R.id.et_date);
        mTvdesc=(TextView)this.findViewById(R.id.tv_desc);
        mTvdate.setText(DateUtils.getShowTime(mDate.getTime()));
        if(mDateString!=null)
            mTvdesc.setText(mDateString);
        mIvState=(ImageView)this.findViewById(R.id.iv_state);
        mIvState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");
                if(mIvState.isSelected()){
                    mIvState.setSelected(false);
                    llDateSelect.setVisibility(GONE);
                    mTvdate.setVisibility(INVISIBLE);
                }else{
                    mIvState.setSelected(true);
                    llDateSelect.setVisibility(VISIBLE);
                    mTvdate.setVisibility(VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 控件监听器
     */
    private NumberPicker.OnValueChangeListener mOnDateChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
            /**
             * 更新日期
             */
            updateDateControl();
            /**
             * 给接口传值
             */
            onDateTimeChanged();
        }
    };

    private NumberPicker.OnValueChangeListener mOnHourChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mHour = mHourSpinner.getValue();
            onDateTimeChanged();
        }
    };

    private NumberPicker.OnValueChangeListener mOnMinuteChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mMinute = mMinuteSpinner.getValue();
            onDateTimeChanged();
        }
    };

    private void updateDateControl() {
        /**
         * 星期几算法
         */
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mDate.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, -7 / 2 - 1);
        mDateSpinner.setDisplayedValues(null);
        for (int i = 0; i < 7; ++i) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            mDateDisplayValues[i] = (String) DateFormat.format("MM.dd EEEE",
                    cal);
        }
        mDateSpinner.setDisplayedValues(mDateDisplayValues);
        mDateSpinner.setValue(7 / 2);
        mDateSpinner.invalidate();
    }


    /*
     *接口回调 参数是当前的View 年月日小时分钟
     */
    public interface OnDateTimeChangedListener {
        void onDateTimeChanged(CustomDateSelect view, int year, int month,
                               int day, int hour, int minute);
    }

    /*
     *对外的公开方法
     */
    public void setOnDateTimeChangedListener(OnDateTimeChangedListener callback) {
        mOnDateTimeChangedListener = callback;
    }

    private void onDateTimeChanged() {
        if (mOnDateTimeChangedListener != null) {
            mOnDateTimeChangedListener.onDateTimeChanged(this, mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH),
                    mDate.get(Calendar.DAY_OF_MONTH), mHour, mMinute);
        }
        mDate.set(Calendar.HOUR_OF_DAY, mHour);
        mDate.set(Calendar.MINUTE, mMinute);
        mDate.set(Calendar.SECOND, 0);
        mTvdate.setText(DateUtils.getShowTime(mDate.getTime()));
    }
}
