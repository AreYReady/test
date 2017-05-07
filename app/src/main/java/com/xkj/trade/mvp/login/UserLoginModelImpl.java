package com.xkj.trade.mvp.login;

import android.content.Context;
import android.os.HandlerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.IO.sslsocket.SSLSocketChannel;
import com.xkj.trade.bean.BeanCurrentServerTime;
import com.xkj.trade.bean.BeanSymbolConfig;
import com.xkj.trade.bean.BeanUnRegister;
import com.xkj.trade.bean.BeanUserLoginData;
import com.xkj.trade.bean.EventBusAllSymbol;
import com.xkj.trade.bean_.BeanBaseResponse;
import com.xkj.trade.bean_.BeanServerTimeForHttp;
import com.xkj.trade.constant.CacheKeyConstant;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.handler.HandlerWrite;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.DateUtils;
import com.xkj.trade.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.xkj.trade.constant.UrlConstant.APP_CONFIG;
import static com.xkj.trade.constant.UrlConstant.URL_LOGIN;
import static com.xkj.trade.constant.UrlConstant.URL_SERVICE_TIME;


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



    int i=0;
    /**
     * 发送数据
     * @param beanLoginData
     */
    private void sendData(final BeanUserLoginData beanLoginData)  {
        i=2;
            //获取时间后，登入
            okhttp3.Request request=new okhttp3.Request.Builder().url(URL_SERVICE_TIME).post(new FormBody.Builder().build()).build();
            OkhttpUtils.enqueue(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(TAG, "onFailure: "+call.request()+e.getMessage());
                    mResultEnum=ResultEnum.erro;
                    mUserLoginPresenter.loginResult(mResultEnum,"连接失败，请检查网络");
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    //请求appconfig;
                    OkhttpUtils.enqueue(APP_CONFIG, new MyCallBack() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            i--;
                            ACache.get(mContext).put(CacheKeyConstant.CACLE_APP_CONFIG,response.body().string());
                            if(i==0){
                                mUserLoginPresenter.loginResult(mResultEnum,"");
                            }
                        }
                    });

                    String s;
                    BeanServerTimeForHttp beanServerTimeForHttp = new Gson().fromJson(s=response.body().string(), BeanServerTimeForHttp.class);
                    Log.i(TAG, "onResponse: "+s);
                    BeanCurrentServerTime.getInstance(DateUtils.getOrderStartTime(beanServerTimeForHttp.getData(),"yyyyMMddHHmmss"));
                    Log.i(TAG, "onResponse: "+DateUtils.getShowTime(DateUtils.getOrderStartTime(beanServerTimeForHttp.getData(),"yyyyMMddHHmmss")));
                    Log.i(TAG, "onResponse: "+DateUtils.getOrderStartTime(beanServerTimeForHttp.getData(),"yyyyMMddHHmmss"));
                    Log.i(TAG, "onResponse: "+DateUtils.getOrderStartTime(beanServerTimeForHttp.getData(),"yyyyMMddHHmmss"));
                    TreeMap<String,String> map=new TreeMap<>();
                    map.put(RequestConstant.ACCOUNT,AesEncryptionUtil.stringBase64toString(beanLoginData.getLogin()));
//                    map.put(RequestConstant.API_ID, ACache.get(mContext).getAsString(RequestConstant.API_ID));
//                    map.put(RequestConstant.API_TIME,DateUtils.getShowTime(BeanCurrentServerTime.instance.getCurrentServerTime()));
                    map.put(RequestConstant.LOGIN_PASSWORD, AesEncryptionUtil.stringBase64toString(beanLoginData.getPassword()));
                    String apiSign;
//                    apiSign=AesEncryptionUtil.getApiSign(UrlConstant.URL_LOGIN,map);
//                    map.put(RequestConstant.API_SIGN,apiSign);
                    OkhttpUtils.enqueue(URL_LOGIN,map, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i(TAG, "onFailure: 登入失败");
                            mResultEnum=ResultEnum.erro;
                            mUserLoginPresenter.loginResult(mResultEnum,"连接失败，请检查网络");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            String result;
                            BeanBaseResponse beanResponse = new Gson().fromJson(result = response.body().string(), BeanBaseResponse.class);
                            if(beanResponse.getStatus()==1){
                                Log.i(TAG, "onResponse: 登入成功"+result);
                                mResultEnum=ResultEnum.succ;
                            }else{
                                Log.i(TAG, "onResponse: 登入失败"+result);
                                mResultEnum=ResultEnum.erro;
                            }
                            i--;
                            if(i==0) {
                                mUserLoginPresenter.loginResult(mResultEnum,beanResponse.getTips()==null?beanResponse.getMsg():beanResponse.getTips());
                            }
                        }
                    });
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
