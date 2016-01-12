package co.jlabs.cersei_retailer.custom_components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;



public class TextView_Appcolor extends TextView {


    public TextView_Appcolor(Context context) {
      super(context);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
        }
    }

    public TextView_Appcolor(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
        }
    }

    public TextView_Appcolor(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
        }
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

}