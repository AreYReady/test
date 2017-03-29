package com.xkj.trade.mvp.operate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.utils.ACache;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by huangsc on 2017-03-28.
 * TODO:出金
 */

public class WithdrawFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_login_)
    TextView mTvLogin;
    @Bind(R.id.tv_amount)
    TextView mTvAmount;
    @Bind(R.id.rv_pay_amount)
    RecyclerView mRvPayAmount;
    @Bind(R.id.ib_wx)
    ImageButton mIbWx;
    @Bind(R.id.fl_wx)
    FrameLayout mFlWx;
    @Bind(R.id.ib_zhifubao)
    ImageButton mIbZhifubao;
    @Bind(R.id.fl_zhifubao)
    FrameLayout mFlZhifubao;
    @Bind(R.id.ib_cup)
    ImageButton mIbCup;
    @Bind(R.id.fl_cup)
    FrameLayout mFlCup;
    @Bind(R.id.tv_enter_pay)
    TextView mTvEnterPay;
    private List<String> payAmount = new ArrayList<>();
    private PayAdapter.PayHolder mHolder;

    private TextView mAmount;
    private TextView mLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_withdraw, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        payAmount.add("$50");
        payAmount.add("$100");
        payAmount.add("$200");
        payAmount.add("$300");
        payAmount.add("$400");
        payAmount.add("$500");
    }

    @Override
    protected void initView() {
        mLogin = (TextView) view.findViewById(R.id.tv_login_);
        mAmount = (TextView) view.findViewById(R.id.tv_amount);
        mLogin.setText(ACache.get(context).getAsString(RequestConstant.ACCOUNT));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_pay_amount);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        mRecyclerView.setAdapter(new PayAdapter());
        mIbWx.setOnClickListener(this);
        mFlWx.setSelected(true);
        mIbCup.setOnClickListener(this);
        mIbZhifubao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_wx:
                mFlWx.setSelected(true);
                mFlCup.setSelected(false);
                mFlZhifubao.setSelected(false);
                break;
            case R.id.ib_zhifubao:
                mFlWx.setSelected(false);
                mFlCup.setSelected(false);
                mFlZhifubao.setSelected(true);
                break;
            case R.id.ib_cup:
                mFlWx.setSelected(false);
                mFlCup.setSelected(true);
                mFlZhifubao.setSelected(false);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayHolder> {
        @Override
        public PayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PayHolder viewHolder = new PayHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_pay_amount, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final PayHolder holder, final int position) {
            holder.mButton.setText(payAmount.get(position));
            if (mHolder == null) {
                mHolder = holder;
                mHolder.mButton.setSelected(true);
                mAmount.setText(payAmount.get(position));
            }
            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHolder.mButton.isSelected()) {
                        mHolder.mButton.setSelected(false);
                    }
                    mHolder = holder;
                    holder.mButton.setSelected(true);
                    mAmount.setText(payAmount.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return payAmount == null ? 0 : payAmount.size();
        }

        class PayHolder extends RecyclerView.ViewHolder {

            private Button mButton;

            public PayHolder(View itemView) {
                super(itemView);
                mButton = (Button) itemView.findViewById(R.id.b_pay_amount);
            }
        }
    }
}
