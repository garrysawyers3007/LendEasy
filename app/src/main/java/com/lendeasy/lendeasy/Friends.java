package com.lendeasy.lendeasy;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Friends#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Friends extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<BorrowModel> list;

    public Friends() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Friends.
     */
    // TODO: Rename and change types and number of parameters
    public static Friends newInstance(String param1, String param2) {
        Friends fragment = new Friends();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.fragment_friends, container, false);


        FirebaseFirestore db=FirebaseFirestore.getInstance();

        db.collection("borrow").
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        list=new ArrayList<>();

                        if(queryDocumentSnapshots!=null)
                        for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots) {

                            String Name,Title,Description,ImageUrl,Time;
                            Name=documentSnapshot.getString("Borrower");
                            Title=documentSnapshot.getString("Title");
                            Description=documentSnapshot.getString("Description");
                            ImageUrl=documentSnapshot.getString("Image");
                            Time=documentSnapshot.getString("Time");
                            Log.d("Wht",Name);
                            list.add(new BorrowModel(Name,Title,Description,ImageUrl,Time));

                        }

                        RecyclerView recyclerView=view.findViewById(R.id.recyclerview);
                        BorrowAdapter adapter=new BorrowAdapter(list,getContext());
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                    }

                });
        // Inflate the layout for this fragment
        return view;
    }
}
