package com.lendeasy.lendeasy;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    private ArrayList<String> list;
    private RecyclerView recyclerView;
    private LentAdapter lentAdapter;
    private ImageView exit;
    private GoogleSignInClient googleSignInClient;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);


        exit=view.findViewById(R.id.logout);
        final TextView target,score,balance;

        target=view.findViewById(R.id.target);
        score=view.findViewById(R.id.time);
        balance=view.findViewById(R.id.balance);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                googleSignInClient.signOut();
                mAuth.signOut();
                Intent i=new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().finish();
                Toast.makeText(getContext(), "Signed Out",
                        Toast.LENGTH_SHORT).show();
            }
        });

        String userid=mAuth.getCurrentUser().getUid();


        db.collection("users").document(userid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(documentSnapshot!=null){

                            Log.d("docsnap",documentSnapshot+"");
                            Log.d("docdouble",documentSnapshot.getDouble("Target")+"");

                            target.setText(documentSnapshot.getDouble("Target")+"");
                            score.setText(documentSnapshot.getDouble("Score")+"");
                            balance.setText(documentSnapshot.getDouble("Balance")+"");

                        }
                    }
                });

        list=new ArrayList<>();
        list.add("Hello");
        recyclerView=view.findViewById(R.id.history);
        lentAdapter=new LentAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(lentAdapter);
        // Inflate the layout for this fragment
        return view;
    }
}
