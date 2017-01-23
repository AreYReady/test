package trade.xkj.com.trade.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.utils.SystemUtil;
import trade.xkj.com.trade.bean.BeanOpenPositionData;
import trade.xkj.com.trade.mvp.operate.OperatePositionActivity;

import static trade.xkj.com.trade.R.id.ll_onclick;

/**
 * Created by huangsc on 2016-12-09.
 * TODO:
 */

public class OpenAdapter extends RecyclerView.Adapter<OpenAdapter.MyViewHolder> implements View.OnClickListener{
    private String TAG= SystemUtil.getTAG(this);
    private List<BeanOpenPositionData> mDataList;
    private Context context;
    private List<Boolean> listSelect=new ArrayList<>();
    public OpenAdapter(Context context,List<BeanOpenPositionData> mDataList){
        this.mDataList=mDataList;
        this.context=context;
        for(int i=0;i<mDataList.size();i++){
            listSelect.add(false);
        }
    }


    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.rv_item_social_card_open_position,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.llOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: "+position+"  tvmoney"+holder.tvMoney.getText());
                if(listSelect.get(position)){
                    holder.llHide.setVisibility(View.GONE);
                    holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.background_trade_item));
                    listSelect.set(position,false);
                }else{
                    holder.llHide.setVisibility(View.VISIBLE);
                    holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.background_trade_item_open));
                    listSelect.set(position,true);
                }
            }
        });
        if(listSelect.get(position)){
            holder.llHide.setVisibility(View.VISIBLE);
            holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.congratulation_joining_background_dark));

        }else{
            holder.llHide.setVisibility(View.GONE);
            holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.color_primary_2_light_transparent));
        }
        holder.bClosePosition.setOnClickListener(this);
        holder.bUnlink.setOnClickListener(this);
        holder.bEditPosition.setOnClickListener(this);
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_edit:
                context.startActivity(new Intent(context, OperatePositionActivity.class).putExtra(OperatePositionActivity.OPERATEACTION, OperatePositionActivity.OperateAction.EDIT_POSITION));
                break;
            case R.id.b_close_position:
                context.startActivity(new Intent(context, OperatePositionActivity.class).putExtra(OperatePositionActivity.OPERATEACTION, OperatePositionActivity.OperateAction.ClOSE_POSITION));
                break;
            case R.id.b_unlink:
                context.startActivity(new Intent(context, OperatePositionActivity.class).putExtra(OperatePositionActivity.OPERATEACTION, OperatePositionActivity.OperateAction.UNLINK));
                break;
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(final View itemView) {
            super(itemView);
            llHide=(LinearLayout)itemView.findViewById(R.id.ll_hide_layout) ;
            llOnclick=(LinearLayout)itemView.findViewById(ll_onclick) ;
            tvCountyName =(TextView)itemView.findViewById(R.id.tv_county_name);
            tvOperate =(TextView)itemView.findViewById(R.id.tv_operater);
            tvMoney =(TextView)itemView.findViewById(R.id.tv_money);
            tvProfit =(TextView)itemView.findViewById(R.id.tv_profit);
            ivType =(ImageView) itemView.findViewById(R.id.iv_type);
            tvCommission =(TextView)itemView.findViewById(R.id.commission);
            tvStopLoss =(TextView)itemView.findViewById(R.id.stop_loss);
            tvTakeProfit =(TextView)itemView.findViewById(R.id.take_profit);
            tvOpenTime1 =(TextView)itemView.findViewById(R.id.open_time1);
            tvOpenTime2 =(TextView)itemView.findViewById(R.id.open_time2);
            tvSwap =(TextView)itemView.findViewById(R.id.swap);
            tvOpenRate =(TextView)itemView.findViewById(R.id.open_rate);
            bEditPosition =(Button)itemView.findViewById(R.id.b_edit);
            bClosePosition =(Button)itemView.findViewById(R.id.b_close_position);
            bUnlink=(Button)itemView.findViewById(R.id.b_unlink);
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
