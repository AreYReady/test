package com.xkj.trade.mvp.master;

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
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.R;
import com.xkj.trade.adapter.FragmentAdapter;
import com.xkj.trade.adapter.MyViewPagerAdapterItem;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.utils.view.CustomMasterLink;
import com.xkj.trade.utils.view.CustomSeekBar;
import com.xkj.trade.utils.view.CustomViewPager;
import com.xkj.trade.utils.view.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

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
    private ProgressBar mProgressBar;
    private TextView mTvMasterName;
    private TextView mTvCopyCount;
    private TextView mTvExperienceTime;
    private TextView mTvHuiceper;
    private TextView mTvProfitper;
    private CustomMasterLink mCustomMasterLink;
    private CustomSeekBar mCustomSeekBar;
    public static final String MASTER_INFO="masterInfo";
    BeanMasterRank.MasterRank rank;
    int[] ints= new int[]{1,10,100,500,1000};

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
        rank = new Gson().fromJson(this.getArguments().getString(MASTER_INFO),new TypeToken<BeanMasterRank.MasterRank>(){}.getType());
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
        mCustomMasterLink=(CustomMasterLink)view.findViewById(R.id.c_master_link);
        mCustomMasterLink.postInvalidate(rank,0);
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
        mProgressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        mTvMasterName=(TextView)view.findViewById(R.id.tv_master_name);
        mTvMasterName.setText(rank.getName());
        mTvCopyCount=(TextView)view.findViewById(R.id.tv_copy_count);
        mTvCopyCount.setText("复制者："+rank.getCopynumber());
        mTvExperienceTime=(TextView)view.findViewById(R.id.tv_experience_time);
        mTvExperienceTime.setText(rank.getTradeexperience()+" 天");
        mTvHuiceper=(TextView)view.findViewById(R.id.tv_huiceper);
        mTvHuiceper.setText(rank.getHuiceper()+"%");
        mTvProfitper=(TextView)view.findViewById(R.id.tv_profitper);
        mTvProfitper.setText(rank.getProfitper()+"%");
        int i=0;
        if(rank.getData()!=null) {
            for (String profit : rank.getData()) {
                if (Double.valueOf(profit) < 0) {
                    i++;
                }
            }
            mProgressBar.setMax(rank.getData().size());
        }
        mProgressBar.setProgress(i);
        mCustomSeekBar=(CustomSeekBar) view.findViewById(R.id.csb_select_voloum);
        mCustomSeekBar.setData(ints,1);
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
