package com.lendeasy.lendeasy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Borrow#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Borrow extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Borrow() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Borrow.
     */
    // TODO: Rename and change types and number of parameters
    public static Borrow newInstance(String param1, String param2) {
        Borrow fragment = new Borrow();
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

        View view=inflater.inflate(R.layout.fragment_borrow, container, false);
        ViewPager viewPager=view.findViewById(R.id.viewpager);
        //ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        FragmentPagerItemAdapter adapter=new FragmentPagerItemAdapter(getChildFragmentManager(),
                FragmentPagerItems.with(getContext())
                        .add("Top 10",Top10.class)
                        .add("Friends",Friends.class)
                        .create());

        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab=view.findViewById(R.id.viewpagertab);

        viewPagerTab.setViewPager(viewPager);
        // Inflate the layout for this fragment
        return view;
    }
}
