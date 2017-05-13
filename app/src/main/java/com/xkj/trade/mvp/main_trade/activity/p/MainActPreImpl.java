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
        //刷新定时器
        timeCountDown();
    }

    @Override
    public void refreshUserInfo(BeanUserListInfo beanUserListInfo) {
        mViewListener.refreshUserInfo(beanUserListInfo);
    }

    private void timeCountDown() {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(cdt!=null){
                    cdt.cancel();
                }
                cdt = new CountDownTimer(Integer.MAX_VALUE, 10000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.i(TAG, "onFinish:定时器刷新用户数据 ");
                        mModelListener.sendUserInfo();
                    }
                    @Override
                    public void onFinish() {

                    }
                };
                cdt.start();
            }
        });

    }
}
