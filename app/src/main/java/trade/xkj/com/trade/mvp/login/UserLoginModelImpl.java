package trade.xkj.com.trade.mvp.login;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.Utils.sslsocket.Decoder;
import trade.xkj.com.trade.Utils.sslsocket.Encoder;
import trade.xkj.com.trade.Utils.sslsocket.SSLDecoderImp;
import trade.xkj.com.trade.Utils.sslsocket.SSLEncodeImp;
import trade.xkj.com.trade.Utils.sslsocket.SSLSocketChannel;
import trade.xkj.com.trade.base.MyApplication;
import trade.xkj.com.trade.bean.BeanSymbolConfig;
import trade.xkj.com.trade.bean.BeanUserLoginData;
import trade.xkj.com.trade.bean.EventBusAllSymbol;
import trade.xkj.com.trade.bean.ResponseEvent;
import trade.xkj.com.trade.constant.MessageType;
import trade.xkj.com.trade.constant.ServerIP;
import trade.xkj.com.trade.handler.HandlerSend;
import trade.xkj.com.trade.handler.HandlerWrite;

/**
 * Created by admin on 2016-11-16.
 */

public class UserLoginModelImpl implements UserLoginModel {
    private String mUserName;
    private String mUserPassword;
    private String TAG= SystemUtil.getTAG(this);
    public static final String THREAD_READ = "threadRead";
    public static final String SSL_SOCKET = "sslSocket";
    public static final String HANDLER_WRITE = "handler_write";
    HandlerThread mHandlerThread;
    private SSLSocketChannel<String> mSSLSocketChannel;
    private HandlerWrite mHandlerWrite;
    private ResultEnum mResultEnum;
    private UserLoginPresenter mUserLoginPresenter;
    private EventBusAllSymbol mEventBusAllSymbol;
    private ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol;
    public UserLoginModelImpl(UserLoginPresenter mUserLoginPresenter){
        this.mUserLoginPresenter=mUserLoginPresenter;
        EventBus.getDefault().register(this);
    }
    @Override
    public int login(BeanUserLoginData beanLoginData) {
        sendData(beanLoginData);
        return 0;
    }

    /**
     * 发送数据
     * @param beanLoginData
     */
    private void sendData(final BeanUserLoginData beanLoginData) {
        mHandlerThread = new HandlerThread("SSL"){
            @Override
            public void run() {
                try {
                    Log.i("123", "run: sslTest");
                    sslTest(String.valueOf(beanLoginData.getLogin()), beanLoginData.getPassword_hash());
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                    mResultEnum=ResultEnum.erro;
                }
                super.run();
            }
        };
        mHandlerThread.start();
        Handler handlerRead = new HandlerSend(mHandlerThread.getLooper(),
                MyApplication.getInstance().getApplicationContext(),mHandlerThread, mSSLSocketChannel, mHandlerWrite);
        ///		Map<String, Object> map = new HashMap<>();
        EventBus.getDefault().postSticky(handlerRead);
    }
    private void sslTest(String name, String passwd) throws KeyManagementException {
        //暂时用固定的
//        final SocketAddress address = new InetSocketAddress(BuildConfig.API_URL, ServerIP.PORT);
        final SocketAddress address = new InetSocketAddress("mm.mgfoption.com", ServerIP.PORT);

        Encoder<String> encoder = new SSLEncodeImp();
        Decoder<String> decoder = new SSLDecoderImp();
        Log.i("123", "doLogin: Opening channel");
        try {
            mSSLSocketChannel = SSLSocketChannel.open(address, encoder, decoder, 1024*1024, 1024*1024);
            HandlerThread writeThread = new HandlerThread("write");
            writeThread.start();
            mHandlerWrite = new HandlerWrite(writeThread.getLooper(), mSSLSocketChannel);
            Log.i("123", "doLogin: Channel opened, initial handshake done");
///		map.put(SSL_SOCKET, sslSocketChannel);
///		map.put(THREAD_READ, mHandlerThread
            Log.i("123", "doLogin: Sending request");

            BeanUserLoginData userLogin = new BeanUserLoginData(Integer.valueOf(name), passwd);
            String loginStr = new Gson().toJson(userLogin, BeanUserLoginData.class);
            mSSLSocketChannel.send(loginStr);

            Log.i("123", "doLogin: Receiving response");
            mHandlerWrite.sendEmptyMessage(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public enum ResultEnum{
        succ,
        erroEmpty,
        erroName,
        erroNet,//网络出错
        erro;//各种错误

    }
    @Subscribe(threadMode=ThreadMode.MAIN)
    public void loginResult(ResponseEvent responseEvent){
        Log.i(TAG, "loginResult:responseEvent "+responseEvent.toString());
        int result_code = responseEvent.getResult_code();
        if(responseEvent==null||(responseEvent.getMsg_type()!= MessageType.TYPE_BINARY_LOGIN_RESULT)){
            mResultEnum=ResultEnum.erro;
        }else if(result_code==100){
            mResultEnum=ResultEnum.erroNet;
        }else if(result_code==0){
            mResultEnum=ResultEnum.succ;
        }else{
            mResultEnum=ResultEnum.erro;
        }
        mUserLoginPresenter.loginResult(mResultEnum);
    }

//    @Subscribe(sticky = true)
//    public void onGetAllSymbol(EventBusAllSymbol eventBusAllSymbol){
//        mEventBusAllSymbol=eventBusAllSymbol;
//    }
//
//
//    @Subscribe(sticky = true)
//    public void onGetShowSymbol(BeanSymbolConfig beanSymbolConfig){
//        BeanSymbolConfig mBeanSymbolConfig1=beanSymbolConfig;
//        TradeDateConstant.tz_delta = mBeanSymbolConfig1.getTz_delta();
//        sendSubSymbol(beanSymbolConfig);
//        Log.i(TAG, "onGetShowSymbol: tz_delta"+ TradeDateConstant.tz_delta);
//    }
//
//    /**
//     * 发送订阅的商品类型
//     * @param beanSymbolConfig
//     */
//    private void sendSubSymbol(BeanSymbolConfig beanSymbolConfig) {
//
//        if(mEventBusAllSymbol!=null) {
//            if(subTradeSymbol==null)
//                subTradeSymbol=new ArrayList<>();
//            subTradeSymbol.clear();
//            Log.i(TAG, "sendSubSymbol: 发送订阅商品实时数据请求");
//            ArrayList<BeanSymbolConfig.SymbolsBean> showSymbolsList = beanSymbolConfig.getSymbols();
//            for (BeanSymbolConfig.SymbolsBean allsymbol : beanSymbolConfig.getSymbols()) {
//                for (BeanSymbolConfig.SymbolsBean symbol : showSymbolsList) {
//                    if (!symbol.getSymbol().equalsIgnoreCase(allsymbol.getSymbol())) {
//                        continue;
//                    } else {
//                        subTradeSymbol.add(symbol);
//                    }
////                    sendMessageToSubThread("{\"msg_type\":1010,\"symbol\":\"" + symbol.getSymbol() + "\"}");
//                }
//            }
//        }
//    }
}
