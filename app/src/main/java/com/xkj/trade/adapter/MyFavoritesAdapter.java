package com.xkj.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.bean_.BeanAllSymbols;
import com.xkj.trade.utils.DataUtil;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangsc on 2017-02-04.
 * TODO:
 */

public class MyFavoritesAdapter extends RecyclerView.Adapter<MyFavoritesAdapter.MyFavoritesHolder> {
    private Context context;
    private List<BeanAllSymbols.SymbolPrices> data;
    private OnItemClickListener listener;
    private String TAG = SystemUtil.getTAG(this);
    private List<String> statusSymbols =new ArrayList<>();
    public MyFavoritesAdapter(Context context, List<BeanAllSymbols.SymbolPrices> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(List<BeanAllSymbols.SymbolPrices> data) {
        this.data = data;
    }

    @Override
    public MyFavoritesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_my_favorite_content, parent, false);
        return new MyFavoritesHolder(view);
    }

    @Override
    public void onBindViewHolder(MyFavoritesHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if(payloads==null){
            onBindViewHolder(holder,position);
        }else{
            BeanAllSymbols.SymbolPrices symbolPrices = data.get(position);
                    SpannableString askTextBig = MoneyUtil.getRealTimePriceTextBig(context, symbolPrices.getBid());
                    SpannableString bidTextBig = MoneyUtil.getRealTimePriceTextBig(context, symbolPrices.getAsk());
                    if (symbolPrices.getBidColor() != 0) {
                        bidTextBig.setSpan(new ForegroundColorSpan(symbolPrices.getBidColor()), 0, bidTextBig.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    if (symbolPrices.getAskColor() != 0) {
                        askTextBig.setSpan(new ForegroundColorSpan(symbolPrices.getAskColor()), 0, askTextBig.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    holder.ask.setText(askTextBig);
                    holder.bid.setText(bidTextBig);
        }
    }

    @Override
    public void onBindViewHolder(final MyFavoritesHolder holder, final int position) {
        final BeanAllSymbols.SymbolPrices symbolPrices = data.get(position);
        holder.flag.setImageResource(DataUtil.getImageId(symbolPrices.getSymbol()));
        SpannableString askTextBig = MoneyUtil.getRealTimePriceTextBig(context, symbolPrices.getBid());
        SpannableString bidTextBig = MoneyUtil.getRealTimePriceTextBig(context, symbolPrices.getAsk());
        if (symbolPrices.getBidColor() != 0) {
            bidTextBig.setSpan(new ForegroundColorSpan(symbolPrices.getBidColor()), 0, bidTextBig.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (symbolPrices.getAskColor() != 0) {
            askTextBig.setSpan(new ForegroundColorSpan(symbolPrices.getAskColor()), 0, askTextBig.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        holder.bid.setText(bidTextBig);
        holder.ask.setText(askTextBig);
        holder.instrumentName.setText(symbolPrices.getSymbol());
        if (symbolPrices.getSign()) {
            if(!statusSymbols.contains(data.get(position).getSymbol())){
                holder.goTo.setVisibility(View.INVISIBLE);
            }else{
                holder.goTo.setVisibility(View.VISIBLE);
            }
            holder.itemOverLay.setVisibility(View.INVISIBLE);
            holder.favoritesIcon.setImageResource(R.drawable.collected);
        } else {
            holder.goTo.setVisibility(View.INVISIBLE);
            holder.itemOverLay.setVisibility(View.VISIBLE);
            holder.favoritesIcon.setImageResource(R.drawable.discollect);
        }
        holder.goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClickGoto(symbolPrices.getSymbol());
            }
        });
        holder.favoritesItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (symbolPrices.getSign()) {
                    //星星亮了
                    if (holder.goTo.getVisibility() != View.VISIBLE) {
                        //还没翻转，就翻转
                        statusSymbols.add(data.get(position).getSymbol());
                        holder.goTo.setVisibility(View.VISIBLE);
                        holder.goTo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up));
                        holder.bidAskContainer.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up2));
//                        mBaseMasterInfo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up2));
//                        mCoverView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up));
                        Log.i(TAG, "onClick:1 ");
                    } else {
                        statusSymbols.remove(data.get(position).getSymbol());
                        //翻转了，就取消订阅
                        holder.goTo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_down));
                        holder.bidAskContainer.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_dwon2));
                        holder.goTo.setVisibility(View.INVISIBLE);
                        holder.favoritesIcon.setImageResource(R.drawable.discollect);
                        holder.itemOverLay.setVisibility(View.VISIBLE);
                        Log.i(TAG, "onClick:2 ");
                        symbolPrices.setSign(false);
                        listener.onItemClick(symbolPrices,position);
                    }
                } else {
                    statusSymbols.add(data.get(position).getSymbol());
                    Log.i(TAG, "onClick:3 ");
                    //星星没亮，就变亮
                    holder.itemOverLay.setVisibility(View.GONE);
                    holder.favoritesIcon.setImageResource(R.drawable.collected);
                    symbolPrices.setSign(true);
                    holder.goTo.setVisibility(View.VISIBLE);
                    holder.goTo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up));
                    holder.bidAskContainer.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up2));
                    if (listener != null)
                        listener.onItemClick(symbolPrices,position);
                }

            }
        });

    }

    private int select(String symbol) {
        int imageResource = R.drawable.ic_country_flag_us;
        switch (symbol) {
            default:
        }
        return imageResource;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyFavoritesHolder extends RecyclerView.ViewHolder {
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
            favoritesItem = (RelativeLayout) itemView.findViewById(R.id.favorites_item);
            flag = (ImageView) itemView.findViewById(R.id.flag);
            instrumentName = (TextView) itemView.findViewById(R.id.instrument_name);
            bid = (TextView) itemView.findViewById(R.id.bid);
            ask = (TextView) itemView.findViewById(R.id.ask);
            goTo = (TextView) itemView.findViewById(R.id.got_it);
            favoritesIcon = (ImageView) itemView.findViewById(R.id.favorites_icon);
            itemOverLay = itemView.findViewById(R.id.item_overlay);
            bidAskContainer = (LinearLayout) itemView.findViewById(R.id.bid_ask_container);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BeanAllSymbols.SymbolPrices symbolPrices,int position);
        void onItemClickGoto(String symbol);
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
