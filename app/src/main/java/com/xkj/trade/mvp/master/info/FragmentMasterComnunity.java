package com.xkj.trade.mvp.master.info;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.util.BatchingListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean_.BeanAdapterComnunity;
import com.xkj.trade.bean_.BeanMasterFocusInfo;
import com.xkj.trade.bean_.BeanMasterMyCopy;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.bean_.BeanMasterRelationCopy;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.diffcallback.MasterComnunityDiff;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.ThreadHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Response;

import static com.xkj.trade.constant.RequestConstant.TAKE_PROFIT;
import static com.xkj.trade.mvp.master.info.FragmentMasterInfo.MASTER_INFO;

/**
 * Created by huangsc on 2016-12-27.
 * TODO:
 */

public class FragmentMasterComnunity extends BaseFragment {
    private RecyclerView mRecyclerView;
    private RadioGroup mRadioGroup;
    private BeanMasterRank.MasterRank rank;
    private MyAdapter mMyAdapter;
    Map<String,String> map=new TreeMap<>();
    private List<BeanAdapterComnunity> focusRelationList=new ArrayList<>();
    private List<BeanAdapterComnunity> copyRelationList=new ArrayList<>();
    private List<BeanAdapterComnunity> focusList=new ArrayList<>();
    private List<BeanAdapterComnunity> copyList=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_master_community, null);
        return view;
    }

    @Override
    protected void initData() {
        rank = new Gson().fromJson(this.getArguments().getString(MASTER_INFO), new TypeToken<BeanMasterRank.MasterRank>() {
        }.getType());
    }

    @Override
    protected void initView() {
        mRadioGroup=(RadioGroup)view.findViewById(R.id.rg_type);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_1:
                        mMyAdapter.setData(focusRelationList);
                        break;
                    case R.id.rb_2:
                        mMyAdapter.setData(focusList);
                        break;
                    case R.id.rb_3:
                        mMyAdapter.setData(copyRelationList);
                        break;
                    case R.id.rb_4:
                        mMyAdapter.setData(copyList);
                        break;
                }
                mMyAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_master_community);

        mRecyclerView.setFocusable(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        requestFocusInfo();
        requestMyCopy();
        requestFocusRelation();
        requestCopyRelation();
    }

    //获取高手被关注的信息
    private void requestFocusRelation() {
        Map<String,String> map=null;
        if(map==null) {
            map = new TreeMap<>();
            map.put(RequestConstant.ACCOUNT, rank.getLogin());
            map.put(RequestConstant.TYPE, "1");
        }
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_master_Relation, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s=response.body().string();
                Log.i(TAG, "onResponse11:  "+s);
                BeanMasterMyCopy beanMasterMyCopy=new Gson().fromJson(s,new TypeToken<BeanMasterMyCopy>(){}.getType());
                if(beanMasterMyCopy.getStatus()==1){
                    List<BeanAdapterComnunity> list=new ArrayList<BeanAdapterComnunity>();
                    for(BeanMasterMyCopy.ResponseBean responseBean:beanMasterMyCopy.getResponse()){
                        list.add(new BeanAdapterComnunity(R.mipmap.ic_instrument_audusd,responseBean.getName(),String.valueOf(responseBean.getProfitper()),0));
                    }
                    response(list,UrlConstant.URL_MASTER_FOLLOW_master_Relation,"1");
                    focusRelationList=list;
                }
            }
        });

    }

    //展示
    DiffUtil.DiffResult diffResult;
    private void response(final List<BeanAdapterComnunity> list, String url, String s) {
        switch (mRadioGroup.getCheckedRadioButtonId()){
                case R.id.rb_1:
                    if(url.equals(UrlConstant.URL_MASTER_FOLLOW_master_Relation)&&"1".equals(s)){
                        if(mMyAdapter==null){
                            ThreadHelper.instance().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    focusRelationList=list;
                                    mRecyclerView.setAdapter(mMyAdapter=new MyAdapter(focusRelationList));
                                    countTimeDown();
                                }
                            });
                        }else {
                            diffResult = DiffUtil.calculateDiff(new MasterComnunityDiff(focusRelationList, list));
                            mMyAdapter.setData(list);
                            diffresult();
                        }
                    }
                    break;
                case R.id.rb_2:
                    if(url.equals(UrlConstant.URL_MASTER_focusinfo)){
                        diffResult=DiffUtil.calculateDiff(new MasterComnunityDiff(focusList,list));
                        mMyAdapter.setData(list);
                    }
                    break;
                case R.id.rb_3:
                    if(url.equals(UrlConstant.URL_MASTER_FOLLOW_master_Relation)&&"2".equals(s)){
                        diffResult=DiffUtil.calculateDiff(new MasterComnunityDiff(copyRelationList,list));
                        mMyAdapter.setData(list);
                    }
                    break;
                case R.id.rb_4:
                    if(url.equals(UrlConstant.URL_MASTER_MY_COPY)){
                        diffResult=DiffUtil.calculateDiff(new MasterComnunityDiff(copyList,list));
                        mMyAdapter.setData(list);
                    }
                    break;
        }
    }

    private void diffresult() {
        ThreadHelper.instance().runOnUiThread(mRunnable);
    }
    Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            if(diffResult!=null){
                diffResult.dispatchUpdatesTo(mMyAdapter);
            }
        }
    };

    //获取高手被复制的信息
    private void requestCopyRelation() {
        Map<String,String> map=null;
        if(map==null) {
            map = new TreeMap<>();
            map.put(RequestConstant.ACCOUNT, rank.getLogin());
            map.put(RequestConstant.TYPE, "2");
        }
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_master_Relation, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s=response.body().string();
                Log.i(TAG, "onResponse13:  "+s);
                BeanMasterRelationCopy beanMasterRelationCopy=new Gson().fromJson(s,new TypeToken<BeanMasterRelationCopy>(){}.getType());
                if(beanMasterRelationCopy.getStatus()==1){
                    List<BeanAdapterComnunity> list=new ArrayList<BeanAdapterComnunity>();
                    for(BeanMasterRelationCopy.ResponseBean responseBean:beanMasterRelationCopy.getResponse()){
                        list.add(new BeanAdapterComnunity(R.mipmap.ic_instrument_audusd,responseBean.getName(),String.valueOf(responseBean.getProfitper()),0));
                    }
                    response(list,UrlConstant.URL_MASTER_FOLLOW_master_Relation,"2");
                    copyRelationList=list;
                }
            }
        });
    }
    //已复制的高手信息
    private void requestMyCopy() {
        Map<String,String> map=null;
        if(map==null) {
            map = new TreeMap<>();
            map.put(RequestConstant.ACCOUNT, ACache.get(context).getAsString(RequestConstant.ACCOUNT));
        }
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_MY_COPY, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s=response.body().string();
                Log.i(TAG, "onResponse14:  "+s);
                BeanMasterMyCopy beanMasterMyCopy=new Gson().fromJson(s,new TypeToken<BeanMasterMyCopy>(){}.getType());
                if(beanMasterMyCopy.getStatus()==1){
                        List<BeanAdapterComnunity> list=new ArrayList<>();
                        for(BeanMasterMyCopy.ResponseBean responseBean:beanMasterMyCopy.getResponse()){
                            list.add(new BeanAdapterComnunity(R.mipmap.ic_instrument_audusd,responseBean.getName(),String.valueOf(responseBean.getProfitper()),0));
                        }
                        response(list,UrlConstant.URL_MASTER_MY_COPY,null);
                        copyList=list;
                }
            }
        });
    }

    //关注的高手信息
    private void requestFocusInfo() {
        Map<String,String> map=null;
        if(map==null) {
            map = new TreeMap<>();
            map.put(RequestConstant.ACCOUNT, ACache.get(context).getAsString(RequestConstant.ACCOUNT));
        }
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_focusinfo, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s=response.body().string();
                Log.i(TAG, "onResponse12:  "+s);
                BeanMasterFocusInfo beanMasterFocusInfo=new Gson().fromJson(s,new TypeToken<BeanMasterFocusInfo>(){}.getType());
                if(beanMasterFocusInfo.getStatus()==1){
                    List<BeanAdapterComnunity> list=new ArrayList<>();
                    for(BeanMasterFocusInfo.ResponseBean responseBean:beanMasterFocusInfo.getResponse()){
                        list.add(new BeanAdapterComnunity(R.mipmap.ic_instrument_audusd,responseBean.getName(),String.valueOf(responseBean.getProfitper()),0));
                    }
                    response(list,UrlConstant.URL_MASTER_focusinfo,null);
                    focusList=list;
                }

            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        private List<BeanAdapterComnunity> list;
        public MyAdapter(List<BeanAdapterComnunity> list){
            this.list=list;
        }
        public void setData(List<BeanAdapterComnunity> list){
            this.list=list;
        }
        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyHolder holder = new MyHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_master_community, parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position, List<Object> payloads) {
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
            } else {
                Bundle payload = (Bundle) payloads.get(0);
                for(String key:payload.keySet()){
                    switch (key){
                        case TAKE_PROFIT:
                            holder.tp.setText(list.get(position).getTp());
                            holder.tp.setTextColor(getResources().getColor(Double.valueOf(list.get(position).getTp())>0?R.color.text_color_price_rise:R.color.text_color_price_fall));
                            break;
                    }
                }
            }
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.name.setText(list.get(position).getName());
            holder.tp.setText(list.get(position).getTp());
            holder.tp.setTextColor(getResources().getColor(Double.valueOf(list.get(position).getTp())>0?R.color.text_color_price_rise:R.color.text_color_price_fall));
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView name;
            TextView tp;
            public MyHolder(View itemView) {
                super(itemView);
                image=(ImageView)itemView.findViewById(R.id.civ_image);
                name=(TextView)itemView.findViewById(R.id.tv_name);
                tp=(TextView)itemView.findViewById(R.id.tv_tp);
            }
        }
    }
    private void countTimeDown(){
        CountDownTimer countDownTimer=new CountDownTimer(10000,10000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: ");
                countTimeDown();
                requestFocusInfo();
                requestCopyRelation();
                requestFocusRelation();
                requestMyCopy();
            }
        };
        countDownTimer.start();
    }
}
