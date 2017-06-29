package com.example.administrator.jkbd.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.jkbd.R;
import com.example.administrator.jkbd.Utils.OkHttpUtils;
import com.example.administrator.jkbd.bean.ExamInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void text(View view) {
      //  Toast.makeText(this,"dd",Toast.LENGTH_LONG).show();
        OkHttpUtils<ExamInfo>  utils=new OkHttpUtils<>(getApplicationContext());
        String url="http://101.251.196.90:8080/JztkServer/examInfo";

        utils.url(url).targetClass(ExamInfo.class).execute(new OkHttpUtils.OnCompleteListener<ExamInfo>(){
            @Override
            public void onSuccess(ExamInfo result) {
                Log.e("main","success:"+result);
                final ExamInfo examInfo;
                examInfo=result;
                Intent intent=new Intent(MainActivity.this,QuestionActivity.class);
                intent.putExtra("examInfo",examInfo);
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                Log.e("main","error");
            }
        });

    }

    public void btn_exit(View view) {
        finish();
    }

    public void all_question(View view) {
        Intent intent=new Intent(MainActivity.this,AllQuestionActivity.class);
        startActivity(intent);
    }

    public void setting(View view) {
        Intent intent=new Intent(MainActivity.this,Setting.class);
        startActivity(intent);
    }
}
