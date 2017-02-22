package com.plexosysconsult.hellofreshug;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {


    FrameLayout frameLayout;
    View root;


    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

                root = inflater.inflate(R.layout.fragment_about_us, container, false);
        frameLayout = (FrameLayout) root.findViewById(R.id.fl_about_us);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

/*


        View aboutPage = new AboutPage(getActivity())
                .isRTL(false)
                .setImage(R.drawable.hellofresh_trans)
         //       .addItem(versionElement)
          //      .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("hellofreshug@gmail.com")
                .addWebsite("http://www.hellofreshuganda.com")
        //        .addFacebook("t")
        //        .addTwitter("medyo80")
         //       .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
         //       .addPlayStore("com.ideashower.readitlater.pro")
         //       .addGitHub("medyo")
         //       .addInstagram("medyo80")
                .create();

        frameLayout.addView(aboutPage);
        */
    }
}
