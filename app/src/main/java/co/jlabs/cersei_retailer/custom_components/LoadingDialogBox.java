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
public class LoadingDialogBox extends Dialog {

    View result,result_dialog,progress;

    public LoadingDialogBox(Context context, int themeResId) {
        super(context, themeResId);
    }

    public LoadingDialogBox(Context context) {
        super(context);
    }

    protected LoadingDialogBox(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public void setUpLayout()
    {
        setContentView(R.layout.lay_loading);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        show();
    }

}
