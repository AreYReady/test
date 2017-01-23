package trade.xkj.com.trade.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import trade.xkj.com.trade.utils.SystemUtil;
import trade.xkj.com.trade.bean.BeanHeartResponse;
import trade.xkj.com.trade.bean.BeanUnRegister;
import trade.xkj.com.trade.bean.ResponseEvent;
import trade.xkj.com.trade.constant.MessageType;
import trade.xkj.com.trade.handler.HandlerSend;
import trade.xkj.com.trade.mvp.login.UserLoginPresenter;

public  abstract class BaseActivity extends AppCompatActivity {
    protected String TAG= SystemUtil.getTAG(this);
    protected Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
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

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void getHander(Handler handler){
        Log.i(TAG, "getHandler: ");
        mHandler=handler;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDisconnectFromServer(String result) {
        if (result.equalsIgnoreCase(UserLoginPresenter.DISCONNECT_FROM_SERVER)) {
            showNetWorkPrompt();
            Log.i(TAG, "onDisconnectFromServer: ");
            EventBus.getDefault().removeAllStickyEvents();
            mHandler.sendEmptyMessage(HandlerSend.CONNECT);
        }
    }
    /**
     * 客户端主动发送心跳
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void sendHeartBeat(BeanHeartResponse beanHeartResponse){
        if(mHandler!=null){
            Log.i(TAG, "sendHeartBeat: ");
            Message message = new Message();
            message.obj = (MessageType.HEAT);
            mHandler.sendMessage(message);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRespond(ResponseEvent responseEvent){
        Log.i(TAG, "getRespond: 登入成功");
        hideNetWorkPrompt();
    }

    /**
     * 网咯异常，显示提示
     */
    protected  void showNetWorkPrompt(){

    }

    /**
     * 网咯正常，隐藏提示,
     */
    protected void hideNetWorkPrompt(){
    }

}
