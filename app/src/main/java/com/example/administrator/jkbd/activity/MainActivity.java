package com.example.administrator.jkbd.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.jkbd.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void text(View view) {
      //  Toast.makeText(this,"dd",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(MainActivity.this,QuestionActivity.class);
        startActivity(intent);
    }

    public void btn_exit(View view) {
        finish();
    }
}
