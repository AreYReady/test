package trade.xkj.com.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.bean.BeanIndicatorData;


/**
 * Created by huangsc on 2016/11/28.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> implements View.OnClickListener {
    private LayoutInflater mInflater;
    protected List<BeanIndicatorData> mDatas;
    private Context mContext;
    private String TAG= SystemUtil.getTAG(this);
    private OnRecyclerItemClickListener mOnRecyclerItemClickListener;
    public static interface OnRecyclerItemClickListener {
        void onClick(View v, String s);
    }

    public GalleryAdapter(Context context, List<BeanIndicatorData> datats)
    {
        mContext=context;
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    @Override
    public void onClick(View v) {
        mOnRecyclerItemClickListener.onClick(v,(String)v.getTag());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        ImageView mImg;
        TextView mTxt;
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.mOnRecyclerItemClickListener = listener;
    }
    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.test_item,
                viewGroup, false);
        view.setOnClickListener(this);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.id_index_gallery_item_image);
        viewHolder.mTxt=(TextView)view.findViewById(R.id.id_index_gallery_item_text_left);

//        AlphaAnimation alam=new AlphaAnimation(1, 0);
//        //设置动画
//        alam.setDuration(2000);
//        viewHolder.mImg.setAnimation(alam);
//        viewHolder.mImg.setAnimation(alam);
//        //启动动画
//        alam.start();
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        viewHolder.mImg.setImageResource(mDatas.get(i).getImageResource());
        viewHolder.itemView.setTag(3+i+"");
    }


}
