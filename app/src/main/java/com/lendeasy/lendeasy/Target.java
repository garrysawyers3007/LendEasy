package com.lendeasy.lendeasy;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Target extends Fragment {
    ImageView tgtedit,blcedit,timedit;
    TextView target,balance,time;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    public Target() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_target, container, false);

        tgtedit=view.findViewById(R.id.tgtedit);
        blcedit=view.findViewById(R.id.blcedit);
        timedit=view.findViewById(R.id.timeedit);
        target=view.findViewById(R.id.target);
        balance=view.findViewById(R.id.balance);

        tgtedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDialog editDialog=new EditDialog(getContext(),"Target");
                editDialog.show();
            }
        });

        blcedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDialog editDialog=new EditDialog(getContext(),"Balance");
                editDialog.show();
            }
        });

        timedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EditDialog editDialog=new EditDialog(getContext(),"");
//                editDialog.show();
            }
        });

        db.collection("users").document(user.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(documentSnapshot!=null){

                            Log.d("docsnap",documentSnapshot+"");
                            Log.d("docdouble",documentSnapshot.getDouble("Target")+"");

                            target.setText(documentSnapshot.getDouble("Target")+"");
                            balance.setText(documentSnapshot.getDouble("Balance")+"");

                        }
                    }
                });
        // Inflate the layout for this fragment
        return view;
    }
}
