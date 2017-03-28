package com.xkj.trade.mvp.operate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.utils.ACache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangsc on 2017-03-28.
 * TODO:
 */

public class PayFragment extends BaseFragment implements View.OnClickListener{
    RecyclerView mRecyclerView;
    private List<String> payAmount=new ArrayList<>();
    private PayAdapter.PayHolder mHolder;
    private LinearLayout mwx;
    private LinearLayout mzhb;
    private LinearLayout myl;
    private TextView mAmount;
    private TextView mLogin;
    RadioButton mrbWx;
    RadioButton mrbzfb;
    RadioButton mrbyl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view=inflater.inflate(R.layout.fragment_pay,container,false);
    }

    @Override
    protected void initData() {
        payAmount.add("50");
        payAmount.add("100");
        payAmount.add("200");
        payAmount.add("300");
        payAmount.add("400");
        payAmount.add("500");
    }

    @Override
    protected void initView() {
        mLogin=(TextView)view.findViewById(R.id.tv_login_);
        mAmount=(TextView)view.findViewById(R.id.tv_amount);
        mwx=(LinearLayout)view.findViewById(R.id.ll_wx);
        mzhb=(LinearLayout)view.findViewById(R.id.ll_zfb);
        myl=(LinearLayout)view.findViewById(R.id.ll_yl);
        mrbWx=(RadioButton)view.findViewById(R.id.rb_wx);
        mrbzfb=(RadioButton)view.findViewById(R.id.rb_zfb);
        mrbyl=(RadioButton)view.findViewById(R.id.rb_yl);
        mwx.setOnClickListener(this);
        mzhb.setOnClickListener(this);
        myl.setOnClickListener(this);
        mrbWx.setChecked(true);
        mLogin.setText(ACache.get(context).getAsString(RequestConstant.ACCOUNT));
        mRecyclerView=(RecyclerView) view.findViewById(R.id.rv_pay_amount);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context,3));
        mRecyclerView.setAdapter(new PayAdapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_wx:
                mrbWx.setChecked(true);
                mrbzfb.setChecked(false);
                mrbyl.setChecked(false);
                break;
            case R.id.ll_zfb:
                mrbWx.setChecked(false);
                mrbzfb.setChecked(true);
                mrbyl.setChecked(false);
                break;
            case R.id.ll_yl:
                mrbWx.setChecked(false);
                mrbzfb.setChecked(false);
                mrbyl.setChecked(true);
                break;
        }
    }

    class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayHolder>{
        @Override
        public PayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PayAdapter.PayHolder viewHolder=new PayAdapter.PayHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_pay_amount,parent,false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final PayHolder holder, final int position) {
            holder.mButton.setText(payAmount.get(position));
            if(mHolder==null){
                mHolder=holder;
                mHolder.mButton.setSelected(true);
                mAmount.setText(payAmount.get(position));
            }
            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mHolder.mButton.isSelected()) {
                        mHolder.mButton.setSelected(false);
                    }
                    mHolder=holder;
                    holder.mButton.setSelected(true);
                    mAmount.setText(payAmount.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return payAmount==null?0:payAmount.size();
        }

        class PayHolder extends RecyclerView.ViewHolder{

            private Button mButton;
            public PayHolder(View itemView) {
                super(itemView);
              mButton=(Button)itemView.findViewById(R.id.b_pay_amount);
            }
        }
    }
}
