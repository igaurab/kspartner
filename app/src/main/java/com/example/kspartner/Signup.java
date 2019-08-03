package com.example.kspartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {

    //Widgets
    private TextView goToLoginActivity;
    private Button signup;
    private TextInputEditText Uemail, Upassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initializeUI();
        mAuth = FirebaseAuth.getInstance();
    }

    public void registerNewUser() {
        String email, password;
        email = Uemail.getText().toString();
        password = Upassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();

        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
        }
       else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent_step_1 = new Intent(Signup.this, MainActivity.class);
                                startActivity(intent_step_1);
                            } else {
                                Toast.makeText(getApplicationContext(), "Registration Failed!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }
    public void initializeUI() {
        //Initialize the widgets
        goToLoginActivity = findViewById(R.id.go_to_login);
        signup = findViewById(R.id.btn_signup_signupActivity);
        Uemail = findViewById(R.id.emailId_signup);
        Upassword =  findViewById(R.id.password_signup);
        goToLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_login = new Intent(Signup.this, Login.class);
                startActivity(intent_login);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });
    }
}
