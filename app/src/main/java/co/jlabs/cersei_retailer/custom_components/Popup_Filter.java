package co.jlabs.cersei_retailer.custom_components;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ListView;

import org.json.JSONArray;

import co.jlabs.cersei_retailer.R;

/**
 * Created by Pradeep on 12/30/2015.
 */
public class Popup_Filter extends Dialog {

    private View mDialogView;
  //  private AnimationSet mModalInAnim;
  //  private AnimationSet mModalOutAnim;

    public Popup_Filter(Context context) {
        super(context);
        setup(context);
    }

    public Popup_Filter(Context context, int themeResId) {
        super(context, themeResId);
        setup(context);
    }

    protected Popup_Filter(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setup(context);
    }

    public void setup(Context context)
    {
/*        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
*/    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
    }

    public void end()
    {
        mDialogView.setVisibility(View.GONE);
        mDialogView.post(new Runnable() {
            @Override
            public void run() {

                Popup_Filter.super.dismiss();

            }
        });

    }

    protected void onStart() {
      //  mDialogView.startAnimation(mModalInAnim);
    }

    @Override
    public void cancel() {
        end();
//        mDialogView.startAnimation(mModalOutAnim);
    }


    public void setUpLayout()
    {
        setContentView(R.layout.select_filter_popup);
        show();
    }

}
