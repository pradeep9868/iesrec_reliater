package co.jlabs.cersei_retailer.custom_components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import co.jlabs.cersei_retailer.R;

/**
 * Created by Pradeep on 12/30/2015.
 */
public class NoInternetDialogBox extends Dialog {


    public NoInternetDialogBox(Context context, int themeResId) {
        super(context, themeResId);
    }

    public NoInternetDialogBox(Context context) {
        super(context);
    }

    protected NoInternetDialogBox(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public void setUpLayout(final onReloadPageSelected reloadPageSelected)
    {
        setContentView(R.layout.nointernetavail);
        findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                reloadPageSelected.Reload_No_Internet();
            }
        });
        findViewById(R.id.cont).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        show();
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onBackPressed() {

    }

    public interface onReloadPageSelected
    {
        void Reload_No_Internet();
    }


}
