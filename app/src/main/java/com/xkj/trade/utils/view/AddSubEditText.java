package com.xkj.trade.utils.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
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
    private String amount="0";
    private int mDitigs=0;
    private String minPrice = "0.01";
    //加减的基数
    private String baseNumber = "0.01";
    private String maxPrice="100";
    //判断是否是跟随SeekBar改变操作
    private boolean isFollow=false;

    public AddSubEditText(Context context) {
        this(context, null);
    }

    public AddSubEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddSubEditText(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = LayoutInflater.from(context).inflate(R.layout.v_add_sub_edittext, null);
        addView(inflate);
        addView = (TextView) inflate.findViewById(R.id.tv_add);
        subView = (TextView) inflate.findViewById(tv_sub);
        editText = (EditText) inflate.findViewById(R.id.et_amount);
//        editText.setText(String.valueof(amount));
        editText.setText(String.valueOf(amount));
//        hideSoftInputMethod(editText);
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
                amount=editText.getText().toString();
                if(listener!=null){
                    if(editText.getText().toString().equals("")||editText.getText().toString().endsWith(".")){
                        amount="0";
                        editText.setText("0");
                    }
                    handler.sendEmptyMessage(2);
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
                    Thread.sleep(50);
                    amount=MoneyUtil.subPriceToString(amount,baseNumber);
                    if(Double.valueOf(amount)>=Double.valueOf(maxPrice)){
                        amount=maxPrice;
                    }
                        amount=Double.valueOf(amount)<0?"0":amount;

                    handler.sendEmptyMessage(0);
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
                    Thread.sleep(50);
//                    editText.setText(String.valueOf(amount));
//                    myHandler.sendEmptyMessage(2);
                    amount= MoneyUtil.addPrice(amount,baseNumber);
                    if(Double.valueOf(amount)<=Double.valueOf(minPrice)){
                        amount=minPrice;
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
                        if(editText.getText().equals("")||editText.getText().equals(".")){
                            amount="0";
                        }
                        editText.setText(amount);
                        if(editText.getVisibility()!=VISIBLE){
                            editText.setVisibility(VISIBLE);
                        }
//                            listener.amountChange(amount);
                    }
                    break;
                //follow
                case 1:
                    isFollow=false;
                    editText.setText(amount);
                    if(editText.getVisibility()!=VISIBLE){
                        editText.setVisibility(VISIBLE);
                    }
                    break;
                case 2:
                    listener.amountChange(amount);
                    break;
            }

        }
    };




    public void followSeekBarChange(int count) {
        isFollow=true;
        amount=MoneyUtil.mulPrice(baseNumber,String.valueOf(count));
        handler.sendEmptyMessage(1);
    }
    public interface AmountChangeListener{
//        void amountChange(int count);
        void amountChange(String amount);
    }
    private AmountChangeListener listener;
    public void setAmountChangeListener(AmountChangeListener listener){
        this.listener=listener;
    }
    public void setEditText(String text){
        editText.setText(text);
    }
    public void setData(int minPrice, int maxPrice, int baseNumber){
        this.maxPrice=String.valueOf(maxPrice);
        this.minPrice=String.valueOf(minPrice);
        this.baseNumber =String.valueOf(baseNumber);
        editText.setText(String.valueOf(minPrice));
        }
    public void setData(String minPrice, String maxPrice, String baseNumber){
        this.maxPrice=maxPrice;
        this.minPrice=minPrice;
        this.baseNumber =baseNumber;
        editText.setText(String.valueOf(minPrice));
        }
    public void setData(double minPrice, double maxPrice, double baseNumber,double amount){
        this.maxPrice=String.valueOf(maxPrice);
        this.minPrice=String.valueOf(minPrice);
        this.baseNumber =String.valueOf(baseNumber);
        this.amount=String.valueOf(amount);
        mDitigs=MoneyUtil.getDigits(String.valueOf(amount));
        editText.setText(String.valueOf(amount));
        if(listener!=null&&!isFollow) {
            if(editText.getText().equals("")||editText.getText().equals(".")){
                this.amount="0";
            }
            listener.amountChange(this.amount);
        }
    }
    public void setData(String minPrice, String maxPrice, String baseNumber,String amount){
        this.maxPrice=maxPrice;
        this.minPrice=minPrice;
        this.baseNumber =baseNumber;
        this.amount=amount;
        mDitigs=MoneyUtil.getDigits(amount);
        editText.setText(amount);
        if(listener!=null&&!isFollow) {
            if(editText.getText().equals("")||editText.getText().equals(".")){
                this.amount="0";
            }
            listener.amountChange(amount);
        }
    }
    public String getNumbel(){
        return editText.getText().toString();
    }
    public EditText getEditText(){
        return editText;
    }
    public void setNumberTextInvisible(){
        editText.setVisibility(INVISIBLE);
    }
    public int getNumberTextVisible(){
        return editText.getVisibility();
    }
    public void setNumberTextVisible(){
        editText.setVisibility(VISIBLE);
    }
    // 隐藏系统键盘
    public void hideSoftInputMethod(){

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
            editText.setInputType(InputType.TYPE_NULL);
        }
        else{
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (NoSuchMethodException e) {
                editText.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
