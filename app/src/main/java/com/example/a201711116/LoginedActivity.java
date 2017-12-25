package com.example.a201711116;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.a201711116.fragment.ExamActivity;
import com.example.a201711116.fragment.NewsActivity;
import com.example.a201711116.fragment.ScheduleActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //学号
    private String userName;
    //Cookies
    private String Cookies;

    private Exam tmpExam;

    private Boolean ischeck;


    //声明一个SharedPreferences对象和一个Editor对象
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logined);

        //得到学号、Cookies
        Intent tmpIntent = getIntent();
        userName = tmpIntent.getStringExtra("userName");
        Cookies = tmpIntent.getStringExtra("Cookies");
        //去掉阴影

        initData();
        initView();

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        new Thread(getExam).start();



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
        ExamActivity tmp = new ExamActivity();
        ScheduleActivity stmp = new ScheduleActivity();
        Bundle bundle = new Bundle();
        bundle.putString("Cookies", Cookies);
        tmp.setArguments(bundle);
        stmp.setArguments(bundle);
        mFragment.add(new NewsActivity());
        mFragment.add(tmp);
        mFragment.add(stmp);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_setting:
                Intent tmpIntent = new Intent(LoginedActivity.this, SettingActivity.class);
                tmpIntent.putExtra("userName", userName);
                tmpIntent.putExtra("Cookies", Cookies);
                startActivity(tmpIntent);
                break;
        }
    }


    //确认退出
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出吗？")
                .setIcon(R.drawable.exit)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        LoginedActivity.this.finish();

                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    sendExamNotice();
                    break;
            }
        }
    };

    private Runnable getExam = new Runnable() {
        @Override
        public void run()
        {
            try
            {
                ischeck = pref.getBoolean("ischecked", false);
                String target = "http://jiaowu.swjtu.edu.cn/student/test/MyTestThisTerm.jsp";
                org.jsoup.Connection con = Jsoup.connect(target);
                con.cookie("JSESSIONID", Cookies);
                org.jsoup.nodes.Document doc = con.get();
                Elements cl = doc.select("tr");
                Elements test = cl.select("td");
                String []data = test.get(10).text().split(" ");
                boolean notFinish = true;
                for (int i = 32, index = 1; i < data.length && notFinish; index++)
                {
                    tmpExam = new Exam("","","","","","",
                            "","","","");
                    tmpExam.setID(data[i++]);
                    tmpExam.setScheduleNumber(data[i++]);
                    tmpExam.setClassID(data[i++]);

                    String className = "";
                    String engName = "", chineseName = "";
                    Pattern pattern = Pattern.compile("^\\d+$");
                    Pattern engPattern = Pattern.compile("[a-zA-Z]+");
                    while (i < data.length)
                    {
                        Matcher isNum = pattern.matcher(data[i]);
                        Matcher isEng = engPattern.matcher(data[i]);
                        if (!isNum.matches())
                        {
                            if (!isEng.matches())
                            {

                                chineseName += data[i] + " ";
                            }
                            else
                            {
                                engName += data[i] + " ";
                            }
                            className += data[i++] + " ";
                        }
                        else
                        {
                            tmpExam.setClassName(className);
                            tmpExam.setChineseName(chineseName);
                            tmpExam.setEnglishName(engName);
                            tmpExam.setClassNum(data[i++]);

                            break;
                        }
                    }
                    tmpExam.setCredit(data[i++]); tmpExam.setTeacher(data[i++]);

                    tmpExam.setExamTime(data[i++]); tmpExam.setExamPlace(data[i++]);
                    String examTeacher = "";
                    while (i < data.length && !(data[i].equals("" + (index + 1)))) examTeacher += data[i++] + " ";
                    tmpExam.setExamTeacher(examTeacher);

                    String examTime = tmpExam.getExamTime();
                    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
                    Date curDate = formatter.parse(formatter.format(new Date(System.currentTimeMillis())));
                    Date examT = formatter.parse(examTime.substring(0, 10));
                    System.out.println("examTime = " + examTime.substring(0, 10));
                    if (curDate.getTime() <= examT.getTime())
                    {
                        notFinish = false;
                        System.out.println("index = " + index);
                    }
                }
                Message msg = new Message();
                msg.what = 0;
                handle.sendMessage(msg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    public void sendExamNotice()
    {
        if (ischeck)
        {
            System.out.println("yes!!! ischecked!!!");
            Intent tmpintent = new Intent(this, ExamLookActivity.class);
            System.out.println(this.toString());
            tmpintent.putExtra("MY_TEXT", tmpExam.getSMore());
            PendingIntent pi = PendingIntent.getActivity(this, 0, tmpintent, PendingIntent.FLAG_UPDATE_CURRENT);
            System.out.println("send TEXT = " + tmpExam.getSMore());

            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder myBuilder = new NotificationCompat.Builder(this);
            myBuilder.setContentTitle("最近考试提醒|" + tmpExam.getChineseName());
            myBuilder.setContentText(tmpExam.getExamTime() + " " + tmpExam.getExamTime());
            myBuilder.setWhen(System.currentTimeMillis());
            myBuilder.setSmallIcon(R.mipmap.ic_launcher);
            myBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
            myBuilder.setContentIntent(pi);
            myBuilder.setAutoCancel(true);
            myBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(tmpExam.getS()));
            Notification notification = myBuilder.build();
            manager.notify(0, notification);
        }
    }


}
