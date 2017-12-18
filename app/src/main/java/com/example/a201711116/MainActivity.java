package com.example.a201711116;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // 声明控件对象
    private EditText et_name, et_pass;
    // 声明显示返回数据库的控件对象
    private TextView tv_result;
    private Button btn,btn_login,btn_clear;
    private ImageView img;
    private String url = "http://jiaowu.swjtu.edu.cn/servlet/GetRandomNumberToJPEG";
    private EditText yan;
    String responseCookie;
    //登录成功标志
    private int Success = -2;

    private static HttpClient httpclient = new DefaultHttpClient();// 创建一个客户端实体
   // public static UserLoginInfo userLoginInfo = new UserLoginInfo();// 创建一个用户实体
    private static String urlCourse = "http://***.***.***.***/student/course/MyCourseThisTerm.jsp";// 课程信息URL
    private String CourseHTML = null;// 保存获得的课程表网页HTML文件的String类型

    @Override
    protected void onCreate(Bundle savedInstanceState)
    { super.onCreate(savedInstanceState);
        // 设置显示的视图
        setContentView(R.layout.activity_main);
        // 通过 findViewById(id)方法获取用户名的控件对象
        et_name = (EditText) findViewById(R.id.count);
        // 通过 findViewById(id)方法获取用户密码的控件对象
        et_pass = (EditText) findViewById(R.id.password);
        // 通过 findViewById(id)方法获取显示返回数据的控件对象
        yan = (EditText) findViewById(R.id.yan);
        tv_result = (TextView) findViewById(R.id.re);
        btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.login);
        btn_login.setOnClickListener(this);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.code_view);

    }


    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    Bitmap bmp=(Bitmap)msg.obj;
                    img.setImageBitmap(bmp);
                    break;
            }
        };
    };



    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.button1) {
            //新建线程加载图片信息，发送到消息队列中
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Bitmap bmp = getURLimage(url);
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = bmp;
                    System.out.println("000");
                    handle.sendMessage(msg);
                }
            } ).start();
        }
        if (v.getId() == R.id.login) {
            login();
            Toast.makeText(this,"cookie = "+responseCookie,Toast.LENGTH_LONG).show();
        }
        if (v.getId() == R.id.btn_clear) {
            tv_result.setText(null);
            Toast.makeText(this,"已清空",Toast.LENGTH_SHORT).show();
        }
    }

    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            String Cookie = conn.getHeaderField("Set-Cookie");// 取到所用的Cookie
            Log.i("MainActivity",Cookie);
            responseCookie = Cookie.substring(11,43);
            Log.i("MainActivity",responseCookie);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    /** * 通过android:onClick="login"指定的方法 ， 要求这个方法中接受你点击控件对象的参数v * * @param v */
    public void login() {
        // 登陆事件的处理

        // 获取用户名
        final String userName = et_name.getText().toString();
        // 获取用户密码
        final String userPass = et_pass.getText().toString();
        final String yanzheng = yan.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPass)||TextUtils.isEmpty(yanzheng))
        { Toast.makeText(this, "用户名或者密码或验证码不能为空", Toast.LENGTH_LONG).show(); }
        else {
            // 开启子线程
            new Thread() {
                public void run() {
                    // 调用loginByPost方法
                    loginByPost(userName, userPass,yanzheng);
                    loginByGet(userName, userPass, yanzheng);
                    if(Success == 0){
                        startActivity(new Intent(MainActivity.this,LoginedActivity.class));
                    }
                };
            }.start();
        }

    }

    public void loginByGet(String userName, String userPass, String yanzheng)
    {
        //发送GET指令到教务，否则无法访问主页面
        try
        {
            String spec = "http://jiaowu.swjtu.edu.cn/servlet/UserLoginCheckInfoAction";
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

            String JSESSIONID = responseCookie;
            urlConnection.setRequestProperty("Host", "jiaowu.swjtu.edu.cn");
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            urlConnection.setRequestProperty("User-Agent", "201711116/1.0");
            urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            urlConnection.setRequestProperty("Referer", "http://jiaowu.swjtu.edu.cn/servlet/UserLoginSQLAction");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            urlConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            //教务设置了4个cookies
            urlConnection.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID);
            urlConnection.setRequestProperty("Cookie", "user_id=" + userName);
            urlConnection.setRequestProperty("Cookie", "user_type=student");
            urlConnection.setRequestProperty("Cookie", "user_style=modern");
            urlConnection.setRequestProperty("Cookie", "language=cn");

            // 设置请求的超时时间
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);

            //教务会回复，让你跳转到主页面
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP)
            {
                //教务会回复一个跳转，跳转到主页面。也就是说只要有这个cookie就可以直接进主页面。

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

    /** * POST请求操作 * * @param userName * @param userPass */
    public void loginByPost(String userName, String userPass,String yanzheng) {
        try {
            // 请求的地址
            String spec = "http://jiaowu.swjtu.edu.cn/servlet/UserLoginSQLAction";
            // 根据地址创建URL对象
            URL url = new URL(spec);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url .openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("POST");
            // 可以读入数据
            urlConnection.setDoInput(true);
            // 可以写数据
            urlConnection.setDoOutput(true);
            // 自动重定向
            urlConnection.setInstanceFollowRedirects(true);


            // 设置各种http头
            String JSESSIONID = responseCookie;
            urlConnection.setRequestProperty("Host", "jiaowu.swjtu.edu.cn");
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Cache-Control", "max-age=0");
            urlConnection.setRequestProperty("Origin", "http://jiaowu.swjtu.edu.cn");
            urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            urlConnection.setRequestProperty("User-Agent", "201711116/1.0");
            urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            urlConnection.setRequestProperty("Referer", "http://jiaowu.swjtu.edu.cn/service/login.jsp?user_type=student");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            urlConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            urlConnection.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // 设置请求的超时时间
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);

            // 传递的数据
            String data =
                    "url=" + "http://jiaowu.swjtu.edu.cn/servlet/UserLoginCheckInfoAction" +
                            "&OperatingSystem=" + "" +
                            "&Browser=" + "" +
                            "&user_id=" + URLEncoder.encode(userName, "UTF-8") +
                            "&password=" + URLEncoder.encode(userPass, "UTF-8") +
                            "&ranstring=" + URLEncoder.encode(yanzheng,"UTF-8") +
                            "&user_type=" + "student" +
                            "&btn1=" + ""
                    ;

            // 设置各种http头
            urlConnection.setRequestProperty("Content-Length", String.valueOf(data.length()));
            urlConnection.setRequestProperty("File Data", String.valueOf(data.length()));

            urlConnection.setDoOutput(true);
            // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true);
            // 发送POST请求必须设置允许输入
            // setDoInput的默认值就是true
            // 获取输出流
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes());
            os.flush();

            // 判断响应
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream in = urlConnection.getInputStream();
                // gzip解压
                GZIPInputStream gis = new GZIPInputStream(in);
                int count;
                byte data_rev[] = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((count = gis.read(data_rev, 0, 1024)) != -1) {
                    baos.write(data_rev, 0, count);
                }
                final String result = new String(baos.toByteArray(),"gb2312");
                // 通过runOnUiThread方法进行修改主线程的控件内容
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override public void run() {
                        // 在这里把返回的数据返回在登录页面下方的TextView中
                        tv_result.setText(result);
                        int isSuccess = result.indexOf("登录成功");
                        if(isSuccess != -1){
                            Success = 0;
                            Toast.makeText(MainActivity.this,"检测到登录成功字段",Toast.LENGTH_SHORT).show();
                        }else {
                            Success = -3;
                            Toast.makeText(MainActivity.this,"登录失败！",Toast.LENGTH_SHORT).show();
                            et_pass.setText(null);
                            yan.setText(null);
                        }
                    }
                } );
            }
            else {
                System.out.println("链接失败.........");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



