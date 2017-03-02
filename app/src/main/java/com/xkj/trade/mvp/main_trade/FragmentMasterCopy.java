package com.xkj.trade.mvp.main_trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.adapter.CopyAdapter;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean.BeanAttentionTraderData;
import com.xkj.trade.bean_.BeanMasterMyCopy;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.bean_notification.NotificationMasterStatus;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.ThreadHelper;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class FragmentMasterCopy extends BaseFragment {
    private RecyclerView mRecyclerView;
    private List<BeanMasterMyCopy.ResponseBean> mDataList;
    private BeanAttentionTraderData mData;
    private CopyAdapter mCopyAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_1,null);
        return view;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_item_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                mRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(mRecyclerView.getWidth(),(int) MainTradeContentActivity.descHeight
//                        -(int)MainTradeContentActivity.flIndicatorHeight));
//            }
//        });
        requestMasterCopy();
    }

    @Override
    protected void initData() {
        mDataList=new ArrayList<>();
    }
   private void requestMasterCopy(){
            Map<String,String> map=new TreeMap<>();
       map.put(RequestConstant.ACCOUNT,ACache.get(context).getAsString(RequestConstant.ACCOUNT));
       OkhttpUtils.enqueue(UrlConstant.URL_MASTER_MY_COPY, map, new MyCallBack() {
           @Override
           public void onResponse(Call call, Response response) throws IOException {
               String  s=response.body().string();
               Log.i(TAG, "onResponse: "+s);
               BeanMasterMyCopy beanMasterMyCopy=new Gson().fromJson(s,new TypeToken<BeanMasterMyCopy>(){}.getType());
               if(beanMasterMyCopy.getStatus()==1){
                   mDataList=beanMasterMyCopy.getResponse();
                   responseMasterCopy(beanMasterMyCopy);
               }
           }
       });
    }
    private void refresh(){
        ThreadHelper.instance().runOnUiThread(mRunnable);
    }

    private void responseMasterCopy(BeanMasterMyCopy beanMasterMyCopy) {
        mDataList=beanMasterMyCopy.getResponse();
        refresh();

    }
    Runnable mRunnable =new Runnable() {
        @Override
        public void run() {
            if(mCopyAdapter==null){
                mRecyclerView.setAdapter(mCopyAdapter=new CopyAdapter(context,mDataList));
            }else{
                mCopyAdapter.notifyDataSetChanged();
            }
        }
    };
    @Subscribe
    public void getNotifaticationChange(NotificationMasterStatus notificationMasterStatus){
        int i=isItemExist(mDataList,notificationMasterStatus);
        if(notificationMasterStatus.getFstatus()==1){
            if(notificationMasterStatus.getStatus()==0){
                //检测复制状态，存在则删除，不存在不做处理
                    if(i>=0) {
                        mDataList.remove(i);
//                        mCopyAdapter.notifyItemRemoved(i);
//                        mCopyAdapter.notifyItemRangeChanged(0,mDataList.size());
                        refresh();

                    }
            }else {
                //符合增加条件，检测复制状态，存在则改变，不存在则正在
                if(i<0){
                    for(BeanMasterRank.MasterRank masterRank:MyApplication.rank.getResponse()){
                        if(masterRank.getLogin().equals(notificationMasterStatus.getLogin())){
                            //
                            mDataList.add(new BeanMasterMyCopy.ResponseBean(
                                    1,masterRank.getFace_url(),masterRank.getCopynumber()
                                    ,masterRank.getCopymoney(),masterRank.getName(),masterRank.getLogin()
                                    ,masterRank.getProfitper(),masterRank.getHuiceper()));
                            refresh();
                        }
                    }
                }
            }
        }else{
            //存在则删除，不存在不做处理
            if(i>=0){
                mDataList.remove(i);
                refresh();
            }
        }
    }

    private int isItemExist(List<BeanMasterMyCopy.ResponseBean> dataList, NotificationMasterStatus notificationMasterStatus) {
        for(int i=0;i<dataList.size();i++){
            if(dataList.get(i).getMasterid().equals(notificationMasterStatus.getLogin())){
                return i;
            }
        }
        return -1;
    }
}
