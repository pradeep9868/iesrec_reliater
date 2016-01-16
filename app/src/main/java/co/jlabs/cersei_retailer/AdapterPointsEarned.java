package co.jlabs.cersei_retailer;

/**
 * Created by Wadi on 19-12-2015.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AdapterPointsEarned extends BaseAdapter {
    private Context context;
    private JSONArray jsonArray;

    public AdapterPointsEarned(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.adap_points_earned, null);
        } else {
            gridView = (View) convertView;
        }
        try {
            ((TextView)gridView.findViewById(R.id.title)).setText(((JSONObject)jsonArray.get(position)).getString("title"));
            ((TextView)gridView.findViewById(R.id.points)).setText(((JSONObject) jsonArray.get(position)).getString("points"));
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