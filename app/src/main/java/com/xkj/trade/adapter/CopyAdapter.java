package com.xkj.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.MyCallBack;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.bean_.BeanBaseResponse;
import com.xkj.trade.bean_.BeanMasterMyCopy;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.bean_notification.NotificationMasterStatus;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.SystemUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static com.xkj.trade.base.MyApplication.rank;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class CopyAdapter extends RecyclerView.Adapter<CopyAdapter.MyViewHolder>{
    private Context context;
    private List<BeanMasterMyCopy.ResponseBean> mDataList;
    private String TAG= SystemUtil.getTAG(this);
    public CopyAdapter(Context context, List<BeanMasterMyCopy.ResponseBean> mDataList){
        this.context=context;
        this.mDataList=mDataList;
    }
    @Override
    public CopyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder=new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_social_card_master_brief,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CopyAdapter.MyViewHolder holder, final int position) {
        BeanMasterMyCopy.ResponseBean responseBean = mDataList.get(position);
        holder.tvName.setText(mDataList.get(position).getName());
        holder.watchButton.setVisibility(View.INVISIBLE);
        holder.actionButton.setText(R.string.uncopy);
        holder.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bClickDown(holder,position);
            }
        });
        holder.bYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDataList.get(position).getUiStatus()){
                    mDataList.get(position).setUiStatus(false);
                }
                requestUnCopyData(mDataList.get(position).getMasterid(),position);
            }
        });
        holder.bNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bClickUp(holder, position);
                if(mDataList.get(position).getUiStatus()){
                    mDataList.get(position).setUiStatus(false);
                }
            }
        });
        holder.tvPrompt.setText(String.format(context.getString(R.string.are_you_sure_uncopy_follow_master),responseBean.getName()));
    }

    private void requestUnCopyData(final String masterid, int position) {
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.MASTER_ID,masterid);
        map.put(RequestConstant.FOLLOW_ID, ACache.get(context).getAsString(RequestConstant.ACCOUNT));
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_NOFOLLOW, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BeanBaseResponse beanBaseResponse=new Gson().fromJson(response.body().string(),new TypeToken<BeanBaseResponse>(){}.getType());
                if(beanBaseResponse.getStatus()==1&&beanBaseResponse.getResponse().contains("cancel success")){
                    for(BeanMasterRank.MasterRank masterRank:rank.getResponse()){
                        if(masterRank.getLogin().equals(masterid)){
                            masterRank.setStatus(0);
                        }
                    }
                    EventBus.getDefault().post(new NotificationMasterStatus(masterid,1,0));
                }
            }
        });
    }

    private void bClickUp(CopyAdapter.MyViewHolder holder, int position) {
        holder.mRlContent.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_dwon2));
        holder.mllCover.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_down));
        holder.mRlContent.setVisibility(View.VISIBLE);
        holder.mllCover.setVisibility(View.GONE);
    }

    private void bClickDown(CopyAdapter.MyViewHolder holder, int position) {
        holder.mRlContent.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up2));
        holder.mllCover.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up));
        holder.mRlContent.setVisibility(View.GONE);
        holder.mllCover.setVisibility(View.VISIBLE);
    }
    
    
    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView copyCount;
        Button actionButton;
        CircleImageView imageView;
        Button watchButton;
        Button bYes;
        Button bNo;
        LinearLayout mllCover;
        RelativeLayout mRlContent;
        TextView tvPrompt;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.username);
            copyCount=(TextView)itemView.findViewById(R.id.copiers);
            imageView=(CircleImageView)itemView.findViewById(R.id.avatar);
            actionButton=(Button)itemView.findViewById(R.id.action_button);
            watchButton=(Button)itemView.findViewById(R.id.watch_button);
            bYes=(Button)itemView.findViewById(R.id.b_yes);
            bNo=(Button)itemView.findViewById(R.id.b_no);
            mRlContent=(RelativeLayout)itemView.findViewById(R.id.rl_content);
            mllCover=(LinearLayout)itemView.findViewById(R.id.ll_cover);
            tvPrompt=(TextView)itemView.findViewById(R.id.tv_action_prompt);
        }
    }
}
