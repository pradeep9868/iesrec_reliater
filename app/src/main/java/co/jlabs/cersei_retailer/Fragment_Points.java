package co.jlabs.cersei_retailer;

/**
 * Created by Pradeep on 12/25/2015.
 */
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.TextView;

import co.jlabs.cersei_retailer.custom_components.PagerSlidingStripPoints;
import co.jlabs.cersei_retailer.custom_components.SampleListView;
import co.jlabs.cersei_retailer.custom_components.ScrollTabHolder;
import co.jlabs.cersei_retailer.custom_components.SmallBangListener;
import co.jlabs.cersei_retailer.custom_components.StarBang;

public class Fragment_Points extends Fragment  implements ScrollTabHolder, ViewPager.OnPageChangeListener,FragmentEventHandler {
    int fragVal;


    public static final boolean NEEDS_PROXY = Integer.valueOf(Build.VERSION.SDK_INT).intValue() < 11;

    private View mHeader;

    private PagerSlidingStripPoints mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private int mMinHeaderHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private StarBang mSmallBang;

    private TextView info;
    private int mLastY;
    FragmentsEventInitialiser eventInitialiser=null;


    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();
    private AccelerateDecelerateInterpolator mSmoothInterpolator;

    View from_balance,to_balance,from_point,to_point,from_rating,to_rating;

    static Fragment_Points init(int val) {
        Fragment_Points truitonFrag = new Fragment_Points();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSmallBang = StarBang.attach2Window(getActivity());
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.lay_points, container,
                false);

        mSmoothInterpolator = new AccelerateDecelerateInterpolator();

        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight;


        to_balance=layoutView.findViewById(R.id.to_balance);
        to_point=layoutView.findViewById(R.id.to_points);
        to_rating=layoutView.findViewById(R.id.to_rating);
        mHeader = layoutView.findViewById(R.id.header);
        from_balance=mHeader.findViewById(R.id.totalbalance);
        from_point=mHeader.findViewById(R.id.points);
        from_rating=mHeader.findViewById(R.id.rating);

        info = (TextView) layoutView.findViewById(R.id.info);

