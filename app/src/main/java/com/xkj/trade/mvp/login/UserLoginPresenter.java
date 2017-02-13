package com.xkj.trade.mvp.login;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.xkj.trade.bean.BeanUserLoginData;
import com.xkj.trade.utils.SystemUtil;

/**
 * Created by admin on 2016-11-17.
 * todo
 */

public class UserLoginPresenter {
    public static final String DISCONNECT_FROM_SERVER = "DISCONNECT_FROM_SERVER";
    public static final String THREAD_READ = "threadRead";
    public static final String SSL_SOCKET = "sslSocket";
    public static final String HANDLER_WRITE = "handler_write";
    private String TAG= SystemUtil.getTAG(this);
    private UserLoginModel mUserLoginModel;
    private UserLoginActivityInterface mLoginActivityInterface;
    private int result;
    private Handler mHandler;
    public UserLoginPresenter(UserLoginActivityInterface mLoginActivityInterface, Handler handler, Context context){
        mUserLoginModel=new UserLoginModelImpl(this,context);
        this.mLoginActivityInterface=mLoginActivityInterface;
        mHandler=handler;
    }
    public void login(BeanUserLoginData beanLoginData){
//        mLoginActivityInterface.showLoading();
       result=mUserLoginModel.login(beanLoginData);
    }
    //登入结果处理
    public void loginResult(final UserLoginModelImpl.ResultEnum mResultEnum){
        Log.i(TAG, "loginResult: 执行了");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mLoginActivityInterface.hintLoading();
                if(mResultEnum== UserLoginModelImpl.ResultEnum.succ){
                    mLoginActivityInterface.toMainActivity();
                }else {
                    mLoginActivityInterface.showFaidPromt(mResultEnum);
                }
            }
        });

    }
}
