package com.xkj.trade.mvp.login;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.xkj.trade.R;
import com.xkj.trade.base.BaseActivity;
import com.xkj.trade.bean_notification.NotificationSignUp;
import com.xkj.trade.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class UserLoginActivity2 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login2);
        super.onCreate(savedInstanceState);
    }
    public static int[] scrren;
    @Override
    public void initData() {
        scrren = SystemUtil.getScrren(this);
    }
    @Override
    public void initView() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_login_content,new LoginFragment());
        fragmentTransaction.commitNow();
    }

    @Override
    public void initEvent() {
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notificationSignup(NotificationSignUp notificationSignUp){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_login_content,new SignUpFragment()).commitNow();
    }
}
