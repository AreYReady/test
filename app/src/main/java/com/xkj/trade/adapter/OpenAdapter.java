package com.xkj.trade.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xkj.trade.R;
import com.xkj.trade.bean_.BeanOpenPosition;
import com.xkj.trade.mvp.operate.OperatePositionActivity;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.ToashUtil;

import java.util.List;

import static com.xkj.trade.constant.RequestConstant.CURRENT_PRICE;
import static com.xkj.trade.constant.RequestConstant.PROFIT;
import static com.xkj.trade.constant.RequestConstant.STOP_LOSS;
import static com.xkj.trade.constant.RequestConstant.TAKE_PROFIT;
import static com.xkj.trade.constant.TradeDateConstant.VOLUME_MONEY;


/**
 * Created by huangsc on 2016-12-09.
 * TODO:
 */

public class OpenAdapter extends RecyclerView.Adapter<OpenAdapter.MyViewHolder> implements View.OnClickListener {
    private String TAG = SystemUtil.getTAG(this);
    private List<BeanOpenPosition.DataBean.ListBean> mDataList;
    private Context context;
//    private List<Boolean> listSelect = new ArrayList<>();
    private int mPosition;

    public OpenAdapter(Context context, List<BeanOpenPosition.DataBean.ListBean> mDataList) {
        this.mDataList = mDataList;
        this.context = context;
//        if(mDataList!=null){
//            for (int i = 0; i < mDataList.size(); i++) {
//                listSelect.add(false);
//            }
//        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_social_card_open_position, parent, false);
        return new MyViewHolder(view);
    }
    public void setData(List<BeanOpenPosition.DataBean.ListBean> mDataList ){
        this.mDataList=mDataList;
//        if(mDataList!=null){
//            for (int i = 0; i < mDataList.size(); i++) {
//                listSelect.add(false);
//            }
//        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Bundle payload = (Bundle) payloads.get(0);
            for(String key:payload.keySet()){
                switch (key){
                    case PROFIT:
                    holder.tvProfit.setText(mDataList.get(position).getProfit());
                        if(Double.valueOf(mDataList.get(position).getProfit())>0)
                            holder.tvProfit.setTextColor(context.getResources().getColor(R.color.text_color_price_rise));
                        else
                            holder.tvProfit.setTextColor(context.getResources().getColor(R.color.text_color_price_fall));
                        break;
                    case CURRENT_PRICE:
                            holder.bClosePosition.setText("平仓"+mDataList.get(position).getPrice());
                        break;
                    case TAKE_PROFIT:
                        holder.tvTakeProfit.setText(mDataList.get(position).getTp());
                        break;
                    case STOP_LOSS:
                        holder.tvStopLoss.setText(mDataList.get(position).getSl());
                        break;
                }
            }

        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BeanOpenPosition.DataBean.ListBean mData = mDataList.get(position);
        holder.llOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mPosition=position;
                if (mData.getStatus()!=0) {
                    holder.llHide.setVisibility(View.GONE);
                    holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.background_trade_item));
                    mData.setStatus(0);
                } else {
                    holder.llHide.setVisibility(View.VISIBLE);
                    holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.background_trade_item_open));
                    mData.setStatus(1);
                }
                ToashUtil.show(context,position+"",0);
            }
        });
        if (mData.getStatus()!=0) {
            holder.llHide.setVisibility(View.VISIBLE);
            holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.congratulation_joining_background_dark));
        } else {
            holder.llHide.setVisibility(View.GONE);
            holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.color_primary_2_light_transparent));
        }
        holder.bClosePosition.setOnClickListener(this);
        holder.bUnlink.setOnClickListener(this);
        holder.bEditPosition.setOnClickListener(this);
        holder.tvCountyName.setText(mData.getSymbol());
        holder.tvMoney.setText(MoneyUtil.deleteZero(MoneyUtil.mulPrice(mData.getVolume(),String.valueOf(VOLUME_MONEY))));
        holder.tvCommission.setText("$" + mData.getCommission());
        if (mData.getCmd().equals("sell")) {
            holder.tvOperate.setText("卖");
            holder.tvOperate.setTextColor(context.getResources().getColor(R.color.text_color_price_fall));
        } else {
            holder.tvOperate.setTextColor(context.getResources().getColor(R.color.text_color_price_rise));
            holder.tvOperate.setText("买");
        }
        holder.bClosePosition.setText(mData.getPrice()!=null?"平仓"+mData.getPrice():"平仓");
        holder.tvProfit.setText(mData.getProfit());
        if(Double.valueOf(mData.getProfit())>0)
            holder.tvProfit.setTextColor(context.getResources().getColor(R.color.text_color_price_rise));
        else
            holder.tvProfit.setTextColor(context.getResources().getColor(R.color.text_color_price_fall));
        holder.tvStopLoss.setText(mData.getSl());
        holder.tvTakeProfit.setText(mData.getTp());
        holder.tvOpenTime1.setText(mData.getOpentime());
        holder.tvOpenRate.setText(mData.getOpenprice());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_edit:
                context.startActivity(new Intent(context, OperatePositionActivity.class)
                        .putExtra(OperatePositionActivity.OPERATEACTION, OperatePositionActivity.OperateAction.EDIT_POSITION)
                        .putExtra(OperatePositionActivity.JSON_DATA,new Gson().toJson(mDataList.get(mPosition), BeanOpenPosition.DataBean.ListBean.class)));
                break;
            case R.id.b_close_position:
                context.startActivity(new Intent(context, OperatePositionActivity.class)
                        .putExtra(OperatePositionActivity.OPERATEACTION, OperatePositionActivity.OperateAction.ClOSE_POSITION)
                        .putExtra(OperatePositionActivity.JSON_DATA,new Gson().toJson(mDataList.get(mPosition), BeanOpenPosition.DataBean.ListBean.class)));
                break;
            case R.id.b_unlink:
                context.startActivity(new Intent(context, OperatePositionActivity.class)
                        .putExtra(OperatePositionActivity.OPERATEACTION, OperatePositionActivity.OperateAction.UNLINK)
                        .putExtra(OperatePositionActivity.JSON_DATA,new Gson().toJson(mDataList.get(mPosition), BeanOpenPosition.DataBean.ListBean.class)));
                break;
        }
    }


   public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(final View itemView) {
            super(itemView);
            llHide = (LinearLayout) itemView.findViewById(R.id.ll_hide_layout);
            llOnclick = (LinearLayout) itemView.findViewById(R.id.ll_onclick);
            tvCountyName = (TextView) itemView.findViewById(R.id.tv_symbol_name);
            tvOperate = (TextView) itemView.findViewById(R.id.tv_operater);
            tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
            tvProfit = (TextView) itemView.findViewById(R.id.tv_profit);
            ivType = (ImageView) itemView.findViewById(R.id.iv_type);
            tvCommission = (TextView) itemView.findViewById(R.id.amount);
            tvStopLoss = (TextView) itemView.findViewById(R.id.stop_loss);
            tvTakeProfit = (TextView) itemView.findViewById(R.id.take_profit);
            tvOpenTime1 = (TextView) itemView.findViewById(R.id.open_time1);
//            tvOpenTime2 = (TextView) itemView.findViewById(R.id.open_time2);
            tvSwap = (TextView) itemView.findViewById(R.id.swap);
            tvOpenRate = (TextView) itemView.findViewById(R.id.open_rate);
            bEditPosition = (Button) itemView.findViewById(R.id.b_edit);
            bClosePosition = (Button) itemView.findViewById(R.id.b_close_position);
            bUnlink = (Button) itemView.findViewById(R.id.b_unlink);
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
        TextView tvOpenTime2;
        TextView tvSwap;
        TextView tvOpenRate;
        Button bEditPosition;
        Button bClosePosition;
        Button bUnlink;

    }
}
