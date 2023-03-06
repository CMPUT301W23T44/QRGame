package com.example.qrgame;
//made by ZhengPang

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SignUp extends AppCompatActivity {
    private Button Finish;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Button Finish = findViewById(R.id.Finish_button);

        Intent sign_page = new Intent(this, MainPageActivity.class);
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                startActivity(sign_page);
                finish();


            }
        });
    }
}