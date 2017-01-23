package trade.xkj.com.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import trade.xkj.com.trade.R;
import trade.xkj.com.trade.utils.SystemUtil;
import trade.xkj.com.trade.bean.BeanAttentionTraderData;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder>{
    private Context context;
    private List<BeanAttentionTraderData> mDataList;
    private String TAG= SystemUtil.getTAG(this);
    public MyRecycleAdapter(Context context, List<BeanAttentionTraderData> mDataList){
        this.context=context;
        this.mDataList=mDataList;
    }
    @Override
    public MyRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder=new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_social_card_master_brief,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRecycleAdapter.MyViewHolder holder, int position) {
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
