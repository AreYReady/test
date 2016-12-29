package trade.xkj.com.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.ToashUtil;
import trade.xkj.com.trade.bean.BeanMasterInfo;

/**
 * Created by huangsc on 2016-12-21.
 * TODO:
 */

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.MyHolder> {
    private Context context;
    private int position;

    //    private OnItemClickListener onItemClickListener;
    public MasterAdapter(Context context) {
        this.context = context;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_master, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        this.position = position;
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToashUtil.showShort(context, position + "");
                EventBus.getDefault().post(new BeanMasterInfo());
            }
        });
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
        return 20;
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
        }
        ImageView mImageView;
        LinearLayout mLlButtons;
        LinearLayout mBaseMasterInfo;
        LinearLayout mCover;
        Button bCopyButton;
        Button bCompleteButton;
    }
}