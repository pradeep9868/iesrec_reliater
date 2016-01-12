package co.jlabs.cersei_retailer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.jlabs.cersei_retailer.custom_components.LocationPopup;

public class SelectLocation extends Activity implements LocationPopup.onLocationSelected {
    LocationPopup dialog;
    Context context;
    JSONArray data=null;
    ListView lv;
    JSONObject json=null;
    String url = StaticCatelog.geturl()+"cersei/location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_location);
        context=this;
       // getLocation();
        download_locations();
    }

    @Override
    public void update_location(String location) {
        ((TextView)findViewById(R.id.select)).setText(location);
        StaticCatelog.setStringProperty(this, "location", location);
        start_activity();
    }

    public void start_activity(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SelectLocation.this, MainDashboard.class);
                startActivity(i);
                finish();
            }
        }, 1000);

    }

    public void getLocation()
    {

//        String s= StaticCatelog.load_json(this,R.raw.location);
//
//        try {
//            json= new JSONObject(s);
//        } catch (JSONException e) {
//
//        }
        int success;
        try {
            success = json.getInt("success");
        } catch (JSONException e) {
            success=0;
        }
        dialog = new LocationPopup(context, R.style.alert_dialog);

        if(success==1)
        {
            findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.BuildDialog(SelectLocation.this,json);
                }
            });
        }
        else
        {
            Toast.makeText(context,"Invalid Data Received",Toast.LENGTH_SHORT).show();
        }
        if(StaticCatelog.getStringProperty(this,"location")==null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    dialog.BuildDialog(SelectLocation.this, json);
                }
            }, 1000);
        }
        else
        {
            ((TextView)findViewById(R.id.select)).setText(StaticCatelog.getStringProperty(this,"location"));
            start_activity();
        }
    }


    private void download_locations() {

        String tag_json_obj = "json_obj_req_get_locations";

        Log.i("Myapp", "Calling url " + url);
        if(json==null) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(final JSONObject response) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    json = response;
                                    getLocation();
                                }
                            });

                        }
                    }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error", "Error: " + error.getMessage());
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }
        else
        {
            getLocation();
        }
    }
}
