package com.xkj.trade.mvp.master.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean_.BeanAdapterStream;
import com.xkj.trade.diffcallback.MasterStreamDiff;
import com.xkj.trade.utils.ThreadHelper;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static com.xkj.trade.constant.RequestConstant.TAKE_PROFIT;

/**
 * Created by huangsc on 2016-12-27.
 * TODO:
 */

public class FragmentMasterStream extends BaseFragment {
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private List<BeanAdapterStream> mBeanAdapterStreamList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_master_stream, null);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_master_stream);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        private List<BeanAdapterStream> list;

        public MyAdapter(List<BeanAdapterStream> list){
            this.list=list;
        }
        public void setData(List<BeanAdapterStream> list){
            this.list=list;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyHolder holder = new MyHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_master_stream, parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position, List<Object> payloads) {
            if(payloads.isEmpty()){
                onBindViewHolder(holder, position);
            }else{
                Bundle bundle = (Bundle)payloads.get(0);
                for(String key:bundle.keySet()){
                    switch (key) {
                        case TAKE_PROFIT:
                            holder.tvTp.setText(list.get(position).getTp());
                            holder.tvTp.setTextColor(getResources().getColor(Double.valueOf(list.get(position).getTp())>0?R.color.text_color_price_rise:R.color.text_color_price_fall));
                            break;
                    }
                }
            }
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            BeanAdapterStream beanAdapterStream = list.get(position);
            if(beanAdapterStream.getStatus()==1) {
                holder.tvPosition.setText(String.format(getString(R.string.open_position_is),beanAdapterStream.getSymbol()));
                holder.tvPositionPrices.setText(String.format(getString(R.string.open_position_is_open_price),beanAdapterStream.getPrices()));
            }else if(beanAdapterStream.getStatus()==2){
                holder.tvPosition.setText(String.format(getString(R.string.closed_position_is),beanAdapterStream.getSymbol()));
                holder.tvPositionPrices.setText(String.format(getString(R.string.closed_position_is_close_price),beanAdapterStream.getPrices()));
            }
            holder.tvTp.setText(beanAdapterStream.getTp());

            holder.tvTp.setTextColor(getResources().getColor(Double.valueOf(beanAdapterStream.getTp().replace("%",""))>0?R.color.text_color_price_rise:R.color.text_color_price_fall));
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            TextView tvPosition;
            TextView tvPositionPrices;
            TextView tvTp;
            public MyHolder(View itemView) {
                super(itemView);
                tvPosition=(TextView)itemView.findViewById(R.id.tv_position);
                tvPositionPrices=(TextView)itemView.findViewById(R.id.tv_position_price);
                tvTp=(TextView)itemView.findViewById(R.id.tv_tp);
            }
        }
    }
    DiffUtil.DiffResult diffResult;
    @Subscribe
    public void getStreamListData(final List<BeanAdapterStream> listData){
        Log.i(TAG, "getStreamListData: ");
        if(mMyAdapter==null){
                    mBeanAdapterStreamList=listData;
        }else{
            diffResult=DiffUtil.calculateDiff(new MasterStreamDiff(mBeanAdapterStreamList,listData));
        }
        ThreadHelper.instance().runOnUiThread(runnable);
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
                if(mMyAdapter==null){
                    mRecyclerView.setAdapter(mMyAdapter=new MyAdapter(mBeanAdapterStreamList));
                }else {
                    diffResult.dispatchUpdatesTo(mMyAdapter);
                }
        }
    };
}
