package co.jlabs.cersei_retailer;

/**
 * Created by Wadi on 19-12-2015.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class Adapter_Location extends BaseAdapter {
    private Context context;
    ImageLoader imageLoader;
    JSONArray jsonArray;
    Boolean mainlocation;


    public Adapter_Location(Context context,JSONArray jsonArray,Boolean mainlocation) {
        this.context = context;
        imageLoader = AppController.getInstance().getImageLoader();
        this.jsonArray=jsonArray;
        this.mainlocation=mainlocation;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View gridView;

        if (convertView == null) {

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.adap_location,null);
          //  gridView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT));

        } else {
            gridView = (View) convertView;
        }
        try {
            if(mainlocation)
                ((TextView)gridView.findViewById(R.id.text)).setText(((JSONObject)jsonArray.get(position)).getString("area"));
            else
                ((TextView)gridView.findViewById(R.id.text)).setText(((String)jsonArray.get(position)));
        } catch (JSONException e) {

        }
        return gridView;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        Object locations=null;

            try {
                locations = jsonArray.get(position);
            } catch (JSONException e) {

            }

        return locations;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}