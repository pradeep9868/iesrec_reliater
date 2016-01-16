package co.jlabs.cersei_retailer.custom_components;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;

import co.jlabs.cersei_retailer.R;

public class MyImageView extends NetworkImageView{

    int dominantcolor=0x88222222;

    VolleyImageInterface volleyImageInterface;


    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if(bm!=null)
        {
            Bitmap bitmap1 = Bitmap.createScaledBitmap(bm, 1, 1, true);
            dominantcolor = bitmap1.getPixel(0, 0);
            int red=Color.red(dominantcolor)-180;
            int green=Color.green(dominantcolor)-180;
            int blue =Color.blue(dominantcolor)-180;
            if(red<0)
                red=0;
            if(green<0)
                green=0;
            if(blue<0)
                blue=0;
            dominantcolor = Color.argb(160, red,green,blue);
            if(volleyImageInterface!=null)
            volleyImageInterface.adjustColor(dominantcolor);

        }
    }
    public void setOnImageChangeListner(VolleyImageInterface volleyImageInterface)
    {
        this.volleyImageInterface =volleyImageInterface;
    }
}