package com.xkj.trade.mvp.operate;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.mvp.operate.pay.DepositFragment;
import com.xkj.trade.mvp.operate.pay.WithdrawFragment;
import com.xkj.trade.utils.ResourceReader;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by huangsc on 2017-03-29.
 * TODO:出入金
 */

public class PayFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.b_deposit)
    Button mBDeposit;
    @Bind(R.id.b_withdraw)
    Button mBWithdraw;
    @Bind(R.id.ll_button_group)
    LinearLayout mLlButtonGroup;
    @Bind(R.id.vp_pay_content)
    NoScrollViewPager mVpPayContent;

    private List<Fragment> mListFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pay, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        mListFragment = new ArrayList<>();
        mListFragment.add(new DepositFragment());
        mListFragment.add(new WithdrawFragment());
    }

    @Override
    protected void initView() {
        ViewTreeObserver viewTreeObserver = mBDeposit.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setTabSelected(mBDeposit);
                mBDeposit.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mBDeposit.setOnClickListener(this);
        mBWithdraw.setOnClickListener(this);
        mVpPayContent.setAdapter(new MyAdapter(getFragmentManager()));
        mVpPayContent.setNoScroll(true);
    }
    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mListFragment.get(position);
        }

        @Override
        public int getCount() {
            return mListFragment.size();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setTabSelected(Button btnSelected) {
        Drawable selectedDrawable = ResourceReader.readDrawable(context, R.drawable.shape_nav_indicator);
        int right = btnSelected.getWidth();
        Log.i(TAG, "setTabSelected: right" + right);
        selectedDrawable.setBounds(0, 0, right, SystemUtil.dp2px(context, 3));
        btnSelected.setSelected(true);
        btnSelected.setCompoundDrawables(null, null, null, selectedDrawable);
        int size = mLlButtonGroup.getChildCount();
        for (int i = 0; i < size; i++) {
            if (btnSelected.getId() != mLlButtonGroup.getChildAt(i).getId()) {
                mLlButtonGroup.getChildAt(i).setSelected(false);
                ((Button) mLlButtonGroup.getChildAt(i)).setCompoundDrawables(null, null, null, null);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_deposit:
                setTabSelected((Button) v);
                mVpPayContent.setCurrentItem(0);
                break;
            case R.id.b_withdraw:
                setTabSelected((Button) v);
                mVpPayContent.setCurrentItem(1);
                break;
        }
    }
}
