package trade.xkj.com.trade.mvp.main_trade.v;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.adapter.MyRecycleAdapter;
import trade.xkj.com.trade.base.BaseFragment;
import trade.xkj.com.trade.bean.BeanAttentionTraderData;

/**
 * Created by huangsc on 2016-12-07.
 * TODO:
 */

public class Fragment1 extends BaseFragment {
    private RecyclerView mRecyclerView;
    private List<BeanAttentionTraderData> mDataList;
    private BeanAttentionTraderData mData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_1,null);
        return view;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_item_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new MyRecycleAdapter(context,mDataList));
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(mRecyclerView.getWidth(),(int)MainTradeContentActivity.descHeight
                        -(int)MainTradeContentActivity.flIndicatorHeight));
            }
        });
    }

    @Override
    protected void initData() {
        mDataList=new ArrayList<>();
        for(int i=0;i<20;i++){
            mData=new BeanAttentionTraderData();
            mData.setCopyCount(100);
            mData.setName("huangshunchao");
            mData.setImageResouce(R.mipmap.menu_avatar);
            mData.setButtonText("复制");
            mDataList.add(mData);

        }
    }

}
