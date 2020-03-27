package com.lendeasy.lendeasy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class EditDialog extends Dialog {

    String key;
    Context context;
    FirebaseFirestore db;
    EditText edit;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    Map<String, Object> data = new HashMap<>();

    public EditDialog(@NonNull Context context,String key) {
        super(context);
        this.context=context;
        this.key=key;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editdialog);

        edit=findViewById(R.id.edit);
        db=FirebaseFirestore.getInstance();

        Button ok,cancel;
        ok=findViewById(R.id.ok);
        cancel=findViewById(R.id.cancel);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String editstr=edit.getText().toString();
                Double detail= Double.parseDouble(editstr);
                if(!editstr.isEmpty()) {
                    data.put(key, detail);

                    db.collection("users").document(user.getUid())
                            .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,"Successfully Updated!!!",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,"Update Failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dismiss();
                }
                else
                    Toast.makeText(context,"Enter Valid Value",Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
