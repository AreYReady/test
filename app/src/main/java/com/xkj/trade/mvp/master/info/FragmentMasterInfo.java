package com.xkj.trade.mvp.master.info;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xkj.trade.R;
import com.xkj.trade.adapter.FragmentAdapter;
import com.xkj.trade.adapter.MyViewPagerAdapterItem;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.bean_.BeanBaseResponse;
import com.xkj.trade.bean_.BeanMasterClosePosition;
import com.xkj.trade.bean_.BeanMasterOpenPosition;
import com.xkj.trade.bean_.BeanMasterRank;
import com.xkj.trade.constant.RequestConstant;
import com.xkj.trade.mvp.master.info.contract.MasterInfoContract;
import com.xkj.trade.mvp.master.info.presenter.MasterInfoPresenterImpl;
import com.xkj.trade.utils.ACache;
import com.xkj.trade.utils.ThreadHelper;
import com.xkj.trade.utils.ToashUtil;
import com.xkj.trade.utils.view.CustomMasterLink;
import com.xkj.trade.utils.view.CustomSeekBar;
import com.xkj.trade.utils.view.CustomViewPager;
import com.xkj.trade.utils.view.MyScrollView;
import com.xkj.trade.utils.view.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangsc on 2016-12-22.
 * TODO:详细介绍单个高手的信息
 */

public class FragmentMasterInfo extends BaseFragment implements View.OnClickListener, MasterInfoContract.View {
//    private BackInterface mBackInterface;
    private CustomViewPager mCustomViewPager;
    private List<String> mDataItem;
    private ViewPager mViewPager;
    private List<Fragment> mListFragment;
    private View mCoverView;
    private Button mCopyButton;
    private View mBaseMasterInfo;
    private LinearLayout mLlButtons;
    private Button mCompleteButton;
    private Button mWatch;
    private ProgressBar mProgressBar;
    private TextView mTvMasterName;
    private TextView mTvCopyCount;
    private TextView mTvExperienceTime;
    private TextView mTvHuiceper;
    private TextView mTvProfitper;
    private CustomMasterLink mCustomMasterLink;
    private CustomSeekBar mCustomSeekBar;
    ImageView mIvCopyDown;
    ImageView mIvUncopyDown;
    LinearLayout mCoverCopy;
    RelativeLayout mCoverUncopy;
    TextView mCoverUncopyPrompt;
    private MyScrollView mMyScrollView;
    private MasterInfoContract.Presenter mPresenterListener;
    public static final String MASTER_INFO = "masterInfo";
    BeanMasterRank.MasterRank rank;
    int[] ints = new int[]{1, 10, 100, 500, 1000};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_master_info, null);
        //又来判断关联的activity是否是实现接口的activity
