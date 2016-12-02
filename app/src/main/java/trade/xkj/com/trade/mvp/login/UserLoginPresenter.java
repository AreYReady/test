package trade.xkj.com.trade.mvp.login;

import android.os.HandlerThread;
import android.util.Log;

import trade.xkj.com.trade.Utils.sslsocket.SSLSocketChannel;
import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.bean.BeanUserLoginData;
import trade.xkj.com.trade.handler.HandlerWrite;

/**
 * Created by admin on 2016-11-17.
 */

public class UserLoginPresenter {
    public static final String DISCONNECT_FROM_SERVER = "DISCONNECT_FROM_SERVER";
    public static final String THREAD_READ = "threadRead";
    public static final String SSL_SOCKET = "sslSocket";
    public static final String HANDLER_WRITE = "handler_write";
    private String TAG= SystemUtil.getTAG(this);
    HandlerThread mHandlerThread;
    private SSLSocketChannel<String> mSSLSocketChannel;
    private HandlerWrite mHandlerWrite;
    private UserLoginModel mUserLoginModel;
    private UserLoginActivityInterface mLoginActivityInterface;
    private int result;
    public UserLoginPresenter(UserLoginActivityInterface mLoginActivityInterface){
        mUserLoginModel=new UserLoginModelImpl(this);
        this.mLoginActivityInterface=mLoginActivityInterface;
    }
    public void login(BeanUserLoginData beanLoginData){
//        mLoginActivityInterface.showLoading();
       result=mUserLoginModel.login(beanLoginData);
    }
    //登入结果处理
    public void loginResult(UserLoginModelImpl.ResultEnum mResultEnum){
        Log.i(TAG, "loginResult: 执行了");
        mLoginActivityInterface.hintLoading();
        if(mResultEnum== UserLoginModelImpl.ResultEnum.succ){
            mLoginActivityInterface.toMainActivity();
        }else {
            mLoginActivityInterface.showFaidPromt(mResultEnum);
        }

    }


}
