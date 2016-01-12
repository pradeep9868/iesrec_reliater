package co.jlabs.cersei_retailer.custom_components;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import co.jlabs.cersei_retailer.R;

/**
 * Created by Pradeep on 12/30/2015.
 */
public class CustomDialogBox extends Dialog {

    View result,result_dialog,progress;

    public CustomDialogBox(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomDialogBox(Context context) {
        super(context);
    }

    protected CustomDialogBox(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void showDialog()
    {
        if(isShowing())
        {
            setLoadingState();
        }
        else
        {
            setUpLayout();
        }
    }

    public void setUpLayout()
    {
        setContentView(R.layout.dialog_layout);
        show();
        result=findViewById(R.id.result);
        result_dialog=findViewById(R.id.result_dialog);
        progress=findViewById(R.id.progress);

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Swipe to dismiss this box", Toast.LENGTH_SHORT).show();
                Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                result.startAnimation(shake);
            }
        });
    }

    public void setLoadingState()
    {
        result.startAnimation(outToRightAnimation());
        result.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
    }
    public void setCardState()
    {
        progress.setVisibility(View.GONE);
        result.setVisibility(View.VISIBLE);
        result.startAnimation(inFromLeftAnimation());

        result_dialog.setOnTouchListener(new SwipeDismissTouchListener(
                result_dialog,
                null,
                new SwipeDismissTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(Object token) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view, Object token) {
                        dismiss();
                    }
                }));
    }


    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(300);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(300);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }
}
