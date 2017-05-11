package com.xkj.trade.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.xkj.trade.IO.okhttp.ChatWebSocket;
import com.xkj.trade.bean.BeanUnRegister;
import com.xkj.trade.bean_notification.NotificationConnectStatus;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public  abstract class BaseActivity extends AppCompatActivity {
    protected String TAG= SystemUtil.getTAG(this);
    protected Handler mHandler;
    private LoadingDialog mLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initRegister();
        initData();
        initView();
        DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
        displayMetrics.scaledDensity=displayMetrics.density;
        MyApplication.getInstance().addActivity(this);
    }

    public  void initRegister(){
        if(!EventBus.getDefault().isRegistered(this))
             EventBus.getDefault().register(this);
    }
    public abstract  void initData();
    public abstract void initView();
    public abstract void initEvent();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        EventBus.getDefault().post(new BeanUnRegister());
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        MyApplication.getInstance().deleteActivity(this);
        if(MyApplication.getInstance().getListSize()==0){
            MyApplication.getInstance().exit();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    protected  void notificationConnectStatus(NotificationConnectStatus notificationConnectStatus){
        switch (notificationConnectStatus.getConnectStatus()){
            case YES:
                hideNetWorkPrompt();
                if(cdt!=null){
                    cdt.cancel();
                }
                break;
            case NO:
//                countDonwTime();
                showNetWorkPrompt();
                break;

        }
    }
    /**
     * 网咯异常，显示提示
     */
    protected  void showNetWorkPrompt(){
        if(mLoadingDialog==null){
            mLoadingDialog=new LoadingDialog(this,"网络连接失败，正在重新连接中");
        }
        if(!mLoadingDialog.isShow()) {
            mLoadingDialog.show();
        }
        ChatWebSocket.getChartWebSocket();
    }

    /**
     * 网咯正常，隐藏提示,
     */
    protected void hideNetWorkPrompt(){
        if(mLoadingDialog!=null&&mLoadingDialog.isShow()){
            mLoadingDialog.close();
        }
    }
    CountDownTimer cdt;
    private void countDonwTime(){
        cdt = new CountDownTimer(Integer.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "onTick: 定时器连接开启");
                showNetWorkPrompt();
            }
            @Override
            public void onFinish() {

            }
        };
        cdt.start();
    }
}
