package com.example.administrator.jkbd.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.jkbd.R;
import com.example.administrator.jkbd.bean.ExamInfo;
import com.example.administrator.jkbd.bean.Result;

public class QuestionActivity extends AppCompatActivity {

    int k=0,score=0;
    TextView textView,tv_a,tv_b,tv_c,tv_d,tv_question,tv_score;
    RadioButton rb_c,rb_d;
    ImageView iv_p1;
    RadioGroup rg;
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
        tv_score.setText("当前分数:"+score);
        Glide.with(this).load(result.getResult().get(k).getUrl()).fitCenter().into(iv_p1);

        tv_a.setText("A."+result.getResult().get(k).getItem1());
        tv_b.setText("B."+result.getResult().get(k).getItem2());
        if(result.getResult().get(k).getItem3()==null || result.getResult().get(k).getItem3().equals("")){
            tv_c.setVisibility(View.GONE);
            tv_d.setVisibility(View.GONE);
            rb_c.setVisibility(View.INVISIBLE);
            rb_d.setVisibility(View.INVISIBLE);
        }else{
            tv_c.setVisibility(View.VISIBLE);
            tv_d.setVisibility(View.VISIBLE);
            rb_c.setVisibility(View.VISIBLE);
            rb_d.setVisibility(View.VISIBLE);
            tv_c.setText("C."+result.getResult().get(k).getItem3());
            tv_d.setText("D."+result.getResult().get(k).getItem4());
        }
        rg.clearCheck();
        tv_question.setText((k+1)+"."+result.getResult().get(k).getQuestion());

    }
    private void init(){
        textView=(TextView) findViewById(R.id.tv_ExamInfo);
        textView.setText(examInfo.toString());
        tv_a=(TextView) findViewById(R.id.tv_a);
        tv_b=(TextView) findViewById(R.id.tv_b);
        tv_c=(TextView) findViewById(R.id.tv_c);
        tv_d=(TextView) findViewById(R.id.tv_d);
        rb_c=(RadioButton)findViewById(R.id.rb_c);
        rb_d=(RadioButton)findViewById(R.id.rb_d);
        tv_score=(TextView) findViewById(R.id.tv_score);
        rg=(RadioGroup)findViewById(R.id.rg);
        iv_p1=(ImageView)findViewById(R.id.iv_p1);
        tv_question=(TextView) findViewById(R.id.tv_question);
    }

    public void next(View view) {
        int i;
        if(k<=99 && result.getResult().get(k).getStatus()==0){
            if(rg.getCheckedRadioButtonId()!=-1){
                String ans=result.getResult().get(k).getAnswer();
                if(ans.equals("1")) ans="A"; else if(ans.equals("2")) ans="B";else if(ans.equals("3")) ans="C";else ans="D";
                String p=((TextView)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                result.getResult().get(k).setStatus(rg.getCheckedRadioButtonId());
                 if( ans.equals(p)){
                    score++;
                }else{
                    Toast.makeText(this, "答案错误\n"+result.getResult().get(k).getExplains(),Toast.LENGTH_SHORT).show();
                    return ;
                }
            }
            else{
                Toast.makeText(this,"答案未选择",Toast.LENGTH_SHORT).show();
            }
        }
        if(k==99){
            Toast.makeText(this,"答题完成,请交卷",Toast.LENGTH_LONG).show();
            return ;
        }
        k++;
        update();
        if ( result.getResult().get(k).getStatus()!=0)
            rg.check(result.getResult().get(k).getStatus());
    }

    public void prev(View view) {

        if(result.getResult().get(k).getStatus()==0){
            if(rg.getCheckedRadioButtonId()!=-1){
                result.getResult().get(k).setStatus(rg.getCheckedRadioButtonId());
                String ans=result.getResult().get(k).getAnswer();
                if(ans.equals("1")) ans="A"; else if(ans.equals("2")) ans="B";else if(ans.equals("3")) ans="C";else ans="D";
                String p=((TextView)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                if( ans.equals(p)){
                    score++;
                }else{
                    Toast.makeText(this, "答案错误\n"+result.getResult().get(k).getExplains(),Toast.LENGTH_SHORT).show();
                    return ;
                }
            }
            else{
                Toast.makeText(this,"答案未选择",Toast.LENGTH_SHORT).show();
            }
        }
        if(k==0){
            Toast.makeText(this,"已经是第一题",Toast.LENGTH_SHORT).show();
            return ;
        }
        k--;
        update();
        if ( result.getResult().get(k).getStatus()!=0)
            rg.check(result.getResult().get(k).getStatus());
    }

    public void commit(View view) {
        Toast.makeText(this,"考试结束:\n成绩："+score,Toast.LENGTH_LONG).show();
        CountDownTimer countDownTimer=new CountDownTimer(3000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                finish();
            }
        };
        countDownTimer.start();
    }
}
