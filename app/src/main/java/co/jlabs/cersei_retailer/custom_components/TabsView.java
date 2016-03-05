package co.jlabs.cersei_retailer.custom_components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.jlabs.cersei_retailer.R;

/**
 * Created by Pradeep on 1/15/2016.
 */
public class TabsView extends LinearLayout{

    private View notification;
    private TextView no_notification;
    private TextView_Appcolor Icon;
    private View fulllayout;
    int num;


    public TabsView(Context context) {
        super(context);
        init(context);
    }

    public TabsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tabsview, this, true);
        fulllayout=getChildAt(0);
        notification = ((ViewGroup)getChildAt(0)).getChildAt(1);
        no_notification = (TextView) ((ViewGroup)((ViewGroup)getChildAt(0)).getChildAt(1)).getChildAt(0);
        Icon = (TextView_Appcolor) ((ViewGroup)((ViewGroup)getChildAt(0)).getChildAt(0)).getChildAt(0);
        num=0;
    }

    public void setText(String s)
    {
        Icon.setText(s);
    }
    public void setTextSize(int type,float size)
    {
        Icon.setTextSize(type, size);
    }

    public void setTextColor(int color)
    {
        Icon.setTextColor(color);
    }
    public void setBackgroundResource(int resId)
    {
        fulllayout.setBackgroundResource(resId);
    }
    public void setOnClickListener(OnClickListener clickListener)
    {
        fulllayout.setOnClickListener(clickListener);
    }

    public void giveCartNotification(Boolean add)
    {
        if(add) {
            num = num + 1;
            no_notification.setText("" + num);
            notification.setVisibility(VISIBLE);
        }
        else
        {
            if(num>1)
            {
                num = num - 1;
                no_notification.setText("" + num);
                notification.setVisibility(VISIBLE);
            }
            else
            {
                removeCartNotification();
            }
        }
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        this.startAnimation(animation);
    }

    public void removeCartNotification()
    {
        num=0;
        no_notification.setText("" + num);
        notification.setVisibility(GONE);
    }
}