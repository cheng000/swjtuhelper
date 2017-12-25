package com.example.a201711116.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.a201711116.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;

public class ScheduleActivity extends Fragment {
    String target = "http://jiaowu.swjtu.edu.cn/student/course/printCourse.jsp";
    Document doc;
    WebView classTable;
    String Cookies;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_schedule,null);
        classTable = view.findViewById(R.id.classTable);
        Bundle bundle = getArguments();
        Cookies = bundle.getString("Cookies");
        new Thread(getClassTable).start();
        return  view;
    }
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Document doc = (Document)msg.obj;
                    Element cl = doc.select("div [id=pageBodyRight]").get(0);
                    cl.select("*").removeAttr("bgcolor");
                    cl.select("*").removeAttr("background");
//                    cl.select("tr class=\"xx\"").remove();
                    classTable.getSettings().setDefaultTextEncodingName("utf-8");
                    String s = cl.html().toString();
                    s = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/></head><body background = \"http://www.hjliao.cn/wp-content/uploads/2017/12/classback.png\">"+s+"</body></html>";
//                    System.out.println(s);
                    WebSettings webSettings = classTable.getSettings();

                    webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                    webSettings.setUseWideViewPort(true);//设置webview推荐使用的窗口
                    webSettings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
                    webSettings.setDisplayZoomControls(false);//隐藏webview缩放按钮
                    webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
                    webSettings.setAllowFileAccess(true); // 允许访问文件
                    webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
                    webSettings.setSupportZoom(true); // 支持缩放

                    classTable.loadDataWithBaseURL(null,s, "text/html", "utf-8",null);
                    break;
            }
        };
    };

    Runnable getClassTable = new Runnable() {
        @Override
        public void run() {
            try
            {
                Connection con = Jsoup.connect(target);
                con.cookie("JSESSIONID", Cookies);
                doc = con.get();

                Message msg = new Message();
                msg.what = 0;
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
}
