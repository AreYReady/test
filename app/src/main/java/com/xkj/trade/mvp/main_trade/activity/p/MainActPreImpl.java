package com.xkj.trade.mvp.main_trade.activity.p;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

import com.xkj.trade.bean_.BeanUserListInfo;
import com.xkj.trade.mvp.main_trade.activity.m.MainActModelImpl;
import com.xkj.trade.mvp.main_trade.activity.v.MainTradeActListener;
import com.xkj.trade.utils.SystemUtil;

/**
 * Created by huangsc on 2017-02-13.
 * TODO:
 */

public class MainActPreImpl implements MainTradeActListener.PreListener {
    private MainTradeActListener.ModelListener mModelListener;
    private MainTradeActListener.ViewListener mViewListener;
    private String TAG= SystemUtil.getTAG(this);
    private Context mContext;
    private Handler mHandler;
    private CountDownTimer cdt;
    public MainActPreImpl(MainTradeActListener.ViewListener mViewListener, Context context, Handler handler){
        mContext=context;
        mModelListener=new MainActModelImpl(this,mContext);
        this.mViewListener=mViewListener;
        mHandler=handler;
    }
    @Override
    public void onDestroy() {
        mModelListener.onDestroy();
    }

    @Override
    public void sendUserInfo() {
        mModelListener.sendUserInfo();
    }

    @Override
    public void refreshUserInfo(BeanUserListInfo beanUserListInfo) {
        //刷新定时器
        mViewListener.refreshUserInfo(beanUserListInfo);
//        timeCountDown();
    }

    private void timeCountDown() {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(cdt!=null){
                    cdt.cancel();
                }
                cdt = new CountDownTimer(10000, 10000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.i(TAG, "onTick: ");
                    }
                    @Override
                    public void onFinish() {
                        Log.i(TAG, "onFinish: ");
                        mModelListener.sendUserInfo();
                    }
                };
                cdt.start();
            }
        });

    }
}
