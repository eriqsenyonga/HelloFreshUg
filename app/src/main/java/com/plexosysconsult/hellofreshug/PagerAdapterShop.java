package com.plexosysconsult.hellofreshug;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by senyer on 6/12/2016.
 */
public class PagerAdapterShop extends FragmentPagerAdapter {

    Context context;

    public PagerAdapterShop(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new ShopVegetablesFragment();
        }

        if (position == 1) {
            return new ShopFruitsFragment();
        }
        if (position == 2) {
            return new ShopSpicesFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Vegetables";
        }
        if (position == 1) {
            return "Fruits";
        }
        if (position == 2) {
            return "Spices";
        }

        return super.getPageTitle(position);
    }
}
