package trade.xkj.com.trade.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import trade.xkj.com.trade.Utils.SystemUtil;

public  abstract class BaseActivity extends AppCompatActivity {
    protected String TAG= SystemUtil.getTAG(this);
    protected Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRegister();
        initData();
        initView();
        DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
        displayMetrics.scaledDensity=displayMetrics.density;
        MyApplication.getInstance().addActivity(this);
    }

    public abstract void initRegister();
    public abstract  void initData();
    public abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
//    //退出时的时间
//    private long mExitTime;
//    //对返回键进行监听
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//
//            exit();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    public void exit() {
//        if ((System.currentTimeMillis() - mExitTime) > 2000) {
//            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            mExitTime = System.currentTimeMillis();
//        } else {
//            MyApplication.getInstance().exit();
//        }
//    }
    @Subscribe(sticky = true)
    public void getHander(Handler handler){
        Log.i(TAG, "getHandler: ");
        mHandler=handler;
    }
}
