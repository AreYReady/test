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
import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean.BeanIndicatorData;
import com.xkj.trade.bean_.BeanOpenPosition;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.RoundImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.xkj.trade.constant.TradeDateConstant.VOLUME_MONEY;

/**
 * Created by huangsc on 2016-12-14.
 * TODO:
 */

public class EditPendingPositionFrament extends BaseFragment {
    @Bind(R.id.riv_trade_symbol)
    RoundImageView mRivTradeSymbol;
    @Bind(R.id.tv_symbol_name)
    TextView mTvSymbolName;
    @Bind(R.id.price_left)
    TextView mPriceLeft;
    @Bind(R.id.price_right)
    TextView mPriceRight;
    //    @Bind(R.id.commission)
//    TextView mAmount;
    @Bind(R.id.amount)
    TextView mCommission;
    @Bind(R.id.tv_enter_button_prompt)
    TextView mTvEnterButtonPrompt;
    @Bind(R.id.tv_enter_button)
    TextView mTvEnterButton;
    @Bind(R.id.tv_play_action)
    TextView mTvPlayAction;
    private TextView tvAction;
    private BeanOpenPosition.DataBean.ListBean mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_operate_edit_pending_position, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        tvAction = (TextView) view.findViewById(R.id.tv_enter_button);
        tvAction.setText(this.getString(R.string.update_position));
        getCurrentSymbol(MyApplication.getInstance().beanIndicatorData);
        if (mData.getCmd().equals("sell")) {
           mTvPlayAction.setText("卖出");
            mTvPlayAction.setTextColor(context.getResources().getColor(R.color.text_color_price_fall));
        } else {
            mTvPlayAction.setTextColor(context.getResources().getColor(R.color.text_color_price_rise));
            mTvPlayAction.setText("买进");
        }
        mCommission.setText(String.valueOf(Double.valueOf(mData.getVolume()) * VOLUME_MONEY));

    }

    @Override
    protected void initData() {
        mData = new Gson().fromJson(this.getArguments().getString(OperatePositionActivity.JSON_DATA), new TypeToken<BeanOpenPosition.DataBean.ListBean>() {
        }.getType());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
}
