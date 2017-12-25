package com.example.a201711116;

/**
 * 项目名：  201711116
 * 包名：   com.example.a201711116
 * 文件名： News
 * 创建者：  cheng
 * 创建时间： 2017/12/1921:53
 * 描述：    TODO
 * Created by cheng on 2017/12/19.
 */

public class News
{
    private String title;
    private String url;
    private Boolean redFlag;
    public News(String title,String url)
    {
        this.title = title;
        this.url = url;
        redFlag = false;
    }
    public String getTitle(){
        return title;
    }
    public String getUrl()
    {
        return url;
    }
    public Boolean getredFlag()
    {
        return redFlag;
    }
    public void setRedFlag(Boolean newRedFlag)
    {
        redFlag = newRedFlag;
    }
}
