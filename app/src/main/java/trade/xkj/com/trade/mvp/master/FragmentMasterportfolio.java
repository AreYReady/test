package trade.xkj.com.trade.mvp.master;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.base.BaseFragment;

/**
 * Created by huangsc on 2016-12-27.
 * TODO:
 */

public class FragmentMasterPortfolio extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_master_portfolio, null);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
    }


}
