package trade.xkj.com.trade.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.bean.HistoryData;

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
        Log.i(TAG, "onDestroy: ");
        EventBus.getDefault().unregister(this);
        MyApplication.getInstance().deleteActivity(this);
        if(MyApplication.getInstance().getListSize()==0){
            MyApplication.getInstance().exit();
        }
    }


    @Subscribe(sticky = true)
    public void getHander(Handler handler){
        Log.i(TAG, "getHandler: ");
        mHandler=handler;
    }
}
