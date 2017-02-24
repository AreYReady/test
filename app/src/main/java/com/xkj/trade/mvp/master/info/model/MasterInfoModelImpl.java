package com.xkj.trade.mvp.master.info.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.bean_.BeanBaseResponse;
import com.xkj.trade.bean_.BeanMasterInfo;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.mvp.master.info.contract.MasterInfoContract;
import com.xkj.trade.utils.SystemUtil;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by huangsc on 2017/02/24
 */

public class MasterInfoModelImpl implements MasterInfoContract.Model {
    private MasterInfoContract.Presenter mPresenterListener;
    private Context context;
    private String TAG = SystemUtil.getTAG(this);

    public MasterInfoModelImpl(MasterInfoContract.Presenter masterInfoPresenter) {
        mPresenterListener = masterInfoPresenter;
    }

    @Override
    public void requestMasterOpenPosition(String masterid, String followid) {
        Map<String, String> map = new TreeMap<>();

    }

    @Override
    public void requestMasterClosePosition(String masterid, String followid, String pageNo) {

    }

    @Override
    public void requestCopyFollow(String masterid,final String copyvolume,final String followid) {
        final Map<String, String> map = new TreeMap();
        map.put(RequestConstant.MASTER_ID, masterid);
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_INFO, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanMasterInfo info=   new Gson().fromJson(response.body().string(),new TypeToken<BeanMasterInfo>(){}.getType());
                map.put(RequestConstant.FOLLOW_ID, followid);
                map.put(RequestConstant.COPY_MONEY, info.getResponse().getFollowfunds());
                map.put(RequestConstant.COPY_WAY, "3");
                map.put(RequestConstant.COPY_VOLUME, copyvolume);
                OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW, map, new MyCallBack() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        BeanBaseResponse info = new Gson().fromJson(response.body().string(), new TypeToken<BeanBaseResponse>() {
                        }.getType());
                        if (info.getStatus() == 1) {
                            Log.i(TAG, "onResponse: " + info.toString());
                            mPresenterListener.responseCopyFollow(info);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void requestUnCopyFollow(String masterid, String followid) {
        final Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.MASTER_ID, masterid);
        map.put(RequestConstant.FOLLOW_ID, followid);
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_NOFOLLOW, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanBaseResponse info = new Gson().fromJson(response.body().string(), new TypeToken<BeanBaseResponse>() {
                }.getType());
                if (info.getStatus() == 1) {
                    Log.i(TAG, "onResponse: " + info.toString());
                    mPresenterListener.responseUnCopyFollow(info);
                }

            }
        });
    }

    @Override
    public void requestFocus(String masterid, String followid) {
        Map<String, String> map = new TreeMap();
        map.put(RequestConstant.FOCUS_ID, masterid);
        map.put(RequestConstant.ACCOUNT_ID, followid);
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_FOCUS, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanBaseResponse beanBaseResponse = new Gson().fromJson(response.body().string(), new TypeToken<BeanBaseResponse>() {
                }.getType());
                Log.i(TAG, "onResponse: " + beanBaseResponse.toString());
                if (beanBaseResponse.getStatus() == 1) {
                    mPresenterListener.responseFocus(beanBaseResponse);
                }
            }
        });
    }

    @Override
    public void requestNoFocus(String masterid, String followid) {
        Map<String, String> map = new TreeMap();
        map.put(RequestConstant.FOCUS_ID, masterid);
        map.put(RequestConstant.ACCOUNT_ID, followid);
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_NOFOCUS, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanBaseResponse beanBaseResponse = new Gson().fromJson(response.body().string(), new TypeToken<BeanBaseResponse>() {
                }.getType());
                Log.i(TAG, "onResponse: " + beanBaseResponse.toString());
                if (beanBaseResponse.getStatus() == 1) {
                    mPresenterListener.responseNoFocus(beanBaseResponse);
                }
            }
        });
    }


}