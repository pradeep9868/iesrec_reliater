package co.jlabs.cersei_retailer.custom_components;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import co.jlabs.cersei_retailer.R;

/**
 * Created by Pradeep on 1/9/2016.
 */
public class SplashPopup extends Dialog {
    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;

    public SplashPopup(Context context) {
        super(context);
        setup(context);
    }

    public SplashPopup(Context context, int themeResId) {
        super(context, themeResId);
        setup(context);
    }

    protected SplashPopup(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setup(context);
    }

    public void setup(Context context)
    {
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {

                        SplashPopup.super.dismiss();

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
    }

    @Override
    public void cancel() {
        mDialogView.startAnimation(mModalOutAnim);
    }

    public void updateforlocation(String location)
    {
        ((TextView)findViewById(R.id.location)).setText("("+location+")");
    }
}
