package com.example.a201711116.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.a201711116.News;
import com.example.a201711116.R;

import java.util.List;

/**
 * 项目名：  201711116
 * 包名：   com.example.a201711116.adapter
 * 文件名： NewsAdapter
 * 创建者：  cheng
 * 创建时间： 2017/12/1921:58
 * 描述：    TODO
 * Created by cheng on 2017/12/19.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    private int resourceId;
    public NewsAdapter(Context context, int textViewResourceId, List<News>objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News news = getItem(position);
        View view;
        view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        viewHolder.newsName = view.findViewById(R.id.news_name);
        view.setTag(viewHolder);
        if (news.getredFlag())
        {
            viewHolder.newsName.setTextColor(Color.RED);
            viewHolder.newsName.setText(news.getTitle());
        }
        else
        {
            viewHolder.newsName.setText(news.getTitle());
        }
        return view;
    }

    class ViewHolder{
        TextView newsName;
    }
}
