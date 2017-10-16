package com.onemenu.driver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;

public class MyProgessView {
    public View VIEW;

    public void showProgress(Resources resources, final boolean show) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime);

                VIEW.setVisibility(show ? View.VISIBLE : View.GONE);
                VIEW.animate().setDuration(shortAnimTime)
                        .alpha(show ? 1 : 0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                VIEW.setVisibility(show ? View.VISIBLE : View.GONE);
                            }
                        });
            } else {
                VIEW.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
