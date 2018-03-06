package com.six.xinyidai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.six.xinyidai.R;
import com.six.xinyidai.ui.adapter.MyViewPagerAdapter;
import com.six.xinyidai.ui.adapter.NoTouchViewPager;
import com.six.xinyidai.ui.fragment.HomeFragment;
import com.six.xinyidai.ui.fragment.RankFragment;
import com.six.xinyidai.ui.fragment.SearchFragment;
import com.six.xinyidai.util.SPUtils;
import com.umeng.analytics.MobclickAgent;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;

public class HomeActivity extends AppCompatActivity {
    public static NoTouchViewPager viewPager1;


    public static MyViewPagerAdapter pagerAdapter;
    @Bind(R.id.tab)
    PageBottomTabLayout tab;

    private NavigationController navigationController;


    public static void launch(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        StatusBarUtil.setColor(this,0,0);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.Blue));
        ButterKnife.bind(this);
        initView();
        //诸葛
        setZhuge();
    }


    private void setZhuge() {
        //定义用户识别码
        String userid = (String) SPUtils.get(this, "id", "");

        //定义用户属性
        JSONObject personObject = new JSONObject();

        try {
            personObject.put("avatar", "");
            personObject.put("name", "");
            personObject.put("gender", "");
            personObject.put("等级", 90);
            personObject.put("APP名称", "任我花");
            personObject.put("渠道", "信和");
            personObject.put("用户ID", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //标识用户
        ZhugeSDK.getInstance().identify(getApplicationContext(), userid,
                personObject);
    }


    private void initView() {
        viewPager1 = (NoTouchViewPager) findViewById(R.id.viewPager);
        navigationController = tab.custom()
                .addItem(newItem(R.mipmap.ic_home, R.mipmap.ic_home_1,"主页"))
                .addItem(newItem(R.mipmap.ic_rank, R.mipmap.ic_rank_1,"排行榜"))
                .addItem(newItem(R.mipmap.ic_search,R.mipmap.ic_search_1,"搜索"))
                .build();
        ArrayList<Fragment> list=new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new RankFragment());
        list.add(new SearchFragment());
        pagerAdapter=new MyViewPagerAdapter(getSupportFragmentManager(),list);
        viewPager1.setAdapter(pagerAdapter);
        //自动适配ViewPager页面切换
        navigationController.setupWithViewPager(viewPager1);

    }
    //创建一个Item
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text){
        NormalItemView normalItemView = new NormalItemView(this);
        normalItemView.initialize(drawable,checkedDrawable,text);
        normalItemView.setTextDefaultColor(Color.GRAY);
        normalItemView.setTextCheckedColor(0xFFF57C00);
        return normalItemView;
    }
    private long mLastBackTime = 0;
    @Override
    public void onBackPressed() {
        // finish while click back key 2 times during 1s.
        if ((System.currentTimeMillis() - mLastBackTime) < 1000) {
            finish();
        } else {
            mLastBackTime = System.currentTimeMillis();
            Toast.makeText(this, "请再确认一次", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        ZhugeSDK.getInstance().flush(getApplicationContext());
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
