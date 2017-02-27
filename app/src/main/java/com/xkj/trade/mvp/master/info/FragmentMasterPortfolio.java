package com.xkj.trade.mvp.master.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean.BeanPortfolioData;
import com.xkj.trade.bean_.BeanMasterProtfolio;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ThreadHelper;
import com.xkj.trade.utils.view.CustomPeriodButtons2;
import com.xkj.trade.utils.view.CustomPortfolio;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Response;

import static com.xkj.trade.mvp.master.info.FragmentMasterInfo.MASTER_INFO;

/**
 * Created by huangsc on 2016-12-27.
 * TODO:
 */

public class FragmentMasterPortfolio extends BaseFragment {
    CustomPortfolio customPortfolio;
    ArrayList<BeanPortfolioData> list;
    BeanMasterRank.MasterRank rank;
    CustomPeriodButtons2 mCustomPeriodButtons2;
    private String time="1";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_master_portfolio, null);
        return view;
    }

    @Override
    protected void initData() {
        rank = new Gson().fromJson(this.getArguments().getString(MASTER_INFO), new TypeToken<BeanMasterRank.MasterRank>() {
        }.getType());
        list=new ArrayList<>();
    }

    @Override
    protected void initView() {
        customPortfolio=(CustomPortfolio)  view.findViewById(R.id.cp_portfolio);
        mCustomPeriodButtons2=(CustomPeriodButtons2)view.findViewById(R.id.cpb_master_period);
        mCustomPeriodButtons2.addCheckChangeListener(new CustomPeriodButtons2.CheckChangeListener() {
            @Override
            public void CheckChange(String period) {
                time=period;
                requestPortfolio();
            }
        });
        requestPortfolio();
    }
    private void requestPortfolio(){
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.ACCOUNT,rank.getLogin());
        map.put(RequestConstant.DATE_RANGE,time);
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_DETAIL, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s;
                Log.i(TAG, "call"+ call.request()+  " onResponse: "+(s=response.body().string()));
                BeanMasterProtfolio beanMasterProtfolio=new Gson().fromJson(s,new TypeToken<BeanMasterProtfolio>(){}.getType());
                if(beanMasterProtfolio.getStatus()==1){
                    responsePortfolio(beanMasterProtfolio);
                }
            }
        });
    }

    private void responsePortfolio(BeanMasterProtfolio beanMasterProtfolio) {
        list.clear();
        int count=0;
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        int begin=0;
        List<BeanMasterProtfolio.ResponseBean.SumordersBean> sumorders = beanMasterProtfolio.getResponse().getSumorders();
        if(sumorders.size()<=0){

        }else{
            BeanMasterProtfolio.ResponseBean.SumordersBean temp;
            for(int i=0;i<sumorders.size()-1;i++){
                for(int j=i+1;j<sumorders.size();j++){
                    if(Integer.valueOf(sumorders.get(i).getOrders())<Integer.valueOf(sumorders.get(j).getOrders())){
                        temp=sumorders.get(i);
                        sumorders.set(i,sumorders.get(j));
                        sumorders.set(j,temp);
                    }
                }
            }
            for(int i=0;i<sumorders.size();i++){
                count=count+Integer.valueOf(sumorders.get(i).getOrders());
            }
            if(sumorders.size()<4){
                for(int i=0;i<sumorders.size();i++) {
                    BeanPortfolioData data = new BeanPortfolioData();
                    data.setBeginAngle(begin);
                    data.setSweepAngle(Integer.valueOf(sumorders.get(i).getOrders()) * (360 / count));
                    begin = begin + Integer.valueOf(sumorders.get(i).getOrders()) * (360 / count);
                    Log.i("hsc", "onCreate: " + begin);
                    data.setSymbol(sumorders.get(i).getSymbol() + "\n" + numberFormat.format((float) Integer.valueOf(sumorders.get(i).getOrders()) / (float) count * 100) + "%");
                    data.setType("测试");
                    list.add(data);
                }
            }else{
                for(int i=0;i<3;i++) {
                    BeanPortfolioData data = new BeanPortfolioData();
                    data.setBeginAngle(begin);
                    data.setSweepAngle(Integer.valueOf(sumorders.get(i).getOrders())*(360/count));
                    begin = begin + Integer.valueOf(sumorders.get(i).getOrders())*(360/count);
                    Log.i("hsc", "onCreate: " + begin);
                    data.setSymbol(sumorders.get(i).getSymbol()+"\n"+numberFormat.format((float) Integer.valueOf(sumorders.get(i).getOrders()) / (float) count* 100)+"%");
                    data.setType("测试");
                    list.add(data);
                }
                BeanPortfolioData data=new BeanPortfolioData();
                data.setBeginAngle(begin);
                data.setSweepAngle(360-begin);
                data.setSymbol("其他\n"+numberFormat.format((360-begin)/ (float) 360* 100)+"%");
                list.add(data);
            }
        }
        customPortfolio.setData(list);
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customPortfolio.postInvalidate();
            }
        });
    }
}
