package trade.xkj.com.trade.mvp.main_trade.v;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.base.BaseFragment;
import trade.xkj.com.trade.bean.BeanOpenPositionData;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class FragmentClosePosition extends BaseFragment {
    private ViewPager mViewPager;
    private List<BeanOpenPositionData> mDataList;
    private BeanOpenPositionData mBeanOpenPositionData;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_5, null);
        return view;
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) view.findViewById(R.id.vp_indicator_content);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv_item3_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new OpenAdapter());
        Log.i(TAG, "initView: mDataList"+mDataList.size());
    }

    @Override
    protected void initData() {
        mDataList=new ArrayList<>();
        for(int i=0;i<30;i++){
            mBeanOpenPositionData=new BeanOpenPositionData();
            mDataList.add(mBeanOpenPositionData);
        }
    }

    class OpenAdapter extends RecyclerView.Adapter<OpenAdapter.MyViewHolder>{


        @Override
        public OpenAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder viewHolder=new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_social_card_close_position,parent,false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final OpenAdapter.MyViewHolder holder, final int position) {
            Log.i(TAG, "onBindViewHolder: "+position);
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
}
