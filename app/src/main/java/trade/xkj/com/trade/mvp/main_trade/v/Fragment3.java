package trade.xkj.com.trade.mvp.main_trade.v;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.base.BaseFragment;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class Fragment3 extends BaseFragment {
    private ViewPager mViewPager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_3,null);
        return view;
    }

    @Override
    protected void initView() {
    mViewPager=(ViewPager)view.findViewById(R.id.vp_indicator_content);

    }

    @Override
    protected void initData() {

    }
}
