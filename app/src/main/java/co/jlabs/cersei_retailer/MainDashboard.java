package co.jlabs.cersei_retailer;

import android.content.Intent;
import android.os.Bundle;
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
import co.jlabs.cersei_retailer.custom_components.NoInternetDialogBox;
import co.jlabs.cersei_retailer.custom_components.PagerSlidingStrip;
import co.jlabs.cersei_retailer.custom_components.PagerSlidingStrip.IconTabProvider;
import co.jlabs.cersei_retailer.custom_components.Popup_Filter;
import co.jlabs.cersei_retailer.custom_components.SmallBang;
import co.jlabs.cersei_retailer.custom_components.SmallBangListener;
import co.jlabs.cersei_retailer.custom_components.TabsView;


public class MainDashboard extends FragmentActivity implements View.OnClickListener,FragmentsEventInitialiser, LocationPopup.onLocationSelected,NoInternetDialogBox.onReloadPageSelected{
    static final int ITEMS = 4;
    static final int page_offers=0;
    static final int page_points=1;
    static final int page_barcode=2;
    static final int page_cart=3;

    MyAdapter mAdapter;
    ViewPager mPager;
    PagerSlidingStrip strip;
    private SmallBang mSmallBang;
    View tab_point,tab_cart;
    View selectLocation;
    LocationPopup dialog;
    Popup_Filter filter_popup;
    JSONObject json=null;
    FragmentEventHandler ViewpagerHandler=null,CameraHandler=null,ScoreHandler=null,CartHandler=null;
    LoadingDialogBox loadingDialogBox;
    Boolean loaded_first_page,loaded_second_page,loaded_third_page,loaded_forth_page;
    Boolean was_success_loaded_first_page,was_success_loaded_second_page,was_success_loaded_third_page,was_success_loaded_forth_page;

    String url = StaticCatelog.geturl()+"cersei/consumer/location";

    NoInternetDialogBox noInternetDialogBox=null;

    View filter_Icon;


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
        ((TextView)selectLocation.findViewById(R.id.text_location)).setText(StaticCatelog.getStringProperty(this, "location"));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.findViewById(R.id.lay_prof).setOnClickListener(this);
        navigationView.findViewById(R.id.lay_myorders).setOnClickListener(this);
        navigationView.findViewById(R.id.lay_myaddresses).setOnClickListener(this);
        navigationView.findViewById(R.id.lay_savedcards).setOnClickListener(this);
        navigationView.findViewById(R.id.lay_coupons).setOnClickListener(this);
        navigationView.findViewById(R.id.lay_settings).setOnClickListener(this);

        filter_Icon=findViewById(R.id.filter_icon);

        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.myviewpager);
        strip = (PagerSlidingStrip) findViewById(R.id.tabs);
        mPager.setAdapter(mAdapter);
        strip.setViewPager(mPager);
        strip.manageFilterIcon(filter_Icon);
        mPager.setOffscreenPageLimit(4);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == page_offers) {
                    ViewpagerHandler.adjustCameraOrViewPager(true);
                } else
                    ViewpagerHandler.adjustCameraOrViewPager(false);
                if (position == page_points) {
                    redText(tab_point);
                    ScoreHandler.adjustCameraOrViewPager(true);
                }
                if (position == page_barcode)
                    CameraHandler.adjustCameraOrViewPager(true);
                else
                    CameraHandler.adjustCameraOrViewPager(false);
                if (position == page_cart) {
                    CartHandler.adjustCameraOrViewPager(true);
                    ((TabsView) tab_cart).removeCartNotification();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tab_point = strip.returntab(page_points);
        tab_cart = strip.returntab(page_cart);
        filter_Icon.setOnClickListener(this);
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
        else if(id==R.id.filter_icon)
        {
            createFilterPopup();
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
        if(position==page_offers)
            ViewpagerHandler=eventHandler;
        else if(position==page_points)
            ScoreHandler=eventHandler;
        else if(position==page_barcode)
            CameraHandler=eventHandler;
        else if(position==page_cart)
            CartHandler=eventHandler;
    }

    @Override
    public void MyloadingCompleted(int position,Boolean successfull) {
        switch (position)
        {
            case page_offers: loaded_first_page=true; was_success_loaded_first_page=successfull; break;
            case page_points: loaded_second_page=true; was_success_loaded_second_page=successfull; break;
            case page_barcode: loaded_third_page=true; was_success_loaded_third_page=successfull; break;
            case page_cart: loaded_forth_page=true; was_success_loaded_forth_page=successfull; break;
        }
        if(loaded_first_page&&loaded_second_page&&loaded_third_page&&loaded_forth_page)
        {
            end_update_ui_for_location();
        }
    }

    @Override
    public void updateCart(Boolean add) {
        ((TabsView)tab_cart).giveCartNotification(add);

    }

    @Override
    public void Reload_No_Internet() {
        update_ui_for_location_withevent(StaticCatelog.getStringProperty(this,"location"));
    }


    public class MyAdapter extends FragmentPagerAdapter implements IconTabProvider {

        private int tabIcons[] = {R.string.ico_home,R.string.rating, R.string.ico_qr_code, R.string.ico_cart};


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
                case page_offers :
                    Fragment_Offers firstPage = Fragment_Offers.init(position);
                    firstPage.addInitialisationEvent(MainDashboard.this);
                    return firstPage;
                case page_points :
                    Fragment_Points pointsFragment = Fragment_Points.init(position);
                    pointsFragment.addInitialisationEvent(MainDashboard.this);
                    return pointsFragment;
                case page_barcode :
                    Fragment_Barcode fragment= Fragment_Barcode.init(position);
                    fragment.addInitialisationEvent(MainDashboard.this);
                    return fragment;
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
        CartHandler.startLoadbylocation(location);
        update_ui_for_location();
    }

    public void update_ui_for_location()
    {
        loadingDialogBox.setUpLayout();
        reset_loaded_fragments();


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

            if(noInternetDialogBox==null)
                noInternetDialogBox= new NoInternetDialogBox(this, R.style.alert_dialog);
            noInternetDialogBox.setUpLayout(this);

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
     //   if(dialog==null)
         dialog = new LocationPopup(this, R.style.alert_dialog);

        if(success==1)
        {   if(!dialog.isShowing())
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
            Toast.makeText(this,"Fetching Locations Please Wait...",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainDashboard.this,"Error! During fetching locations",Toast.LENGTH_SHORT).show();
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }
        else
        {
            getLocation();
        }
    }

    public void createFilterPopup()
    {
       // if(filter_popup==null)
  //      filter_popup = new Popup_Filter(this, R.style.alert_dialog);
  //      filter_popup.setUpLayout();
    Toast.makeText(this,"Filter To be Added",Toast.LENGTH_SHORT).show();
    }

}
