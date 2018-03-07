package com.wuxiao.yourday;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wuxiao.yourday.base.BaseActivity;
import com.wuxiao.yourday.event.MainSetCurrentEvent;
import com.wuxiao.yourday.event.RefreshUserInfoEvent;
import com.wuxiao.yourday.ui.activity.SettingActivity;
import com.wuxiao.yourday.ui.adapter.FragmentViewPagerAdapter;
import com.wuxiao.yourday.ui.fragment.CategoryManagerFragment;
import com.wuxiao.yourday.ui.fragment.HistoryFragment;
import com.wuxiao.yourday.ui.fragment.StatementFragment;
import com.wuxiao.yourday.ui.fragment.TodayFragment;
import com.wuxiao.yourday.ui.fragment.UserCentreFragment;
import com.wuxiao.yourday.util.DoubleClickExit;
import com.wuxiao.yourday.view.CustomViewPager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by lihuabin on 2016/10/20.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View nVheader;
    private ImageView userIconImg;
    private TextView nickNameTv;
    private TextView mailTv;
    private CustomViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ArrayList<Integer> history = new ArrayList<>();

    public static int TODAY = 0;
    public static int HISTORY = 1;
    public static int STATEMENT = 2;
    public static int USER_CENTRE = 3;
    public static int CATEGORY_MANAGER = 4;
    public static void launch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void assignViews() {
        super.assignViews();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        nVheader = navigationView.getHeaderView(0);
        nickNameTv = (TextView) nVheader.findViewById(R.id.user_nick);
        mailTv = (TextView) nVheader.findViewById(R.id.user_mail);
        userIconImg = (ImageView) nVheader.findViewById(R.id.user_icon);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        eventBus.register(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        nVheader.setOnClickListener(this);
        fragments = new ArrayList<>();
        fragments.add(new TodayFragment());
        fragments.add(new HistoryFragment());
        fragments.add(new StatementFragment());
        fragments.add(new UserCentreFragment());
        fragments.add(new CategoryManagerFragment());
        viewPager.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setScanScroll(false);
        viewPager.setOffscreenPageLimit(fragments.size());
        updateUserInfo();
        if (mBundle != null) {
            int current = mBundle.getInt("current", TODAY);
            try {
                setCurrentItem(current);
            } catch (Exception e) {
                viewPager.setCurrentItem(current);
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void updateUserInfo() {

            userIconImg.setImageResource(R.mipmap.logo);
            // TODO: 2016/11/10 设置用户头像
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.nav_view_header:
                setCurrentItem(USER_CENTRE);
                navigationView.getMenu().findItem(R.id.nav_user_centre).setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (history.size() > 0) {
            setCurrentItem(history.get(history.size() - 1));
            history.remove(history.size() - 1);
        } else if (viewPager.getCurrentItem() != TODAY) {
            history.removeAll(history);
            setCurrentItem(TODAY);
            navigationView.getMenu().getItem(TODAY).setChecked(true);
        } else {
            if (!DoubleClickExit.check()) {
                Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        history.removeAll(history);//点击侧滑切换的不在返回
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_today) {
            setCurrentItem(TODAY);
        } else if (id == R.id.nav_history) {
            setCurrentItem(HISTORY);
        } else if (id == R.id.nav_statement) {
            setCurrentItem(STATEMENT);
        } else if (id == R.id.nav_user_centre) {
            setCurrentItem(USER_CENTRE);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(mContext, SettingActivity.class));
        } else if (id == R.id.nav_category) {
            setCurrentItem(CATEGORY_MANAGER);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        menu.findItem(R.id.go_today).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_today:
                HistoryFragment h = (HistoryFragment) (getSupportFragmentManager().getFragments().get(1));
                h.goToday();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCurrentItem(int currentItem) {
        if (currentItem > fragments.size()) {
            Log.e("mainAct", "error!! currentItem is " + currentItem + "fragments.size is" + fragments.size());
            return;
        }
        //history.add(currentItem);
        viewPager.setCurrentItem(currentItem, false);
        switch (currentItem) {
            case 0:
                toolbar.getMenu().findItem(R.id.go_today).setVisible(false);
                toolbar.setTitle(getString(R.string.app_name));

                break;
            case 1:
                toolbar.getMenu().findItem(R.id.go_today).setVisible(true);
                toolbar.setTitle(getString(R.string.history_fragment_title));
                break;
            case 2:
                toolbar.getMenu().findItem(R.id.go_today).setVisible(false);
                toolbar.setTitle(getString(R.string.statement_fragment_title));
                break;
            case 3:
                toolbar.getMenu().findItem(R.id.go_today).setVisible(false);
                toolbar.setTitle(getString(R.string.user_centre));
                break;
            case 4:
                toolbar.getMenu().findItem(R.id.go_today).setVisible(false);
                toolbar.setTitle(getString(R.string.category_manager_title));
                break;
            default:
                break;
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void onEventMainThread(RefreshUserInfoEvent event) {
        updateUserInfo();
    }

    public void onEventMainThread(MainSetCurrentEvent event) {
        int i = event.getCurrent();
        setCurrentItem(i);
        history.add(event.getOld());
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }
}
