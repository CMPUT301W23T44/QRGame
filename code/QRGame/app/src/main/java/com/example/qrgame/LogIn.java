package com.example.qrgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LogIn extends AppCompatActivity {
    Button login_button;
    Button signup_button;
    TextView title_txt;
    TextView noAccount_txt;
    EditText username;
    EditText phone_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        title_txt = findViewById(R.id.login_title);
        username = findViewById(R.id.username);
        phone_number = findViewById(R.id.number);
        login_button = findViewById(R.id.login_button);
        noAccount_txt = findViewById(R.id.noAccount);
        signup_button = findViewById(R.id.signup_button);

        Intent sign_page = new Intent(this, SignUp.class);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(sign_page);
                finish();


            }
        });




    }
}