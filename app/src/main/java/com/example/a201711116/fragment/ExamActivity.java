package com.example.a201711116.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.a201711116.Exam;
import com.example.a201711116.ExamLookActivity;
import com.example.a201711116.NewsLookActivity;
import com.example.a201711116.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExamActivity extends Fragment {

    private String Cookies;
    ListView mList;
    private ArrayList<Exam> examList;
    private ArrayList<Map<String, Object>> examData = new ArrayList<Map<String, Object>>();
    private ArrayList<Map<String, Object>> examDataMore = new ArrayList<Map<String, Object>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_exam,null);
        findView(view);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            Cookies = bundle.getString("Cookies");
        }
        examList = new ArrayList<>();
        new Thread(getExam).start();
        return  view;
    }

    //初始化View
    private void findView(View view) {
        mList = view.findViewById(R.id.exam_item);
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startExamAct();

                    break;
            }
        };
    };

    public void startExamAct()
    {
        for (Exam tmp : examList)
        {
            Map<String,Object> item = new HashMap<String,Object>();
            item.put("title",tmp.getS());
            examData.add(item);
        }

        for (Exam tmp : examList)
        {
            Map<String,Object> item = new HashMap<String,Object>();
            item.put("",tmp.getSMore());
            examDataMore.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity(),examData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{"title"},new int[]{android.R.id.text1});
        mList.setAdapter(adapter);

        Intent tmpIntent = new Intent(getActivity().getApplicationContext(), ExamLookActivity.class);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Map<String, Object> tmpMap = (Map<String, Object>)examDataMore.get(position);
                tmpIntent.putExtra("TEXT", (String)tmpMap.get(""));
                getActivity().startActivity(tmpIntent);
            }
        } );
    }

    private Runnable getExam = new Runnable() {
        @Override
        public void run()
        {
            try
            {
                String target = "http://jiaowu.swjtu.edu.cn/student/test/MyTestThisTerm.jsp";
                org.jsoup.Connection con = Jsoup.connect(target);
                con.cookie("JSESSIONID", Cookies);
                org.jsoup.nodes.Document doc = con.get();
                Elements cl = doc.select("tr");
                Elements test = cl.select("td");
                String []data = test.get(10).text().split(" ");
                for (int i = 32, index = 1; i < data.length; index++)
                {
                    Exam tmp = new Exam("","","","","","",
                            "","","","");
                    tmp.setID(data[i++]);
                    tmp.setScheduleNumber(data[i++]);
                    tmp.setClassID(data[i++]);

                    String className = "";
                    String engName = "", chineseName = "";
                    Pattern pattern = Pattern.compile("^\\d+$");
                    Pattern engPattern = Pattern.compile("[a-zA-Z]+");
                    while (i < data.length)
                    {
                        Matcher isNum = pattern.matcher(data[i]);
                        Matcher isEng = engPattern.matcher(data[i]);
                        if (!isNum.matches())
                        {
                            if (!isEng.matches())
                            {

                                chineseName += data[i] + " ";
                            }
                            else
                            {
                                engName += data[i] + " ";
                            }
                            className += data[i++] + " ";
                        }
                        else
                        {
                            tmp.setClassName(className);
                            tmp.setChineseName(chineseName);
                            tmp.setEnglishName(engName);
                            tmp.setClassNum(data[i++]);

                            break;
                        }
                    }
                    tmp.setCredit(data[i++]); tmp.setTeacher(data[i++]);

                    tmp.setExamTime(data[i++]); tmp.setExamPlace(data[i++]);
                    String examTeacher = "";
                    while (i < data.length && !(data[i].equals("" + (index + 1)))) examTeacher += data[i++] + " ";
                    tmp.setExamTeacher(examTeacher);
                    examList.add(tmp);
                    //System.out.println(tmp.getS());
                }
                Message msg = new Message();
                msg.what = 0;
                msg.obj = doc;
                handle.sendMessage(msg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };
}