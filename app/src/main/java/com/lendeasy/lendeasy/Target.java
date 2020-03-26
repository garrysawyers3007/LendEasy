package com.lendeasy.lendeasy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Target extends Fragment {
    ImageView tgtedit,blcedit,timedit;
    TextView target,balance,time;

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

        tgtedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDialog editDialog=new EditDialog(getContext());
                editDialog.show();
            }
        });

        blcedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDialog editDialog=new EditDialog(getContext());
                editDialog.show();
            }
        });

        timedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}
