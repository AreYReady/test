package com.xkj.trade.mvp.operate;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.utils.ResourceReader;
import com.xkj.trade.utils.SystemUtil;

/**
 * Created by huangsc on 2016-12-10.
 * TODO:
 */

public class CardPendingFrag extends BaseFragment implements View.OnClickListener{
    LinearLayout layoutTab;
    Button bBuyLimit;
    Button bBuyStopLost;
    Button bSellLimit;
    Button bSellStopLost;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.vp_item_pending_frag,null);
        return view;
    }

    @Override
    protected void initView() {
        layoutTab=(LinearLayout)view.findViewById(R.id.ll_button_group);
        bBuyLimit =(Button) view.findViewById(R.id.b_buy_limit);
        bBuyStopLost =(Button) view.findViewById(R.id.b_buy_stop_lost);
        bSellStopLost =(Button)view.findViewById(R.id.b_sell_stop_lost);
        bSellLimit =(Button)view.findViewById(R.id.b_sell_limit);
        bSellStopLost.setOnClickListener(this);
        bSellLimit.setOnClickListener(this);
        bBuyLimit.setOnClickListener(this);
        bBuyStopLost.setOnClickListener(this);
        bSellLimit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setTabSelected(bSellLimit);
                bSellLimit.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void initData() {

    }
    private void setTabSelected(Button buttonSelect) {
        Drawable selectedDrawable = ResourceReader.readDrawable(context, R.drawable.shape_nav_indicator_green);

//        int screenWidth = DensityUtils.getScreenSize(MainActivity.this)[0];

        int right = buttonSelect.getWidth();
        Log.i(TAG, "setTabSelected: right"+right);
        selectedDrawable.setBounds(0, 0, right, SystemUtil.dp2px(context,10));
        buttonSelect.setSelected(true);
        buttonSelect.setCompoundDrawables(null, null, null, selectedDrawable);
        int size = layoutTab.getChildCount();
        for (int i = 0; i < size; i++) {
            if (buttonSelect.getId() != layoutTab.getChildAt(i).getId()) {
                if(layoutTab.getChildAt(i) instanceof Button){
                    layoutTab.getChildAt(i).setSelected(false);
                    ((Button) layoutTab.getChildAt(i)).setCompoundDrawables(null, null, null, null);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_sell_stop_lost:
                setTabSelected(bSellStopLost);
                break;
            case R.id.b_sell_limit:
                setTabSelected(bSellLimit);
                break;
            case R.id.b_buy_stop_lost:
                setTabSelected(bBuyStopLost);
                break;
            case R.id.b_buy_limit:
                setTabSelected(bBuyLimit);
                break;
        }
    }
}
