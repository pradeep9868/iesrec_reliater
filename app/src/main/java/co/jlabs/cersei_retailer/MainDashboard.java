package co.jlabs.cersei_retailer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import co.jlabs.cersei_retailer.custom_components.LoadingDialogBox;
import co.jlabs.cersei_retailer.custom_components.LocationPopup;
import co.jlabs.cersei_retailer.custom_components.PagerSlidingStrip;
import co.jlabs.cersei_retailer.custom_components.PagerSlidingStrip.IconTabProvider;
import co.jlabs.cersei_retailer.custom_components.SmallBang;
import co.jlabs.cersei_retailer.custom_components.SmallBangListener;


public class MainDashboard extends FragmentActivity implements View.OnClickListener,FragmentsEventInitialiser, LocationPopup.onLocationSelected{
    static final int ITEMS = 4;
    MyAdapter mAdapter;
    ViewPager mPager;
    PagerSlidingStrip strip;
    private SmallBang mSmallBang;
    View tab_point,tab_cart;
    View selectLocation;
    LocationPopup dialog;
    JSONObject json=null;
    FragmentEventHandler ViewpagerHandler=null,CameraHandler=null,ScoreHandler=null,cartHandler=null;
    LoadingDialogBox loadingDialogBox;
    Boolean loaded_first_page,loaded_second_page,loaded_third_page,loaded_forth_page;
    Boolean was_success_loaded_first_page,was_success_loaded_second_page,was_success_loaded_third_page,was_success_loaded_forth_page;

    String url = StaticCatelog.geturl()+"cersei/location";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSmallBang = SmallBang.attach2Window(this);
        selectLocation= toolbar.findViewById(R.id.location);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        loadingDialogBox= new LoadingDialogBox(this, R.style.alert_dialog);
        selectLocation.setOnClickListener(this);

