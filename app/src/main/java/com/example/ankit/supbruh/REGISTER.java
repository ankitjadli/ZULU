package com.example.ankit.supbruh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class REGISTER extends AppCompatActivity {

    Button register ;
    EditText email;
    EditText pass;
    Button login ;
    FirebaseAuth mauth;
   // LottieAnimationView lottieAnimationView;
    private ProgressDialog mprogressdialog;

     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
       // lottieAnimationView.playAnimation();
        register = (Button) findViewById(R.id.newuserbttn);
        email=(EditText)findViewById(R.id.loginemail);
        pass=(EditText)findViewById(R.id.loginpassowrd);
        login=(Button)findViewById(R.id.loginbttn);
        mauth=FirebaseAuth.getInstance();
        mprogressdialog=new ProgressDialog(this);
        mprogressdialog.setTitle("Logging In");
        mprogressdialog.setMessage("Please wait while we log you in");
        mprogressdialog.setCanceledOnTouchOutside(false);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregister = new Intent(REGISTER.this,Login.class);
                startActivity(gotoregister);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passcode = pass.getText().toString();
                final String emaill=email.getText().toString();
                mprogressdialog.show();

                if(!TextUtils.isEmpty(passcode)&&!TextUtils.isEmpty(emaill))
                {
                    mauth.signInWithEmailAndPassword(emaill,passcode).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                FirebaseUser crnt;
                                String currentuser;
                                String user_token;
                                user_token = FirebaseInstanceId.getInstance().getToken();
                                crnt = FirebaseAuth.getInstance().getCurrentUser();
                                currentuser = crnt.getUid().toString();
                                DatabaseReference mdatabse;
                                mdatabse =FirebaseDatabase.getInstance().getReference().child("Users");
                                    mdatabse.child(currentuser).child("device_token").setValue(user_token);
                                mprogressdialog.dismiss();
                                Intent loginsucess = new Intent(REGISTER.this,newusersetup.class);
                                startActivity(loginsucess);
                                finish();

                            }
                            else
                            {
                                mprogressdialog.hide();
                                String error = task.getException().getMessage();
                                Toast.makeText(REGISTER.this,""+error,Toast.LENGTH_LONG).show();
                            }
                        }

                    });


                }
                else
                {
                    if (TextUtils.isEmpty(passcode))
                    {
                        mprogressdialog.hide();
                        Toast.makeText(REGISTER.this,"Enter Password",Toast.LENGTH_LONG).show();
                    }
                    else {
                        mprogressdialog.hide();
                        Toast.makeText(REGISTER.this,"Enter Email",Toast.LENGTH_LONG).show();
                    }
                }



            }
        });

    }

}
