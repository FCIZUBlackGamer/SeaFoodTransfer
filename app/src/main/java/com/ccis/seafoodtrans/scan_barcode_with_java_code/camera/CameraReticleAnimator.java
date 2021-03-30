package com.ccis.seafoodtrans.scan_barcode_with_java_code.camera;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import org.jetbrains.annotations.NotNull;

public final class CameraReticleAnimator {
    private float rippleAlphaScale;
    private float rippleSizeScale;
    private float rippleStrokeWidthScale =1f;
    private final AnimatorSet animatorSet;
    private static final long DURATION_RIPPLE_FADE_IN_MS = 333L;
    private static final long DURATION_RIPPLE_FADE_OUT_MS = 500L;
    private static final long DURATION_RIPPLE_EXPAND_MS = 833L;
    private static final long DURATION_RIPPLE_STROKE_WIDTH_SHRINK_MS = 833L;
    private static final long DURATION_RESTART_DORMANCY_MS = 1333L;
    private static final long START_DELAY_RIPPLE_FADE_OUT_MS = 667L;
    private static final long START_DELAY_RIPPLE_EXPAND_MS = 333L;
    private static final long START_DELAY_RIPPLE_STROKE_WIDTH_SHRINK_MS = 333L;
    private static final long START_DELAY_RESTART_DORMANCY_MS = 1167L;

    public final float getRippleAlphaScale() {
        return this.rippleAlphaScale;
    }

    public final float getRippleSizeScale() {
        return this.rippleSizeScale;
    }

    public final float getRippleStrokeWidthScale() {
        return this.rippleStrokeWidthScale;
    }

    public final void start() {
        if (!this.animatorSet.isRunning()) {
            this.animatorSet.start();
        }

    }

    public final void cancel() {
        this.animatorSet.cancel();
        this.rippleAlphaScale = 0.0F;
        this.rippleSizeScale = 0.0F;
        this.rippleStrokeWidthScale = 1.0F;
    }

    public CameraReticleAnimator(@NotNull final GraphicOverlay graphicOverlay) {
        super();
        ValueAnimator rippleFadeInAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F}).setDuration(DURATION_RIPPLE_FADE_IN_MS);
        rippleFadeInAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener) (new ValueAnimator.AnimatorUpdateListener() {
            public final void onAnimationUpdate(ValueAnimator animation) {
                rippleAlphaScale = (Float) animation.getAnimatedValue();
                graphicOverlay.postInvalidate();

            }
        }));
        ValueAnimator rippleFadeOutAnimator = ValueAnimator.ofFloat(new float[]{1.0F, 0.0F}).setDuration(DURATION_RIPPLE_FADE_OUT_MS);
        rippleFadeOutAnimator.setStartDelay(START_DELAY_RIPPLE_FADE_OUT_MS);
        rippleFadeOutAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener) (new ValueAnimator.AnimatorUpdateListener() {
            public final void onAnimationUpdate(ValueAnimator animation) {
                rippleAlphaScale = (Float) animation.getAnimatedValue();
                graphicOverlay.postInvalidate();
            }

        }));
        ValueAnimator rippleExpandAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F}).setDuration(DURATION_RIPPLE_EXPAND_MS);
        rippleExpandAnimator.setStartDelay(START_DELAY_RIPPLE_EXPAND_MS);
        rippleExpandAnimator.setInterpolator((TimeInterpolator) (new FastOutSlowInInterpolator()));
        rippleExpandAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener) (new ValueAnimator.AnimatorUpdateListener() {
            public final void onAnimationUpdate(ValueAnimator animation) {
                rippleSizeScale = (Float) animation.getAnimatedValue();
                graphicOverlay.postInvalidate();

            }
        }));
        ValueAnimator rippleStrokeWidthShrinkAnimator = ValueAnimator.ofFloat(new float[]{1.0F, 0.5F}).setDuration(DURATION_RIPPLE_STROKE_WIDTH_SHRINK_MS);
        rippleStrokeWidthShrinkAnimator.setStartDelay(START_DELAY_RIPPLE_STROKE_WIDTH_SHRINK_MS);
        rippleStrokeWidthShrinkAnimator.setInterpolator((TimeInterpolator) (new FastOutSlowInInterpolator()));
        rippleStrokeWidthShrinkAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener) (new ValueAnimator.AnimatorUpdateListener() {
            public final void onAnimationUpdate(ValueAnimator animation) {
                rippleStrokeWidthScale = (Float) animation.getAnimatedValue();
                    graphicOverlay.postInvalidate();
            }
        }));

        ValueAnimator fakeAnimatorForRestartDelay = ValueAnimator.ofInt(new int[]{0, 0}).setDuration(DURATION_RESTART_DORMANCY_MS);
        fakeAnimatorForRestartDelay.setStartDelay(START_DELAY_RESTART_DORMANCY_MS);
        this.animatorSet = new AnimatorSet();
        this.animatorSet.playTogether(new Animator[]{(Animator) rippleFadeInAnimator, (Animator) rippleFadeOutAnimator, (Animator) rippleExpandAnimator, (Animator) rippleStrokeWidthShrinkAnimator, (Animator) fakeAnimatorForRestartDelay});
    }



}

