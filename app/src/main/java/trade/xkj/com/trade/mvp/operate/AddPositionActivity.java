package trade.xkj.com.trade.mvp.operate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.base.OperateBaseActivity;

/**
 * Created by huangsc on 2016-12-10.
 * TODO:
 */

public class AddPositionActivity extends OperateBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_add);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_operate_content,new AddPositionFragment());
        fragmentTransaction.commit();
    }
}
