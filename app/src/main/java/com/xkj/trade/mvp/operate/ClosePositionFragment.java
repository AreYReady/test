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
import com.xkj.trade.bean_notification.NotificationClosePosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.DataUtil;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.RoundImageView;

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

import static com.xkj.trade.mvp.main_trade.fragment_content.v.MainTradeContentFrag.realTimeMap;

/**
 * Created by huangsc on 2016-12-14.
 * TODO:
 */

public class ClosePositionFragment extends BaseFragment {
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
    @Bind(R.id.stop_loss)
    TextView mStopLoss;
    @Bind(R.id.take_profit)
    TextView mTakeProfit;
    @Bind(R.id.copy_from)
    TextView mCopyFrom;
    @Bind(R.id.tv_copy_stop_loss)
    TextView mTvCopyStopLoss;
    @Bind(R.id.tv_copy_take_profit)
    TextView mTvCopyTakeProfit;
    @Bind(R.id.ll_copy_from)
    LinearLayout mLlCopyFrom;
//    @Bind(R.id.tv_close_position_prompt)
//    TextView mTvClosePositionPrompt;
    @Bind(R.id.tv_enter_button_prompt)
    TextView mTvEnterButtonPrompt;
    @Bind(R.id.tv_enter_button)
    TextView mTvEnterButton;
    @Bind(R.id.tv_estimated_profit_amount)
    TextView mTvEstimatedProfitAmount;
    private BeanOpenPosition.DataBean.ListBean mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_operate_close_position, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        if (mData.getCmd().contains("sell")) {
            mTvPlayAction.setText("卖出");
            mTvPlayAction.setTextColor(context.getResources().getColor(R.color.text_color_price_rise));
        } else {
            mTvPlayAction.setTextColor(context.getResources().getColor(R.color.text_color_price_fall));
            mTvPlayAction.setText("买进");
        }
        mAmount.setText(mData.getVolume());
        mOpenRate.setText(mData.getOpenprice());
        mOpenTime1.setText(mData.getOpentime());
        mStopLoss.setText(mData.getSl());
        mTakeProfit.setText(mData.getTp());
        mTvEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(context);
                enterOrder();
            }
        });
        if(realTimeMap.containsKey(mData.getSymbol())) {
            BeanAllSymbols.SymbolPrices symbolPrices = realTimeMap.get(mData.getSymbol());
            setHeader(symbolPrices.getSymbol(),symbolPrices.getAsk(),symbolPrices.getBid());
            setTvEstimatedProfitAmount(mData.getSymbol(),symbolPrices.getAsk(), symbolPrices.getBid());
        }
        requestSubSymbol();

    }

    public void setTvEstimatedProfitAmount( String symbol,String ask,String bid) {
        mTvEstimatedProfitAmount.setText("$:"+ DataUtil.getProfit(symbol,Double.valueOf(ask),Double.valueOf(bid),mData.getCmd(),mData.getOpenprice(),mData.getVolume()));
        if(Double.valueOf(DataUtil.getProfit(symbol,Double.valueOf(ask),Double.valueOf(bid),mData.getCmd(),mData.getOpenprice(),mData.getVolume()))<0){
            mTvEstimatedProfitAmount.setTextColor(getResources().getColor(R.color.text_color_price_fall));
        }else{
            mTvEstimatedProfitAmount.setTextColor(getResources().getColor(R.color.text_color_price_rise));
        }
    }
    public void setTEstimatedProfitAmount( String symbol, double ask,double bid) {
        mTvEstimatedProfitAmount.setText("$:"+ DataUtil.getProfit(symbol,ask,bid,mData.getCmd(),mData.getOpenprice(),mData.getVolume()));
        if(Double.valueOf(DataUtil.getProfit(symbol,ask,bid,mData.getCmd(),mData.getOpenprice(),mData.getVolume()))<0){
            mTvEstimatedProfitAmount.setTextColor(getResources().getColor(R.color.text_color_price_fall));
        }else{
            mTvEstimatedProfitAmount.setTextColor(getResources().getColor(R.color.text_color_price_rise));
        }
    }

    private void enterOrder() {
        Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        map.put(RequestConstant.ACTION, RequestConstant.Action.CLOSE.toString());
        map.put(RequestConstant.ORDERNO, String.valueOf(mData.getOrder()));
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_ORDER_EXE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: 平仓 " + call.request());
                showFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s;
               beanBaseResponse=new Gson().fromJson(s=response.body().string(),BeanBaseResponse.class);
                Log.i(TAG, "onResponse: 平仓"+s);
                if(beanBaseResponse.getStatus()==1) {
                    //通知刷新
                    EventBus.getDefault().post(new NotificationClosePosition(mData.getOrder()));
                    //发送通知activity关闭
//                    EventBus.getDefault().post(new BeanBaseResponse());
                    showSucc();
                }else{
                    showFail("操作未成功：\n"+beanBaseResponse.getMsg());
                }
            }
        });
    }
    BeanBaseResponse beanBaseResponse;
    @Override
    protected void eventSucc() {
        super.eventSucc();
        //发送通知activity关闭
        EventBus.getDefault().post(beanBaseResponse);
    }

    @Override
    protected void initData() {
        mData = new Gson().fromJson(this.getArguments().getString(OperatePositionActivity.JSON_DATA), BeanOpenPosition.DataBean.ListBean.class);
        title=getString(R.string.close);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void requestSubSymbol() {
        ChatWebSocket chartWebSocket = ChatWebSocket.getChartWebSocket();
        if (chartWebSocket != null) {
            chartWebSocket.sendMessage("{\"msg_type\":1010,\"symbol\":\"" + mData.getSymbol() + "\"}");
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRealTimeData(RealTimeDataList realTimeDataList) {
        Log.i(TAG, "getRealTimeData: ");
        for(RealTimeDataList.BeanRealTime beanRealTime:realTimeDataList.getQuotes()){
            if(beanRealTime.getSymbol().equals(mData.getSymbol())){
                setHeader(String.valueOf(beanRealTime.getSymbol()),String.valueOf(beanRealTime.getAsk()),String.valueOf(beanRealTime.getBid()));
                setTEstimatedProfitAmount(beanRealTime.getSymbol(),beanRealTime.getAsk(),beanRealTime.getBid());
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
}
