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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import co.jlabs.cersei_retailer.ActivityTransition.ActivityTransition;
import co.jlabs.cersei_retailer.custom_components.VolleyImageInterface;
import co.jlabs.cersei_retailer.custom_components.transforms.*;

/**
 * Created by Pradeep on 1/1/2016.
 */

public class Details extends Activity {

    TextView tv_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailspage);
        int position=getIntent().getIntExtra("position",1);
        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
        tv_position = (TextView) findViewById(R.id.position);
        vpPager.setClipToPadding(false);
        vpPager.setPageMargin(-20);
        CustomPagerAdapter adapter = new CustomPagerAdapter(this);
        vpPager.setAdapter(adapter);
        vpPager.setCurrentItem(position - 1);
        tv_position.setText("" + position + " of 50 offers");
        vpPager.setPageTransformer(true, new MyCubeOutTransformer());
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_position.setText("" + (position + 1) + " of 50 offers");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ActivityTransition.with(getIntent()).to(vpPager).start(savedInstanceState);
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ImageLoader imageLoader;

        public CustomPagerAdapter(Context context) {
            mContext = context;
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
            return 50;
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
            NetworkImageView Pic= (NetworkImageView) itemView.findViewById(R.id.pic);
            String s="";
            if(position%10==0)
            {
                s= "20% off on Fortune Oil";
                Pic.setImageUrl("http://agromantra.com/uploads/vendor/banner_31.jpg", imageLoader);
            }
            else if(position%10==1)
            {
                s= "40% off on M.D.H";
                Pic.setImageUrl("http://martjackstorage.blob.core.windows.net/in-resources/8ebea48a-1ea1-4eba-987d-4f8ab57370a4/Images/ProductImages/Source/ID03082_100.jpg", imageLoader);
            }
            else if(position%10==2)
            {
                s= "10% off on Curd Farm";
                Pic.setImageUrl("http://www.bigbasket.com/media/uploads/p/s/40003162_1-milky-mist-curd-farm-fresh.jpg", imageLoader);
            }
            else if(position%10==3)
            {
                s= "25% off on Colgate";
                Pic.setImageUrl("http://www.bigbasket.com/media/uploads/p/s/40001077_1-colgate-toothpaste-visible-white.jpg", imageLoader);
            }
            else if(position%10==4)
            {
                s= "29% off on Lifebuoy";
                Pic.setImageUrl("http://www.daangar.com/wp-content/uploads/2015/03/lifebuoy.jpg", imageLoader);
            }
            else if(position%10==5)
            {
                s= "20% off on MatchBox";
                Pic.setImageUrl("http://www.bigbasket.com/media/uploads/p/s/40027412_1-home-lite-matchbox-big.jpg", imageLoader);
            }
            else if(position%10==6)
            {
                s= "99% off on Scrubber";
                Pic.setImageUrl("http://www.bigbasket.com/media/uploads/p/s/40003996_1-good-home-stainless-steel-scrubber-magnetic-grade.jpg", imageLoader);
            }
            else if(position%10==7)
            {
                s= "20% off on Nescafe";
                Pic.setImageUrl("http://www.bigbasket.com/media/uploads/p/s/267376_2-nescafe-coffee-classic.jpg", imageLoader);
            }
            else if(position%10==8)
            {
                s= "20% off on Parachute Oil";
                Pic.setImageUrl("http://p1.zopnow.com/images/products/320/parachute-parachute-advansed-coconut-hair-oil-300-ml.png", imageLoader);
            }
            else
            {
                s= "20% off on Maggi";
                Pic.setImageUrl("http://www.quizified.in/uploads/266165_1-maggi-meri-masala.jpg", imageLoader);
            }

            ((TextView)itemView.findViewById(R.id.title)).setText(s);
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
