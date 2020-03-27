package com.lendeasy.lendeasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {

    EditText phone,otp;
    Button sendotp,verifyotp;
    String codeSent,phoneNumber;
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        phone=findViewById(R.id.phone);
        otp=findViewById(R.id.otp);

        sendotp=findViewById(R.id.sendotp);
        verifyotp=findViewById(R.id.verifyotp);

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });

        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifySignIn();
            }
        });
    }

    private void verifySignIn(){
        String code=otp.getText().toString();

        if(code.equals(codeSent)){
            Map<String, Object> data = new HashMap<>();
            data.put("PhoneNumber",phoneNumber);

            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            db.collection("users").document(user.getUid())
                    .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    SharedPreferences sharedPref=getSharedPreferences("Phone", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("PhoneNum", phoneNumber);
                    editor.apply();

                    Intent i = new Intent(PhoneAuth.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });

        }
    }

    private void sendVerificationCode(){
        phoneNumber=phone.getText().toString();

        if(phoneNumber.isEmpty()){
            Toast.makeText(this,"Invalid",Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d("TAG","No way");

            Map<String, Object> data = new HashMap<>();
            data.put("PhoneNumber",phoneNumber);

            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            db.collection("users").document(user.getUid())
                    .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    SharedPreferences sharedPref=getSharedPreferences("Phone", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("PhoneNum", phoneNumber);
                    editor.apply();

                    Intent i = new Intent(PhoneAuth.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d("TAG","Nahi");
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            Log.d("TAG","Gaya");
            codeSent=s;
        }
    };
}
