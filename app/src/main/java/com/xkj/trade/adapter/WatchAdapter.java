package com.xkj.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.xkj.trade.bean_.BeanWatchInfo;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.SystemUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.MyViewHolder>{
    private Context context;
    private List<BeanWatchInfo.ResponseBean> mDataList;
    private String TAG= SystemUtil.getTAG(this);
    private Map<Integer,Boolean> status=new TreeMap<>();
    public WatchAdapter(Context context, List<BeanWatchInfo.ResponseBean> mDataList){
        this.context=context;
        this.mDataList=mDataList;
    }
    public void setData(List<BeanWatchInfo.ResponseBean> mDataList){
        this.mDataList=mDataList;
    }
    @Override
    public WatchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder=new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_social_card_master_brief,parent,false));
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final WatchAdapter.MyViewHolder holder, final int position) {
        holder.tvName.setText(mDataList.get(position).getName());
        holder.copyCount.setText(String.valueOf(mDataList.get(position).getCopynumber()));
        holder.actionButton.setText(context.getResources().getString(R.string.copy));
        holder.watchButton.setText(R.string.unwatch);
        holder.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.watchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消关注
                if(status.get(position)==null){
                    bClickDown(holder,position);
                    status.put(position,true);
                }
            }
        });
        holder.bYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bClickUp(holder,position);
                if(status.get(position)!=null){
                    status.remove(position);
                }
                requestUnwatch(mDataList.get(position).getFocusid(),position);
            }
        });
        holder.bNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bClickUp(holder, position);
                if(status.get(position)!=null){
                    status.remove(position);
                }
            }
        });
        holder.unWatchPrompt.setText(String.format(context.getString(R.string.are_you_sure_you_want_to_unwatch),mDataList.get(position).getName()));
    }
    private void bClickUp(MyViewHolder holder, int position) {
        holder.mRlContent.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_dwon2));
        holder.mllCover.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_down));
        holder.mRlContent.setVisibility(View.VISIBLE);
        holder.mllCover.setVisibility(View.GONE);
    }

    private void bClickDown(MyViewHolder holder, int position) {
        holder.mRlContent.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up2));
        holder.mllCover.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up));
        holder.mRlContent.setVisibility(View.GONE);
        holder.mllCover.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mDataList==null?0:mDataList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView copyCount;
        TextView unWatchPrompt;
        Button actionButton;
        Button watchButton;
        Button bYes;
        Button bNo;
        CircleImageView imageView;
        LinearLayout mllCover;
        RelativeLayout mRlContent;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.username);
            copyCount=(TextView)itemView.findViewById(R.id.copiers);
            unWatchPrompt =(TextView)itemView.findViewById(R.id.tv_unwatch_prompt);
            imageView=(CircleImageView)itemView.findViewById(R.id.avatar);
            actionButton=(Button)itemView.findViewById(R.id.action_button);
            watchButton=(Button)itemView.findViewById(R.id.watch_button);
            bYes=(Button)itemView.findViewById(R.id.b_yes);
            bNo=(Button)itemView.findViewById(R.id.b_no);
            mRlContent=(RelativeLayout)itemView.findViewById(R.id.rl_content);
            mllCover=(LinearLayout)itemView.findViewById(R.id.ll_cover);
        }
    }
    private WatchListener mWatchListener;
    public interface WatchListener {
        void unWatch(int Position);
    }
    public void setWacthListener(WatchListener listener){
        mWatchListener=listener;
    }
    private void  requestUnwatch(String focusid, final int position){
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.FOCUS_ID,focusid);
        map.put(RequestConstant.ACCOUNT_ID, ACache.get(context).getAsString(RequestConstant.ACCOUNT));
        OkhttpUtils.enqueue(UrlConstant.URL_MASTER_FOLLOW_NOFOCUS, map, new MyCallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
               BeanBaseResponse beanBaseResponse= new Gson().fromJson(response.body().string(),new TypeToken<BeanBaseResponse>(){}.getType());
                if(beanBaseResponse.getStatus()==1){
                    if(mWatchListener!=null){
                        mWatchListener.unWatch(position);
                    }
                }
            }
        });
    }

}
