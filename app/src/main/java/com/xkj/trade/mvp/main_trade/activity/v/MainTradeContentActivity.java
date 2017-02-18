package com.xkj.trade.mvp.main_trade.activity.v;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xkj.trade.R;
import com.xkj.trade.adapter.FragmentAdapter;
import com.xkj.trade.adapter.MyViewPagerAdapterItem;
import com.xkj.trade.base.BaseActivity;
import com.xkj.trade.base.BaseFragment;
import com.xkj.trade.base.MyApplication;
import com.xkj.trade.bean.BeanMasterInfo;
import com.xkj.trade.bean_.BeanUserListInfo;
import com.xkj.trade.mvp.main_trade.FragmentClosePosition;
import com.xkj.trade.mvp.main_trade.FragmentMasterCopy;
import com.xkj.trade.mvp.main_trade.FragmentMasterWatch;
import com.xkj.trade.mvp.main_trade.FragmentOpenPosition;
import com.xkj.trade.mvp.main_trade.FragmentPendingPosition;
import com.xkj.trade.mvp.main_trade.activity.p.MainActPreImpl;
import com.xkj.trade.mvp.main_trade.fragment_content.v.MainTradeContentFrag;
import com.xkj.trade.mvp.master.FragmentMaster;
import com.xkj.trade.mvp.master.FragmentMasterInfo;
import com.xkj.trade.mvp.operate.OperatePositionActivity;
import com.xkj.trade.utils.MoneyUtil;
import com.xkj.trade.utils.ToashUtil;
import com.xkj.trade.utils.view.CustomSwitch;
import com.xkj.trade.utils.view.CustomViewPager;
import com.xkj.trade.utils.view.PullBottomViewDragLayout;
import com.xkj.trade.utils.view.ZoomOutPageTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainTradeContentActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseFragment.BackInterface, MainTradeActListener.ViewListener {
    private PullBottomViewDragLayout mPullViewDragLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CustomViewPager mHeadViewPager;
    private List<String> mDataItem;
    private Context context;
    private ViewPager mViewPagerFrag;
    private List<Fragment> mFragmentList;
    private CustomSwitch mCSSwitch;
    private BaseFragment mBaseFragment;
    private DrawerLayout drawer;
    private Activity activity;
    public static float descHeight;
    public static float flIndicatorHeight;
    public LinearLayout llNetworkPrompt;
    private FrameLayout mFrameLayout;
    private MainTradeActListener.PreListener mPreListener;
    private TextView mOpenPl;
    private TextView mBalance;
    private TextView mEquity;
    private TextView mMargin;
    private TextView mMarginLevel;
    private TextView mCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initRegister() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {

        context = this;
        activity = this;
        mDataItem = new ArrayList<>();
        mDataItem.add("我关注的操盘手");
        mDataItem.add("我复制的操盘手");
        mDataItem.add("持仓仓位");
        mDataItem.add("挂单");
        mDataItem.add("平仓仓位");
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new FragmentMasterWatch());
        mFragmentList.add(new FragmentMasterCopy());
        mFragmentList.add(new FragmentOpenPosition());
        mFragmentList.add(new FragmentPendingPosition());
        mFragmentList.add(new FragmentClosePosition());
        mPreListener = new MainActPreImpl(this, context, handler);
    }


    @Override
    public void initView() {
        Log.i(TAG, "initView: ");
        setContentView(R.layout.activity_main);
        mPreListener.sendUserInfo();
        llNetworkPrompt = (LinearLayout) findViewById(R.id.ll_net_exception_prompt);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mCSSwitch = (CustomSwitch) findViewById(R.id.cs_switch_tag);
        mFrameLayout = (FrameLayout) findViewById(R.id.fl_main_trade_content);
        mCSSwitch.setSelectedChangeListener(new CustomSwitch.SelectedChangedListener() {
            @Override
            public void SelectChange(Boolean select) {
                if (select) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fl_main_trade_content, new FragmentMaster(), "1");
                    fragmentTransaction.addToBackStack("TAG");
                    fragmentTransaction.commit();
                } else {
                    fragmentManager.popBackStack("TAG", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        });
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, OperatePositionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).putExtra(OperatePositionActivity.OPERATEACTION, OperatePositionActivity.OperateAction.ADD));
//                TransitionsHeleper.startActivity(activity, new Intent(context, OperatePositionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).putExtra(OperatePositionActivity.OPERATEACTION, OperatePositionActivity.OperateAction.ADD), findViewById(R.id.fab));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main_trade_content, new MainTradeContentFrag(), "1");
        fragmentTransaction.commit();

        mPullViewDragLayout = (PullBottomViewDragLayout) findViewById(R.id.dragLayout);
        mPullViewDragLayout.getChildAt(0).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //将fragment固定在mPullViewDragLayout上面
                mPullViewDragLayout.getChildAt(0).getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                int initHight = mPullViewDragLayout.getMeasuredHeight() - mPullViewDragLayout.getChildAt(0).getWidth() / 2;
