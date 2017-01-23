package trade.xkj.com.trade.mvp.master;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.utils.view.CustomViewPager;
import trade.xkj.com.trade.utils.view.ZoomOutPageTransformer;
import trade.xkj.com.trade.adapter.FragmentAdapter;
import trade.xkj.com.trade.adapter.MyViewPagerAdapterItem;
import trade.xkj.com.trade.base.BaseFragment;

/**
 * Created by huangsc on 2016-12-22.
 * TODO:详细介绍单个高手的信息
 */

public class FragmentMasterInfo extends BaseFragment implements View.OnClickListener {
    private BackInterface mBackInterface;
    private CustomViewPager mCustomViewPager;
    private List<String> mDataItem;
    private ViewPager mViewPager;
    private List<Fragment> mListFragment;
    private View mCoverView;
    private Button mCopyButton;
    private View mBaseMasterInfo;
    private LinearLayout mLlButtons;
    private Button mCompleteButton;
    private TextView mTvRebound;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_master_info, null);
        //又来判断关联的activity是否是实现接口的activity
        if (!(getActivity() instanceof BackInterface)) {
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        } else {
            this.mBackInterface = (BackInterface) getActivity();
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
        mDataItem = new ArrayList<>();
        mDataItem.add("社区");
        mDataItem.add("流动表");
        mDataItem.add("仓位");
        mDataItem.add("投资组合");
        mListFragment = new ArrayList<>();
        mListFragment.add(new FragmentMasterComnunity());
        mListFragment.add(new FragmentMasterStream());
        mListFragment.add(new FragmentMasterPosition());
        mListFragment.add(new FragmentMasterPortfolio());
    }

    @Override
    protected void initView() {
        mCustomViewPager = (CustomViewPager) view.findViewById(R.id.cvp_master_info_indicator);
        mCustomViewPager.setAdapter(new MyViewPagerAdapterItem(context, mDataItem));
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
                mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //将scrollview撑下来的空白布局
        final Space space = (Space) view.findViewById(R.id.s_cover_master_base_info);
        space.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                space.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                space.setLayoutParams(new LinearLayout.LayoutParams(view.findViewById(R.id.ll_master_base_info).getWidth(), view.findViewById(R.id.ll_master_base_info).getHeight()));
                view.findViewById(R.id.ll_cover).setLayoutParams(new FrameLayout.LayoutParams(mBaseMasterInfo.getWidth(), mBaseMasterInfo.getHeight()));
            }
        });
        mViewPager = (ViewPager) view.findViewById(R.id.vp_master_info);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCustomViewPager.setCurrentItem(position, true);
            }
        });
        mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mViewPager.setLayoutParams(new LinearLayout.LayoutParams(mViewPager.getWidth(), view.findViewById(R.id.rl_master_middle).getHeight() - mCustomViewPager.getMeasuredHeight()));
            }
        });
        mViewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(), mListFragment));
        mBaseMasterInfo = view.findViewById(R.id.ll_master_base_info);
        mCoverView = view.findViewById(R.id.ll_cover);
        mCopyButton = (Button) view.findViewById(R.id.b_copy);
        mLlButtons = (LinearLayout) view.findViewById(R.id.ll_buttons);
        mCompleteButton = (Button) view.findViewById(R.id.b_complete);
        mTvRebound =(TextView)view.findViewById(R.id.tv_take_rebound);
        mTvRebound.setOnClickListener(this);
        mCopyButton.setOnClickListener(this);
        mCompleteButton.setOnClickListener(this);

    }


    @Override
    public Boolean onBackPressed() {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStackImmediate();
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_copy://点击复制
                mBaseMasterInfo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up2));
                mCoverView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_up));
                mBaseMasterInfo.setVisibility(View.GONE);
                mCoverView.setVisibility(View.VISIBLE);
                mLlButtons.setVisibility(View.GONE);
                mCompleteButton.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_take_rebound://点击回缩
                mCoverView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_down));
                mCoverView.setVisibility(View.INVISIBLE);
                mBaseMasterInfo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_dwon2));
                mBaseMasterInfo.setVisibility(View.VISIBLE);
                mCoverView.setVisibility(View.GONE);
                mLlButtons.setVisibility(View.VISIBLE);
                mCompleteButton.setVisibility(View.GONE);
                break;
        }
    }
}
