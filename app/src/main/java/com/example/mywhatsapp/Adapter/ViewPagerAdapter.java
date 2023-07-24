package com.example.mywhatsapp.Adapter;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mywhatsapp.fragments.Call;
import com.example.mywhatsapp.fragments.Chat;
import com.example.mywhatsapp.fragments.Status;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Chat();
            case 1: return new Status();
            case 2: return new Call();
            default: return new Chat();
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title=null;
        switch(position){
            case 0: title="Chats"; break;
            case 1: title="Status"; break;
            case 2: title="Calls"; break;
        }
        return title;
    }
}
