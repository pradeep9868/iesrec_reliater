package co.jlabs.cersei_retailer;

/**
 * Created by Wadi on 19-12-2015.
 */
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class AdapterReward_Redeemed extends BaseAdapter {
    private Context context;
    private final String[] mobileValues;
    int type;

    public AdapterReward_Redeemed(Context context, String[] mobileValues,int type) {
        this.context = context;
        this.mobileValues = mobileValues;
        this.type=type;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.adap_reward, null);
        } else {
            gridView = (View) convertView;
        }

        if(type==0)
        {
            ((TextView)gridView.findViewById(R.id.staricon)).setTextColor(context.getResources().getColor(R.color.orange_dark));
        }
        else
        {
            ((TextView)gridView.findViewById(R.id.staricon)).setTextColor(context.getResources().getColor(R.color.red));
            ((TextView)gridView.findViewById(R.id.points)).setTextColor(context.getResources().getColor(R.color.red));
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return mobileValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}