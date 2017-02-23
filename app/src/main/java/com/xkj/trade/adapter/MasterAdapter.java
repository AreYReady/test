package com.xkj.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.bean_.BeanMasterInfo;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.ThreadHelper;
import com.xkj.trade.utils.ToashUtil;
import com.xkj.trade.utils.view.CustomMasterLink;
import com.xkj.trade.utils.view.CustomSeekBar;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by huangsc on 2016-12-21.
 * TODO:
 */

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.MyHolder> {
    private Context context;
    int[] ints= new int[]{1,10,100,500,1000};
    private BeanMasterRank mBeanMasterRank;
    private BeanMasterRank.MasterRank masterRank;
    private String TAG= SystemUtil.getTAG(this);
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
        holder.mName.setText(masterRank.getName());
        holder.mTvCopyCount.setText(String.valueOf(masterRank.getCopynumber()));
        holder.mTvExperienceTime.setText(masterRank.getTradeexperience()+" 天");
        holder.mTvProfitper.setText(String.valueOf(masterRank.getProfitper()));
        holder.mTvHuiceper.setText(String.valueOf(masterRank.getHuiceper())+"%");
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToashUtil.showShort(context, position + "");
                EventBus.getDefault().post(mBeanMasterRank.getResponse().get(position));
            }
        });

        holder.mCustomSeekBar.setData(ints,1);
        if(masterRank.getStatus()==1){
            holder.bCopyButton.setText(R.string.uncopy);
//            holder.bCompleteButton
        }else{
            holder.bCopyButton.setText(R.string.copy);
        }
        if(masterRank.getFstatus()==1){
            holder.bWatch.setText(R.string.unwatch);
        }else{
            holder.bWatch.setText(R.string.watch);
        }
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
                if(holder.mCustomSeekBar.getProgress()>0&&holder.mCustomSeekBar.getVisible()==View.VISIBLE) {
                    requestCopyFollow(mBeanMasterRank.getResponse().get(position),holder);
                }else{
                    ToashUtil.show(context,"手数比例无效", Toast.LENGTH_SHORT);
                }
            }
        });
        holder.bWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestFocus(masterRank,holder);
            }
        });
                holder.mCustomMasterLink.postInvalidate(masterRank,position);
    }
    //请求复制高手

    private void requestFocus(final BeanMasterRank.MasterRank masterRank, final MyHolder holder) {
        Map<String,String> map=new TreeMap();
        map.put(RequestConstant.FOCUS_ID,masterRank.getLogin());
        map.put(RequestConstant.ACCOUNT_ID,ACache.get(context).getAsString(RequestConstant.ACCOUNT));
        if(masterRank.getFstatus()==1){
            //已关注，取消关注
            OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_NOFOCUS, map, new MyCallBack() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG, "onResponse: "+response.body().string());
                    ThreadHelper.instance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.bWatch.setText(R.string.watch);
                        }
                    });
                    masterRank.setFstatus(0);
                }
            });
        }else{
            //未关注，取消关注
            OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_FOCUS, map, new MyCallBack() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG, "onResponse: "+response.body().string());
                    ThreadHelper.instance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.bWatch.setText(R.string.unwatch);
                        }
                    });
                    masterRank.setFstatus(1);
                }
            });
        }
    }

    private void requestCopyFollow(BeanMasterRank.MasterRank rank, final MyHolder holder) {
        final Map<String,String> map=new TreeMap();
        map.put(RequestConstant.MASTER_ID,rank.getLogin());
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_INFO, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s=response.body().string();
                Log.i(TAG, "onResponse: "+s);
                BeanMasterInfo info= new Gson().fromJson(s,new TypeToken<BeanMasterInfo>(){}.getType());
                map.put(RequestConstant.FOLLOW_ID, ACache.get(context).getAsString(RequestConstant.ACCOUNT));
                map.put(RequestConstant.COPY_MONEY,info.getResponse().getFollowfunds());
                map.put(RequestConstant.COPY_WAY,"3");
                map.put(RequestConstant.COPY_VOLUME,String.valueOf(holder.mCustomSeekBar.getMoney()));
                OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW, map, new MyCallBack() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i(TAG, "onResponse: "+response.body().string());
                    }
                });
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
            bWatch = (Button) itemView.findViewById(R.id.b_watch);
            mLlButtons = (LinearLayout) itemView.findViewById(R.id.ll_buttons);
            mImageView=(ImageView)itemView.findViewById(R.id.civ_master);
            mCustomSeekBar=(CustomSeekBar) itemView.findViewById(R.id.csb_rv_item_master);
            mName=(TextView)itemView.findViewById(R.id.tv_name);
            mTvCopyCount=(TextView)itemView.findViewById(R.id.tv_copy_count);
            mTvHuiceper =(TextView)itemView.findViewById(R.id.tv_huiceper);
            mTvProfitper =(TextView)itemView.findViewById(R.id.tv_profitper);
            mTvExperienceTime =(TextView)itemView.findViewById(R.id.tv_experience_time);
            mCustomMasterLink=(CustomMasterLink)itemView.findViewById(R.id.c_master_link);
        }
        ImageView mImageView;
        LinearLayout mLlButtons;
        LinearLayout mBaseMasterInfo;
        LinearLayout mCover;
        Button bCopyButton;
        Button bCompleteButton;
        Button bWatch;
        CustomSeekBar mCustomSeekBar;
        TextView mName;
        TextView mTvCopyCount;
        TextView mTvHuiceper;
        TextView mTvProfitper;
        TextView mTvExperienceTime;
        CustomMasterLink mCustomMasterLink;
    }
//    public interface onClickListener{
//        void clickCopy();
//        void clickAtten();
//    }
}
