package com.xkj.trade.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xkj.trade.R;

import java.util.List;


/**
 * Created by huangsc on 2016-12-23.
 * TODO:
 */

public class MyViewPagerAdapterItem extends PagerAdapter {
    private Context context;
    private List<String > mDataItem;
    public MyViewPagerAdapterItem(Context context,List<String> mDataItem){
        this.context=context;
        this.mDataItem=mDataItem;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String s = mDataItem.get(position);
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_viewpager, null);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_item_name);
        textView.setText(s);
        container.addView(inflate);
        return inflate;
    }

    @Override
    public int getCount() {
        return mDataItem.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}