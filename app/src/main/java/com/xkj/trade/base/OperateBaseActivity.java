package com.xkj.trade.base;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by huangsc on 2016-12-10.
 * TODO:
 */

public abstract class OperateBaseActivity extends BaseActivity {

    @Override
    public void initRegister() {
        EventBus.getDefault().register(this);
    }
}
