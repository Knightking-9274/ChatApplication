package com.example.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapplication.databinding.ActivityVerifyOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukeshsolanki.OnOtpCompletionListener;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {
    ActivityVerifyOtpBinding verifyBinding;
    FirebaseAuth auth;

    String verificationId;

    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyBinding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(verifyBinding.getRoot());
        auth = FirebaseAuth.getInstance();
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Waiting for OTP!..");
        prgDialog.setCancelable(false);


        String data = getIntent().getStringExtra("PhoneNumber");
        verifyBinding.txtViewPhone.setText("Verify "+data);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(data).setTimeout(60L, TimeUnit.SECONDS).setActivity(VerifyOTP.this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyOTP.this,"Verification Failed"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationId = s;
                            }
                        }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        verifyBinding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {


            @Override
            public void onOtpCompleted(String otp) {
                if(verificationId!=null && !verificationId.isEmpty()){
                PhoneAuthCredential cred = PhoneAuthProvider.getCredential(verificationId,otp);

                auth.signInWithCredential(cred).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(VerifyOTP.this,SetupActivity.class);
                            startActivity(i);
                            finishAffinity();
                        } else {
                            Toast.makeText(VerifyOTP.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }


                });

            }else{
                    Toast.makeText(VerifyOTP.this,"Vrification ID is Empty!",Toast.LENGTH_SHORT).show();
            }

            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}