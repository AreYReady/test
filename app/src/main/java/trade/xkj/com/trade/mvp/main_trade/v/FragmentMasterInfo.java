package trade.xkj.com.trade.mvp.main_trade.v;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Space;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.SystemUtil;
import trade.xkj.com.trade.Utils.view.CustomViewPager;
import trade.xkj.com.trade.Utils.view.ZoomOutPageTransformer;
import trade.xkj.com.trade.adapter.FragmentAdapter;
import trade.xkj.com.trade.adapter.MyViewPagerAdapterItem;
import trade.xkj.com.trade.base.BaseFragment;
import trade.xkj.com.trade.mvp.master.FragmentMastComnunity;

/**
 * Created by huangsc on 2016-12-22.
 * TODO:详细介绍单个高手的信息
 */

public class FragmentMasterInfo extends BaseFragment {
    private BackInterface mBackInterface;
    private CustomViewPager mCustomViewPager;
    private List<String> mDataItem;
    private ViewPager mViewPager;
    private List<Fragment> mListFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_master_info,null);
        Log.i(TAG, "onCreateView: ");
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
    protected void initData() {
        mDataItem=new ArrayList<>();
        mDataItem.add("投资组合");
        mDataItem.add("流动表");
        mDataItem.add("仓位");
        mDataItem.add("社区");
        mListFragment=new ArrayList<>();
        mListFragment.add(new FragmentMastComnunity());
        mListFragment.add(new Fragment2());
        mListFragment.add(new Fragment1());
        mListFragment.add(new Fragment2());
    }
    @Override
    protected void initView() {
        mCustomViewPager=(CustomViewPager) view.findViewById(R.id.cvp_master_info_indicator);
        mCustomViewPager.setAdapter(new MyViewPagerAdapterItem(context,mDataItem));
        mCustomViewPager.setOffscreenPageLimit(mDataItem.size());
        mCustomViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mCustomViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int mPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                if (mCustomViewPager.getSpeed() < -1800) {
                    mCustomViewPager.setCurrentItem(mPosition + 1);
                    mCustomViewPager.setSpeed(0);
                    mViewPager.setCurrentItem(mPosition + 1);
                } else if (mCustomViewPager.getSpeed() > 1800 && mPosition > 0) {
                    //当手指右滑速度大于2000时viewpager左滑（注意item-1即可）
                    mCustomViewPager.setCurrentItem(mPosition - 1);
                    mCustomViewPager.setSpeed(0);
                    mViewPager.setCurrentItem(mPosition - 1);
                }
                Log.i(TAG, "onPageSelected: mPosition" + position);
                mViewPager.setCurrentItem(position, true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //将scrollview撑下来的空白布局
        final Space space=(Space)view.findViewById(R.id.s_cover_master_base_info);
        space.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                space.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                space.setLayoutParams(new LinearLayout.LayoutParams(view.findViewById(R.id.ll_master_base_info).getWidth(),view.findViewById(R.id.ll_master_base_info).getHeight()));
            }
        });
        mViewPager=(ViewPager) view.findViewById(R.id.vp_master_info);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                mCustomViewPager.setCurrentItem(position,true);
            }
        });
        mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mViewPager.setLayoutParams(new LinearLayout.LayoutParams(mViewPager.getWidth(),view.findViewById(R.id.rl_master_middle).getHeight()-mCustomViewPager.getMeasuredHeight()));
            }
        });
        mViewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(),mListFragment));
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
