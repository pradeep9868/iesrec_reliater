package co.jlabs.cersei_retailer.ActivityTransition;

/**
 * Created by Pradeep on 12/27/2015.
 */
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.view.animation.DecelerateInterpolator;

import co.jlabs.cersei_retailer.ActivityTransition.core.MoveData;
import co.jlabs.cersei_retailer.ActivityTransition.core.TransitionAnimation;

public class ExitActivityTransition {
    private final MoveData moveData;
    private TimeInterpolator interpolator;


    public ExitActivityTransition(MoveData moveData) {
        this.moveData = moveData;
    }

    public ExitActivityTransition interpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    public void exit(final Activity activity) {
        if (interpolator == null) {
            interpolator = new DecelerateInterpolator();
        }
        TransitionAnimation.startExitAnimation(moveData, interpolator, new Runnable() {
            @Override
            public void run() {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }
        });
    }

}