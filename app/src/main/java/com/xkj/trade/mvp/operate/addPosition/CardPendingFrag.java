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
import com.xkj.trade.bean_.BeanPendingPosition;
import com.xkj.trade.bean_notification.NotificationKeyBoard;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.TradeDateConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.ResourceReader;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.ToashUtil;
import com.xkj.trade.utils.view.AddSubEditText;
import com.xkj.trade.utils.view.CustomASETGroup;
import com.xkj.trade.utils.view.CustomSeekBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class CardPendingFrag extends BaseFragment implements View.OnClickListener,AddSubEditText.AmountChangeListener{
    LinearLayout layoutTab;
    Button bBuyLimit;
    Button bBuyStopLost;
    Button bSellLimit;
    Button bSellStopLost;
    @Bind(R.id.csb_vo)
    CustomSeekBar mCsbVo;
    @Bind(R.id.price)
    CustomASETGroup mCPrice;
    @Bind(R.id.stop_loss)
    CustomASETGroup mCStopLost;
    @Bind(R.id.take_profit)
    CustomASETGroup mCTakeProfit;
    @Bind(R.id.tv_enter_order)
    TextView mTvEnterOrder;
    private RequestConstant.Exc exc;
    private String price;
    BeanBaseResponse beanBaseResponse;
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
        initC();
    }

    private void initC() {
        int mDigits = MoneyUtil.getDigits(price);
        String mBaseNumble = MoneyUtil.getBaseNumble(mDigits);
        mCPrice.setMoneyChangeListener(this);
        mCPrice.setData("0", String.valueOf(Integer.MAX_VALUE), mBaseNumble, price);
        mCStopLost.setMoneyChangeListener(this);
        mCTakeProfit.setMoneyChangeListener(this);
        mCStopLost.setData("0", String.valueOf(Integer.MAX_VALUE), mBaseNumble, MoneyUtil.subPriceToString(price, String.valueOf(mBaseNumble)));
        mCTakeProfit.setData("0", String.valueOf(Integer.MAX_VALUE), mBaseNumble, MoneyUtil.addPrice(price, String.valueOf(mBaseNumble)));
        mCPrice.setVisible();
    }

    private void promptChange() {
                switch (exc) {
                    case SELL_LIMIT:
                    case SELL_STOP:
                        if (Double.valueOf(mCStopLost.getMoneyString()) < Double.valueOf(mCPrice.getMoneyString())) {
                            mCStopLost.setmTVDescPrompt(String.format(getResources().getString(R.string.rate_or_higher), mCPrice.getMoneyString()));
                        } else {
                            mCStopLost.setmTVDescPrompt("$" + MoneyUtil.deleteZero(MoneyUtil.mulPrice(MoneyUtil.mulPrice(mCsbVo.getMoney(), TradeDateConstant.VOLUME_MONEY_STRING), (MoneyUtil.subPriceToString(mCPrice.getMoneyString(), mCStopLost.getMoneyString())))));
                        }
                        if (Double.valueOf(mCTakeProfit.getMoneyString()) > Double.valueOf(mCPrice.getMoneyString())) {
                            mCTakeProfit.setmTVDescPrompt(String.format(getResources().getString(R.string.rate_or_lower), mCPrice.getMoneyString()));
                        } else {
                            mCTakeProfit.setmTVDescPrompt("$" + MoneyUtil.deleteZero(MoneyUtil.mulPrice(MoneyUtil.mulPrice(mCsbVo.getMoney(), TradeDateConstant.VOLUME_MONEY_STRING), (MoneyUtil.subPriceToString(mCPrice.getMoneyString(), mCTakeProfit.getMoneyString())))));
                        }
                        break;
                    case BUY_LIMIT:
                    case BUY_STOP:
                        if (Double.valueOf(mCStopLost.getMoneyString()) > Double.valueOf(mCPrice.getMoneyString())) {
                            mCStopLost.setmTVDescPrompt(String.format(getResources().getString(R.string.rate_or_lower), mCPrice.getMoneyString()));
                        } else {
                            mCStopLost.setmTVDescPrompt("$" + MoneyUtil.deleteZero(MoneyUtil.mulPrice(MoneyUtil.mulPrice(mCsbVo.getMoney(), TradeDateConstant.VOLUME_MONEY_STRING), (MoneyUtil.subPriceToString(mCStopLost.getMoneyString(), mCPrice.getMoneyString())))));
                        }
                        if (Double.valueOf(mCTakeProfit.getMoneyString()) < Double.valueOf(mCPrice.getMoneyString())) {
                            mCTakeProfit.setmTVDescPrompt(String.format(getResources().getString(R.string.rate_or_higher), mCPrice.getMoneyString()));
                        } else {
                            mCTakeProfit.setmTVDescPrompt("$" + MoneyUtil.deleteZero(MoneyUtil.mulPrice(MoneyUtil.mulPrice(mCsbVo.getMoney(), TradeDateConstant.VOLUME_MONEY_STRING), (MoneyUtil.subPriceToString(mCTakeProfit.getMoneyString(), mCPrice.getMoneyString())))));
                        }
                        break;
                }
                switch (exc) {
                    case BUY_LIMIT:
                        if (Double.valueOf(mCPrice.getMoneyString()) >Double.valueOf(MyApplication.getInstance().beanIndicatorData.getAsk())) {
                            mCPrice.setmTVDescPrompt(String.format(getResources().getString(R.string.rate_or_lower),MyApplication.getInstance().beanIndicatorData.getAsk()));
                        }else{
                            mCPrice.setmTVDescPrompt("");
                        }
                        break;
                    case SELL_LIMIT:
                        if (Double.valueOf(mCPrice.getMoneyString()) <Double.valueOf(MyApplication.getInstance().beanIndicatorData.getBid())) {
                            mCPrice.setmTVDescPrompt(String.format(getResources().getString(R.string.rate_or_higher),MyApplication.getInstance().beanIndicatorData.getBid()));
                        }else{
                            mCPrice.setmTVDescPrompt("");
                        }
                        break;
                    case BUY_STOP:
                        if (Double.valueOf(mCPrice.getMoneyString()) <Double.valueOf(MyApplication.getInstance().beanIndicatorData.getAsk())) {
                            mCPrice.setmTVDescPrompt(String.format(getResources().getString(R.string.rate_or_higher),MyApplication.getInstance().beanIndicatorData.getAsk()));
                        }else{
                            mCPrice.setmTVDescPrompt("");
                        }
                        break;
                    case SELL_STOP:
                        if (Double.valueOf(mCPrice.getMoneyString()) >Double.valueOf(MyApplication.getInstance().beanIndicatorData.getBid())) {
                            mCPrice.setmTVDescPrompt(String.format(getResources().getString(R.string.rate_or_lower), MyApplication.getInstance().beanIndicatorData.getBid()));
                        }else{
                            mCPrice.setmTVDescPrompt("");
                        }
                        break;
                }
        }


    @Override
    protected void initData() {
        exc = RequestConstant.Exc.SELL_LIMIT;
        price = MyApplication.getInstance().beanIndicatorData.getBid();
        title=getString(R.string.open_position);
    }

    private void setTabSelected(Button buttonSelect) {
        Drawable selectedDrawable;

        if (buttonSelect.getId() == R.id.b_sell_limit || buttonSelect.getId() == R.id.b_sell_stop_lost) {
            selectedDrawable = ResourceReader.readDrawable(context, R.drawable.shape_nav_indicator);
            price = MyApplication.getInstance().beanIndicatorData.getAsk();
        } else {
            selectedDrawable = ResourceReader.readDrawable(context, R.drawable.shape_nav_indicator_green);
            price = MyApplication.getInstance().beanIndicatorData.getBid();
        }
        int right = buttonSelect.getWidth();
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
                exc = RequestConstant.Exc.SELL_STOP;
                setTabSelected(bSellStopLost);
                promptChange();
                break;
            case R.id.b_sell_limit:
                exc = RequestConstant.Exc.SELL_LIMIT;
                setTabSelected(bSellLimit);
                promptChange();
                break;
            case R.id.b_buy_stop_lost:
                exc = RequestConstant.Exc.BUY_STOP;
                setTabSelected(bBuyStopLost);
                promptChange();
                break;
            case R.id.b_buy_limit:
                exc = RequestConstant.Exc.BUY_LIMIT;
                setTabSelected(bBuyLimit);
                promptChange();
                break;
            case R.id.tv_enter_order:
                showLoading(context);
                enterOrder();
                break;
        }
    }
    //下单

    private void enterOrder() {
        //②	action=pending	exc , login, symbol, volume, price, sl, tp 为必传参数
        Map<String, String> map = new TreeMap<>();
        if(mCsbVo.getVisible()!=View.VISIBLE&&mCsbVo.getMoney().equals("0")){
            ToashUtil.show(context,"手数不能用空", Toast.LENGTH_SHORT);
            hideLoading();
        }
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        map.put(RequestConstant.SYMBOL, AesEncryptionUtil.stringBase64toString(MyApplication.getInstance().beanIndicatorData.getSymbol()));
        map.put(RequestConstant.ACTION, RequestConstant.Action.PENDING.toString());
            map.put(RequestConstant.VOLUME,mCsbVo.getMoney());
        map.put(RequestConstant.EXC, exc.toString());
        map.put(RequestConstant.PRICE, mCPrice.getMoneyString());
        if(mCTakeProfit.getDataVisitity()==View.VISIBLE)
        map.put(RequestConstant.SL, mCStopLost.getMoneyString());
        if(mCStopLost.getDataVisitity()==View.VISIBLE)
        map.put(RequestConstant.TP, mCTakeProfit.getMoneyString());
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_ORDER_EXE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + call.request());
                showFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = AesEncryptionUtil.decodeUnicode(response.body().string());
                 beanBaseResponse=new Gson().fromJson(s,BeanBaseResponse.class);
                Log.i(TAG, "onResponse: 开挂单"+s);
                if (beanBaseResponse.getStatus() == 1) {
                    EventBus.getDefault().post(new BeanPendingPosition());
                    showSucc();
                    //发送通知activity关闭
//                    EventBus.getDefault().post(beanBaseResponse);
                }else{
                    showFail("操作未成功：\n"+beanBaseResponse.getMsg());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getSoftKeyHeight(NotificationKeyBoard notificationKeyBoard){
//        if(notificationKeyBoard.getHight()==0){
//            view.findViewById(R.id.s_space).setVisibility(View.GONE);
//        }else{
//            view.findViewById(R.id.s_space).setLayoutParams(new LinearLayout.LayoutParams(view.getWidth(),notificationKeyBoard.getHight()));
//            view.findViewById(R.id.s_space).setVisibility(View.VISIBLE);
//        }
////        view.findViewById(R.id.sv_).setFocusable(true);
    }

    @Override
    public void amountChange(String amount) {
        promptChange();
    }
}
