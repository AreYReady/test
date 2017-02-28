package com.xkj.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.bean.BeanAttentionTraderData;
import com.xkj.trade.utils.SystemUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class CopyAdapter extends RecyclerView.Adapter<CopyAdapter.MyViewHolder>{
    private Context context;
    private List<BeanAttentionTraderData> mDataList;
    private String TAG= SystemUtil.getTAG(this);
    public CopyAdapter(Context context, List<BeanAttentionTraderData> mDataList){
        this.context=context;
        this.mDataList=mDataList;
    }
    @Override
    public CopyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder=new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_social_card_master_brief,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CopyAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(mDataList.get(position).getName());
        holder.copyCount.setText(String.valueOf(mDataList.get(position).getCopyCount()));
        holder.imageView.setImageResource(mDataList.get(position).getImageResouce());
        holder.actionButton.setText(mDataList.get(position).getButtonText());
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
        public MyViewHolder(View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.username);
            copyCount=(TextView)itemView.findViewById(R.id.copiers);
            imageView=(CircleImageView)itemView.findViewById(R.id.avatar);
            actionButton=(Button)itemView.findViewById(R.id.action_button);
        }
    }
}
