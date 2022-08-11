package com.a404nofund.cs446.cs446;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.a404nofund.cs446.cs446.dataType.User;
import com.a404nofund.cs446.cs446.utils.FireBaseUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/* It starts google authentation, for comment purposes*/
public class RegisterActivity extends ShareBaseActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;



    String position;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.google_button).setOnClickListener(this);
        position = getIntent().getStringExtra("Position");



    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.google_button:
                showProgressDialog();
                signIn();
        }
    }

    private void signIn(){
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent,RC_SIGN_IN);

    }





    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == RC_SIGN_IN){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(result.isSuccess()){
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    dissmissProgressDialog();
                }
            } else {
                dissmissProgressDialog();
            }
        } else {
            dissmissProgressDialog();
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account){
       String x = account.getIdToken();
       String y = account.getId();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User();
                            String photoUrl = null;
                            if(account.getPhotoUrl() != null){
                                user.setPhoto(account.getPhotoUrl().toString());
                            }
                            user.setEmail(account.getEmail());
                            user.setUser(account.getDisplayName());
                            user.setUid(mAuth.getCurrentUser().getUid());
                            FireBaseUtils.getUserRef(account.getEmail().replace(".",","),position)
                                    .setValue(user, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            mFirebaseUser = mAuth.getCurrentUser();
                                            Intent returnIntent = new Intent();
                                            setResult(Activity.RESULT_OK,returnIntent);
                                            finish();
                                        }
                                    });
                        }
                        else {
                            dissmissProgressDialog();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_FIRST_USER,returnIntent);
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        signOut();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
    }

    @Override
    public void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop(){
        super.onStop();
        mGoogleApiClient.disconnect();
    }
}
