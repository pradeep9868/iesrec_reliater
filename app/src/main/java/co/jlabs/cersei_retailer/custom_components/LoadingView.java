package co.jlabs.cersei_retailer.custom_components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by Pradeep on 1/1/2016.
 */
public class LoadingView extends View {

    Paint mPaint = new Paint();
    Paint mPaint1 = new Paint();
    Paint mPaint2 = new Paint();
    Paint mPaint3 = new Paint();

    private Animation anim;

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);

        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setStrokeWidth(8);
        mPaint1.setColor(Color.GREEN);

        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeWidth(6);
        mPaint2.setColor(Color.BLUE);

        mPaint3.setStyle(Paint.Style.STROKE);
        mPaint3.setStrokeWidth(10);
        mPaint3.setColor(Color.parseColor("#FBA000"));
    }

    private void createAnimation(Canvas canvas) {
        anim = new RotateAnimation(0, 360, getWidth()/2, getHeight()/2);
        anim.setRepeatMode(Animation.RESTART);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(1000L);
        startAnimation(anim);
    }

    protected void onDraw(Canvas canvas) {

        int cx = getWidth()/2; // x-coordinate of center of the screen
        int cy = getHeight()/2; // y-coordinate of the center of the screen



        // Starts the animation to rotate the circle.
        if (anim == null)
            createAnimation(canvas);





        RectF oval3 = new RectF();
        oval3.set(cx-(cx*80/100), cy-(cy*80/100), cx+(cx*80/100), cy+(cy*80/100));
        canvas.drawArc(oval3, 135, 225, false, mPaint3);

        RectF oval2 = new RectF();
        oval2.set(cx-(cx*50/100), cy-(cy*50/100), cx+(cx*50/100), cy+(cy*50/100));
        canvas.drawArc(oval2, 90, 180, false, mPaint2);

        RectF oval1 = new RectF();
        oval1.set(cx-(cx*40/100), cy-(cy*40/100), cx+(cx*40/100), cy+(cy*40/100));
        canvas.drawArc(oval1, 45, 135, false, mPaint1);

        RectF oval = new RectF();
        oval.set(cx-(cx*20/100), cy-(cy*20/100), cx+(cx*20/100), cy+(cy*20/100));
        canvas.drawArc(oval, 0, 90, false, mPaint);

    }
}