package com.xkj.trade.base;

import android.util.Log;

import com.xkj.trade.bean_notification.NotificationKeyBoard;
import com.xkj.trade.utils.SoftKeyBoardListener;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by huangsc on 2016-12-10.
 * TODO:
 */

public abstract class OperateBaseActivity extends BaseActivity {

    @Override
    public void initRegister() {
        EventBus.getDefault().register(this);
        SoftKeyBoardListener.setListener(this,new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Log.i(TAG, "keyBoardShow: "+height);
                EventBus.getDefault().post(new NotificationKeyBoard(height));
            }

            @Override
            public void keyBoardHide(int height) {
                Log.i(TAG, "keyBoardHide: "+height);
                EventBus.getDefault().post(new NotificationKeyBoard(0));
            }
        });
    }
}
