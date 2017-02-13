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
import com.xkj.trade.utils.ResourceReader;
import com.xkj.trade.utils.SystemUtil;
import com.xkj.trade.utils.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangsc on 2016-12-10.
 * TODO:
 */

public class AddPositionFragment extends BaseFragment implements View.OnClickListener {

    private NoScrollViewPager mViewPager;
    private List<Fragment> mListFragment;
    private Button bOrder;
    private Button bPendingOrder;
    private LinearLayout layoutTab;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_operate_add,null);
        return view;
    }

    @Override
    protected void initView() {
        mViewPager=(NoScrollViewPager) view.findViewById(R.id.vp_operate_add_content);
        mViewPager.setAdapter(new MyAdapter(getFragmentManager()));
        mViewPager.setNoScroll(true);
        bOrder=(Button)view.findViewById(R.id.b_order);
         ViewTreeObserver viewTreeObserver = bOrder.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setTabSelected(bOrder);
                bOrder.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        bPendingOrder=(Button)view.findViewById(R.id.b_pending_order);
        bOrder.setOnClickListener(this);
        bPendingOrder.setOnClickListener(this);
        layoutTab=(LinearLayout)view.findViewById(R.id.ll_button_group);

    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
    }

    @Override
    protected void initData() {
        mListFragment=new ArrayList<>();
        mListFragment.add(new CardOrderFrag());
        mListFragment.add(new CardPendingFrag());
    }


    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        switch (v.getId()){
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

    class MyAdapter extends FragmentPagerAdapter{

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

//        int screenWidth = DensityUtils.getScreenSize(MainActivity.this)[0];

        int right = btnSelected.getWidth();
        Log.i(TAG, "setTabSelected: right"+right);
        selectedDrawable.setBounds(0, 0, right, SystemUtil.dp2px(context,3));
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

}
