package com.github.detb.trekkie;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.detb.trekkie.db.SignInViewModel;
import com.github.detb.trekkie.ui.home.RecentFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterUserActivity extends AppCompatActivity{
    private RegisterUserViewModel viewModel;
    private FirebaseAuth mAuth;
    private EditText signUpMail, signUpPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RegisterUserViewModel.class);
        setContentView(R.layout.register_activity);
        mAuth = FirebaseAuth.getInstance();

        signUpMail = findViewById(R.id.EmailAddress);
        signUpPass = findViewById(R.id.TextPassword);

    }

    public void createUser(View v){
        String email = signUpMail.getText().toString();
        String pass = signUpPass.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please enter your E-mail address",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(getApplicationContext(),"Please enter your Password",Toast.LENGTH_LONG).show();
        }
        if (pass.length() == 0){
            Toast.makeText(getApplicationContext(),"Please enter your Password",Toast.LENGTH_LONG).show();
        }
        if (pass.length()<8){
            Toast.makeText(getApplicationContext(),"Password must be more than 8 digit",Toast.LENGTH_LONG).show();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(RegisterUserActivity.this, task -> {

                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterUserActivity.this, "ERROR",Toast.LENGTH_LONG).show();
                        }
                        else {
                            startActivity(new Intent(RegisterUserActivity.this, SignInActivity.class));
                            finish();
                        }
                    });}
    }
}
