package com.xkj.trade.utils.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.utils.BigdecimalUtils;
import com.xkj.trade.utils.SystemUtil;

/**
 * Created by huangsc on 2016-12-12.
 * TODO:
 */

public class CustomSeekBar extends FrameLayout {
    private TextView tv0, tv1, tv2, tv3, tv4;
    private SeekBar mSeekBar;
    private String TAG = SystemUtil.getTAG(this);
    private String tvString0 = "10k", tvString1 = "100k", tvString2 = "1M", tvString3 = "10M", tvString4 = "100M";
    private AddSubEditText mASEditText;
    private int max = 40000;
    //加减的基数
    private int baseNumber = 10000;
    private int[] ints = new int[]{10000, 100000, 1000000, 10000000, 100000000};
    private double setProgress;
    private boolean isAmountChange = false;
    int i = max / 4;

    public CustomSeekBar(Context context) {
        this(context, null);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = LayoutInflater.from(context).inflate(R.layout.v_seekbar, null);
        mASEditText = (AddSubEditText) inflate.findViewById(R.id.aset_aset);
        mASEditText.setAmountChangeListener(new AddSubEditText.AmountChangeListener() {
            @Override
            public void amountChange(int amount) {
                try {

//                    Log.i(TAG, "amountChange: " + amount);
                    isAmountChange = true;
                    if (amount <= ints[1]) {
                        setProgress = (amount - ints[0]) * i / (ints[1] - ints[0]) + 0 * i;
                    } else if (amount <= ints[2]) {
                        setProgress =BigdecimalUtils.mul(BigdecimalUtils.div(amount - ints[1], ints[2] - ints[1], 5),i)+1*i;
                    } else if (amount <= ints[3]) {
                        setProgress =BigdecimalUtils.mul(BigdecimalUtils.div(amount - ints[2], ints[3] - ints[2], 5),i)+2*i;
                    } else if (amount <= ints[4]) {
                        setProgress =BigdecimalUtils.mul(BigdecimalUtils.div(amount - ints[3], ints[4] - ints[3], 5),i)+3*i;
                    }
                    handler.sendEmptyMessage(0);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        tv0 = (TextView) inflate.findViewById(R.id.amount_seek_bar_label0);
        tv1 = (TextView) inflate.findViewById(R.id.amount_seek_bar_label1);
        tv2 = (TextView) inflate.findViewById(R.id.amount_seek_bar_label2);
        tv3 = (TextView) inflate.findViewById(R.id.amount_seek_bar_label3);
        tv4 = (TextView) inflate.findViewById(R.id.amount_seek_bar_label4);
        mSeekBar = (SeekBar) inflate.findViewById(R.id.amount_seek_bar_seek);
        addView(inflate);
        tv0.setText(tvString0);
        tv1.setText(tvString1);
        tv2.setText(tvString2);
        tv3.setText(tvString3);
        tv4.setText(tvString4);
        mSeekBar.setMax(max);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            double count;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    double x = BigdecimalUtils.div((double) i, (double) (ints[1] - ints[0]) / baseNumber, 5);
                    double x1 = BigdecimalUtils.div((double) i, (double) (ints[2] - ints[1]) / baseNumber, 5);
                    double x2 = BigdecimalUtils.div((double) i, (double) (ints[3] - ints[2]) / baseNumber, 5);
                    double x3 = BigdecimalUtils.div((double) i, (double) (ints[4] - ints[3]) / baseNumber, 5);
//                    Log.i(TAG, "onProgressChanged: " + x + "  " + x1 + "  " + x2 + "  " + x3 + "  ");
//                    Log.i(TAG, "onProgressChanged: " + ints[1]);

                    if (!isAmountChange) {
                        if (progress <= i) {
                            count = progress / x + BigdecimalUtils.div((double) ints[0], (double) baseNumber, 5);
                        } else if (progress <= 2 * i) {
                            count = (progress - i) / x1 + BigdecimalUtils.div((double) ints[1], (double) baseNumber, 5);
                        } else if (progress <= 3 * i) {
                            count = (progress - 2 * i) / x2 + BigdecimalUtils.div((double) ints[2], (double) baseNumber, 5);
                        } else if (progress <= 4 * i) {
                            count = (progress - 3 * i) / x3 + BigdecimalUtils.div((double) ints[3], (double) baseNumber, 5);
                        }
                        Log.i(TAG, "onProgressChanged: count " + count + "  progress " + progress);
                        mASEditText.followSeekBarChange((int) count);
                    }
                    //一次操作结束
                    isAmountChange = false;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
            }
            mSeekBar.setProgress((int) setProgress);
        }
    };

//

    /**
     * 设置尺度：例如ints{10000,100000,100000,1000000,1000000}
     *
     * @param ints
     */
    public void setData(int[] ints,int baseNumber) {
        mASEditText.setData(ints[0], ints[4], baseNumber);
        this.baseNumber=baseNumber;
        this.ints = ints;
        tv0.setText(String.valueOf(ints[0]));
        tv1.setText(String.valueOf(ints[1]));
        tv2.setText(String.valueOf(ints[2]));
        tv3.setText(String.valueOf(ints[3]));
        tv4.setText(String.valueOf(ints[4]));
    }
    private float getXPosition(float progress){
        float v = (progress * mSeekBar.getWidth()) / mSeekBar.getMax()+mSeekBar.getX();
        Log.i(TAG, "getXPosition: "+v);
        return v;
    }


}
