package com.xkj.trade.mvp.operate.addPosition;

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
import android.widget.TextView;
import android.widget.Toast;

import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.ResourceReader;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.view.CustomASETGroup;
import com.xkj.trade.utils.view.CustomSeekBar;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.xkj.trade.constant.TradeDateConstant.VOLUME_MONEY;

/**
 * Created by huangsc on 2016-12-10.
 * TODO:
 */

public class CardPendingFrag extends BaseFragment implements View.OnClickListener {
    LinearLayout layoutTab;
    Button bBuyLimit;
    Button bBuyStopLost;
    Button bSellLimit;
    Button bSellStopLost;
    @Bind(R.id.csb_vo)
    CustomSeekBar mCsbVo;
    @Bind(R.id.price)
    CustomASETGroup mPrice;
    @Bind(R.id.stop_loss)
    CustomASETGroup mStopLoss;
    @Bind(R.id.take_profit)
    CustomASETGroup mTakeProfit;
    @Bind(R.id.tv_enter_order)
    TextView mTvEnterOrder;
    private String exc;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.vp_item_pending_frag, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        layoutTab = (LinearLayout) view.findViewById(R.id.ll_button_group);
        bBuyLimit = (Button) view.findViewById(R.id.b_buy_limit);
        bBuyStopLost = (Button) view.findViewById(R.id.b_buy_stop_lost);
        bSellStopLost = (Button) view.findViewById(R.id.b_sell_stop_lost);
        bSellLimit = (Button) view.findViewById(R.id.b_sell_limit);
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
        mTvEnterOrder.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        exc= RequestConstant.Exc.SELL_LIMIT.toString();
    }
    private void setTabSelected(Button buttonSelect) {
        Drawable selectedDrawable;

        if (buttonSelect.getId() == R.id.b_sell_limit || buttonSelect.getId() == R.id.b_sell_stop_lost) {
            selectedDrawable = ResourceReader.readDrawable(context, R.drawable.shape_nav_indicator);
        } else {
            selectedDrawable = ResourceReader.readDrawable(context, R.drawable.shape_nav_indicator_green);
        }
        int right = buttonSelect.getWidth();
        Log.i(TAG, "setTabSelected: right" + right);
        selectedDrawable.setBounds(0, 0, right, SystemUtil.dp2px(context, 5));
        buttonSelect.setSelected(true);
        buttonSelect.setCompoundDrawables(null, null, null, selectedDrawable);
        int size = layoutTab.getChildCount();
        for (int i = 0; i < size; i++) {
            if (buttonSelect.getId() != layoutTab.getChildAt(i).getId()) {
                if (layoutTab.getChildAt(i) instanceof Button) {
                    layoutTab.getChildAt(i).setSelected(false);
                    ((Button) layoutTab.getChildAt(i)).setCompoundDrawables(null, null, null, null);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_sell_stop_lost:
                exc= RequestConstant.Exc.SELL_STOP.toString();
                setTabSelected(bSellStopLost);
                break;
            case R.id.b_sell_limit:
                exc=RequestConstant.Exc.SELL_LIMIT.toString();
                setTabSelected(bSellLimit);
                break;
            case R.id.b_buy_stop_lost:
                exc=RequestConstant.Exc.BUY_STOP.toString();
                setTabSelected(bBuyStopLost);
                break;
            case R.id.b_buy_limit:
                exc=RequestConstant.Exc.BUY_LIMIT.toString();
                setTabSelected(bBuyLimit);
                break;
            case R.id.tv_enter_order:
                enterOrder();
                break;
        }
    }
    //下单

    private void enterOrder() {
        if(mCsbVo.getMoney()==0){
            Toast.makeText(context,"必要数据不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mPrice.getMoney()==0){
            Toast.makeText(context,"必要数据不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mStopLoss.getMoney()==0){
            Toast.makeText(context,"必要数据不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mTakeProfit.getMoney()==0){
            Toast.makeText(context,"必要数据不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        //②	action=pending	exc , login, symbol, volume, price, sl, tp 为必传参数
        Map<String ,String> map=new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        map.put(RequestConstant.SYMBOL, AesEncryptionUtil.stringBase64toString(MyApplication.getInstance().beanIndicatorData.getSymbol()));
        map.put(RequestConstant.ACTION,RequestConstant.Action.PENDING.toString());
        try {
            map.put(RequestConstant.VOLUME, String.valueOf(MoneyUtil.div((double) (mCsbVo.getMoney()),VOLUME_MONEY,3)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        map.put(RequestConstant.EXC,exc);
        map.put(RequestConstant.PRICE,String.valueOf(mPrice.getMoney()));
        map.put(RequestConstant.SL,String.valueOf(mStopLoss.getMoney()));
        map.put(RequestConstant.TP,String.valueOf(mTakeProfit.getMoney()));
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_ORDER_EXE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: "+call.request());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: "+call.request());
                Log.i(TAG, "onResponse: "+response.body().string());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
