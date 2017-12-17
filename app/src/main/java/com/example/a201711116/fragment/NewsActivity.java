package com.example.a201711116.fragment;




import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a201711116.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;

import java.io.IOException;


public class NewsActivity extends Fragment {


    private TextView mText;
    Elements link;
    Elements title;


    //初始化View
    private void findView(View view) {

        mText = view.findViewById(R.id.news_item);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_news,null);
        findView(view);
        getNews();

        return  view;
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Document doc = (Document)msg.obj;
                    link = doc.select("link");//取得所有的url
                    title = doc.select("title");
                    System.out.println("+++++++++++++++");
                    for(int i=1;i<title.size();i++)
                    {
                        System.out.println(title.get(i).html());
                        mText.append(Html.fromHtml("<a href='" + link.get(i).html() + "'>" + title.get(i).html() + "</a>"));
                        mText.append("\n\n");
                        //mText.append(i+" "+title.get(i).html()+"\n");
                        //mText.append(link.get(i).html() + "\n");
                    }
                    break;
            }
        };
    };

    public void getNews() {

        //创建线程，启动联网
        new Thread(new Runnable() {
            public void run() {
                try {
                    //连接网址
                    Document doc = Jsoup.connect("http://dean.swjtu.edu.cn/rss.jsp").get();
                    System.out.println("Start");


                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = doc;
                    handle.sendMessage(msg);

                }
                catch(MalformedURLException e)
                {
                    System.out.println(e);
                }

                 catch (IOException e)
                 {
                        e.printStackTrace();
                 }



        }}).start();
    }



}
