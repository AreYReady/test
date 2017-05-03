package com.xkj.trade.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.bean_.BeanBaseResponse;
import com.xkj.trade.bean_.BeanMasterInfo;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.bean_notification.NotificationMasterStatus;
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
    int[] ints = new int[]{1, 10, 100, 500, 1000};
    String[] strings = new String[]{"1", "10", "100", "500", "1000"};
    private BeanMasterRank mBeanMasterRank;
    private BeanMasterRank.MasterRank masterRank;
    private String TAG = SystemUtil.getTAG(this);
    private Map<Integer, MasterStatus> mapStatus = new ArrayMap<>();

    //    private OnItemClickListener onItemClickListener;
    public MasterAdapter(Context context, BeanMasterRank beanMasterRank) {
        this.context = context;
        this.mBeanMasterRank = beanMasterRank;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_master, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder: ");
        masterRank = mBeanMasterRank.getResponse().get(position);
        holder.mName.setText(masterRank.getName());
        holder.mTvCopyCount.setText(String.valueOf(masterRank.getCopynumber()));
        holder.mTvExperienceTime.setText(masterRank.getTradeexperience() + " 天");
        holder.mTvProfitper.setText(String.valueOf(masterRank.getProfitper()));
        holder.mTvHuiceper.setText(String.valueOf(masterRank.getHuiceper()) + "%");
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(mBeanMasterRank.getResponse().get(position));
            }
        });
        holder.mCoverUncopyPrompt.setText(String.format(context.getResources().getString(R.string.are_you_sure_uncopy_follow_master),masterRank.getName()));
        //判断是否存在，是否需要重置
        if (mapStatus.get(position) != null && mapStatus.get(position).getEnter()) {
        } else {
            holder.mCustomSeekBar.setData(strings, "1");
        }
        if (masterRank.getStatus() == 1) {
            holder.bCopyButton.setText(R.string.uncopy);
            holder.mCoverUncopy.setVisibility(View.VISIBLE);
            holder.mCoverCopy.setVisibility(View.GONE);
        } else {
            holder.bCopyButton.setText(R.string.copy);
            holder.mCoverUncopy.setVisibility(View.GONE);
            holder.mCoverCopy.setVisibility(View.VISIBLE);
        }
        if (masterRank.getFstatus() == 1) {
            holder.bWatch.setText(R.string.unwatch);
        } else {
            holder.bWatch.setText(R.string.watch);
        }

        holder.bCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bCopyButtonClick(holder, position);
                mapStatus.put(position, new MasterStatus(null, true));
            }
        });
        holder.bCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterStatus status = MasterAdapter.this.mapStatus.get(position);
                bClickUp(holder,position);
                if (mBeanMasterRank.getResponse().get(position).getStatus() == 1) {
                    //已经复制，取消复制
                    requestUncopyFollow(mBeanMasterRank.getResponse().get(position),holder);
                } else {
                    //没有复制，请求复制
                    if (holder.mCustomSeekBar.getProgress() > 0 && holder.mCustomSeekBar.getVisible() == View.VISIBLE) {
                        requestCopyFollow(mBeanMasterRank.getResponse().get(position), holder);
                        status.setVolume(String.valueOf(holder.mCustomSeekBar.getMoney()));
                    } else {
                        ToashUtil.show(context, "手数比例无效", Toast.LENGTH_SHORT);
                        status.setVolume(null);
                    }
                }
            }
        });
        holder.bWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestFocus(mBeanMasterRank.getResponse().get(position), holder);
            }
        });
        holder.mCustomMasterLink.postInvalidate(masterRank);
        holder.mIvCopyDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bClickUp(holder,position);
            }
        });
        holder.mIvUncopyDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bClickUp(holder,position);
            }
        });
        //状态处理
        holder.mCustomSeekBar.setTextChangeListener(new CustomSeekBar.TextChangeListener() {
            @Override
            public void textChange(String amount) {
                if (mapStatus.get(position) != null && mapStatus.get(position).getEnter()) {
                    if (!"1".equals(amount))//只能人工判断了。后期想办法
                        mapStatus.get(position).setVolume(amount);
                }
            }
        });
        if (mapStatus.get(position) != null && mapStatus.get(position).getEnter()) {
            holder.mCover.setVisibility(View.VISIBLE);
            holder.mBaseMasterInfo.setVisibility(View.GONE);
            holder.mCustomSeekBar.setMoney(mapStatus.get(position).getVolume());
            holder.mLlButtons.setVisibility(View.GONE);
            holder.bCompleteButton.setVisibility(View.VISIBLE);
        } else {
            holder.mBaseMasterInfo.setVisibility(View.VISIBLE);
            holder.mCover.setVisibility(View.GONE);
            holder.mLlButtons.setVisibility(View.VISIBLE);
            holder.bCompleteButton.setVisibility(View.GONE);
        }
    }



    private void bClickUp(MyHolder holder,int position) {
        holder.mBaseMasterInfo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_dwon2));
        holder.mCover.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_down));
        holder.mBaseMasterInfo.setVisibility(View.VISIBLE);
        holder.mCover.setVisibility(View.GONE);
        holder.mLlButtons.setVisibility(View.VISIBLE);
        holder.bCompleteButton.setVisibility(View.GONE);
        if (mapStatus.get(position) != null && mapStatus.get(position).getEnter()) {
            mapStatus.get(position).setEnter(false);
        }

    }

    private void bCopyButtonClick(MyHolder holder, int position) {
        holder.mBaseMasterInfo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up2));
        holder.mCover.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up));
        holder.mBaseMasterInfo.setVisibility(View.GONE);
        holder.mCover.setVisibility(View.VISIBLE);
        holder.mLlButtons.setVisibility(View.GONE);
        holder.bCompleteButton.setVisibility(View.VISIBLE);

    }
    //请求关注高手

    private void requestFocus(final BeanMasterRank.MasterRank rank, final MyHolder holder) {
        Map<String, String> map = new TreeMap();
        map.put(RequestConstant.FOCUS_ID, rank.getLogin());
        map.put(RequestConstant.ACCOUNT_ID, ACache.get(context).getAsString(RequestConstant.ACCOUNT));
        if (rank.getFstatus() == 1) {
            //已关注，取消关注
            OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_NOFOCUS, map, new MyCallBack() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG, "onResponse: " + response.body().string());
                    ThreadHelper.instance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.bWatch.setText(R.string.watch);
                        }
                    });
                    rank.setFstatus(0);
                    //通知关注状态改变
                        EventBus.getDefault().post(new NotificationMasterStatus(rank.getLogin(),rank.getFstatus(),rank.getStatus()));
                }
            });
        } else {
            //未关注，关注
            OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_FOCUS, map, new MyCallBack() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG, "onResponse: " + response.body().string());
                    ThreadHelper.instance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.bWatch.setText(R.string.unwatch);
                        }
                    });
                    rank.setFstatus(1);
                        EventBus.getDefault().post(new NotificationMasterStatus(rank.getLogin(),rank.getFstatus(),rank.getStatus()));
                }
            });
        }
    }
    //请求取消复制高手
    private void requestUncopyFollow(final BeanMasterRank.MasterRank rank, final MyHolder holder) {
        final Map<String ,String> map=new TreeMap<>();
        map.put(RequestConstant.MASTER_ID, rank.getLogin());
        map.put(RequestConstant.FOLLOW_ID, ACache.get(context).getAsString(RequestConstant.ACCOUNT));
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_NOFOLLOW, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
               BeanBaseResponse info =new Gson().fromJson(response.body().string(),BeanBaseResponse.class);
                if(info.getStatus()==1){
                    Log.i(TAG, "onResponse: "+info.toString());
                    rank.setStatus(0);
                        EventBus.getDefault().post(new NotificationMasterStatus(rank.getLogin(),rank.getFstatus(),rank.getStatus()));

                    ThreadHelper.instance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.bCopyButton.setText(R.string.copy);
                            holder.mCoverCopy.setVisibility(View.VISIBLE);
                            holder.mCoverUncopy.setVisibility(View.GONE);
                        }
                    });
                }

            }
        });
    }
    //请求复制高手
    private void requestCopyFollow(final BeanMasterRank.MasterRank rank, final MyHolder holder) {
        final Map<String, String> map = new TreeMap();
        map.put(RequestConstant.MASTER_ID, rank.getLogin());
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_INFO, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, "onResponse: " + s);
                BeanMasterInfo info = new Gson().fromJson(s, BeanMasterInfo.class);
                map.put(RequestConstant.FOLLOW_ID, ACache.get(context).getAsString(RequestConstant.ACCOUNT));
                map.put(RequestConstant.COPY_MONEY, info.getResponse().getFollowfunds());
                map.put(RequestConstant.COPY_WAY, "3");
                map.put(RequestConstant.COPY_VOLUME, String.valueOf(holder.mCustomSeekBar.getMoney()));
                OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW, map, new MyCallBack() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.i(TAG, "onResponse: " + response.body().string());
                        BeanBaseResponse info =new Gson().fromJson(response.body().string(),BeanBaseResponse.class);
                        if(info.getStatus()==1){
                            Log.i(TAG, "onResponse: "+info.toString());
                            rank.setStatus(1);
                                EventBus.getDefault().post(new NotificationMasterStatus(rank.getLogin(),rank.getFstatus(),rank.getStatus()));
                            ThreadHelper.instance().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.bCopyButton.setText(R.string.uncopy);
                                    holder.mCoverCopy.setVisibility(View.GONE);
                                    holder.mCoverUncopy.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeanMasterRank.getResponse()==null?0:mBeanMasterRank.getResponse().size();
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
            mImageView = (ImageView) itemView.findViewById(R.id.civ_master);
            mCustomSeekBar = (CustomSeekBar) itemView.findViewById(R.id.csb_rv_item_master);
            mName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvCopyCount = (TextView) itemView.findViewById(R.id.tv_copy_count);
            mTvHuiceper = (TextView) itemView.findViewById(R.id.tv_huiceper);
            mTvProfitper = (TextView) itemView.findViewById(R.id.tv_profitper);
            mTvExperienceTime = (TextView) itemView.findViewById(R.id.tv_experience_time);
            mCustomMasterLink = (CustomMasterLink) itemView.findViewById(R.id.c_master_link);
            mIvCopyDown = (ImageView) itemView.findViewById(R.id.iv_take_rebound);
            mIvUncopyDown = (ImageView) itemView.findViewById(R.id.iv_uncopy_take_rebound);
            mCoverCopy = (LinearLayout) itemView.findViewById(R.id.ll_cover_copy);
            mCoverUncopy = (RelativeLayout) itemView.findViewById(R.id.rl_cover_uncopy);
            mCoverUncopyPrompt = (TextView) itemView.findViewById(R.id.tv_cover_uncopy_prompt);

        }

        LinearLayout mCoverCopy;
        RelativeLayout mCoverUncopy;
        TextView mCoverUncopyPrompt;
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
        ImageView mIvCopyDown;
        ImageView mIvUncopyDown;
    }

    class MasterStatus {
        String volume;
        boolean enter;

        public MasterStatus(String volume, boolean enter) {
            this.volume = volume;
            this.enter = enter;
        }

        public String getVolume() {
            return volume;
        }

        public boolean getEnter() {
            return enter;
        }

        public void setEnter(boolean enter) {
            this.enter = enter;
        }

        public void setVolume(String volume) {
            this.volume = volume;

        }
    }
}
