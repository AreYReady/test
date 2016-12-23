package trade.xkj.com.trade.mvp.main_trade.v;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.view.CustomViewPager;
import trade.xkj.com.trade.Utils.view.ZoomOutPageTransformer;
import trade.xkj.com.trade.adapter.MyViewPagerAdapterItem;
import trade.xkj.com.trade.base.BaseFragment;

/**
 * Created by huangsc on 2016-12-22.
 * TODO:详细介绍单个高手的信息
 */

public class FragmentMasterInfo extends BaseFragment {
    private BackInterface mBackInterface;
    private CustomViewPager mCustomViewPager;
    private List<String> mDataItem;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_master_info,null);

        //又来判断关联的activity是否是实现接口的activity
        if(!(getActivity() instanceof BackInterface)){
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }else{
            this.mBackInterface = (BackInterface)getActivity();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBackInterface.setSelectedFragment(this);
    }

    @Override
    protected void initView() {
        mCustomViewPager=(CustomViewPager) view.findViewById(R.id.cvp_master_info);
        mCustomViewPager.setAdapter(new MyViewPagerAdapterItem(context,mDataItem));
        mCustomViewPager.setOffscreenPageLimit(mDataItem.size());
        mCustomViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    protected void initData() {
    mDataItem=new ArrayList<>();
        mDataItem.add("投资组合");
        mDataItem.add("流动表");
        mDataItem.add("仓位");
        mDataItem.add("社区");
    }

    @Override
    public Boolean onBackPressed() {
        if(getFragmentManager()!=null) {
            getFragmentManager().popBackStackImmediate();
            return true;
        }
       return super.onBackPressed();
    }
}
