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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import trade.xkj.com.trade.R;
import trade.xkj.com.trade.Utils.ToashUtil;
import trade.xkj.com.trade.Utils.view.CustomViewPager;
import trade.xkj.com.trade.Utils.view.PullBottomViewDragLayout;
import trade.xkj.com.trade.Utils.view.ZoomOutPageTransformer;
import trade.xkj.com.trade.adapter.FragmentAdapter;
import trade.xkj.com.trade.base.BaseActivity;
import trade.xkj.com.trade.mvp.operate.OperatePositionActivity;

public class MainTradeContentActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private PullBottomViewDragLayout mPullViewDragLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CustomViewPager mHeadViewPager;
    private List<String> mDataItem;
    private Context context;
    private ViewPager mViewPagerFrag;
    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initRegister() {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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
        mViewPagerFrag = (ViewPager) findViewById(R.id.vp_indicator_content);
        mViewPagerFrag.setAdapter(new FragmentAdapter(fragmentManager, mFragmentList));
        mViewPagerFrag.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                mHeadViewPager.setCurrentItem(position,true);
            }
        });

        mHeadViewPager = (CustomViewPager) findViewById(R.id.cvp_indicator_item);
        mHeadViewPager.setAdapter(new ViewpagerAdapterItem());
        mHeadViewPager.setOffscreenPageLimit(mDataItem.size());
        mHeadViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mHeadViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int mPosition=0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPosition=position;
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
                Log.i(TAG, "onPageSelected: mPosition"+position);
                    mViewPagerFrag.setCurrentItem(position,true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

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

    public class ViewpagerAdapterItem extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String s = mDataItem.get(position);
            View inflate = LayoutInflater.from(context).inflate(R.layout.item_viewpager, null);
            TextView textView = (TextView) inflate.findViewById(R.id.tv_item_name);
            textView.setText(s);
            container.addView(inflate);
            return inflate;
        }

        @Override
        public int getCount() {
            return mDataItem.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


}
