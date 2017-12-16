package com.example.a201711116;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.a201711116.fragment.ExamActivity;
import com.example.a201711116.fragment.NewsActivity;
import com.example.a201711116.fragment.ScheduleActivity;

import java.util.ArrayList;
import java.util.List;

public class LoginedActivity extends AppCompatActivity implements View.OnClickListener{

    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager mViewPager;
    //标题
    private List<String>mTitle;
    //Fragment
    private List<Fragment>mFragment;

    //悬浮窗
    private FloatingActionButton fab_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logined);

        //去掉阴影

        initData();
        initView();
    }

    //初始化View
    private void initView() {
        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        fab_setting.setOnClickListener(this);

        /*
        //默认隐藏
        fab_setting.setVisibility(View.GONE);
        */

        mTabLayout = findViewById(R.id.mTabLayout);
        mViewPager = findViewById(R.id.mViewPager);
        //预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());
        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            //选中的item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //返回item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });

        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

    //初始化数据
    private void initData() {
        mTitle = new ArrayList<>();
        mTitle.add("教务新闻");
        mTitle.add("考试安排");
        mTitle.add("课表查看");

        mFragment = new ArrayList<>();
        mFragment.add(new NewsActivity());
        mFragment.add(new ExamActivity());
        mFragment.add(new ScheduleActivity());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}
