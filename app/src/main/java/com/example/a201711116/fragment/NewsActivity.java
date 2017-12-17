package com.example.a201711116.fragment;




import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.io.IOException;


public class NewsActivity extends Fragment {

    String target = "http://dean.swjtu.edu.cn/rss.jsp";
    String result;
    Document doc;
    private TextView mText;



    //初始化View
    private void findView(View view) {

        mText = view.findViewById(R.id.password);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_news,null);
        findView(view);
        getNews();
        return  view;
    }

    //显示新闻
    public void getNews(){
        String ss = null;
        try
        {
            doc = Jsoup.connect(target).get();
            System.out.println(doc.html());

//            Elements link = doc.select("link");//取得所有的url
//            Elements title = doc.select("title");
//            for(int i=1;i<link.size();i++)
//            {
//                ss = title.get(i).html().toString();
//                mText.append(ss);
//                System.out.println(ss);
//            }
//            System.out.println(link.get(0).html());
//            System.out.println(title.get(0));
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


}
