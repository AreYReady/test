package com.xkj.trade.mvp.operate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xkj.trade.IO.okhttp.ChatWebSocket;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean.RealTimeDataList;
import com.xkj.trade.bean_.BeanAllSymbols;
import com.xkj.trade.bean_.BeanBaseResponse;
import com.xkj.trade.bean_.BeanOpenPosition;
import com.xkj.trade.bean_notification.NotificationEditPosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.DataUtil;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.RoundImageView;
import com.xkj.trade.utils.view.AddSubEditText;
import com.xkj.trade.utils.view.CustomASETGroup;

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

import static com.xkj.trade.constant.TradeDateConstant.VOLUME_MONEY;
import static com.xkj.trade.mvp.main_trade.fragment_content.v.MainTradeContentFrag.realTimeMap;

/**
 * Created by huangsc on 2016-12-14.
 * TODO:
 */

public class EditPositionFragment extends BaseFragment implements AddSubEditText.AmountChangeListener{
    @Bind(R.id.riv_trade_symbol)
    RoundImageView mRivTradeSymbol;
    @Bind(R.id.tv_symbol_name)
    TextView mTvSymbolName;
    @Bind(R.id.price_left)
    TextView mPriceLeft;
    @Bind(R.id.price_right)
    TextView mPriceRight;
    @Bind(R.id.tv_play_action)
    TextView mTvPlayAction;
    @Bind(R.id.amount)
    TextView mAmount;
    @Bind(R.id.open_rate)
    TextView mOpenRate;
    @Bind(R.id.open_time1)
    TextView mOpenTime1;
    @Bind(R.id.copy_from)
    TextView mCopyFrom;
    @Bind(R.id.stop_loss)
    TextView mStopLoss;
    @Bind(R.id.take_profit)
    TextView mTakeProfit;
    @Bind(R.id.ll_copy_from)
    LinearLayout mLlCopyFrom;
    @Bind(R.id.tv_enter_button_prompt)
    TextView mTvEnterButtonPrompt;
    @Bind(R.id.tv_enter_button)
    TextView mTvEnterButton;
    @Bind(R.id.c_stop_lost)
    CustomASETGroup mCStopLost;
    @Bind(R.id.c_take_profit)
    CustomASETGroup mCTakeProfit;
    private TextView tvAction;
    private String jsonData;
    private BeanOpenPosition.DataBean.ListBean mData;
    private int mDigits;
    private String mBaseNumble;
     Map<String, String> map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_operate_edit_position, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        tvAction = (TextView) view.findViewById(R.id.tv_enter_button);
        tvAction.setText(this.getString(R.string.edit_position));
        if (mData.getCmd().equals("sell")) {
            mTvPlayAction.setText("卖出");
            mTvPlayAction.setTextColor(context.getResources().getColor(R.color.text_color_price_rise));
        } else {
            mTvPlayAction.setTextColor(context.getResources().getColor(R.color.text_color_price_fall));
            mTvPlayAction.setText("买进");
        }
        mAmount.setText(mData.getVolume());
        mOpenRate.setText(mData.getOpenprice());
        mOpenTime1.setText(mData.getOpentime());
        mTvEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(context);
                enterOrder();
            }
        });
        mCStopLost = (CustomASETGroup) view.findViewById(R.id.c_stop_lost);
        mDigits = MoneyUtil.getDigits(mData.getOpenprice());
        mBaseNumble = MoneyUtil.getBaseNumble(mDigits);
        mCTakeProfit = (CustomASETGroup) view.findViewById(R.id.c_take_profit);
        mCStopLost.setMoneyChangeListener(this);
        mCTakeProfit.setMoneyChangeListener(this);
        if (Double.valueOf(mData.getSl()) == 0) {
            //没有止损有效值
            mCStopLost.setData("0", mData.getOpenprice(), mBaseNumble, MoneyUtil.subPriceToString(mData.getOpenprice(), String.valueOf(mBaseNumble)));
        } else {
            mCStopLost.setData("0", mData.getSl(), mBaseNumble, mData.getSl());
            mCStopLost.setVisible();
        }
        if (Double.valueOf(mData.getTp()) == 0) {
            //没有获利有效值
            mCTakeProfit.setData(mData.getOpenprice(), "10000", mBaseNumble, MoneyUtil.addPrice(mData.getOpenprice(), String.valueOf(mBaseNumble)));
        } else {
            mCTakeProfit.setData(mData.getTp(), "10000", mBaseNumble,mData.getTp());
            mCTakeProfit.setVisible();
        }
        mRivTradeSymbol.setImageResource(DataUtil.getImageId(mData.getSymbol()));
        if(realTimeMap.containsKey(mData.getSymbol())) {
            BeanAllSymbols.SymbolPrices symbolPrices = realTimeMap.get(mData.getSymbol());
            setHeader(symbolPrices.getSymbol(),symbolPrices.getAsk(),symbolPrices.getBid());
        }
        requestSubSymbol();
    }
    @Override
    public void amountChange(String amount) {
        switch (mData.getCmd()){
            case "buy":
                if(Double.valueOf(mCStopLost.getMoneyString())>Double.valueOf(mData.getOpenprice())){
                    mCStopLost.setmTVDescPrompt(String.format(getResources().getString(R.string.rate_or_lower),mData.getOpenprice()));
                }else {
                    mCStopLost.setmTVDescPrompt("$" + MoneyUtil.deleteZero(MoneyUtil.mulPrice(String.valueOf(Double.valueOf(mData.getVolume()) * VOLUME_MONEY), (MoneyUtil.subPriceToString(mCStopLost.getMoneyString(), mData.getOpenprice())))));
                }
                if(Double.valueOf(mCTakeProfit.getMoneyString())<Double.valueOf(mData.getOpenprice())){
                    mCTakeProfit.setmTVDescPrompt(String.format(getResources().getString(R.string.rate_or_higher),mData.getOpenprice()));
                } else if (mCTakeProfit.getMoneyString().equals(mData.getOpenprice())) {
                    mCTakeProfit.setmTVDescPrompt("$0.0");
                }else {
                    mCTakeProfit.setmTVDescPrompt("$" + MoneyUtil.deleteZero(MoneyUtil.mulPrice(MoneyUtil.mulPrice(mData.getVolume(), String.valueOf(VOLUME_MONEY)), (MoneyUtil.subPriceToString(mCTakeProfit.getMoneyString(), mData.getOpenprice())))));
                }
                break;
            case "sell":
                if(Double.valueOf(mCStopLost.getMoneyString())<Double.valueOf(mData.getOpenprice())){
                    mCStopLost.setmTVDescPrompt(String.format(getResources().getString(R.string.rate_or_higher),mData.getOpenprice()));
                }else {
                    mCStopLost.setmTVDescPrompt("$" + MoneyUtil.deleteZero(MoneyUtil.mulPrice(String.valueOf(Double.valueOf(mData.getVolume()) * VOLUME_MONEY), (MoneyUtil.subPriceToString(mData.getOpenprice(),mCStopLost.getMoneyString())))));
                }
                if(Double.valueOf(mCTakeProfit.getMoneyString())>Double.valueOf(mData.getOpenprice())){
                    mCTakeProfit.setmTVDescPrompt(String.format(getResources().getString(R.string.rate_or_lower),mData.getOpenprice()));
                } else if (mCTakeProfit.getMoneyString().equals(mData.getOpenprice())) {
                    mCTakeProfit.setmTVDescPrompt("$0.0");
                }else {
                    mCTakeProfit.setmTVDescPrompt("$" + MoneyUtil.deleteZero(MoneyUtil.mulPrice(MoneyUtil.mulPrice(mData.getVolume(), String.valueOf(VOLUME_MONEY)), (MoneyUtil.subPriceToString( mData.getOpenprice(),mCTakeProfit.getMoneyString())))));
                }
                break;

        }
    }

    private void requestSubSymbol() {
        ChatWebSocket chartWebSocket = ChatWebSocket.getChartWebSocket();
        if (chartWebSocket != null) {
            chartWebSocket.sendMessage("{\"msg_type\":1010,\"symbol\":\"" + mData.getSymbol() + "\"}");
        }
    }
    private void enterOrder() {
        map = new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        map.put(RequestConstant.SYMBOL, AesEncryptionUtil.stringBase64toString(mData.getSymbol()));
        map.put(RequestConstant.ACTION, RequestConstant.Action.EDIT.toString());
        map.put(RequestConstant.ORDERNO, String.valueOf(mData.getOrder()));
        map.put(RequestConstant.PRICE, String.valueOf(Double.valueOf(mData.getVolume()) * VOLUME_MONEY));
        if (mCStopLost.getDataVisitity() == View.VISIBLE) {
            map.put(RequestConstant.SL, mCStopLost.getMoneyString());
        }
        if (mCTakeProfit.getDataVisitity() == View.VISIBLE) {
            map.put(RequestConstant.TP, mCTakeProfit.getMoneyString());
        }
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_ORDER_EXE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + call.request());
                showFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = null;
                Log.i(TAG, "onResponse修改持仓结果: " + (s = response.body().string()));
                beanBaseResponse = new Gson().fromJson(s, BeanBaseResponse.class);
                if (beanBaseResponse.getStatus() == 1) {
                    notificationEditPosition = new NotificationEditPosition();
//                    BeanOpenPosition.DataBean.ListBean listBean = new BeanOpenPosition.DataBean.ListBean();
                    if(map.get(RequestConstant.SL)!=null){
                        notificationEditPosition.setSl(map.get(RequestConstant.SL));
                    }
                    if(map.get(RequestConstant.TP)!=null){
                        notificationEditPosition.setTp(map.get(RequestConstant.TP));
                    }
                    notificationEditPosition.setOrder(mData.getOrder());
                    showSucc();
                }else {
                    showFail("操作未成功：\n"+beanBaseResponse.getMsg());
                }
            }
        });
    }
    NotificationEditPosition notificationEditPosition;
    BeanBaseResponse beanBaseResponse;
    @Override
    protected void eventSucc() {
        super.eventSucc();
        EventBus.getDefault().post(notificationEditPosition);
        //发送通知activity关闭
        EventBus.getDefault().post(beanBaseResponse);
    }


    @Override
    protected void initData() {
        mData = new Gson().fromJson(this.getArguments().getString(OperatePositionActivity.JSON_DATA), BeanOpenPosition.DataBean.ListBean.class);
        title=getString(R.string.edit_order);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRealTimeData(RealTimeDataList realTimeDataList) {
        Log.i(TAG, "getRealTimeData: ");
        for(RealTimeDataList.BeanRealTime beanRealTime:realTimeDataList.getQuotes()){
            if(beanRealTime.getSymbol().equals(mData.getSymbol())){
                setHeader(String.valueOf(beanRealTime.getSymbol()),String.valueOf(beanRealTime.getAsk()),String.valueOf(beanRealTime.getBid()));
            }
        }
    }
    public void setHeader(String symbol,String ask,String bid){
        SpannableString askTextBig = MoneyUtil.getRealTimePriceTextBig(context, ask);
        SpannableString bidTextBig = MoneyUtil.getRealTimePriceTextBig(context, bid);
        if(mPriceRight.getText().toString()!=""){
            askTextBig.setSpan(new ForegroundColorSpan(getResources().getColor(Double.valueOf(ask) > Double.valueOf(mPriceLeft.getText().toString()) ? R.color.text_color_price_rise : R.color.text_color_price_fall)), 0, bidTextBig.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if(mPriceRight.getText().toString()!=""){
            bidTextBig.setSpan(new ForegroundColorSpan(getResources().getColor(Double.valueOf(bid) > Double.valueOf(mPriceRight.getText().toString()) ? R.color.text_color_price_rise : R.color.text_color_price_fall)), 0, bidTextBig.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        mTvSymbolName.setText(symbol);
        mPriceLeft.setText(askTextBig);
        mPriceRight.setText(bidTextBig);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
