package com.hilbing.banda.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.hilbing.banda.MainActivity;
import com.hilbing.banda.R;

import java.util.Arrays;

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
    @BindView(R.id.login_facebook_BT)
    LoginButton facebookBT;
    @BindView(R.id.email_signin_TV)
    TextView emailTV;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mUser;

    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();



        if (mUser == null){

            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this);

            callbackManager = CallbackManager.Factory.create();
            facebookBT.setReadPermissions(Arrays.asList("email"));


        }
        else {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String passw = passwordET.getText().toString();
                if (email.isEmpty()) {
                    emailET.setError(getResources().getString(R.string.enter_email_address));
                    emailET.requestFocus();

                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailET.setError(getResources().getString(R.string.please_enter_a_valid_email));
                    emailET.requestFocus();
                }

                if (passw.length() < 6){
                    passwordET.setError(getResources().getString(R.string.minimum_length_of_password_should_be_6));
                    passwordET.requestFocus();
                    return;
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
                                if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.you_are_already_registered), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
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

    public void buttonClickLoginFacebook(View v){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.user_cancelled_it), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleFacebookToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser myUserObj = mFirebaseAuth.getCurrentUser();
                    updateUi(myUserObj);
                }
                else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.could_not_register_to_firebase), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void updateUi(FirebaseUser myUserObj) {
        emailTV.setText(myUserObj.getEmail());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }
}
