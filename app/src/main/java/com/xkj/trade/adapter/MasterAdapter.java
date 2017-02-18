package com.xkj.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.bean.BeanMasterInfo;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.utils.ToashUtil;
import com.xkj.trade.utils.view.CustomSeekBar;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by huangsc on 2016-12-21.
 * TODO:
 */

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.MyHolder> {
    private Context context;
    private int position;
    int[] ints= new int[]{100,120,140,200,300};
    private BeanMasterRank mBeanMasterRank;
    private BeanMasterRank.MasterRank masterRank;
    //    private OnItemClickListener onItemClickListener;
    public MasterAdapter(Context context, BeanMasterRank beanMasterRank) {
        this.context = context;
        this.mBeanMasterRank=beanMasterRank;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_master, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        masterRank = mBeanMasterRank.getResponse().get(position);
        this.position = position;
        holder.mName.setText(masterRank.getName());
        holder.mTvCopyCount.setText(String.valueOf(masterRank.getCopynumber()));
        holder.mTvExperienceTime.setText(masterRank.getTradeexperience()+" 天");
        holder.mTvProfitper.setText(String.valueOf(masterRank.getProfitper()));
        holder.mTvHuiceper.setText(String.valueOf(masterRank.getHuiceper())+"%");
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToashUtil.showShort(context, position + "");
                EventBus.getDefault().post(new BeanMasterInfo());
            }
        });

        holder.mCustomSeekBar.setData(ints,1);
        holder.bCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mBaseMasterInfo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up2));
                holder.mCover.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up));
                holder.mBaseMasterInfo.setVisibility(View.GONE);
                holder.mCover.setVisibility(View.VISIBLE);
                holder.mLlButtons.setVisibility(View.GONE);
                holder.bCompleteButton.setVisibility(View.VISIBLE);
            }
        });
        holder.bCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mBaseMasterInfo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_dwon2));
                holder.mCover.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_down));
                holder.mBaseMasterInfo.setVisibility(View.VISIBLE);
                holder.mCover.setVisibility(View.GONE);
                holder.mLlButtons.setVisibility(View.VISIBLE);
                holder.bCompleteButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeanMasterRank.getResponse().size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(View itemView) {
            super(itemView);
            mBaseMasterInfo = (LinearLayout) itemView.findViewById(R.id.ll_master_base);
            mCover = (LinearLayout) itemView.findViewById(R.id.ll_cover);
            bCompleteButton = (Button) itemView.findViewById(R.id.b_complete);
            bCopyButton = (Button) itemView.findViewById(R.id.b_copy);
            mLlButtons = (LinearLayout) itemView.findViewById(R.id.ll_buttons);
            mImageView=(ImageView)itemView.findViewById(R.id.civ_master);
            mCustomSeekBar=(CustomSeekBar) itemView.findViewById(R.id.csb_rv_item_master);
            mName=(TextView)itemView.findViewById(R.id.tv_name);
            mTvCopyCount=(TextView)itemView.findViewById(R.id.tv_Copy_count);
            mTvHuiceper =(TextView)itemView.findViewById(R.id.tv_huiceper);
            mTvProfitper =(TextView)itemView.findViewById(R.id.tv_profitper);
            mTvExperienceTime =(TextView)itemView.findViewById(R.id.tv_experience_time);
        }
        ImageView mImageView;
        LinearLayout mLlButtons;
        LinearLayout mBaseMasterInfo;
        LinearLayout mCover;
        Button bCopyButton;
        Button bCompleteButton;
        CustomSeekBar mCustomSeekBar;
        TextView mName;
        TextView mTvCopyCount;
        TextView mTvHuiceper;
        TextView mTvProfitper;
        TextView mTvExperienceTime;
    }
}
