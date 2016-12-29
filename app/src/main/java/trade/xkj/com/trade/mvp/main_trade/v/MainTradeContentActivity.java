package trade.xkj.com.trade.mvp.main_trade.v;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.ToashUtil;
import trade.xkj.com.trade.Utils.view.CustomViewPager;
import trade.xkj.com.trade.Utils.view.PullBottomViewDragLayout;
import trade.xkj.com.trade.Utils.view.ZoomOutPageTransformer;
import trade.xkj.com.trade.adapter.FragmentAdapter;
import trade.xkj.com.trade.adapter.MyViewPagerAdapterItem;
import trade.xkj.com.trade.base.BaseActivity;
import trade.xkj.com.trade.base.BaseFragment;
import trade.xkj.com.trade.base.MyApplication;
import trade.xkj.com.trade.bean.BeanMasterInfo;
import trade.xkj.com.trade.mvp.master.FragmentMasterInfo;
import trade.xkj.com.trade.mvp.operate.OperatePositionActivity;

public class MainTradeContentActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseFragment.BackInterface {
    private PullBottomViewDragLayout mPullViewDragLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CustomViewPager mHeadViewPager;
    private List<String> mDataItem;
    private Context context;
    private ViewPager mViewPagerFrag;
    private List<Fragment> mFragmentList;
    private ImageButton mIbSwitch;
    private BaseFragment mBaseFragment;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initRegister() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        context = this;
        mDataItem = new ArrayList<>();
        mDataItem.add("我关注的操盘手");
        mDataItem.add("我复制的操盘手");
        mDataItem.add("持仓仓位");
        mDataItem.add("挂单");
        mDataItem.add("平仓仓位");
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new Fragment1());
        mFragmentList.add(new Fragment2());
        mFragmentList.add(new FragmentOpenPosition());
        mFragmentList.add(new FragmentPendingPosition());
        mFragmentList.add(new FragmentClosePosition());

    }


    @Override
    public void initView() {
        Log.i(TAG, "initView: ");
        setContentView(R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        mIbSwitch = (ImageButton) findViewById(R.id.ib_switch);
//        mIbSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fl_main_trade_content, new FragmentMaster(), "1");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, OperatePositionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).putExtra(OperatePositionActivity.OPERATEACTION, OperatePositionActivity.OperateAction.ADD));
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
                findViewById(R.id.s_temp).setLayoutParams(new LinearLayout.LayoutParams(1,mPullViewDragLayout.getChildAt(0).getHeight()/2));
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
        mHeadViewPager.setAdapter(new MyViewPagerAdapterItem(context,mDataItem));
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
}
