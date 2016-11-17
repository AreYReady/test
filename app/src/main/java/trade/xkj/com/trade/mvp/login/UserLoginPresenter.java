package trade.xkj.com.trade.mvp.login;

import android.os.HandlerThread;

import trade.xkj.com.trade.Utils.SSLSOCKET.SSLSocketChannel;
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
    HandlerThread mHandlerThread;
    private SSLSocketChannel<String> mSSLSocketChannel;
    private HandlerWrite mHandlerWrite;
    private UserLoginModel mUserLoginModel;
    private UserLoginActivityInterface mLoginActivityInterface;
    private int result;
    public UserLoginPresenter(UserLoginActivityInterface mLoginActivityInterface){
        mUserLoginModel=new UserLoginModelImpl();
        this.mLoginActivityInterface=mLoginActivityInterface;
    }
    public void login(BeanUserLoginData beanLoginData){
       result=mUserLoginModel.login(beanLoginData);
        if(result==0){
            mLoginActivityInterface.toMainActivity();
        }
    }


}
