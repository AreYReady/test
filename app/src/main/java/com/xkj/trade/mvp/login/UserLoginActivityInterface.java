package com.xkj.trade.mvp.login;

import android.content.Context;

/**
 * Created by admin on 2016-11-17.
 * auth:huangshunchao
 */

public interface UserLoginActivityInterface {
    void toMainActivity();
    void showFaidPromt(UserLoginModelImpl.ResultEnum resultEnum,String msg);
    void hintLoading();
    void showLoading();
    Context getContext();
}
