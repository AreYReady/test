package trade.xkj.com.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.ToashUtil;
import trade.xkj.com.trade.bean.BeanMasterInfo;

/**
 * Created by huangsc on 2016-12-21.
 * TODO:
 */

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.MyHolder>{
    private Context context;
    private int position;
//    private OnItemClickListener onItemClickListener;
    public MasterAdapter(Context context){
        this.context=context;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder=new MyHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_master,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        this.position=position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToashUtil.showShort(context,position+"");
                EventBus.getDefault().post(new BeanMasterInfo());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
//    public interface OnItemClickListener{
//        void onItemClick();
//    }
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
//        this.onItemClickListener=onItemClickListener;
//    }
}
