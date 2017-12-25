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
import android.support.v4.app.NotificationCompat;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a201711116.fragment.NewsActivity;
import com.example.a201711116.notice.NoticeNewsActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView img;
    private TextView mInfo;
    private CalendarView calendarView;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private int isCheck1 = -1;
    private boolean ischeck2;
    String userName;
    String Cookies;

    Elements title;
    Elements url;
    Elements pubDate;

    Document doc;
    Document docabout;
    String target = "http://dean.swjtu.edu.cn/rss.jsp";


    String targetContext;

    String help;
    private  Button about;

    //声明一个SharedPreferences对象和一个Editor对象
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_setting);

        img = findViewById(R.id.myPhoto);
        mInfo = findViewById(R.id.myInfo);
        calendarView = findViewById(R.id.calendarview);
        about = findViewById(R.id.about);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Toast.makeText(SettingActivity.this,
                        year+"年"+(month+1)+"月"+dayOfMonth+"日", Toast.LENGTH_SHORT).show();
                String date = year+"年"+(month+1)+"月"+dayOfMonth+"日";
                Intent intent = new Intent(SettingActivity.this,NotebookActivity.class);
                intent.putExtra("DATE",date);
                startActivity(intent);

            }
        });

        Intent tmpIntent = getIntent();
        userName = tmpIntent.getStringExtra("userName");
        Cookies = tmpIntent.getStringExtra("Cookies");

        new Thread(getStudentPhoto).start();
        new Thread(getInfo).start();
        new Thread(getNews).start();
        new Thread(getHelp).start();
