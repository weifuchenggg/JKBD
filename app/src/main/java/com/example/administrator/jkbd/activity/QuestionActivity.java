package com.example.administrator.jkbd.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.jkbd.R;
import com.example.administrator.jkbd.bean.ExamInfo;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
        Intent intent= new Intent();
        ExamInfo examInfo=(ExamInfo) intent.getSerializableExtra("examInfo");
        Log.e("main",examInfo+" 11111111111");
    }
}
