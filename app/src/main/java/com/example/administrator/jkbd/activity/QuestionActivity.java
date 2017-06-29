package com.example.administrator.jkbd.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.jkbd.R;
import com.example.administrator.jkbd.bean.ExamInfo;
import com.example.administrator.jkbd.bean.Result;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
        Intent intent=this.getIntent();
        ExamInfo examInfo=(ExamInfo) intent.getSerializableExtra("examInfo");
        Result result=(Result) intent.getSerializableExtra("result");
        Log.e("main",result.getReason());
        TextView textView=(TextView) findViewById(R.id.tv_ExamInfo);
        textView.setText(examInfo.toString());
    }
}