        mPagerSlidingTabStrip = (PagerSlidingStripPoints) layoutView.findViewById(R.id.tabs);
        mViewPager = (ViewPager) layoutView.findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);

        mPagerAdapter = new PagerAdapter(getContext());
        mPagerAdapter.setTabHolderScrollingContent(this);

        mViewPager.setAdapter(mPagerAdapter);

        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(this);
        mLastY=0;

        return layoutView;
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {
        // nothing
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (positionOffsetPixels > 0) {
            int currentItem = mViewPager.getCurrentItem();

            SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
            ScrollTabHolder currentHolder;

            if (position < currentItem) {
                currentHolder = scrollTabHolders.valueAt(position);
            } else {
                currentHolder = scrollTabHolders.valueAt(position + 1);
            }

            if (NEEDS_PROXY) {
                // TODO is not good
                currentHolder.adjustScroll(mHeader.getHeight() - mLastY);
                mHeader.postInvalidate();
            } else {
                currentHolder.adjustScroll((int) (mHeader.getHeight() + mHeader.getTranslationY()));
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
        ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
        if(NEEDS_PROXY){
            //TODO is not good
            currentHolder.adjustScroll(mHeader.getHeight()-mLastY);
            mHeader.postInvalidate();
        }else{
            currentHolder.adjustScroll((int) (mHeader.getHeight() +mHeader.getTranslationY()));
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            int scrollY = getScrollY(view);
            if(NEEDS_PROXY){
                mLastY=-Math.max(-scrollY, mMinHeaderTranslation);
                info.setText(String.valueOf(scrollY));
                mHeader.scrollTo(0, mLastY);
                mHeader.postInvalidate();
            }else{
                mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
            }

            float ratio = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);

            interpolate( from_balance,to_balance, mSmoothInterpolator.getInterpolation(ratio));
            interpolate(from_point,to_point, mSmoothInterpolator.getInterpolation(ratio));
            interpolate(from_rating,to_rating, mSmoothInterpolator.getInterpolation(ratio));
        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        // nothing
    }

    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderHeight;
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    @Override
    public void adjustCameraOrViewPager(boolean on) {
        animateTextView(4145, 5273, (TextView) from_point);
        ((TextView)to_point).setText("5273");
    }

    @Override
    public void startLoadbylocation(String location) {
        //here I will load Only first list that shows offers based on location
        tellThatLoadedSuccessfully();
    }


    private class PagerAdapter extends android.support.v4.view.PagerAdapter implements PagerSlidingStripPoints.IconTabProvider {

        final String[] MOBILE_OS = new String[] {
                "Android", "iOS","Windows", "Blackberry","Android", "iOS","Windows", "Blackberry" };
        final String[] MOBILE_OS1 = new String[] {
                "Android", "iOS","Windows" };
        final String[] MOBILE_OS2 = new String[] {
                "Android", "iOS","Windows", "Blackberry","Android", "iOS","Windows", "Blackberry", "iOS","Windows", "Blackberry","Android", "iOS","Windows", "Blackberry" };
        private String tabIcons[] = {"10\n\nRewards", "2458\n\nPoints Earned","1280\n\nPoints Redeemed"};

        private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
        private ScrollTabHolder mListener;
        Context context;

        PagerAdapter(Context context)
        {
            mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
            this.context=context;
        }

        public int getCount() {
            return 3;
        }

        public Object instantiateItem(View collection, int position) {

            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            SampleListView lv = new SampleListView(context,inflater,position);



            mScrollTabHolders.put(position, lv);
            if (mListener != null) {
                lv.setScrollTabHolder(mListener);
            }
            switch (position) {
                default:
                    lv.setAdapter(new AdapterReward_Redeemed(context, MOBILE_OS,0));
                    break;
                case 0:
                    lv.setAdapter(new AdapterReward_Redeemed(context, MOBILE_OS,0));
                    break;
                case 1:
                    lv.setAdapter(new AdapterPointsEarned(context, MOBILE_OS1));
                    break;
                case 2:
                    lv.setAdapter(new AdapterReward_Redeemed(context, MOBILE_OS2,1));
                    break;
            }

            ((ViewPager) collection).addView(lv, 0);

            return lv;
        }

        @Override
        public String getPageIconResId(int position) {
            return tabIcons[position];
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);

        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        public void setTabHolderScrollingContent(ScrollTabHolder listener) {
            mListener = listener;
        }
        public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
            return mScrollTabHolders;
        }
    }

    private void interpolate(View view1, View view2, float interpolation) {
        getOnScreenRect(mRect1, view1);
        getOnScreenRect(mRect2, view2);

        float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
        float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

        view1.setTranslationX(translationX);
        view1.setTranslationY(translationY - mHeader.getTranslationY());
        view1.setScaleX(scaleX);
        view1.setScaleY(scaleY);
    }

    private RectF getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }

    public void animateTextView(int initialValue, int finalValue, final TextView  textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt((int)initialValue, (int)finalValue);
        valueAnimator.setDuration(1500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textview.setText(valueAnimator.getAnimatedValue().toString());
            }

        });
        valueAnimator.start();
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                redText(from_point);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    public void redText(View view){
        //((TextView)tab_point).setTextColor(0xFFCD8BF8);
        mSmallBang.bang(view, 300, new SmallBangListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                // Toast.makeText(MainDashboard.this,"Added",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void addInitialisationEvent(FragmentsEventInitialiser eventInitialiser)
    {
        this.eventInitialiser=eventInitialiser;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(eventInitialiser!=null)
            eventInitialiser.registerMyevent(3,this);
        tellThatLoadedSuccessfully();

    }


    public void tellThatLoadedSuccessfully()
    {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (eventInitialiser != null)
                    eventInitialiser.MyloadingCompleted(3);
            }
        }, 1000);

    }
}