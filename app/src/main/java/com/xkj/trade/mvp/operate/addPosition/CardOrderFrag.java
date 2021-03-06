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

import com.google.gson.Gson;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean_.BeanBaseResponse;
import com.xkj.trade.bean_.BeanOpenPosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.ResourceReader;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.view.CustomSeekBar;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by huangsc on 2016-12-10.
 * TODO:
 */

public class CardOrderFrag extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.ll_play_action)
    LinearLayout mLlPlayAction;
    private String mASk;
    private String mBid;
    LinearLayout layoutTab;
    Button bBuy;
    Button bSell;
    @Bind(R.id.ll_button_group)
    LinearLayout mLlButtonGroup;
    @Bind(R.id.csb_vp_item_order_frag)
    CustomSeekBar mCsbVpItemOrderFrag;
    @Bind(R.id.tv_action)
    TextView mTvAction;
    @Bind(R.id.tv_buy_ask)
    TextView mTvBuyAsk;
    BeanBaseResponse beanBaseResponse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.vp_item_order_frag, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        layoutTab = (LinearLayout) view.findViewById(R.id.ll_button_group);
        bBuy = (Button) view.findViewById(R.id.b_buy);
        bSell = (Button) view.findViewById(R.id.b_sell);
        bSell.setOnClickListener(this);
        bBuy.setOnClickListener(this);
        bSell.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setTabSelected(bSell);
                bSell.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mLlPlayAction.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        title=getString(R.string.open_position);
    }

    private void setTabSelected(Button buttonSelect) {
        Drawable selectedDrawable;
        if(buttonSelect.getId()==R.id.b_sell){
            selectedDrawable = ResourceReader.readDrawable(context, R.drawable.shape_nav_indicator);
        }else {
             selectedDrawable = ResourceReader.readDrawable(context, R.drawable.shape_nav_indicator_green);
        }
//        int screenWidth = DensityUtils.getScreenSize(MainActivity.this)[0];
        int right = buttonSelect.getWidth();
        Log.i(TAG, "setTabSelected: right" + right);
        selectedDrawable.setBounds(0, 0, right, SystemUtil.dp2px(context, 10));
        buttonSelect.setSelected(true);
        buttonSelect.setCompoundDrawables(null, null, null, selectedDrawable);
        int size = layoutTab.getChildCount();
        for (int i = 0; i < size; i++) {
            if (buttonSelect.getId() != layoutTab.getChildAt(i).getId() && layoutTab.getChildAt(i).getId() != R.id.v_line) {
                layoutTab.getChildAt(i).setSelected(false);
                ((Button) layoutTab.getChildAt(i)).setCompoundDrawables(null, null, null, null);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_sell:
                setTabSelected(bSell);
                mTvAction.setText(bSell.getText());
                if (mBid != null) {
                    mTvBuyAsk.setText(mBid);
                }
                break;
            case R.id.b_buy:
                setTabSelected(bBuy);
                mTvAction.setText(bBuy.getText());
                if (mASk != null) {
                    mTvBuyAsk.setText(mASk);
                }
                break;
            case R.id.ll_play_action:
                showLoading(context);
                requestActionOrder();
                break;
        }
    }


    private void requestActionOrder() {
        if(Double.valueOf(mCsbVpItemOrderFrag.getMoney())<=0){
            Toast.makeText(context,"手数不能为空",Toast.LENGTH_SHORT).show();
            hideLoading();
            return;
        }
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        map.put(RequestConstant.SYMBOL,AesEncryptionUtil.stringBase64toString(MyApplication.getInstance().beanIndicatorData.getSymbol()));
        map.put(RequestConstant.ACTION,"maket");
        if (mTvAction.getText().equals(bBuy.getText())) {
            map.put(RequestConstant.EXC,"BUY");
        } else {
            map.put(RequestConstant.EXC,"SELL");
        }
        map.put(RequestConstant.VOLUME,mCsbVpItemOrderFrag.getMoney());
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_ORDER_EXE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: "+call.request());
                showFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                beanBaseResponse=new Gson().fromJson(response.body().string(),BeanBaseResponse.class);
                Log.i(TAG, "onResponse:开仓 "+beanBaseResponse.toString());
                if(beanBaseResponse.getStatus()==1){
                    EventBus.getDefault().post(new BeanOpenPosition());
                    //发送通知activity关闭
//                    EventBus.getDefault().post(beanBaseResponse);
                    showSucc();
                }else{
                    showFail(String.format(getString(R.string.action_fail),beanBaseResponse.getTips()!=null?beanBaseResponse.getTips():beanBaseResponse.getMsg()));
                }
            }
        });
    }

    @Override
    protected void eventSucc() {
        super.eventSucc();
        //发送通知activity关闭
        EventBus.getDefault().post(beanBaseResponse);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getCurrentSymbol(BeanIndicatorData beanIndicatorData) {
//        Log.i(TAG, "getCurrentSymbol: " + beanIndicatorData.getSymbol());
//        mASk = beanIndicatorData.getAsk();
//        mBid = beanIndicatorData.getBid();
//        if (mTvAction.getText().equals(bBuy.getText())) {
//            mTvBuyAsk.setText(beanIndicatorData.getAsk());
//        } else {
//            mTvBuyAsk.setText(beanIndicatorData.getBid());
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
