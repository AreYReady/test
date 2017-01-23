package trade.xkj.com.trade.mvp.main_trade.v;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.adapter.OpenAdapter;
import trade.xkj.com.trade.base.BaseFragment;
import trade.xkj.com.trade.bean.BeanOpenPositionData;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:持仓
 */

public class FragmentOpenPosition extends BaseFragment {
    private List<BeanOpenPositionData> mDataList;
    private BeanOpenPositionData mBeanOpenPositionData;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_open_position, null);
        return view;
    }

    @Override
    protected void initView() {
        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv_open_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new OpenAdapter(context,mDataList));
        Log.i(TAG, "initView: mDataList"+mDataList.size());
        //未知原因，可能是因为ViewDragHelper和recycle多种嵌套导致fragment高度大于父类。暂不探究原理。代码动态计算高度
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(mRecyclerView.getWidth(),(int)MainTradeContentActivity.descHeight
                        -view.findViewById(R.id.ll_positions_opened_header_impl).getHeight()
                        -(int)MainTradeContentActivity.flIndicatorHeight));
                }
        });
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
