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
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText userName,pwdEdt;
    private Button login;
    private ProgressBar loadingPB;
    private TextView RegisterTV;
    private FirebaseAuth myAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName=findViewById(R.id.idEdtUserName);
        pwdEdt = findViewById(R.id.idEdtPassword);
        login=findViewById(R.id.idBtnLogin);
        loadingPB = findViewById(R.id.idPBLoading);
        RegisterTV=findViewById(R.id.idTVRegister);
        //Getting instance from firebase
        myAuth=FirebaseAuth.getInstance();

        RegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                String uname = userName.getText().toString();
                String password = pwdEdt.getText().toString();
                //Toast.makeText(LoginActivity.this, uname+" "+password, Toast.LENGTH_SHORT).show();
                if(TextUtils.isEmpty(uname) && TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Please Enter Your Credentials...", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    //to Sign in with account
//                    if( myAuth.isSignInWithEmailLink(uname)){
//                        Toast.makeText(LoginActivity.this, "Not a user", Toast.LENGTH_SHORT).show();
//                    }
                    password="User"+uname+password;
                    myAuth.signInWithEmailAndPassword(uname,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(LoginActivity.this, "Yes a user", Toast.LENGTH_SHORT).show();
//                            }
                            if(task.isComplete() && task.isSuccessful() ){
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Logged in Successfully !..", Toast.LENGTH_SHORT).show();
                                ArrayList<String> al = new ArrayList<>(Arrays.asList(uname.split("@")));
                                String name=al.get(0);
                                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                i.putExtra("user",name);
                                startActivity(i);
                                // so that if user press bach it will not go to login page again
                                finish();

                            }else{
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Fail to login", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });



    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = myAuth.getCurrentUser();
        //String UserName = user.getDisplayName();
        if(user!=null){
            String UserName = user.getEmail();
            ArrayList<String> al = new ArrayList<>(Arrays.asList(UserName.split("@")));
            UserName=al.get(0);
            //Toast.makeText(LoginActivity.this, "Username log :"+UserName, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            i.putExtra("user",UserName);
            startActivity(i);

            this.finish();
        }
    }
}