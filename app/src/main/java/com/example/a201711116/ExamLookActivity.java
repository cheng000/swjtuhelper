package com.example.a201711116;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ExamLookActivity extends AppCompatActivity {

    private static String Text;
    private TextView examText;

//    public static void actionStart(Context context, String TEXT)
//    {
//        Text = TEXT;
//        System.out.println("TEXT = " + TEXT);
//        Text = Text.replace('{',' ');
//        Text = Text.replace('}',' ');
//        Text = Text.substring(3);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_look);

        examText = findViewById(R.id.exam_text);
        Intent tmpIntent = getIntent();
        if (tmpIntent.getStringExtra("TEXT") != null)
            Text = tmpIntent.getStringExtra("TEXT");
        else if (tmpIntent.getStringExtra("MY_TEXT") != null) {
            System.out.println("yes");
            Text = tmpIntent.getStringExtra("MY_TEXT");
        }

        System.out.println("my_text = " + Text);
        examText.setText(Text);
    }
}