//        if (!(getActivity() instanceof BackInterface)) {
//            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
//        } else {
//            this.mBackInterface = (BackInterface) getActivity();
//        }
        rank = new Gson().fromJson(this.getArguments().getString(MASTER_INFO), new TypeToken<BeanMasterRank.MasterRank>() {
        }.getType());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        mBackInterface.setSelectedFragment(this);
    }

    @Override
    protected void initData() {
        mDataItem = new ArrayList<>();
        mDataItem.add("社区");
        mDataItem.add("流动表");
        mDataItem.add("仓位");
        mDataItem.add("投资组合");
        mListFragment = new ArrayList<>();
        BaseFragment baseFragment1 = new FragmentMasterComnunity();
        baseFragment1.setArguments(this.getArguments());
        BaseFragment baseFragment2 = new FragmentMasterStream();
        baseFragment2.setArguments(this.getArguments());
        BaseFragment baseFragment3 = new FragmentMasterPosition();
        baseFragment3.setArguments(this.getArguments());
        BaseFragment baseFragment4 = new FragmentMasterPortfolio();
        baseFragment4.setArguments(this.getArguments());
        mListFragment.add(baseFragment1);
        mListFragment.add(baseFragment2);
        mListFragment.add(baseFragment3);
        mListFragment.add(baseFragment4);
        mPresenterListener=new MasterInfoPresenterImpl(this);
    }

    @Override
    protected void initView() {
        mCustomMasterLink = (CustomMasterLink) view.findViewById(R.id.c_master_link);
        mCustomMasterLink.postInvalidate(rank, 0);
        mCustomViewPager = (CustomViewPager) view.findViewById(R.id.cvp_master_info_indicator);
        mCustomViewPager.setAdapter(new MyViewPagerAdapterItem(context, mDataItem));
        mCustomViewPager.setOffscreenPageLimit(4);
        mCustomViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mCustomViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int mPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
//                mViewPager.setFocusable(false);
                if (mCustomViewPager.getSpeed() < -1800) {
                    mCustomViewPager.setCurrentItem(mPosition + 1);
                    mCustomViewPager.setSpeed(0);
                    mViewPager.setCurrentItem(mPosition + 1,false);
                } else if (mCustomViewPager.getSpeed() > 1800 && mPosition > 0) {
                    //当手指右滑速度大于2000时viewpager左滑（注意item-1即可）
                    mCustomViewPager.setCurrentItem(mPosition - 1);
                    mCustomViewPager.setSpeed(0);
                    mViewPager.setCurrentItem(mPosition - 1,false);
                }else {
                    mViewPager.setCurrentItem(position, false);
                }

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
        mMyScrollView=(MyScrollView)view.findViewById(R.id.msv_my_scroll_view);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new FragmentAdapter(getChildFragmentManager(), mListFragment));
