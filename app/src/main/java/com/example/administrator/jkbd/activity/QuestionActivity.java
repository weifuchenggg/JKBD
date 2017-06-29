package com.example.administrator.jkbd.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.jkbd.R;
import com.example.administrator.jkbd.bean.ExamInfo;
import com.example.administrator.jkbd.bean.Result;

public class QuestionActivity extends AppCompatActivity {

    int k=0;
    TextView textView,tv_a,tv_b,tv_c,tv_d,tv_question;
    ExamInfo examInfo;
    Result result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
        Intent intent=this.getIntent();
        examInfo=(ExamInfo) intent.getSerializableExtra("examInfo");
        result=(Result) intent.getSerializableExtra("result");
        Log.e("main",result.getReason());

        init();  //初始化
        update();//更新


    }
    private void update(){
        tv_a.setText("A."+result.getResult().get(k).getItem1());
        tv_b.setText("B."+result.getResult().get(k).getItem2());
        tv_c.setText("C."+result.getResult().get(k).getItem3());
        tv_d.setText("D."+result.getResult().get(k).getItem4());
        tv_question.setText((k+1)+"."+result.getResult().get(k).getQuestion());

    }
    private void init(){
        textView=(TextView) findViewById(R.id.tv_ExamInfo);
        textView.setText(examInfo.toString());
        tv_a=(TextView) findViewById(R.id.tv_a);
        tv_b=(TextView) findViewById(R.id.tv_b);
        tv_c=(TextView) findViewById(R.id.tv_c);
        tv_d=(TextView) findViewById(R.id.tv_d);
        tv_question=(TextView) findViewById(R.id.tv_question);
    }

    public void next(View view) {
        if(k<99){
            k++;
            update();
        }else{
            Toast.makeText(this,"已经是最后一题",Toast.LENGTH_LONG).show();
        }
    }

    public void prev(View view) {
        if(k!=0){
            k--;
            update();
        }else{
            Toast.makeText(this,"已经是第一题",Toast.LENGTH_LONG).show();
        }
    }
}
