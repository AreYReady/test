package trade.xkj.com.trade.mvp.operate;

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

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.utils.ResourceReader;
import trade.xkj.com.trade.utils.SystemUtil;
import trade.xkj.com.trade.base.BaseFragment;

/**
 * Created by huangsc on 2016-12-10.
 * TODO:
 */

public class CardOrderFrag extends BaseFragment implements View.OnClickListener{
    LinearLayout layoutTab;
    Button bBuy;
    Button bSell;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.vp_item_order_frag,null);
        return view;
    }

    @Override
    protected void initView() {
        layoutTab=(LinearLayout)view.findViewById(R.id.ll_button_group);
        bBuy =(Button) view.findViewById(R.id.b_buy);
        bSell =(Button)view.findViewById(R.id.b_sell);
        bSell.setOnClickListener(this);
        bBuy.setOnClickListener(this);
        bSell.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setTabSelected(bSell);
                bSell.getViewTreeObserver().removeOnGlobalLayoutListener(this);
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
            if (buttonSelect.getId() != layoutTab.getChildAt(i).getId()&&layoutTab.getChildAt(i).getId()!=R.id.v_line) {
                layoutTab.getChildAt(i).setSelected(false);
                ((Button) layoutTab.getChildAt(i)).setCompoundDrawables(null, null, null, null);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_sell:
                setTabSelected(bSell);
                break;
            case R.id.b_buy:
                setTabSelected(bBuy);
                break;
        }
    }
}
