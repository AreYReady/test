package com.xkj.trade.utils.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.SystemUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.xkj.trade.R.id.tv_sub;


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
    private int minPrice = 1000;
    //加减的基数
    private int baseNumber = 1000;
    private int maxPrice=10000000;
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
        addView = (TextView) inflate.findViewById(R.id.tv_add);
        subView = (TextView) inflate.findViewById(tv_sub);
        editText = (EditText) inflate.findViewById(R.id.et_amount);
        editText.setText(MoneyUtil.parseMoney(amount));
        hideSoftInputMethod(editText);
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

    private boolean isOnLongClick;
    private SubThread subThread;
    private PlusThread plusThread;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int eventAction = event.getAction();
        Log.i(TAG, "onTouch: ");
        switch (v.getId()) {
            case R.id.tv_sub:
                if (eventAction == MotionEvent.ACTION_DOWN) {
                    subThread = new SubThread();
                    isOnLongClick = true;
                    subThread.start();
                } else if (eventAction == MotionEvent.ACTION_UP) {
                    if (subThread != null) {
                        isOnLongClick = false;
                    }
                } else if (eventAction == MotionEvent.ACTION_MOVE) {
                    if (subThread != null) {
                        isOnLongClick = true;
                    }
                }
                break;
            case R.id.tv_add:
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

    class SubThread extends Thread {
        @Override
        public void run() {
            while (isOnLongClick) {
                try {
                    Thread.sleep(100);
                    Log.i(TAG, "run: ");
                    if(amount<=minPrice){
                        amount=minPrice;
                    }
                    amount = amount - baseNumber;
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
                    Thread.sleep(100);
//                    editText.setText(MoneyUtil.parseMoney(amount));
//                    myHandler.sendEmptyMessage(2);
                    amount = amount + baseNumber;
                    if(amount>=maxPrice){
                        amount=maxPrice;
                    }
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
                        listener.amountChange(amount);
                    }
                    break;
                //follow
                case 1:
                    isFollow=false;
                    break;
            }
            editText.setText(MoneyUtil.parseMoney(amount));
            if(editText.getVisibility()!=VISIBLE){
                editText.setVisibility(VISIBLE);
            }
        }
    };




    public void followSeekBarChange(int count) {
        isFollow=true;
        amount = baseNumber * count;
        handler.sendEmptyMessage(1);
        if(editText.getVisibility()!=VISIBLE){
            editText.setVisibility(VISIBLE);
        }
    }
    public interface AmountChangeListener{
//        void amountChange(int count);
        void amountChange(int amount);
    }
    private AmountChangeListener listener;
    public void setAmountChangeListener(AmountChangeListener listener){
        this.listener=listener;
    }
    public void setData(int minPrice, int maxPrice, int baseNumber){
        this.maxPrice=maxPrice;
        this.minPrice=minPrice;
        this.baseNumber =baseNumber;
        editText.setText(MoneyUtil.parseMoney(minPrice));
    }
    public String getNumbel(){
        return editText.getText().toString();
    }
    public void setNumberTextInvisible(){
        editText.setVisibility(INVISIBLE);
    }
    public void setNumberTextVisible(){
        editText.setVisibility(VISIBLE);
    }
    // 隐藏系统键盘
    public void hideSoftInputMethod(EditText ed){

        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if(currentVersion >= 16){
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        }
        else if(currentVersion >= 14){
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }

        if(methodName == null){
            ed.setInputType(InputType.TYPE_NULL);
        }
        else{
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(ed, false);
            } catch (NoSuchMethodException e) {
                ed.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
