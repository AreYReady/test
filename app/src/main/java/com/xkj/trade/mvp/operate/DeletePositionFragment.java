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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.ChatWebSocket;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean.RealTimeDataList;
import com.xkj.trade.bean_.BeanBaseResponse;
import com.xkj.trade.bean_.BeanOpenPosition;
import com.xkj.trade.bean_.BeanPendingPosition;
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

import static com.xkj.trade.constant.TradeDateConstant.VOLUME_MONEY;

/**
 * Created by huangsc on 2016-12-14.
 * TODO:
 */

public class DeletePositionFragment extends BaseFragment {
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
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_expiry_time)
    TextView mTvExpiryTime;
    @Bind(R.id.stop_loss)
    TextView mStopLoss;
    @Bind(R.id.take_profit)
    TextView mTakeProfit;
    @Bind(R.id.tv_enter_button)
    TextView mTvEnterButton;
    @Bind(R.id.riv_trade_symbol)
    RoundImageView mRivTradeSymbol;

    private TextView tvAction;
    private BeanPendingPosition.DataBean.ListBean mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_operate_delete_pending_position, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        tvAction = (TextView) view.findViewById(R.id.tv_enter_button);
        tvAction.setText(this.getString(R.string.delete_pending_order));
        tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterOrder();
            }
        });
        if (mData.getCmd().contains("sell")) {
            mTvPlayAction.setText("卖出");
            mTvPlayAction.setTextColor(context.getResources().getColor(R.color.text_color_price_fall));
        } else {
            mTvPlayAction.setTextColor(context.getResources().getColor(R.color.text_color_price_rise));
            mTvPlayAction.setText("买进");
        }
        mAmount.setText(String.valueOf(Double.valueOf(mData.getVolume()) * VOLUME_MONEY));
        mStopLoss.setText(mData.getSl());
        mTakeProfit.setText(mData.getTp());
        mTvPrice.setText(mData.getOpenprice());
        mRivTradeSymbol.setImageResource(DataUtil.getImageId(mData.getSymbol()));

        requestSubSymbol();
    }

    private void enterOrder() {
        Map<String, String> map = new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        map.put(RequestConstant.SYMBOL, AesEncryptionUtil.stringBase64toString(MyApplication.getInstance().beanIndicatorData.getSymbol()));
        map.put(RequestConstant.ACTION, RequestConstant.Action.DELETE.toString());
        map.put(RequestConstant.ORDERNO, String.valueOf(mData.getOrder()));
        OkhttpUtils.enqueue(UrlConstant.URL_TRADE_ORDER_EXE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + call.request());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = null;
                Log.i(TAG, "onResponse: " + call.request());
                Log.i(TAG, "onResponse: " + (s = response.body().string()));
                BeanBaseResponse beanBaseResponse = new Gson().fromJson(s, new TypeToken<BeanBaseResponse>() {
                }.getType());
                if (beanBaseResponse.getStatus() == 1) {
                    EventBus.getDefault().post(new BeanOpenPosition());
                    //发送通知activity关闭
                    EventBus.getDefault().post(beanBaseResponse);
                }
            }
        });
    }

    private void requestSubSymbol() {
        ChatWebSocket chartWebSocket = ChatWebSocket.getChartWebSocket();
        if (chartWebSocket != null) {
            chartWebSocket.sendMessage("{\"msg_type\":1010,\"symbol\":\"" + mData.getSymbol() + "\"}");
        }
    }

    @Override
    protected void initData() {
        mData = new Gson().fromJson(this.getArguments().getString(OperatePositionActivity.JSON_DATA), new TypeToken<BeanPendingPosition.DataBean.ListBean>() {
        }.getType());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRealTimeData(RealTimeDataList realTimeDataList) {
        Log.i(TAG, "getRealTimeData: ");
        for (RealTimeDataList.BeanRealTime beanRealTime : realTimeDataList.getQuotes()) {
            if (beanRealTime.getSymbol().equals(mData.getSymbol())) {
                setHeader(String.valueOf(beanRealTime.getSymbol()), String.valueOf(beanRealTime.getAsk()), String.valueOf(beanRealTime.getBid()));
            }
        }
    }

    public void setHeader(String symbol, String ask, String bid) {
        SpannableString askTextBig = MoneyUtil.getRealTimePriceTextBig(context, ask);
        SpannableString bidTextBig = MoneyUtil.getRealTimePriceTextBig(context, bid);
        if (mPriceRight.getText().toString() != "") {
            askTextBig.setSpan(new ForegroundColorSpan(getResources().getColor(Double.valueOf(ask) > Double.valueOf(mPriceLeft.getText().toString()) ? R.color.text_color_price_rise : R.color.text_color_price_fall)), 0, bidTextBig.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (mPriceRight.getText().toString() != "") {
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
