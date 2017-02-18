package com.xkj.trade.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.bean_.BeanPendingPosition;
import com.xkj.trade.mvp.operate.OperatePositionActivity;
import com.xkj.trade.utils.SystemUtil;

import java.util.List;
import java.util.Map;

import static com.xkj.trade.constant.TradeDateConstant.VOLUME_MONEY;

/**
 * Created by huangsc on 2017-02-16.
 * TODO:挂单
 */

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.MyViewHolder>implements View.OnClickListener {
    private String TAG= SystemUtil.getTAG(this);
    private Context context;
    private List<BeanPendingPosition.DataBean.ListBean> mDataList;
    private int mPosition;
    private BeanPendingPosition.DataBean.ListBean mData;
//    private List<Boolean> isClick;
    private Map<Integer,Boolean> isClick=new ArrayMap<Integer,Boolean>();

    public PendingAdapter(Context context, List<BeanPendingPosition.DataBean.ListBean> mDataList){
        this.context=context;
        this.mDataList=mDataList;
    }
    public void setData(List<BeanPendingPosition.DataBean.ListBean> mDataList){
        this.mDataList=mDataList;
    }
    @Override
    public PendingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PendingAdapter.MyViewHolder viewHolder = new PendingAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_social_card_pending, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PendingAdapter.MyViewHolder holder, final int position) {
        mData=mDataList.get(position);
        holder.llOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClick.containsKey(position)){
                    isClick.remove(position);
                    holder.llHide.setVisibility(View.GONE);
                    holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.color_primary_2_light_transparent));
                }else{
                    isClick.put(position,true);
                    holder.llHide.setVisibility(View.VISIBLE);
                    holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.congratulation_joining_background_dark));
                }
                Log.i(TAG, "onClick: "+mPosition);

            }
        });
        if(isClick.containsKey(position)){
            holder.llHide.setVisibility(View.VISIBLE);
            holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.congratulation_joining_background_dark));
        }else{
            holder.llHide.setVisibility(View.GONE);
            holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.color_primary_2_light_transparent));
        }
        holder.bDeletePendingPosition.setOnClickListener(this);
        holder.bEditPendingPosition.setOnClickListener(this);
        holder.tvCountyName.setText(mData.getSymbol());
        holder.tvMoney.setText(String.valueOf(Double.valueOf(mData.getVolume()) * VOLUME_MONEY));
        if (mData.getCmd().equals("sell")) {
            holder.tvOperate.setText("卖");
            holder.tvOperate.setTextColor(context.getResources().getColor(R.color.text_color_price_fall));
        } else {
            holder.tvOperate.setTextColor(context.getResources().getColor(R.color.text_color_price_rise));
            holder.tvOperate.setText("买");
        }
        holder.tvProfit.setText(mData.getProfit());
        if(Double.valueOf(mData.getProfit())>0)
            holder.tvProfit.setTextColor(context.getResources().getColor(R.color.text_color_price_rise));
        else
            holder.tvProfit.setTextColor(context.getResources().getColor(R.color.text_color_price_fall));
        holder.tvStopLoss.setText(mData.getSl());
        holder.tvTakeProfit.setText(mData.getTp());
//        holder.tvOpenTime1.setText(mData.getOpentime());
//        holder.tvOpenRate.setText(mData.getOpenprice());
    }
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.b_delete_pending_position:
                context.startActivity(new Intent(context, OperatePositionActivity.class).putExtra(OperatePositionActivity.OPERATEACTION, OperatePositionActivity.OperateAction.DELETE_PENDING_POSITION));
                break;
            case R.id.b_edit:
                context.startActivity(new Intent(context, OperatePositionActivity.class).putExtra(OperatePositionActivity.OPERATEACTION, OperatePositionActivity.OperateAction.EDIT_PENDING_POSITION));
                break;
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
            llHide = (LinearLayout) itemView.findViewById(R.id.ll_hide_layout);
            llOnclick = (LinearLayout) itemView.findViewById(R.id.ll_onclick);
            tvCountyName = (TextView) itemView.findViewById(R.id.tv_county_name);
            tvOperate = (TextView) itemView.findViewById(R.id.tv_operater);
            tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
            tvProfit = (TextView) itemView.findViewById(R.id.tv_profit);
            ivType = (ImageView) itemView.findViewById(R.id.iv_type);
            tvCommission = (TextView) itemView.findViewById(R.id.amount);
            tvStopLoss = (TextView) itemView.findViewById(R.id.stop_loss);
            tvTakeProfit = (TextView) itemView.findViewById(R.id.take_profit);
            tvOpenTime1 = (TextView) itemView.findViewById(R.id.open_time1);
//                tvOpenTime2 = (TextView) itemView.findViewById(R.id.open_time2);
            tvSwap = (TextView) itemView.findViewById(R.id.swap);
            tvOpenRate = (TextView) itemView.findViewById(R.id.open_rate);
            bEditPendingPosition = (Button) itemView.findViewById(R.id.b_edit);
            bDeletePendingPosition = (Button) itemView.findViewById(R.id.b_delete_pending_position);
        }

        LinearLayout llHide;
        LinearLayout llOnclick;
        TextView tvCountyName;
        TextView tvOperate;
        TextView tvMoney;
        TextView tvProfit;
        ImageView ivType;
        TextView tvCommission;
        TextView tvStopLoss;
        TextView tvTakeProfit;
        TextView tvOpenTime1;
        //            TextView tvOpenTime2;
        TextView tvSwap;
        TextView tvOpenRate;
        Button bDeletePendingPosition;
        Button bEditPendingPosition;
    }
}