//        new Thread(getExam).start();

        checkBox1 = findViewById(R.id.send_notice_checkbox);
        Button sendNotice = findViewById(R.id.send_notice);
        checkBox2 = findViewById(R.id.send_exam_notice_checkbox);
        Button sendExamNotice = findViewById(R.id.send_exam_notice);
        sendNotice.setOnClickListener(this);
        sendExamNotice.setOnClickListener(this);
        about = findViewById(R.id.about);
        about.setOnClickListener(this);

        ischeck2 = pref.getBoolean("ischecked", false);
        checkBox2.setChecked(ischeck2);
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.send_notice:
                isCheck1 = isCheck1*-1;
                if(isCheck1 == 1)
                {

                    Intent intent =new Intent(this, NoticeNewsActivity.class);
                    intent.putExtra("URL",url.get(1).html());
                    intent.putExtra("TITLE",title.get(1).html());
                    PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);

                    NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    NotificationCompat.Builder myBuilder = new NotificationCompat.Builder(this);
                    myBuilder.setContentTitle("最近新闻通知|"+pubDate.get(1).html());
                    myBuilder.setContentText(title.get(1).html());
                    myBuilder.setWhen(System.currentTimeMillis());
                    myBuilder.setSmallIcon(R.mipmap.ic_launcher);
                    myBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
                    myBuilder.setContentIntent(pi);
                    myBuilder.setAutoCancel(true);
                    myBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(title.get(1).html()));
                    Notification notification = myBuilder.build();
                    manager.notify(1, notification);
                    checkBox1.setChecked(true);
                }
                else
                {
                    checkBox1.setChecked(false);
                }
                break;
            case R.id.about:

                new AlertDialog.Builder(this).setTitle(help)
                        .setIcon(R.drawable.aboutme)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“确认”后的操作
                            }
                        })
                        .show();
                break;
            case R.id.send_exam_notice:
                editor = pref.edit();
                ischeck2 = !ischeck2;
                if(this.ischeck2 == true)
                {
                    editor.putBoolean("ischecked", true);
                    checkBox2.setChecked(true);
                    editor.apply();
                }
                else
                {
                    checkBox2.setChecked(false);
                    editor.putBoolean("ischecked", false);
                    editor.apply();
                }
                break;

        }
    }





    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bmp = (Bitmap)msg.obj;
                    img.setImageBitmap(bmp);
                    break;
                case 1:
                    Elements td = (Elements) msg.obj;
                    mInfo.append("姓名: ");
                    mInfo.append(td.get(20).html().substring(6));
                    mInfo.append("\n");
                    mInfo.append("学院: ");
                    mInfo.append(td.get(31).html().substring(6));
                    mInfo.append("\n");
                    mInfo.append("年级: ");
                    mInfo.append(td.get(33).html().substring(6));
                    mInfo.append("\n");
                    mInfo.append("班级: ");
                    mInfo.append(td.get(37).html().substring(6));
                    mInfo.append("\n");
                    mInfo.append("学号: ");
                    mInfo.append(td.get(18).html().substring(6));
                    mInfo.append("\n");
                    break;
                case 2:
                    Document doc = (Document)msg.obj;
                    url = doc.select("link");
                    title = doc.select("title");
                    pubDate = doc.select("pubDate");
                    targetContext = url.get(1).html();
                case 3:
                    Element h = (Element)msg.obj;
                    help = h.html().toString();

                    break;
            }
        };
    };



    Runnable getStudentPhoto = new Runnable() {
        @Override
        public void run() {
            try
            {
                // 请求的地址
                String spec = "http://jiaowu.swjtu.edu.cn/servlet/StudentPhotoView?UserName="+userName;
                // 根据地址创建URL对象
                URL url = new URL(spec);
                // 根据URL对象打开链接
                HttpURLConnection urlConnection = (HttpURLConnection) url .openConnection();
                // 设置请求的方式
                urlConnection.setRequestMethod("GET");
                // 可以读入数据
                urlConnection.setDoInput(true);
                // 可以写数据
                urlConnection.setDoOutput(true);
                // 自动重定向
                urlConnection.setInstanceFollowRedirects(true);

                urlConnection.setRequestProperty("Cookie", "JSESSIONID=" + Cookies);

                // 设置请求的超时时间
                urlConnection.setReadTimeout(5000);
                urlConnection.setConnectTimeout(5000);

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    System.out.println("Content-Type = " + urlConnection.getHeaderField("Content-Type"));
                    System.out.println("Transfer-Encoding = " + urlConnection.getHeaderField("Transfer-Encoding"));
                    InputStream is = urlConnection.getInputStream();
                    byte[] buffer = new byte[10000];
                    int len = 0;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    while ((len = is.read(buffer)) != -1)
                    {
                        bos.write(buffer, 0, len);
                    }
                    byte[] data = bos.toByteArray();
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    bos.close();
                    is.close();
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = bmp;
                    handle.sendMessage(msg);
                }
                else {
                    System.out.println("链接失败.........");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    Runnable getInfo = new Runnable() {
        @Override
        public void run() {
            try
            {
                Document doc;
                Connection con = Jsoup.connect("http://jiaowu.swjtu.edu.cn/servlet/StudentInfoMapAction?MapID=101&PageUrl=../student/student/student.jsp");
                con.cookie("JSESSIONID", Cookies);
                doc = con.get();
                Elements td = doc.select("td");
                Message msg = new Message();
                msg.what = 1;
                msg.obj = td;
                handle.sendMessage(msg);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    };

    Runnable getNews = new Runnable() {
        @Override
        public void run() {
            try
            {
                doc = Jsoup.connect(target).get();
                Message msg = new Message();
                msg.what = 2;
                msg.obj = doc;
                handle.sendMessage(msg);
            }
            catch(MalformedURLException e)
            {
                System.out.println(e);
            }
            catch(IOException e)
            {
                System.out.println(e);
            }
        }
    };

    Runnable getHelp = new Runnable() {
        @Override
        public void run() {
            try
            {
                String target = "http://www.hjliao.cn/gethelp.php";
                Document doc;

                Connection con = Jsoup.connect(target);
                doc = con.post();
                Element result = doc.select("body").first();

                Message msg = new Message();
                msg.what = 3;
                msg.obj = result;
                handle.sendMessage(msg);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    };



}
