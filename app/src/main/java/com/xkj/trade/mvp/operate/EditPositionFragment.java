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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.IO.okhttp.OkhttpUtils;
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean.BeanIndicatorData;
import com.xkj.trade.bean_.BeanOpenPosition;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.constant.UrlConstant;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.AesEncryptionUtil;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.RoundImageView;

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

public class EditPositionFragment extends BaseFragment {
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
    private TextView tvAction;
    private String jsonData;
    private BeanOpenPosition.DataBean.ListBean mData;

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
        getCurrentSymbol(MyApplication.getInstance().beanIndicatorData);
        if (mData.getCmd().equals("sell")) {
            mTvPlayAction.setText("卖出");
            mTvPlayAction.setTextColor(context.getResources().getColor(R.color.text_color_price_fall));
        } else {
            mTvPlayAction.setTextColor(context.getResources().getColor(R.color.text_color_price_rise));
            mTvPlayAction.setText("买进");
        }
        mAmount.setText(String.valueOf(Double.valueOf(mData.getVolume()) * VOLUME_MONEY));
        mOpenRate.setText(mData.getOpenprice());
        mOpenTime1.setText(mData.getOpentime());
    mTvEnterButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            enterOrder();
        }
    });
    }

    private void enterOrder() {
        Map<String,String> map=new TreeMap<>();
        map.put(RequestConstant.LOGIN, AesEncryptionUtil.stringBase64toString(ACache.get(context).getAsString(RequestConstant.ACCOUNT)));
        map.put(RequestConstant.SYMBOL,AesEncryptionUtil.stringBase64toString(MyApplication.getInstance().beanIndicatorData.getSymbol()));
        map.put(RequestConstant.ORDERNO,String.valueOf(mData.getOrder()));
        map.put(RequestConstant.PRICE,String.valueOf(Double.valueOf(mData.getVolume()) * VOLUME_MONEY));
        if(Double.valueOf(mData.getSl())>=Double.valueOf(mData.getPrice())){
            Toast.makeText(context,"止损金额不能大于开仓价",Toast.LENGTH_SHORT).show();
            return;
        }
        if(Double.valueOf(mData.getTp())<=Double.valueOf(mData.getPrice())){
            Toast.makeText(context,"获利金额不能小于开仓价",Toast.LENGTH_SHORT).show();
            return;
        }
        map.put(RequestConstant.SL,mData.getSl());
        map.put(RequestConstant.TP,mData.getTp());
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
    protected void initData() {
        mData = new Gson().fromJson(this.getArguments().getString(OperatePositionActivity.JSON_DATA), new TypeToken<BeanOpenPosition.DataBean.ListBean>() {
        }.getType());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getCurrentSymbol(BeanIndicatorData beanIndicatorData) {
        Log.i(TAG, "getCurrentSymbol: " + beanIndicatorData.getSymbol());
        SpannableString askTextBig = MoneyUtil.getRealTimePriceTextBig(context, beanIndicatorData.getBid());
        SpannableString bidTextBig = MoneyUtil.getRealTimePriceTextBig(context, beanIndicatorData.getAsk());
        if (beanIndicatorData.getBidColor() != 0) {
            bidTextBig.setSpan(new ForegroundColorSpan(beanIndicatorData.getBidColor()), 0, bidTextBig.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (beanIndicatorData.getAskColor() != 0) {
            askTextBig.setSpan(new ForegroundColorSpan(beanIndicatorData.getAskColor()), 0, askTextBig.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        mTvSymbolName.setText(beanIndicatorData.getSymbol());
        mPriceLeft.setText(askTextBig);
        mPriceRight.setText(bidTextBig);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
