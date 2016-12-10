package trade.xkj.com.trade.mvp.operate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.base.BaseFragment;

/**
 * Created by huangsc on 2016-12-10.
 * TODO:
 */

public class OrderCardFrag  extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.vp_item_order_frag,null);
        return view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
