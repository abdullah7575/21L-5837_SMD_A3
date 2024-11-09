package com.example.navigation_smd_7a;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.HashMap;
import java.util.Map;

public class ViewPagerAdapter extends FragmentStateAdapter {
//    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
//        Fragment f;
        switch (position)
        {
            case 0:
                return new ScheduleFragment();
            case 1:
                return new DeliveredFragment();
            default:
                return new NewOrderFragment();
        }
//        fragmentMap.put(position, f);
//        return f;
    }
//    public Fragment getFragment(int position) {
//        return fragmentMap.get(position);
//    }
    @Override
    public int getItemCount() {
        return 3;
    }
}
