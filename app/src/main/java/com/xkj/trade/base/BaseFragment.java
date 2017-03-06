package com.xkj.trade.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xkj.trade.R;
import com.xkj.trade.utils.DialogUtils;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.ThreadHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by admin on 2016-11-23.
 * TODO:所有的fragment都需要继承的父类
 */

public  abstract class BaseFragment extends Fragment {
    protected View view;
    protected Context context;
    protected Handler mHandler;
    protected final String TAG= SystemUtil.getTAG(this);
    protected String title;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view.setClickable(true);
        initData();
        initView();
    }
    protected abstract void initData();
    protected abstract void initView();

    public Boolean onBackPressed(){
        return false;
    }
    @Subscribe(sticky = true)
    public void getHander(Handler handler){
        mHandler=handler;
    }
    public interface BackInterface {
          void setSelectedFragment(BaseFragment selectedFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
    protected  void showFail(final String msg){
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new DialogUtils(context,title,msg);
            }
        });
    }
    protected  void showFail(){
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new DialogUtils(context,title,getString(R.string.action_fail_please_try_again));
            }
        });
    }

}
