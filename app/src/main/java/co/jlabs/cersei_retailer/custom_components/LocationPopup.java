package co.jlabs.cersei_retailer.custom_components;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.jlabs.cersei_retailer.Adapter_Location;
import co.jlabs.cersei_retailer.R;

/**
 * Created by Pradeep on 1/9/2016.
 */
public class LocationPopup extends Dialog {
    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    ListView lv;
    JSONArray data=null;
    private onLocationSelected locationInterface;

    public LocationPopup(Context context) {
        super(context);
        setup(context);
    }

    public LocationPopup(Context context, int themeResId) {
        super(context, themeResId);
        setup(context);
    }

    protected LocationPopup(Context context, boolean cancelable, OnCancelListener cancelListener) {
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

                        LocationPopup.super.dismiss();

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
        ((TextView)findViewById(R.id.location)).setText("(" + location + ")");
    }


    public void BuildDialog(onLocationSelected LocationInterface,JSONObject json)
    {
        setContentView(R.layout.select_location_popup);
        findViewById(R.id.dismissifclickedhere).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.locationInterface=LocationInterface;
        lv = (ListView) findViewById(R.id.listview);
        try {
            data=json.getJSONArray("data");
        } catch (JSONException e) {

        }
        lv.setAdapter(new Adapter_Location(getContext(),data,true));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dismiss();

                JSONObject obj = (JSONObject) parent.getItemAtPosition(position);
                try {
                    data = obj.getJSONArray("locations");
                    BuildDialog_2(obj.getString("area"));
                    Log.i("Myapp", "Locations " + parent.getItemAtPosition(position).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        show();
    }

    public void BuildDialog_2(String obj)
    {
        setContentView(R.layout.select_location_popup);
        findViewById(R.id.dismissifclickedhere).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        updateforlocation(obj);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(new Adapter_Location(getContext(), data, false));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                locationInterface.update_location((String) parent.getItemAtPosition(position));
            }
        });
        show();
    }

    public interface onLocationSelected
    {
        void update_location(String location);
    }
}
