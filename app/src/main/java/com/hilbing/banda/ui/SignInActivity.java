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
import com.hilbing.banda.MainActivity;
import com.hilbing.banda.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.email_signin_ET)
    EditText emailET;
    @BindView(R.id.password_signin_ET)
    EditText passwordET;
    @BindView(R.id.signup_signin_BT)
    Button signIn;
    @BindView(R.id.have_account_signin_TV)
    TextView haveAccount;

    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(SignInActivity.this, getResources().getString(R.string.fields_are_empty), Toast.LENGTH_LONG).show();
                }
                else if (!(email.isEmpty() && passw.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, passw).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this, getResources().getString(R.string.sign_up_unsuccessful), Toast.LENGTH_LONG).show();

                            }
                            else {
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignInActivity.this, getResources().getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
                }

            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