        ((TextView)selectLocation.findViewById(R.id.text_location)).setText(StaticCatelog.getStringProperty(this,"location"));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.findViewById(R.id.lay_prof).setOnClickListener(this);
        navigationView.findViewById(R.id.lay_myorders).setOnClickListener(this);
        navigationView.findViewById(R.id.lay_myaddresses).setOnClickListener(this);
        navigationView.findViewById(R.id.lay_savedcards).setOnClickListener(this);
        navigationView.findViewById(R.id.lay_coupons).setOnClickListener(this);
        navigationView.findViewById(R.id.lay_settings).setOnClickListener(this);


        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.myviewpager);
        strip = (PagerSlidingStrip) findViewById(R.id.tabs);
        mPager.setAdapter(mAdapter);
        strip.setViewPager(mPager);
        mPager.setOffscreenPageLimit(4);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ViewpagerHandler.adjustCameraOrViewPager(true);
                } else
                    ViewpagerHandler.adjustCameraOrViewPager(false);


                if (position == 1)
                    CameraHandler.adjustCameraOrViewPager(true);
                else
                    CameraHandler.adjustCameraOrViewPager(false);


                if (position == 2) {
                    redText(tab_point);
                    ScoreHandler.adjustCameraOrViewPager(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tab_point = strip.returntab(2);
        tab_cart = strip.returntab(3);
        update_ui_for_location();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        String s ="";
        if (id == R.id.lay_prof) {
            s="Profile";
        } else if (id == R.id.lay_myorders) {
            s="My Orders";
        } else if (id == R.id.lay_myaddresses) {
            s="My Addresses";
        } else if (id == R.id.lay_savedcards) {
            s="Saved Cards";
        } else if (id == R.id.lay_coupons) {
            s="Coupons";
        } else if (id == R.id.lay_settings) {
            s="Settings";
        }
        else if (id == R.id.location) {
            download_locations();
        }
        if(s!="")
        {
            Toast.makeText(this, "Clicked " + s, Toast.LENGTH_SHORT).show();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void registerMyevent(int position, FragmentEventHandler eventHandler) {
        Log.i("Myapp", "Successfully registered event for " + position);
        if(position==1)
            ViewpagerHandler=eventHandler;
        else if(position==2)
            CameraHandler=eventHandler;
        else if(position==3)
            ScoreHandler=eventHandler;
        else if(position==4)
            cartHandler=eventHandler;
    }

    @Override
    public void MyloadingCompleted(int position,Boolean successfull) {
        switch (position)
        {
            case 1: loaded_first_page=true; was_success_loaded_first_page=successfull; break;
            case 2: loaded_second_page=true; was_success_loaded_second_page=successfull; break;
            case 3: loaded_third_page=true; was_success_loaded_third_page=successfull; break;
            case 4: loaded_forth_page=true; was_success_loaded_forth_page=successfull; break;
        }
        if(loaded_first_page&&loaded_second_page&&loaded_third_page&&loaded_forth_page)
        {
            end_update_ui_for_location();
        }
    }


    public class MyAdapter extends FragmentPagerAdapter implements IconTabProvider {

        private int tabIcons[] = {R.string.ico_home, R.string.ico_qr_code,R.string.rating, R.string.ico_cart};


        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        @Override
        public int getCount() {
            return ITEMS;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0 :

                    Fragment_Offers firstPage = Fragment_Offers.init(position);
                    firstPage.addInitialisationEvent(MainDashboard.this);
                    return firstPage;
                case 1 :
                    Fragment_Barcode fragment= Fragment_Barcode.init(position);
                    fragment.addInitialisationEvent(MainDashboard.this);
                    return fragment;
                case 2 :
                    Fragment_Points pointsFragment = Fragment_Points.init(position);
                    pointsFragment.addInitialisationEvent(MainDashboard.this);
                    return pointsFragment;
                default:
                    Fragment_Cart cartFragment = Fragment_Cart.init(position);
                    cartFragment.addInitialisationEvent(MainDashboard.this);
                    return cartFragment;
            }
        }

        @Override
        public int getPageIconResId(int position) {
            return tabIcons[position];
        }

    }

    public void redText(View view){
        //((TextView)tab_point).setTextColor(0xFFCD8BF8);
        mSmallBang.bang(view, 50, new SmallBangListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                // Toast.makeText(MainDashboard.this,"Added",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Myapp", "On Resume Called");
        if(CameraHandler!=null&&ViewpagerHandler!=null)
        {
            ViewpagerHandler.adjustCameraOrViewPager(true);
            CameraHandler.adjustCameraOrViewPager(false);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if(CameraHandler!=null&&ViewpagerHandler!=null)
        {
            ViewpagerHandler.adjustCameraOrViewPager(false);
            CameraHandler.adjustCameraOrViewPager(false);
        }
        super.startActivity(intent);
    }

    public void update_ui_for_location_withevent(String location)
    {
        ViewpagerHandler.startLoadbylocation(location);
        CameraHandler.startLoadbylocation(location);
        ScoreHandler.startLoadbylocation(location);
        cartHandler.startLoadbylocation(location);
        update_ui_for_location();
    }

    public void update_ui_for_location()
    {
        loadingDialogBox.setUpLayout();
        reset_loaded_fragments();

//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                end_update_ui_for_location();
//            }
//        }, 1000);

    }

    public void reset_loaded_fragments()
    {
        loaded_first_page=false;
        loaded_second_page=false;
        loaded_third_page=false;
        loaded_forth_page=false;
        was_success_loaded_first_page=false;
        was_success_loaded_second_page=false;
        was_success_loaded_third_page=false;
        was_success_loaded_forth_page=false;
    }

    public void end_update_ui_for_location()
    {
        String s="";
        loadingDialogBox.dismiss();
        if(!(was_success_loaded_first_page&&was_success_loaded_second_page&&was_success_loaded_third_page&&was_success_loaded_forth_page))
        {
            if(!was_success_loaded_first_page)
                s= s+" First ";
            if(!was_success_loaded_second_page)
                s=s+" Second ";
            if(!was_success_loaded_third_page)
                s=s+" Third ";
            if(!was_success_loaded_forth_page)
                s=s+" Fourth ";
            Toast.makeText(this,"An Error occurred during loading data for page:"+s,Toast.LENGTH_SHORT).show();
        }
    }

    public void getLocation()
    {
        int success;
        try {
            success = json.getInt("success");
        } catch (JSONException e) {
            success=0;
        }
        dialog = new LocationPopup(this, R.style.alert_dialog);

        if(success==1)
        {
            dialog.BuildDialog(MainDashboard.this, json);
        }
        else
        {
            Toast.makeText(this,"Invalid Data Received",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void update_location(String location) {
        ((TextView)selectLocation.findViewById(R.id.text_location)).setText(location);
        StaticCatelog.setStringProperty(this, "location", location);
        update_ui_for_location_withevent(location);
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
