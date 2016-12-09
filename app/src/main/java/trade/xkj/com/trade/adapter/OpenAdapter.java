package trade.xkj.com.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.bean.BeanOpenPositionData;

import static android.content.ContentValues.TAG;

/**
 * Created by huangsc on 2016-12-09.
 * TODO:
 */

public class OpenAdapter extends RecyclerView.Adapter<OpenAdapter.MyViewHolder>{
    private List<BeanOpenPositionData> mDataList;
    private Context context;
    public OpenAdapter(Context context,List<BeanOpenPositionData> mDataList){
        this.mDataList=mDataList;
        this.context=context;
    }


    @Override
    public  OpenAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OpenAdapter.MyViewHolder viewHolder=new OpenAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_social_card_open_position,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final OpenAdapter.MyViewHolder holder, final int position) {
        holder.llOnclick.setTag(0);
        holder.llOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.llHide.getTag()==null) {
                    holder.llHide.setTag(0);
                }
                if(0==(int)holder.llHide.getTag()) {
                    holder.llHide.setVisibility(View.VISIBLE);
                    holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.congratulation_joining_background_dark));
                    holder.llHide.setTag(1);
                }
                else{
                    holder.llHide.setVisibility(View.GONE);
                    holder.llOnclick.setBackgroundColor(context.getResources().getColor(R.color.color_primary_2_light_transparent));
                    holder.llHide.setTag(0);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
            llHide=(LinearLayout)itemView.findViewById(R.id.ll_hide_layout) ;
            llOnclick=(LinearLayout)itemView.findViewById(R.id.ll_onclick) ;
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
    }
}
