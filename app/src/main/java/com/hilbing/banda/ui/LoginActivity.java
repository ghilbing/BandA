package com.hilbing.banda.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.hilbing.banda.ForgotActivity;
import com.hilbing.banda.MainActivity;
import com.hilbing.banda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_login_ET)
    EditText emailET;
    @BindView(R.id.password_login_ET)
    EditText passwordET;
    @BindView(R.id.signin_login_BT)
    Button sighInBT;
    @BindView(R.id.not_registered_loginin_TV)
    TextView notRegisteredTV;
    @BindView(R.id.forgot_login_TV)
    TextView forgotTV;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser != null){
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.you_are_logged_in), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {

                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_login),Toast.LENGTH_LONG).show();
                }
            }
        };

        sighInBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String passw = passwordET.getText().toString();
                if (email.isEmpty()) {
                    emailET.setError(getResources().getString(R.string.enter_email_address));
                    emailET.requestFocus();
                }
                else if (passw.isEmpty()) {
                    passwordET.setError(getResources().getString(R.string.prompt_password));
                    passwordET.requestFocus();
                }
                else if (email.isEmpty() && passw.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.fields_are_empty), Toast.LENGTH_LONG).show();
                }
                else if (!(email.isEmpty() && passw.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, passw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                            }
                            else {
                                Intent intToHome = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
                }

            }
        });

        notRegisteredTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
               startActivity(intent);

            }
        });

        forgotTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
       // mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
