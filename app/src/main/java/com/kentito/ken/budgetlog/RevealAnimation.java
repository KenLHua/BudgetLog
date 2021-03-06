package com.kentito.ken.budgetlog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import androidx.interpolator.view.animation.FastOutLinearInInterpolator;


class RevealAnimation {
    static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    private final View mView;
    private Activity mActivity;

    private int revealX;
    private int revealY;

     RevealAnimation(View view, Intent intent, Activity activity) {
        mView = view;
        mActivity = activity;

        view.setVisibility(View.INVISIBLE);

        revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
        revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    revealActivity(revealX, revealY);
                    mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }

    }

    private void revealActivity(int x, int y) {

        float finalRadius = (float) (Math.max(mView.getWidth(), mView.getHeight()) * 1.1);

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(mView, x, y, 0, finalRadius);
        circularReveal.setDuration(300);
        circularReveal.setInterpolator(new FastOutLinearInInterpolator());

        // make the view visible and start the animation
        mView.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    void unRevealActivity() {
        float finalRadius = (float) (Math.max(mView.getWidth(), mView.getHeight()) * 1.1);
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                mView, revealX, revealY, finalRadius, 0);

        circularReveal.setDuration(Constant.ANIMATION_FAB_DURATION);
        circularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mView.setVisibility(View.INVISIBLE);
                mActivity.finish();
                mActivity.overridePendingTransition(0, 0);
            }
        });

        circularReveal.start();

    }
}
