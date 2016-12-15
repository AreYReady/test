package trade.xkj.com.trade.Utils.view;

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

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.SystemUtil;

/**
 * Created by huangsc on 2016-12-12.
 * TODO:
 */

public class CustomSeekBar extends FrameLayout {
    private TextView tv0,tv1,tv2,tv3,tv4;
    private SeekBar mSeekBar;
    private String TAG= SystemUtil.getTAG(this);
    private String tvString0="10k",tvString1="100k",tvString2="1M",tvString3="10M",tvString4="100M";
    private AddSubEditText mASEditText;
    private int max=40000;
    private int maxPrice=100000000;
    private int setProgress;
    private boolean isAmountChange=false;
    int i = max / 4;
    public CustomSeekBar(Context context) {
        this(context,null);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        this(context, null,0);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = LayoutInflater.from(context).inflate(R.layout.v_seekbar, null);
        mASEditText=(AddSubEditText)inflate.findViewById(R.id.aset_aset);
        mASEditText.setAmountChangeListener(new AddSubEditText.AmountChangeListener() {
            @Override
            public void amountChange(int count) {
                Log.i(TAG, "amountChange: ");
                isAmountChange=true;
                if(count<=10){
                    setProgress=count*1000;
                }else if(count<=100){
                    setProgress=(count-10)*100+i;
                }else if(count<=1000){
                    setProgress=(count-100)*10+2*i;
                }else if(count<=10000){
                    setProgress=(count-1000)+3*i;
                }
                handler.sendEmptyMessage(0);
            }
        });
        tv0=(TextView)inflate.findViewById(R.id.amount_seek_bar_label0);
        tv1=(TextView)inflate.findViewById(R.id.amount_seek_bar_label1);
        tv2=(TextView)inflate.findViewById(R.id.amount_seek_bar_label2);
        tv3=(TextView)inflate.findViewById(R.id.amount_seek_bar_label3);
        tv4=(TextView)inflate.findViewById(R.id.amount_seek_bar_label4);
        mSeekBar=(SeekBar)inflate.findViewById(R.id.amount_seek_bar_seek);
        addView(inflate);
        tv0.setText(tvString0);
//        tv1.setText(tvString1);
        tv2.setText(tvString2);
//        tv3.setText(tvString3);
        tv4.setText(tvString4);
        mSeekBar.setMax(max);
        mASEditText.setMaxPrice(maxPrice);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int count;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!isAmountChange) {
                    if (progress <= i) {
                        count = progress / 1000;
                    } else if (progress <= 2 * i) {
                        count = (progress - i) / 100+10;
                    } else if (progress <= 3 * i) {
                        count = (progress - 2 * i) / 10+100;
                    } else if (progress <= 4 * i) {
                        count = (progress - 3 * i)+1000;
                    }
                    Log.i(TAG, "onProgressChanged: count " + count + "  progress " + progress);
                    mASEditText.followSeekBarChange(count);
                }
                //一次操作结束
                isAmountChange=false;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    break;
                case 1:
                    break;
            }
            mSeekBar.setProgress(setProgress);
        }
    };
//
}
