package trade.xkj.com.trade.mvp.operate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.view.NoScrollViewPager;
import trade.xkj.com.trade.base.BaseFragment;

/**
 * Created by huangsc on 2016-12-10.
 * TODO:
 */

public class AddPositionFragment extends BaseFragment implements View.OnClickListener {

    private NoScrollViewPager mViewPager;
    private List<Fragment> mListFragment;
    private Button bOrder;
    private Button bPendingOrder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_operate_add,null);
        return view;
    }

    @Override
    protected void initView() {
        mViewPager=(NoScrollViewPager) view.findViewById(R.id.vp_operate_add_content);
        mViewPager.setAdapter(new MyAdapter(getFragmentManager()));
        mViewPager.setNoScroll(true);
        bOrder=(Button)view.findViewById(R.id.b_order);
        bPendingOrder=(Button)view.findViewById(R.id.b_pending_order);
        bOrder.setOnClickListener(this);
        bPendingOrder.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mListFragment=new ArrayList<>();
        mListFragment.add(new OrderCardFrag());
        mListFragment.add(new OrderCardFrag());
    }


    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        switch (v.getId()){
            case R.id.b_order:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.b_pending_order:
                mViewPager.setCurrentItem(1);
                break;
        }
    }

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mListFragment.get(position);
        }

        @Override
        public int getCount() {
            return mListFragment.size();
        }
    }
}
