package co.jlabs.cersei_retailer;

/**
 * Created by pradeep on 19-12-2015.
 */
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.jlabs.cersei_retailer.custom_components.MyImageView;


public class AdapterReward_Redeemed extends BaseAdapter {
    private Context context;
    private JSONArray jsonArray;
    int type;
    ImageLoader imageLoader;

    public AdapterReward_Redeemed(Context context, JSONArray jsonArray,int type) {
        this.context = context;
        this.jsonArray=jsonArray;
        this.type=type;
        imageLoader = AppController.getInstance().getImageLoader();
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
        //new java.util.Date(Long.parseLong(timeInMillis))
        if(type==0)
        {
            ((TextView)gridView.findViewById(R.id.staricon)).setTextColor(context.getResources().getColor(R.color.orange_dark));
        }
        else
        {
            ((TextView)gridView.findViewById(R.id.staricon)).setTextColor(context.getResources().getColor(R.color.red));
            ((TextView)gridView.findViewById(R.id.points)).setTextColor(context.getResources().getColor(R.color.red));
        }
        try {
            ((TextView)gridView.findViewById(R.id.title)).setText(((JSONObject)jsonArray.get(position)).getString("title"));
            ((TextView)gridView.findViewById(R.id.points)).setText(((JSONObject) jsonArray.get(position)).getString("points"));
            ((MyImageView) gridView.findViewById(R.id.pic)).setImageUrl(((JSONObject) jsonArray.get(position)).getString("img"), imageLoader);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return gridView;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
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