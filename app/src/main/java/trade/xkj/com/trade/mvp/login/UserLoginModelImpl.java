package trade.xkj.com.trade.mvp.login;

import android.content.Context;
import android.os.HandlerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import trade.xkj.com.trade.IO.okhttp.OkhttpUtils;
import trade.xkj.com.trade.IO.sslsocket.SSLSocketChannel;
import trade.xkj.com.trade.bean.BeanCurrentServerTime;
import trade.xkj.com.trade.bean.BeanSymbolConfig;
import trade.xkj.com.trade.bean.BeanUnRegister;
import trade.xkj.com.trade.bean.BeanUserLoginData;
import trade.xkj.com.trade.bean.EventBusAllSymbol;
import trade.xkj.com.trade.bean_.BeanResponse;
import trade.xkj.com.trade.constant.RequestConstant;
import trade.xkj.com.trade.constant.UrlConstant;
import trade.xkj.com.trade.handler.HandlerWrite;
import trade.xkj.com.trade.utils.ACache;
import trade.xkj.com.trade.utils.AesEncryptionUtil;
import trade.xkj.com.trade.utils.DateUtils;
import trade.xkj.com.trade.utils.SystemUtil;

import static trade.xkj.com.trade.constant.UrlConstant.URL_LOGIN;


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
    private Context mContext;
    private ArrayList<BeanSymbolConfig.SymbolsBean> subTradeSymbol;
    public UserLoginModelImpl(UserLoginPresenter mUserLoginPresenter, Context context){
        this.mUserLoginPresenter=mUserLoginPresenter;
        EventBus.getDefault().register(this);
        mContext=context;
    }
    @Override
    public int login(BeanUserLoginData beanLoginData) {
        sendData(beanLoginData);
        return 0;
    }
    @Subscribe
    public void unRegister(BeanUnRegister beanUnRegister) {
        if(EventBus.getDefault().isRegistered(this))
            Log.i(TAG, "unRegister: ");
            EventBus.getDefault().unregister(this);
    }




    /**
     * 发送数据
     * @param beanLoginData
     */
    private void sendData(final BeanUserLoginData beanLoginData)  {
        TreeMap<String,String> map=new TreeMap<>();
        map.put(RequestConstant.LOGIN_NAME,AesEncryptionUtil.stringBase64toString(beanLoginData.getLogin()));
        map.put(RequestConstant.API_ID, ACache.get(mContext).getAsString(RequestConstant.API_ID));
        map.put(RequestConstant.API_TIME,DateUtils.getShowTime(BeanCurrentServerTime.instance.getCurrentServerTime()));
        map.put(RequestConstant.LOGIN_PASSWORD, AesEncryptionUtil.stringBase64toString(beanLoginData.getPassword()));
        String apiSign;
        apiSign=AesEncryptionUtil.getApiSign(UrlConstant.URL_LOGIN,map);
        map.put(RequestConstant.API_SIGN,apiSign);
        OkhttpUtils.enqueue(URL_LOGIN,map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: 登入失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result;
                BeanResponse beanResponse = new Gson().fromJson(result = response.body().string(), new TypeToken<BeanResponse>() {
                }.getType());
                if(beanResponse.getStatus()==1){
                    Log.i(TAG, "onResponse: 登入成功"+result);
                    mResultEnum=ResultEnum.succ;
                }else{
                    Log.i(TAG, "onResponse: 登入失败"+result);
                    mResultEnum=ResultEnum.erro;
                }
                mUserLoginPresenter.loginResult(mResultEnum);
            }
        });
    }
    public enum ResultEnum{
        succ,
        erroEmpty,
        erroName,
        erroNet,//网络出错
        erro;//各种错误

    }
}
