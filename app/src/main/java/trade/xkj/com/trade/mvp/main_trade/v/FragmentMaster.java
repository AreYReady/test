package trade.xkj.com.trade.mvp.main_trade.v;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.adapter.MasterAdapter;
import trade.xkj.com.trade.base.BaseFragment;

/**
 * Created by huangsc on 2016-12-20.
 * TODO:显示社区高手的一些信息和状态线图
 */

public class FragmentMaster extends BaseFragment {
    private RecyclerView mRvMaster;
    private MasterAdapter mMasterAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_masters,null);
        return view;
    }

    @Override
    protected void initView() {
        mRvMaster=(RecyclerView)view.findViewById(R.id.rv_master);
        mRvMaster.setAdapter(mMasterAdapter=new MasterAdapter(context));
        mRvMaster.setLayoutManager(new LinearLayoutManager(context));
//        mMasterAdapter.setOnItemClickListener(new MasterAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick() {
//
//            }
//        });
    }

    @Override
    protected void initData() {
    }
}
