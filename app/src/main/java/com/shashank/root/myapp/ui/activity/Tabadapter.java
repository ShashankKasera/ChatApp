package com.shashank.root.myapp.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shashank.root.myapp.ui.fragment.ChatsFragment;
import com.shashank.root.myapp.ui.fragment.FriendsFragment;
import com.shashank.root.myapp.ui.fragment.RequestFragment;

public class Tabadapter extends FragmentPagerAdapter {
    public Tabadapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position)
        {
            case 0:
                return fragment = new RequestFragment();
            case 1:
                return fragment = new ChatsFragment();
            case 2:
                return fragment = new FriendsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int Position)
    {
        switch (Position)
        {
            case 0:

                return "Request";

            case 1:
                return "Chats";

            case 2:
                return "Friends";

            default:
                return null;
        }
    }

}
