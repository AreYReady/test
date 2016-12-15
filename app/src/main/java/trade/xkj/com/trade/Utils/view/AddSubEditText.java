package trade.xkj.com.trade.Utils.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.MoneyUtil;
import trade.xkj.com.trade.Utils.SystemUtil;

import static trade.xkj.com.trade.R.id.tv_add;
import static trade.xkj.com.trade.R.id.tv_sub;

/**
 * Created by huangsc on 2016-12-12.
 * TODO:
 */

public class AddSubEditText extends FrameLayout implements View.OnTouchListener {
    private String TAG = SystemUtil.getTAG(this);
    private TextView addView;
    private TextView subView;
    private EditText editText;
    private int amount;
    private int minPrice = 10000;
    //加减的基数
    private int baseNumbel = 10000;
    private int maxPrice=-1;
    //判断是否是跟随SeekBar改变操作
    private boolean isFollow=false;

    public AddSubEditText(Context context) {
        this(context, null);
    }

    public AddSubEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddSubEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = LayoutInflater.from(context).inflate(R.layout.v_add_sub_edittext, null);
        addView(inflate);
        addView = (TextView) inflate.findViewById(tv_add);
        subView = (TextView) inflate.findViewById(R.id.tv_sub);
        editText = (EditText) inflate.findViewById(R.id.et_amount);
        amount = minPrice;
        editText.setText(MoneyUtil.parseMoney(amount));
//        addView.setOnClickListener(this);
//        subView.setOnClickListener(this);
        subView.setLongClickable(true);
        subView.setOnTouchListener(this);
        addView.setOnTouchListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (amount < minPrice) {
                    amount = minPrice;
                    editText.setText(MoneyUtil.parseMoney(amount));
                }
                if(maxPrice!=-1&&amount>maxPrice){
                    Log.i(TAG, "afterTextChanged: amount"+amount);
                    amount=maxPrice;
                    editText.setText(MoneyUtil.parseMoney(maxPrice));
                }
            }
        });
    }


    public void setBaseNumbel(int baseNumbel) {
        this.baseNumbel = baseNumbel;
    }

    private boolean isOnLongClick;
    private MiusThread miusThread;
    private PlusThread plusThread;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int eventAction = event.getAction();
        Log.i(TAG, "onTouch: ");
        editText.setVisibility(VISIBLE);
        switch (v.getId()) {
            case tv_sub:
                if (eventAction == MotionEvent.ACTION_DOWN) {
                    miusThread = new MiusThread();
                    isOnLongClick = true;
                    miusThread.start();
                } else if (eventAction == MotionEvent.ACTION_UP) {
                    if (miusThread != null) {
                        isOnLongClick = false;
                    }
                } else if (eventAction == MotionEvent.ACTION_MOVE) {
                    if (miusThread != null) {
                        isOnLongClick = true;
                    }
                }
                break;
            case tv_add:
                if (eventAction == MotionEvent.ACTION_DOWN) {
                    plusThread = new PlusThread();
                    isOnLongClick = true;
                    plusThread.start();
                } else if (eventAction == MotionEvent.ACTION_UP) {
                    if (plusThread != null) {
                        isOnLongClick = false;
                    }
                } else if (eventAction == MotionEvent.ACTION_MOVE) {
                    if (plusThread != null) {
                        isOnLongClick = true;
                    }
                }
                break;
        }
        return true;
    }

    class MiusThread extends Thread {
        @Override
        public void run() {
            while (isOnLongClick) {
                try {
                    Thread.sleep(200);
                    Log.i(TAG, "run: ");
                    amount = amount - baseNumbel;
                    handler.sendEmptyMessage(0);
//                    editText.setText(MoneyUtil.parseMoney(amount));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }
    }
    //加操作
    class PlusThread extends Thread {
        @Override
        public void run() {
            while (isOnLongClick) {
                try {
                    Thread.sleep(200);
//                    editText.setText(MoneyUtil.parseMoney(amount));
//                    myHandler.sendEmptyMessage(2);
                    amount = amount + baseNumbel;
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //onTouch
                case 0:
                    if(listener!=null&&!isFollow) {
                        listener.amountChange((amount-minPrice) / baseNumbel);
                    }
                    break;
                //follow
                case 1:
                    isFollow=false;
                    break;
            }
            editText.setText(MoneyUtil.parseMoney(amount));
        }
    };




    public void followSeekBarChange(int count) {
        isFollow=true;
        amount = baseNumbel * count+ minPrice;
        handler.sendEmptyMessage(1);
    }
    public interface AmountChangeListener{
        void amountChange(int count);
    }
    private AmountChangeListener listener;
    public void setAmountChangeListener(AmountChangeListener listener){
        this.listener=listener;
    }
    public void setMaxPrice(int maxPrice){
        this.maxPrice=maxPrice;
    }
    public void setNumbelTextInvisible(){
        editText.setVisibility(INVISIBLE);
    }
    public void setNumbelTextVisible(){
        editText.setVisibility(VISIBLE);
    }
}
