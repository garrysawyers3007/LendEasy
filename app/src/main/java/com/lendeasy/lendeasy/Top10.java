package com.lendeasy.lendeasy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Top10 extends Fragment {
    private RecyclerView recyclerView;
    private LentAdapter lentAdapter;
    private ArrayList<String> list;

    public Top10() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_top10, container, false);

        list=new ArrayList<>();
        list.add("Hello");
        recyclerView=view.findViewById(R.id.recyclerview);
        lentAdapter=new LentAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(lentAdapter);
        // Inflate the layout for this fragment
        return view;
    }
}
