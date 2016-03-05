package co.jlabs.cersei_retailer;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.jlabs.cersei_retailer.ActivityTransition.ActivityTransition;
import co.jlabs.cersei_retailer.custom_components.AddOrRemoveCart;
import co.jlabs.cersei_retailer.custom_components.Sqlite_cart;
import co.jlabs.cersei_retailer.custom_components.VolleyImageInterface;
import co.jlabs.cersei_retailer.custom_components.transforms.*;

/**
 * Created by Pradeep on 1/1/2016.
 */

public class Details extends Activity {

    TextView tv_position;
    int num_item=0;
    Context context;
    Sqlite_cart cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailspage);
        JSONArray offers=null;
        context=this;
        int position=getIntent().getIntExtra("position",1);
        try {
            offers = new JSONArray(getIntent().getStringExtra("offers"));
            num_item=offers.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cart=new Sqlite_cart(this);
        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
        tv_position = (TextView) findViewById(R.id.position);
        vpPager.setClipToPadding(false);
        vpPager.setPageMargin(-20);
        OffersPages adapter = new OffersPages(this,offers);
        vpPager.setAdapter(adapter);
        vpPager.setCurrentItem(position - 1);
        tv_position.setText("" + position + " of "+offers.length()+" offers");
        vpPager.setPageTransformer(true, new MyCubeOutTransformer());
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_position.setText("" + (position + 1) + " of "+num_item+" offers");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.dismissifclickedhere).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActivityTransition.with(getIntent()).to(vpPager).start(savedInstanceState);
    }

    class OffersPages extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ImageLoader imageLoader;
        JSONArray offers;

        public OffersPages(Context context,JSONArray offers) {
            mContext = context;
            this.offers=offers;
            mLayoutInflater = LayoutInflater.from(mContext);
            imageLoader = AppController.getInstance().getImageLoader();
        }

        @Override
        public float getPageWidth(int position) {
            return 0.93f;
        }

        // Returns the number of pages to be displayed in the ViewPager.
        @Override
        public int getCount() {
            return offers.length();
        }

        // Returns true if a particular object (page) is from a particular page
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // This method should create the page for the given position passed to it as an argument.
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate the layout for the page
            View itemView = mLayoutInflater.inflate(R.layout.adap_details, container, false);
            try {
                ((NetworkImageView) itemView.findViewById(R.id.pic)).setImageUrl(((JSONObject)offers.get(position)).getString("img"), imageLoader);
                ((TextView) itemView.findViewById(R.id.title)).setText(((JSONObject) offers.get(position)).getString("title"));
                ((TextView) itemView.findViewById(R.id.price)).setText(((JSONObject) offers.get(position)).getString("price"));
                ((AddOrRemoveCart)itemView.findViewById(R.id.add_or_remove_cart)).addOnItemClickListner(new AddOrRemoveCart.ItemsClickListener() {
                    @Override
                    public int addItemClicked(int position) {
                        Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
                        int quantity = 0;

                        try {

                            quantity = cart.addToCart(((JSONObject) offers.get(position)));
                        } catch (Exception e) {

                        }

                        return quantity;
                    }

                    @Override
                    public int removeItemClicked(View v,int position) {
                        Toast.makeText(context, "Removed From Cart", Toast.LENGTH_SHORT).show();
                        int quantity = 0;

                        try {
                            quantity = cart.removeFromCart(((JSONObject) offers.get((position))).getInt("offer_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return quantity;
                    }
                }, position, cart.findIfOfferAlreadyExistsInCart(((JSONObject) offers.get((position))).getInt("offer_id")));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            container.addView(itemView);
            return itemView;
        }

        // Removes the page from the container for the given position.
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }



}
