package com.xkj.trade.mvp.main_trade.activity.m;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.bean_.BeanUserListInfo;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.mvp.main_trade.activity.v.MainTradeActListener;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.SystemUtil;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.xkj.trade.constant.UrlConstant.URL_MT4_USERLIST;

/**
 * Created by huangsc on 2017-02-13.
 * TODO:
 */

public class MainActModelImpl implements MainTradeActListener.ModelListener {
    public String TAG= SystemUtil.getTAG(this);
    private Context mContext;
    private MainTradeActListener.PreListener mPreListener;
    public MainActModelImpl(MainTradeActListener.PreListener mPreListener,Context context){
        mContext=context;
        this.mPreListener=mPreListener;

    }
    @Override
    public void onDestroy() {

    }

    @Override
    public void sendUserInfo() {
//        Map<String, String> map = new TreeMap<>();
//        map.put(RequestConstant.API_ID, ACache.get(mContext).getAsString(RequestConstant.API_ID));
//        map.put(RequestConstant.API_TIME, DateUtils.getShowTime(BeanCurrentServerTime.instance.getCurrentServerTime()));
        Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(mContext).getAsString(RequestConstant.ACCOUNT)));
//        map.put(RequestConstant.API_SIGN, AesEncryptionUtil.getApiSign(URL_MT4_USERLIST, map));
        OkhttpUtils.enqueue(URL_MT4_USERLIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + call.request());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanUserListInfo info= new Gson().fromJson(response.body().string(),BeanUserListInfo.class);
                if(info.getStatus()==1){
                    mPreListener.refreshUserInfo(info);
                }
            }
        });
    }
}
