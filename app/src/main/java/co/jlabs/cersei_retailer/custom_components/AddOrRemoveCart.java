package co.jlabs.cersei_retailer.custom_components;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.jlabs.cersei_retailer.R;

/**
 * Created by Pradeep on 1/15/2016.
 */
public class AddOrRemoveCart extends LinearLayout implements View.OnClickListener {

    private View AddtoCart,RemoveFromCart;
    private TextView num_of_items;
    private  ItemsClickListener listner;
    private int position;
    ObjectAnimator AddtoCartColorAnimator,RemoveFromCartColorAnimator;

    public AddOrRemoveCart(Context context) {
        super(context);
        init(context);
    }

    public AddOrRemoveCart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddOrRemoveCart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.add_or_remove_cart, this, true);

        RemoveFromCart = ((ViewGroup)getChildAt(0)).getChildAt(0);
        num_of_items = (TextView) ((ViewGroup)getChildAt(0)).getChildAt(1);
        AddtoCart = ((ViewGroup)getChildAt(0)).getChildAt(2);
        AddtoCartColorAnimator = ObjectAnimator.ofObject(AddtoCart,
                "textColor",
                new ArgbEvaluator(),
                0xFF004d00,
                0xFF777777);
        AddtoCartColorAnimator.setDuration(400);
        RemoveFromCartColorAnimator = ObjectAnimator.ofObject(RemoveFromCart,
                "textColor",
                new ArgbEvaluator(),
                0xFF990000,
                0xFF777777);
        RemoveFromCartColorAnimator.setDuration(400);
    }

    public void addOnItemClickListner(ItemsClickListener listner,int position,int quantity)
    {
        this.listner=listner;
        this.position=position;
        RemoveFromCart.setOnClickListener(this);
        AddtoCart.setOnClickListener(this);
        num_of_items.setText(""+quantity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.removefromcart :
                int quantity=listner.removeItemClicked(v,position);
                if(quantity>=0)
                 num_of_items.setText(""+quantity);
                 RemoveFromCartColorAnimator.start();
                break;
            case R.id.addtocart :
                num_of_items.setText("" + listner.addItemClicked(position));
                AddtoCartColorAnimator.start();
                break;
        }
    }


    public interface ItemsClickListener
    {
        int addItemClicked(int position);
        int removeItemClicked(View v,int position);
    }

}