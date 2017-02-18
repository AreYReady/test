package com.xkj.trade.mvp.operate.addPosition;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xkj.trade.R;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean.BeanIndicatorData;
import com.xkj.trade.mvp.operate.addPosition.contract.AddPositionContract;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.ResourceReader;
import com.xkj.trade.utils.RoundImageView;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.view.NoScrollViewPager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by huangsc on 2016-12-10.
 * TODO:
 */

public class AddPositionFragment extends BaseFragment implements View.OnClickListener, AddPositionContract.View {

    @Bind(R.id.riv_trade_symbol)
    RoundImageView mRivTradeSymbol;
    @Bind(R.id.price_left)
    TextView mPriceLeft;
    @Bind(R.id.price_right)
    TextView mPriceRight;
    @Bind(R.id.tv_symbol_name)
    TextView mTvSymbolName;
    private NoScrollViewPager mViewPager;
    private List<Fragment> mListFragment;
    private Button bOrder;
    private Button bPendingOrder;
    private LinearLayout layoutTab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_operate_add, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_operate_add_content);
        mViewPager.setAdapter(new MyAdapter(getFragmentManager()));
        mViewPager.setNoScroll(true);
        bOrder = (Button) view.findViewById(R.id.b_order);
        ViewTreeObserver viewTreeObserver = bOrder.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setTabSelected(bOrder);
                bOrder.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        bPendingOrder = (Button) view.findViewById(R.id.b_pending_order);
        bOrder.setOnClickListener(this);
        bPendingOrder.setOnClickListener(this);
        layoutTab = (LinearLayout) view.findViewById(R.id.ll_button_group);
        getCurrentSymbol(MyApplication.getInstance().beanIndicatorData);
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
    }

    @Override
    protected void initData() {
        mListFragment = new ArrayList<>();
        mListFragment.add(new CardOrderFrag());
        mListFragment.add(new CardPendingFrag());
    }


    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.b_order:
                setTabSelected((Button) v);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.b_pending_order:
                setTabSelected((Button) v);
                mViewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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

    private void setTabSelected(Button btnSelected) {
        Drawable selectedDrawable = ResourceReader.readDrawable(context, R.drawable.shape_nav_indicator);
        int right = btnSelected.getWidth();
        Log.i(TAG, "setTabSelected: right" + right);
        selectedDrawable.setBounds(0, 0, right, SystemUtil.dp2px(context, 3));
        btnSelected.setSelected(true);
        btnSelected.setCompoundDrawables(null, null, null, selectedDrawable);
        int size = layoutTab.getChildCount();
        for (int i = 0; i < size; i++) {
            if (btnSelected.getId() != layoutTab.getChildAt(i).getId()) {
                layoutTab.getChildAt(i).setSelected(false);
                ((Button) layoutTab.getChildAt(i)).setCompoundDrawables(null, null, null, null);
            }
        }
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
