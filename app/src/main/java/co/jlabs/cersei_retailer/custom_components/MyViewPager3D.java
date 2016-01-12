package co.jlabs.cersei_retailer.custom_components;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

/**
 * Created by Pradeep on 12/30/2015.
 */
public class MyViewPager3D extends ViewPager {
    private Camera mCamera;
    private Matrix mMatrix;
    boolean first=false;


    public MyViewPager3D(Context context) {
        super(context);
    //    postInitViewPager();
        mCamera = new Camera();
        mMatrix = new Matrix();
    }

    public MyViewPager3D(Context context, AttributeSet attrs) {
        super(context, attrs);
    //    postInitViewPager();
        mCamera = new Camera();
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!first) {
            mCamera.save();
            //  mCamera.rotate(0, 0, 0);
            mCamera.translate(0, 0, 60);
            mCamera.getMatrix(mMatrix);
            mMatrix.preTranslate(-getWidth() / 2, -getHeight() / 2);
            mMatrix.postTranslate(getWidth() / 2, getHeight() / 2);
            canvas.concat(mMatrix);
        }
        super.onDraw(canvas);
        if(!first)
         mCamera.restore();
        first=true;
    }

    private CustomDurationScroller mScroller = null;

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void postInitViewPager() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewpager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new CustomDurationScroller(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
        }
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }
}
