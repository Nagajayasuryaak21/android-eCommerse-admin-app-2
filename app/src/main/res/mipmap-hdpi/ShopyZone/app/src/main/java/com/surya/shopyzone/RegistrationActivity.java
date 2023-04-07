package com.surya.shopyzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText userName,pwdEdt,cnfPwdEdt;
    private Button registration;
    private ProgressBar loadingPB;
    private TextView loginTV;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        userName=findViewById(R.id.idEdtUserName);
        pwdEdt = findViewById(R.id.idEdtPassword);
        cnfPwdEdt=findViewById(R.id.idEdtCnfPassword);
        registration=findViewById(R.id.idBtnRegister);
        loadingPB = findViewById(R.id.idPBLoading);
        loginTV=findViewById(R.id.idTVLogin);
        //Getting instance from firebase
        myAuth=FirebaseAuth.getInstance();

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                String username = userName.getText().toString();
                String pwd = pwdEdt.getText().toString();
                String cnfPwd = cnfPwdEdt.getText().toString();
                if(!pwd.equals(cnfPwd)){
                    Toast.makeText(RegistrationActivity.this, "Check Your Password. Password Miss Match...", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(cnfPwd)){
                    Toast.makeText(RegistrationActivity.this, "Please add your credentials...", Toast.LENGTH_SHORT).show();
                }else{
                    // To create a Username and Passwoed in firebase
                    pwd="User"+username+pwd;
                    myAuth.createUserWithEmailAndPassword(username,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isComplete()){
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegistrationActivity.this, "User Registered...", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegistrationActivity.this , LoginActivity.class );
                                startActivity(i);
                                //to close current activity
                                finish();
                            }
                            else{
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegistrationActivity.this, "Fail To Register User..", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }
}