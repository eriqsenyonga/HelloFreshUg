package com.plexosysconsult.hellofreshug;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by senyer on 6/12/2016.
 */
public class PagerAdapterShop extends FragmentStatePagerAdapter {

    Context context;
    public static int SHOPADAPTER = 1;
    public static int ABOUTADAPTER = 2;
    public static int ORDERDETAILSADAPTER = 3;
    int which;
    int tabNumber;

    public PagerAdapterShop(FragmentManager fm, Context context, int whichAdapter) {
        super(fm);

        this.context = context;
        which = whichAdapter;

        if (which == SHOPADAPTER) {
            tabNumber = 5;
        }

        if (which == ABOUTADAPTER) {
            tabNumber = 2;
        }
        if (which == ORDERDETAILSADAPTER) {

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
                return new ShopHerbsFragment();
            }
            if (position == 3) {

                return new ShopSpicesFragment();
            }

            if (position == 4) {

                return new ShopSeaFoodFragment();
            }

            if (position == 5) {

                return new ShopRedWineFragment();
            }
            if (position == 6) {

                return new ShopWhiteWineFragment();
            }

            return new ShopFruitsFragment();

        }


        if (which == ABOUTADAPTER) {


            if (position == 0) {
                return new AboutHelloFreshFragment();
            }
            if (position == 1) {
                return new AboutDevFragment();
            }
        }

        if (which == ORDERDETAILSADAPTER) {


            if (position == 0) {
                return new OrderItemsFragment();
            }
            if (position == 1) {
                return new OrderInfoFragment();
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
                return "Herbs";
            }

            if (position == 3) {
                return "Spices";
            }
            if (position == 4) {
                return "Sea Food";
            }

            if (position == 5) {
                return "Red Wine";
            }

            if (position == 6) {
                return "White Wine";
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

        if (which == ORDERDETAILSADAPTER) {

            if (position == 0) {
                return "ITEMS";
            }
            if (position == 1) {
                return "INFO";
            }
        }


        return super.getPageTitle(position);
    }
}