//        mViewPager.setFocusable(false);
        mBaseMasterInfo = view.findViewById(R.id.ll_master_base_info);
        mCoverView = view.findViewById(R.id.ll_cover);
        mCopyButton = (Button) view.findViewById(R.id.b_copy);
        mLlButtons = (LinearLayout) view.findViewById(R.id.ll_buttons);
        mCompleteButton = (Button) view.findViewById(R.id.b_complete);
        mCopyButton.setOnClickListener(this);
        mCompleteButton.setOnClickListener(this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mTvMasterName = (TextView) view.findViewById(R.id.tv_master_name);
        mTvMasterName.setText(rank.getName());
        mTvCopyCount = (TextView) view.findViewById(R.id.tv_copy_count);
        mTvCopyCount.setText("复制者：" + rank.getCopynumber());
        mTvExperienceTime = (TextView) view.findViewById(R.id.tv_experience_time);
        mTvExperienceTime.setText(rank.getTradeexperience() + " 天");
        mTvHuiceper = (TextView) view.findViewById(R.id.tv_huiceper);
        mTvHuiceper.setText(rank.getHuiceper() + "%");
        mTvProfitper = (TextView) view.findViewById(R.id.tv_profitper);
        mTvProfitper.setText(rank.getProfitper() + "%");

        int i = 0;
        if (rank.getData() != null) {
            for (String profit : rank.getData()) {
                if (Double.valueOf(profit) < 0) {
                    i++;
                }
            }
            mProgressBar.setMax(rank.getData().size());
        }
        mProgressBar.setProgress(i);
        mCustomSeekBar = (CustomSeekBar) view.findViewById(R.id.csb_select_voloum);
        mCustomSeekBar.setData(ints, 1);
        mIvCopyDown = (ImageView) view.findViewById(R.id.iv_copy_take_rebound);
        mIvCopyDown.setOnClickListener(this);
        mIvUncopyDown = (ImageView) view.findViewById(R.id.iv_uncopy_take_rebound);
        mIvUncopyDown.setOnClickListener(this);
        mCoverCopy = (LinearLayout) view.findViewById(R.id.ll_cover_copy);
        mCoverUncopy = (RelativeLayout) view.findViewById(R.id.rl_cover_uncopy);
        mCoverUncopyPrompt = (TextView) view.findViewById(R.id.tv_cover_uncopy_prompt);
        mWatch=(Button)view.findViewById(R.id.b_watch);
        if(rank.getStatus()==0){
            mCopyButton.setText(R.string.copy);
            mCoverCopy.setVisibility(View.VISIBLE);
            mCoverUncopy.setVisibility(View.GONE);
        }else{
            mCoverCopy.setVisibility(View.GONE);
            mCoverUncopy.setVisibility(View.VISIBLE);
            mCopyButton.setText(R.string.uncopy);
        }
        if(rank.getFstatus()==0){
            mWatch.setText(R.string.watch);
        }else{
            mWatch.setText(R.string.unwatch);
        }
        mWatch.setOnClickListener(this);
        mCoverUncopyPrompt.setText(String.format(getResources().getString(R.string.are_you_sure_uncopy_follow_master),rank.getName()));
//        request();
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
            case R.id.b_complete://点击完成
                if(rank.getStatus()==0) {
                    if (mCustomSeekBar.getMoney() >= 0) {
                        mPresenterListener.requestCopyFollow(rank.getLogin(), String.valueOf(mCustomSeekBar.getMoney()), ACache.get(context).getAsString(RequestConstant.ACCOUNT));
                    } else {
                        ToashUtil.show(context, "无效手数", Toast.LENGTH_SHORT);
                    }
                }else{
                    mPresenterListener.requestUnCopyFollow(rank.getLogin(),ACache.get(context).getAsString(RequestConstant.ACCOUNT));
                }
            case R.id.iv_copy_take_rebound://点击回缩
            case R.id.iv_uncopy_take_rebound:
                mCoverView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_down));
                mCoverView.setVisibility(View.INVISIBLE);
                mBaseMasterInfo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.transtale_y_dwon2));
                mBaseMasterInfo.setVisibility(View.VISIBLE);
                mCoverView.setVisibility(View.GONE);
                mLlButtons.setVisibility(View.VISIBLE);
                mCompleteButton.setVisibility(View.GONE);
                break;
            case R.id.b_watch://关注
                if(rank.getFstatus()==0){
                    mPresenterListener.requestFocus(rank.getLogin(),ACache.get(context).getAsString(RequestConstant.ACCOUNT));
                }else{
                    mPresenterListener.requestNoFocus(rank.getLogin(),ACache.get(context).getAsString(RequestConstant.ACCOUNT));
                }
                break;
        }
    }

    @Override
    public void responseMasterOpenPosition(BeanMasterOpenPosition beanMasterOpenPosition) {
    }

    @Override
    public void responseMasterClosePosition(BeanMasterClosePosition beanMasterClosePosition) {

    }

    @Override
    public void responseCopyFollow(BeanBaseResponse beanBaseResponse) {
        if(beanBaseResponse.getStatus()==1){
            ThreadHelper.instance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCopyButton.setText(R.string.uncopy);
                    rank.setStatus(1);
                    mCoverUncopy.setVisibility(View.VISIBLE);
                    mCoverCopy.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void responseUnCopyFollow(BeanBaseResponse beanBaseResponse) {
        if(beanBaseResponse.getStatus()==1){
            ThreadHelper.instance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCopyButton.setText(R.string.copy);
                    rank.setStatus(0);
                    mCoverUncopy.setVisibility(View.GONE);
                    mCoverCopy.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void responseNoFocus(BeanBaseResponse beanBaseResponse) {
        if(beanBaseResponse.getStatus()==1){
            ThreadHelper.instance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rank.setFstatus(0);
                    mWatch.setText(R.string.watch);
                }
            });
        }
    }

    @Override
    public void responseFocus(BeanBaseResponse beanBaseResponse) {
        if(beanBaseResponse.getStatus()==1){
            ThreadHelper.instance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rank.setFstatus(1);
                    mWatch.setText(R.string.unwatch);
                }
            });
        }
    }
}
