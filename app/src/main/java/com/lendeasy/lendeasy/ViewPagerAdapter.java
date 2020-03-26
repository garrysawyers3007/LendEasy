package com.lendeasy.lendeasy;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<androidx.fragment.app.Fragment> Fragment = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Profile();
            default:
                return new Lend();

        }
    }

    public void add(Fragment Frag) {
        Fragment.add(Frag);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
