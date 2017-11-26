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


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                String url = "http://www.cheng1996.cn";
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
   */
  /*
  //声明地址
  private Button btn;
    private ImageView img;
    private String url = "http://jiaowu.swjtu.edu.cn/servlet/GetRandomNumberToJPEG";
    private EditText name,pass,yan;
    private TextView resultView;
    private Button lg;

    private String result = "";

    //http://jiaowu.swjtu.edu.cn/servlet/UserLoginSQLAction

    //在消息队列中实现对控件的更改

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(this);
        resultView = (TextView)findViewById(R.id.re);
        img = (ImageView) findViewById(R.id.code_view);

        lg = (Button) findViewById(R.id.login);
        lg.setOnClickListener(this);
    }

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
            }).start();
        }

        if(v.getId() == R.id.login){
            send();
        }
    }

    private void send() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://www.baidu.com");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    //DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    //out.writeBytes("username=2015111897&password=3631036");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    //读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    show(response.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(reader != null){
                        try {
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }


        }).start();
    }

    private void show(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultView.setText(s);
            }
        });
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
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    */
    // 声明控件对象
    private EditText et_name, et_pass;
    // 声明显示返回数据库的控件对象
    private TextView tv_result;
    private Button btn,btn_login,btn_clear;
    private ImageView img;
    private String url = "http://jiaowu.swjtu.edu.cn/servlet/GetRandomNumberToJPEG";
    private EditText yan;
    String responseCookie;

    @Override protected void onCreate(Bundle savedInstanceState)
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
            }).start();
        }
        if(v.getId() == R.id.login){
            login();
            Toast.makeText(this,"cookie = "+responseCookie,Toast.LENGTH_LONG).show();
        }
        if(v.getId() == R.id.btn_clear){
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
                            public void run() { loginByPost(userName, userPass,yanzheng);
                            // 调用loginByPost方法
                                };
                                }.start();
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
                                        urlConnection.setDoInput(true);
                                        urlConnection.setDoOutput(true);
                                        urlConnection.setInstanceFollowRedirects(true);
                                        urlConnection.setRequestProperty("Content-Type","text/html;charset=GB2312");
                                        String JSESSIONID = responseCookie;
                                        urlConnection.setRequestProperty("Cookie", JSESSIONID);// 给服务器送登录后的cookie
                                        urlConnection.setRequestProperty("user_id", URLEncoder.encode(userName, "UTF-8"));
                                        urlConnection.setRequestProperty("password", URLEncoder.encode(userPass, "UTF-8"));
                                        urlConnection.setRequestProperty("ranstring", URLEncoder.encode(yanzheng,"UTF-8"));
                                        Log.i("MainActivity",userName);
                                        Log.i("MainActivity",userPass);
                                        Log.i("MainActivity",yanzheng);

                                        // 设置请求的超时时间
                                        urlConnection.setReadTimeout(5000);
                                        urlConnection.setConnectTimeout(5000);

                                        // 传递的数据
                                        String data = "&user_id=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + URLEncoder.encode(userPass, "UTF-8")+"&ranstring="+URLEncoder.encode(yanzheng,"UTF-8")+"&user_type=student&btn1=";

                                        /*
                                        // 设置请求的头
                                        urlConnection.setRequestProperty("Connection", "keep-alive");
                                        // 设置请求的头
                                        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                        // 设置请求的头
                                        urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                                        // 设置请求的头
                                        urlConnection .setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
                                        */
                                        urlConnection.setDoOutput(true);
                                        // 发送POST请求必须设置允许输出
                                        urlConnection.setDoInput(true);
                                        // 发送POST请求必须设置允许输入
                                        // setDoInput的默认值就是true
                                        // 获取输出流
                                        OutputStream os = urlConnection.getOutputStream();
                                        os.write(data.getBytes());
                                        os.flush();
                                        if (urlConnection.getResponseCode() == 200)
                                        {
                                            // 获取响应的输入流对象
                                            InputStream is = urlConnection.getInputStream();
                                            // 创建字节输出流对象
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            // 定义读取的长度
                                            int len = 0;
                                            // 定义缓冲区
                                            byte buffer[] = new byte[1024];
                                            // 按照缓冲区的大小，循环读取
                                            while ((len = is.read(buffer)) != -1)
                                            {
                                                // 根据读取的长度写入到os对象中
                                                baos.write(buffer, 0, len); }
                                                // 释放资源
                                            is.close(); baos.close();
                                            // 返回字符串
                                            final String result = new String(baos.toByteArray(),"gb2312");
                                            // 通过runOnUiThread方法进行修改主线程的控件内容
                                            MainActivity.this.runOnUiThread(new Runnable() { @Override public void run()
                                            {
                                                // 在这里把返回的数据写在控件上 会出现什么情况尼

                                                tv_result.setText(result); } }); }
                                                else { System.out.println("链接失败.........");
                                        }
                                    } catch (Exception e)
                                    { e.printStackTrace();
                                    }
                                }
        }



