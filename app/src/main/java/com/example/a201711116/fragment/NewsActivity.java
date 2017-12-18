package com.example.a201711116.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;

public class NewsActivity extends Fragment {
    String target = "http://dean.swjtu.edu.cn/rss.jsp";
    Document doc;
    private TextView mText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_news,null);
        mText = view.findViewById(R.id.news_item);
        new Thread(getNews).start();
        return view;
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Document doc = (Document)msg.obj;
                    Elements link = doc.select("link");
                    Elements title = doc.select("title");
                    for(int i=1;i<link.size();i++)
                    {
                        mText.append(Html.fromHtml("<a href='" + link.get(i).html() + "'>"
                        + title.get(i).html() + "</a>"));
                        mText.append("\n\n");
                    }
                    break;
            }
        };
    };

    Runnable getNews = new Runnable() {
        @Override
        public void run() {
            try
            {
                doc = Jsoup.connect(target).get();
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
