package trade.xkj.com.trade.mvp.main_trade.v;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.base.BaseFragment;

/**
 * Created by huangsc on 2016-12-20.
 * TODO:
 */

public class FragmentMaster extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_master_content,null);
        return view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
