package com.plexosysconsult.hellofreshug;

import android.os.Build;
import android.text.Html;

/**
 * Created by senyer on 9/4/2016.
 */
public class UsefulFunctions {

    public String stripHtml(String html) {

        if (Build.VERSION.SDK_INT < 24) {

            return Html.fromHtml(html).toString();
        } else {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        }
    }

}
