package com.xkj.trade.mvp.login;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.xkj.trade.R;
import com.xkj.trade.base.BaseActivity;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean_notification.NotificationSignUp;
import com.xkj.trade.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class UserLoginActivity extends BaseActivity {

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
    //退出时的时间
    private long mExitTime;

    //    对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: " + MyApplication.getInstance().getListSize());
        //双击退出
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                exit();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            MyApplication.getInstance().exit();
        }
    }
}
