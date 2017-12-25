package com.example.a201711116.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.a201711116.News;
import com.example.a201711116.NewsLookActivity;
import com.example.a201711116.R;
import com.example.a201711116.adapter.NewsAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.media.CamcorderProfile.get;

public class NewsActivity extends Fragment {
    String target = "http://dean.swjtu.edu.cn/rss.jsp";
    Document doc;
    ListView mList;

    Elements link;
    Elements title;


    private ArrayList<News> NewsData = new ArrayList<>();
    //private List<News>NewsList = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_news,null);
        findView(view);
        new Thread(getNews).start();



        return view;
    }

    //初始化View
    private void findView(View view) {
        mList = view.findViewById(R.id.news_item);

    }

    public Elements GetLink(){
        return link;
    }

    public Elements GetTitle(){
        return title;
    }



    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Document doc = (Document)msg.obj;
                    link = doc.select("link");
                    title = doc.select("title");
                    for(int i=1;i<link.size();i++)
                    {
                        String newsTitle = title.get(i).html();
                        String newsLink = link.get(i).html();
                        //News tmpNews = new News(title.get(i).html(), link.get(i).html());
                        if (title.get(i).html().startsWith("[color]"))
                        {
                            News tmpNews = new News(newsTitle.substring(7), newsLink);
                            tmpNews.setRedFlag(true);
                            NewsData.add(tmpNews);
                        }
                        else
                        {
                            News tmpNews = new News(newsTitle, newsLink);
                            tmpNews.setRedFlag(false);
                            NewsData.add(tmpNews);
                        }



                    }
                    NewsAdapter adapter = new NewsAdapter(getActivity(), R.layout.news_item, NewsData);
//                    SimpleAdapter adapter = new SimpleAdapter(getActivity(),NewsData,android.R.layout.simple_expandable_list_item_2,
//                            new String[]{"title"},new int[]{android.R.id.text1});


                    //NewsAdapter newsAdapter = new NewsAdapter(getActivity(),R.layout.news_item,NewsList);
                    mList.setAdapter(adapter);
                    //mList.setAdapter(newsAdapter);
                    mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            //News news = mList.get(position);
                            //String URL = news.getUrl();
                            System.out.println("******点击了第"+(position+1)+title.get(position+1).html()+"***"+link.get(position+1).html());
                            NewsLookActivity.actionStart(getActivity(),link.get(position+1).html());
                        }
                    });
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
