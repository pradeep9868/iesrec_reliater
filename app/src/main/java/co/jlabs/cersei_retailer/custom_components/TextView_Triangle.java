package co.jlabs.cersei_retailer.custom_components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class TextView_Triangle extends TextView {


    public TextView_Triangle(Context context) {
      super(context);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
            this.setTextSize(12);
            this.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public TextView_Triangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
            this.setTextSize(12);
            this.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public TextView_Triangle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
            this.setTextSize(12);
            this.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    protected void onDraw (Canvas canvas) {
        drawBackground(canvas);
        super.onDraw(canvas);
    }

    private void drawBackground(Canvas canvas) {
        int width=dpToPx(50),height=dpToPx(20);
        int extra=dpToPx(8);
        Point a = new Point(0, 0);
        Point b = new Point(width, 0);
        Point c = new Point(width+extra, height/2);
        Point d = new Point(width, height);
        Point e = new Point(0, height);


        Path path = new Path();
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(d.x, d.y);
        path.lineTo(e.x, e.y);
        Paint mPointedBackgroundPaint = new Paint();
        mPointedBackgroundPaint.setColor(Color.parseColor("#FFA000"));
        canvas.drawPath(path, mPointedBackgroundPaint);
    }

    public int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}