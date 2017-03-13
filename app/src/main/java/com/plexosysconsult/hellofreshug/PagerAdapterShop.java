package com.plexosysconsult.hellofreshug;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by senyer on 6/12/2016.
 */
public class PagerAdapterShop extends FragmentStatePagerAdapter {

    Context context;
    public static int SHOPADAPTER = 1;
    public static int ABOUTADAPTER = 2;
    int which;
    int tabNumber;

    public PagerAdapterShop(FragmentManager fm, Context context, int whichAdapter) {
        super(fm);

        this.context = context;
        which = whichAdapter;

        if (which == SHOPADAPTER) {
            tabNumber = 3;
        }

        if (which == ABOUTADAPTER) {
            tabNumber = 2;
        }
    }

    @Override
    public Fragment getItem(int position) {


        if (which == SHOPADAPTER) {
            if (position == 0) {
                return new ShopVegetablesFragment();
            }

            if (position == 1) {
                return new ShopFruitsFragment();
            }
            if (position == 2) {
                return new ShopSpicesFragment();
            }

        }


        if (which == ABOUTADAPTER) {


            if (position == 0) {
                return new AboutHelloFreshFragment();
            }
            if (position == 1) {
                return new AboutDevFragment();
            }
        }


        return null;
    }

    @Override
    public int getCount() {
        return tabNumber;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (which == SHOPADAPTER) {
            if (position == 0) {
                return "Vegetables";
            }
            if (position == 1) {
                return "Fruits";
            }
            if (position == 2) {
                return "Spices";
            }
        }

        if (which == ABOUTADAPTER) {

            if (position == 0) {
                return "Hello Fresh Uganda";
            }
            if (position == 1) {
                return "Developers";
            }
        }


        return super.getPageTitle(position);
    }
}
