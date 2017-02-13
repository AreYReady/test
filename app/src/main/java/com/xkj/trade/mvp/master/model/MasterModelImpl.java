package com.xkj.trade.mvp.master.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.mvp.master.contract.MasterContract;
import com.xkj.trade.mvp.master.presenter.MasterPresenterImpl;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.DataUtil;
import com.xkj.trade.utils.SystemUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.xkj.trade.constant.UrlConstant.URL_MT4_RANKING;

/**
* Created by huangsc on 2017/02/13
*/

public class MasterModelImpl implements MasterContract.Model{
    private MasterContract.Presenter mPresenterListener;
    private Context mContext;
    private String TAG= SystemUtil.getTAG(this);
    public MasterModelImpl(Context mContext, MasterPresenterImpl masterPresenter) {
        mPresenterListener=masterPresenter;
        this.mContext=mContext;
    }

    @Override
    public void requestMasterRank(String rankType) {
        Log.i(TAG, "requestMasterRank: ");
        Map<String, String> map= DataUtil.postMap();
        map.put(RequestConstant.LOGIN, ACache.get(mContext).getAsString(RequestConstant.ACCOUNT));
        map.put(RequestConstant.RANK_TYPE,rankType);
        map.put(RequestConstant.API_SIGN, AesEncryptionUtil.getApiSign(URL_MT4_RANKING, map));
        OkhttpUtils.enqueue(URL_MT4_RANKING, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + call.request());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = "";
                Log.i(TAG, "onResponse: "+(resp=response.body().string()));
               BeanMasterRank info= new Gson().fromJson(resp, new TypeToken<BeanMasterRank>(){}.getType());
                if(info.getStatus()==1){
                    mPresenterListener.responseMasterRank(info);
                }
            }
        });
    }
}