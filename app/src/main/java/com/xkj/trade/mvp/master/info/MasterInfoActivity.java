package com.xkj.trade.mvp.master.info;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.xkj.trade.R;
import com.xkj.trade.base.BaseActivity;

import static com.xkj.trade.mvp.master.info.FragmentMasterInfo.MASTER_INFO;

/**
 * Created by huangsc on 2017-02-28.
 * TODO:包裹高手信息的列表
 */

public class MasterInfoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_master_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        FragmentMasterInfo   mFragmentMasterInfo = new FragmentMasterInfo();
        Bundle bundle=new Bundle();
        String s=getIntent().getExtras().getString(MASTER_INFO);
        Log.i(TAG, "initView: "+s);
        bundle.putString(MASTER_INFO,s);
        mFragmentMasterInfo.setArguments(bundle);
        transaction.add(R.id.fl_main_trade_content,mFragmentMasterInfo);
        transaction.commit();
    }
    @Override
    public void initEvent() {
    }
}
