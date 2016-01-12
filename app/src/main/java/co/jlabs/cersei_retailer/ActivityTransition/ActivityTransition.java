package co.jlabs.cersei_retailer.ActivityTransition;

/**
 * Created by Pradeep on 12/27/2015.
 */

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import co.jlabs.cersei_retailer.ActivityTransition.core.MoveData;
import co.jlabs.cersei_retailer.ActivityTransition.core.TransitionAnimation;

public class ActivityTransition {
    private int duration = 300;
    private View toView;
    private TimeInterpolator interpolator;
    private Intent fromIntent;

    private ActivityTransition(Intent intent) {
        this.fromIntent = intent;
    }

    public static ActivityTransition with(Intent intent) {
        return new ActivityTransition(intent);
    }

    public ActivityTransition to(View toView) {
        this.toView = toView;
        return this;
    }

    public ActivityTransition duration(int duration) {
        this.duration = duration;
        return this;
    }

    public ActivityTransition interpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    public ExitActivityTransition start(Bundle savedInstanceState) {
        if (interpolator == null) {
            interpolator = new DecelerateInterpolator();
        }
        final Context context = toView.getContext();
        final Bundle bundle = fromIntent.getExtras();
        final MoveData moveData = TransitionAnimation.startAnimation(context, toView, bundle, savedInstanceState, duration, interpolator);
        return new ExitActivityTransition(moveData);
    }

}
