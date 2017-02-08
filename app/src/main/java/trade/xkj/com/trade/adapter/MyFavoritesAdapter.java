package trade.xkj.com.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.bean_.BeanAllSymbols;
import trade.xkj.com.trade.utils.MoneyUtil;
import trade.xkj.com.trade.utils.SystemUtil;

/**
 * Created by huangsc on 2017-02-04.
 * TODO:
 */

public class MyFavoritesAdapter extends RecyclerView.Adapter<MyFavoritesAdapter.MyFavoritesHolder> {
    private Context context;
    private List<BeanAllSymbols.SymbolPrices> data;
    private OnItemClickListener listener;
    private String TAG= SystemUtil.getTAG(this);

    public MyFavoritesAdapter(Context context, List<BeanAllSymbols.SymbolPrices> data) {
        this.context = context;
        this.data = data;
    }
    public void setData( List<BeanAllSymbols.SymbolPrices> data ){
        this.data=data;
    }

    @Override
    public MyFavoritesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_my_favorite_content, parent, false);
        return new MyFavoritesHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyFavoritesHolder holder, int position) {
        final BeanAllSymbols.SymbolPrices symbolPrices = data.get(position);
        holder.flag.setImageResource( select(symbolPrices.getSymbol()));
        holder.bid.setText(MoneyUtil.getRealTimePriceTextBig(context,symbolPrices.getBid()));
        holder.ask.setText(MoneyUtil.getRealTimePriceTextBig(context,symbolPrices.getAsk()));
        holder.instrumentName.setText(symbolPrices.getSymbol());
        if(symbolPrices.getSign()){
            holder.itemOverLay.setVisibility(View.GONE);
            holder.favoritesIcon.setImageResource(R.drawable.collected);
        }else{
            holder.itemOverLay.setVisibility(View.VISIBLE);
            holder.favoritesIcon.setImageResource(R.drawable.discollect);
        }
        holder.goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onItemClickGoto(symbolPrices.getSymbol());
            }
        });
        holder.favoritesItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symbolPrices.getSign()){
                    //星星亮了
                    if(holder.goTo.getVisibility()!=View.VISIBLE){
                        //还没翻转，就翻转
                        holder.goTo.setVisibility(View.VISIBLE);
                        holder.goTo.startAnimation(AnimationUtils.loadAnimation(context,R.anim.transtale_y_up));
                        holder.bidAskContainer.startAnimation(AnimationUtils.loadAnimation(context,R.anim.transtale_y_up2));
//                        mBaseMasterInfo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up2));
//                        mCoverView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up));
                        Log.i(TAG, "onClick:1 ");
                    }else{
                        //翻转了，就取消订阅
                        holder.goTo.startAnimation(AnimationUtils.loadAnimation(context,R.anim.transtale_y_down));
                        holder.bidAskContainer.startAnimation(AnimationUtils.loadAnimation(context,R.anim.transtale_y_dwon2));
                        holder.goTo.setVisibility(View.INVISIBLE);
                        holder.favoritesIcon.setImageResource(R.drawable.discollect);
                        holder.itemOverLay.setVisibility(View.VISIBLE);
                        Log.i(TAG, "onClick:2 ");
                        symbolPrices.setSign(false);
                        listener.onItemClick(symbolPrices);
                    }
                }else{
                    Log.i(TAG, "onClick:3 ");
                    //星星没亮，就变亮
                    holder.itemOverLay.setVisibility(View.GONE);
                    holder.favoritesIcon.setImageResource(R.drawable.collected);
                    symbolPrices.setSign(true);
                    holder.goTo.setVisibility(View.VISIBLE);
                    holder.goTo.startAnimation(AnimationUtils.loadAnimation(context,R.anim.transtale_y_up));
                    holder.bidAskContainer.startAnimation(AnimationUtils.loadAnimation(context,R.anim.transtale_y_up2));
                    if(listener!=null)
                    listener.onItemClick(symbolPrices);
                }

            }
        });

    }

    private int select(String symbol) {
        int imageResource=R.drawable.ic_country_flag_us ;
        switch (symbol){
            default:
        }
        return imageResource;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyFavoritesHolder extends RecyclerView.ViewHolder {
        ImageView flag;
        TextView instrumentName;
        TextView bid;
        TextView ask;
        TextView goTo;
        ImageView favoritesIcon;
       View itemOverLay;
        RelativeLayout favoritesItem;
        LinearLayout bidAskContainer;

        public MyFavoritesHolder(View itemView) {
            super(itemView);
            favoritesItem=(RelativeLayout)itemView.findViewById(R.id.favorites_item);
            flag = (ImageView) itemView.findViewById(R.id.flag);
            instrumentName = (TextView) itemView.findViewById(R.id.instrument_name);
            bid = (TextView) itemView.findViewById(R.id.bid);
            ask = (TextView) itemView.findViewById(R.id.ask);
            goTo = (TextView) itemView.findViewById(R.id.got_it);
            favoritesIcon = (ImageView) itemView.findViewById(R.id.favorites_icon);
            itemOverLay = itemView.findViewById(R.id.item_overlay);
            bidAskContainer=(LinearLayout)itemView.findViewById(R.id.bid_ask_container);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(BeanAllSymbols.SymbolPrices symbolPrices);
        void onItemClickGoto(String symbol);
    }
    public void addOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}