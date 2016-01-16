package co.jlabs.cersei_retailer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.google.zxing.Result;

import co.jlabs.cersei_retailer.custom_components.CustomDialogBox;
import co.jlabs.cersei_retailer.zxingfragmentlib.BarCodeScannerFragment;

/**
 * Created by Pradeep on 12/25/2015.
 */
public class Fragment_Barcode extends BarCodeScannerFragment implements FragmentEventHandler {

    int fragVal;

    Fragment_Barcode barcodeFragment;
    CustomDialogBox dialog;
    FragmentsEventInitialiser eventInitialiser=null;

    static Fragment_Barcode init(int val) {
        Fragment_Barcode truitonFrag = new Fragment_Barcode();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 2;
        barcodeFragment=this;
        dialog = new CustomDialogBox(getContext(),R.style.Theme_Dialog);

        this.setmCallBack(new IResultCallback() {
            @Override
            public void result(Result lastResult) {
                //Toast.makeText(getActivity(), "Scan: " + lastResult.toString(), Toast.LENGTH_SHORT).show();
                barcodeFragment.stopScan();
                dialog.showDialog();


                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        dialog.setCardState();
                        barcodeFragment.startScan();
                    }
                }, 5000);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        barcodeFragment.startScan();
                    }
                });
            }
        });

    }

    public void funstopscan()
    {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                try {
                    stopScan();
                } catch (Exception e) {
                    funstopscan();
                }
            }

            ;
        }, 200);
    }

    public Fragment_Barcode() {

    }

    @Override
    public void adjustCameraOrViewPager(boolean on) {
        if(on)
        {

            startScanbyMe();
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        else
        {
            funstopscan();
            Window window = getActivity().getWindow();
            window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void startLoadbylocation(String location) {
        //Since this is barcode fragment hence will not be used
        tellThatLoadedSuccessfully();
    }


    public void addInitialisationEvent(FragmentsEventInitialiser eventInitialiser)
    {
        this.eventInitialiser=eventInitialiser;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(eventInitialiser!=null)
            eventInitialiser.registerMyevent(fragVal,this);
        tellThatLoadedSuccessfully();
    }

    public void tellThatLoadedSuccessfully()
    {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (eventInitialiser != null)
                    eventInitialiser.MyloadingCompleted(fragVal,true);
            }
        }, 1000);

    }
}