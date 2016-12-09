package trade.xkj.com.trade.mvp.main_trade.v;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.adapter.OpenAdapter;
import trade.xkj.com.trade.base.BaseFragment;
import trade.xkj.com.trade.bean.BeanOpenPositionData;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class FragmentOpenPosition extends BaseFragment {
    private ViewPager mViewPager;
    private List<BeanOpenPositionData> mDataList;
    private BeanOpenPositionData mBeanOpenPositionData;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_3, null);
        return view;
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) view.findViewById(R.id.vp_indicator_content);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv_item3_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new OpenAdapter(context,mDataList));
        Log.i(TAG, "initView: mDataList"+mDataList.size());
    }

    @Override
    protected void initData() {
        mDataList=new ArrayList<>();
        for(int i=0;i<30;i++){
            mBeanOpenPositionData=new BeanOpenPositionData();
            mDataList.add(mBeanOpenPositionData);
        }
    }
}