//                Log.i(TAG, "onGlobalLayout: "+initHight);
//                mPullViewDragLayout.setPadding(0,initHight,0,0);
                findViewById(R.id.s_temp).setLayoutParams(new LinearLayout.LayoutParams(1, mPullViewDragLayout.getChildAt(0).getHeight() / 2));
                descHeight = findViewById(R.id.desc).getHeight();
                flIndicatorHeight = findViewById(R.id.fl_indicator_item).getHeight();
            }
        });


        mViewPagerFrag = (ViewPager) findViewById(R.id.vp_indicator_content);
        mViewPagerFrag.setAdapter(new FragmentAdapter(fragmentManager, mFragmentList));
        mViewPagerFrag.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mHeadViewPager.setCurrentItem(position, true);
            }
        });

        mHeadViewPager = (CustomViewPager) findViewById(R.id.cvp_indicator_item);
        mHeadViewPager.setAdapter(new MyViewPagerAdapterItem(context, mDataItem));
        mHeadViewPager.setOffscreenPageLimit(mDataItem.size());
        mHeadViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mHeadViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int mPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                if (mHeadViewPager.getSpeed() < -1800) {
                    mHeadViewPager.setCurrentItem(mPosition + 1);
                    mHeadViewPager.setSpeed(0);
                    mViewPagerFrag.setCurrentItem(mPosition + 1);
                } else if (mHeadViewPager.getSpeed() > 1800 && mPosition > 0) {
                    //当手指右滑速度大于2000时viewpager左滑（注意item-1即可）
                    mHeadViewPager.setCurrentItem(mPosition - 1);
                    mHeadViewPager.setSpeed(0);
                    mViewPagerFrag.setCurrentItem(mPosition - 1);
                }
                Log.i(TAG, "onPageSelected: mPosition" + position);
                mViewPagerFrag.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPullViewDragLayout = (PullBottomViewDragLayout) findViewById(R.id.dragLayout);
        mViewPagerFrag = (ViewPager) findViewById(R.id.vp_indicator_content);
        mHeadViewPager = (CustomViewPager) findViewById(R.id.cvp_indicator_item);

        mEquity = (TextView) findViewById(R.id.tv_pull_equity);
        mCredit = (TextView) findViewById(R.id.tv_pull_credit);
        mMargin = (TextView) findViewById(R.id.tv_pull_margin);
        mMarginLevel = (TextView) findViewById(R.id.tv_pull_margin_level);
        mBalance = (TextView) findViewById(R.id.tv_pull_balance);
        mOpenPl = (TextView) findViewById(R.id.tv_pull_open_p_l);

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed: ");

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }
        ToashUtil.showShort(this, "功能暂未开放");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.mBaseFragment = selectedFragment;
    }

    /**
     * 展示高手的个人信息
     */
    private FragmentMasterInfo mFragmentMasterInfo;

    @Subscribe
    public void getShowMasterInfo(BeanMasterInfo mBeanMasterInfo) {
        Log.i(TAG, "getShowMasterInfo: 展示高手个人信息");
        fragmentManager.beginTransaction().add(R.id.fl_main_trade_content, mFragmentMasterInfo = new FragmentMasterInfo()).addToBackStack("tag").commit();
    }

    //退出时的时间
    private long mExitTime;

    //    对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: " + MyApplication.getInstance().getListSize());
        //双击退出
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                //判断fragment是否消费返回键.
            } else if (mBaseFragment != null && mBaseFragment.onBackPressed()) {
            } else {
                exit();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            MyApplication.getInstance().exit();
        }
    }

    @Override
    protected void hideNetWorkPrompt() {
        super.hideNetWorkPrompt();
        if (llNetworkPrompt != null && llNetworkPrompt.getVisibility() != View.GONE) {
            llNetworkPrompt.setVisibility(View.GONE);
        }
    }

    @Override
    protected void showNetWorkPrompt() {
        super.showNetWorkPrompt();
        if (llNetworkPrompt != null && llNetworkPrompt.getVisibility() != View.VISIBLE) {
            llNetworkPrompt.setVisibility(View.VISIBLE);
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case refreshUserInfo:
                    BeanUserListInfo.BeanUserList.BeanUserInfo beanUserInfo = beanUserListInfo.getData().getList().get(0);
                    mEquity.setText(MoneyUtil.moneyFormat(beanUserInfo.getEquity()));
                    mBalance.setText(MoneyUtil.moneyFormat(beanUserInfo.getBalance()));
                    mCredit.setText(MoneyUtil.moneyFormat(beanUserInfo.getCredit()));
                    mMargin.setText(MoneyUtil.moneyFormat(beanUserInfo.getMargin()));
                    mOpenPl.setText(MoneyUtil.moneyFormat(String.valueOf(MoneyUtil.subPrice(beanUserInfo.getEquity(), MoneyUtil.addPrice(beanUserInfo.getBalance(), beanUserInfo.getCredit())))));
                    try {
                        if (!beanUserInfo.getMargin().equals("0"))
                            mMarginLevel.setText(MoneyUtil.moneyFormat(String.valueOf(MoneyUtil.div(beanUserInfo.getBalance(), beanUserInfo.getMargin()))));
                        else {
                            mMarginLevel.setText("0.0");
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private final int refreshUserInfo = 0;
    private BeanUserListInfo beanUserListInfo;

    //刷新顶部
    @Override
    public void refreshUserInfo(final BeanUserListInfo beanUserListInfo) {
        Log.i(TAG, "refreshUserInfo: ");
        this.beanUserListInfo = beanUserListInfo;
        handler.sendEmptyMessage(refreshUserInfo);
    }
}
