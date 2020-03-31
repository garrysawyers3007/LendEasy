package com.lendeasy.lendeasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class BorrowActivity extends AppCompatActivity {

    EditText title,description,time;
    Button submit;
    Map<String, Object> data;
    Uri uri;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        Intent intent=getIntent();
        uri=(Uri)intent.getExtras().get("Image");

        title=findViewById(R.id.title);
        description=findViewById(R.id.description);
        time=findViewById(R.id.time);

        submit=findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
    }

    private void sendData(){
        if(title.getText().toString().isEmpty()){
            Toast.makeText(this,"Enter Title",Toast.LENGTH_SHORT).show();
            return;
        }
        if(time.getText().toString().isEmpty()){
            Toast.makeText(this,"Enter Time",Toast.LENGTH_SHORT).show();
            return;
        }

        storageReference= FirebaseStorage.getInstance().getReference().child("Images");
        final String path=mAuth.getCurrentUser().getUid()+""+uri.getLastPathSegment();

        data = new HashMap<>();

        data.put("Title",title.getText().toString());
        data.put("Type","Item");
        data.put("Time",time.getText().toString());
        data.put("Description",description.getText().toString().isEmpty()?"":description.getText().toString());
        data.put("Image",path);
        data.put("Borrower",mAuth.getCurrentUser().getDisplayName());
        data.put("Lender","");
        data.put("isDone",-1);
        data.put("Timestamp", FieldValue.serverTimestamp());
        data.put("LenderId",mAuth.getCurrentUser().getUid());
        data.put("BorrowerId","");

        final ProgressDialog progressDialog=new ProgressDialog(BorrowActivity.this,R.style.Theme_AppCompat_NoActionBar);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();

        db.collection("borrow").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                data.put("TransactionId",documentReference.getId());
                db.collection("users").document(mAuth.getCurrentUser().getUid())
                        .set(data, SetOptions.merge());

                storageReference.child(path).putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                progressDialog.dismiss();
                                Toast.makeText(BorrowActivity.this,"Upload Successful!!",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(BorrowActivity.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BorrowActivity.this,"Upload Failed!!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }
}
