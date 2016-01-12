package co.jlabs.cersei_retailer.custom_components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class TextView_Black extends TextView {


    public TextView_Black(Context context) {
      super(context);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
            this.setTextColor(Color.parseColor("#777777"));
        }
    }

    public TextView_Black(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
            this.setTextColor(Color.parseColor("#777777"));
        }
    }

    public TextView_Black(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tf = FontCache.get("fonts/icomoon.ttf", context);
        if(tf != null) {
            this.setTypeface(tf,Typeface.BOLD);
            this.setTextColor(Color.parseColor("#777777"));
        }
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

}