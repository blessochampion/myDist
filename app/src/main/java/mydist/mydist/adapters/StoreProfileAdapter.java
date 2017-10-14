package mydist.mydist.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Blessing.Ekundayo on 10/14/2017.
 */

public class StoreProfileAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;
    List<String> titles;

    public StoreProfileAdapter(FragmentManager manager, List<Fragment> fragments, List<String> titles) {
        super(manager);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
