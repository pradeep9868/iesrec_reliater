package co.jlabs.cersei_retailer;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.jlabs.cersei_retailer.ActivityTransition.ActivityTransitionLauncher;
import co.jlabs.cersei_retailer.custom_components.MyImageView;
import co.jlabs.cersei_retailer.custom_components.VolleyImageInterface;

/**
 * Created by Pradeep on 12/31/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int VIEW_HEADER = 0;
    private static final int VIEW_NORMAL = 1;
    private View headerView;
    ImageLoader imageLoader;
    private FragmentEventHandler eventHandler;
    JSONArray json_offers;


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
            public HeaderViewHolder(View v) {
                super(v);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public View textView;

            public ViewHolder(View v) {
                super(v);
                textView = v;
            }
        }

        public RecyclerAdapter(FragmentEventHandler eventHandler ,JSONArray json) {
            this.eventHandler=eventHandler;
            this.json_offers=json;
            imageLoader = AppController.getInstance().getImageLoader();
        }

        public void setHeader(View v) {
            this.headerView = v;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? VIEW_HEADER : VIEW_NORMAL;
        }

        @Override
        public int getItemCount() {
            int count=0;
            try
            {
                count=json_offers.length()+1;
            }
            catch (Exception e)
            {

            }

            return count;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_HEADER) {
                return new HeaderViewHolder(headerView);

            } else {
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
                return new ViewHolder(textView);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

            if (position == 0)
            {
                return;
            }
            else {
                ViewHolder holder = (ViewHolder) viewHolder;
                final View gridView = holder.textView;
                MyImageView Pic= (MyImageView) gridView.findViewById(R.id.pic);
                String s="";

                try {
                    s=((JSONObject) json_offers.get(position - 1)).getString("title")+"\n\n";
                    s=s+((JSONObject) json_offers.get(position - 1)).getString("weight")+"\n\n";
                    s=s+((JSONObject) json_offers.get(position - 1)).getString("price");
                    ((TextView)gridView.findViewById(R.id.text)).setText(s);
                    Pic.setImageUrl(((JSONObject) json_offers.get(position - 1)).getString("img"), imageLoader);
                    Pic.setOnImageChangeListner(new VolleyImageInterface() {
                        @Override
                        public void adjustColor(int color) {
                            gridView.findViewById(R.id.mask).setBackgroundColor(color);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

//
//                if(position%10==1)
//                {
//                    s= "20% off on Fortune Oil\n\n1.0L\n\nRs 80";
//                    Pic.setImageUrl("http://agromantra.com/uploads/vendor/banner_31.jpg", imageLoader);
//                }
//                else if(position%10==2)
//                {
//                    s= "40% off on M.D.H\n\n500gm\n\nRs 120";
//                    Pic.setImageUrl("http://martjackstorage.blob.core.windows.net/in-resources/8ebea48a-1ea1-4eba-987d-4f8ab57370a4/Images/ProductImages/Source/ID03082_100.jpg", imageLoader);
//                }
//                else if(position%10==3)
//                {
//                    s= "10% off on Curd Farm\n\n1.0L\n\nRs 80";
//                    Pic.setImageUrl("http://www.bigbasket.com/media/uploads/p/s/40003162_1-milky-mist-curd-farm-fresh.jpg", imageLoader);
//                }
//                else if(position%10==4)
//                {
//                    s= "25% off on Colgate\n\n500gm\n\nRs 220";
//                    Pic.setImageUrl("http://www.bigbasket.com/media/uploads/p/s/40001077_1-colgate-toothpaste-visible-white.jpg", imageLoader);
//                }
//                else if(position%10==5)
//                {
//                    s= "29% off on Lifebuoy\n\n1.0L\n\nRs 80";
//                    Pic.setImageUrl("http://www.daangar.com/wp-content/uploads/2015/03/lifebuoy.jpg", imageLoader);
//                }
//                else if(position%10==6)
//                {
//                    s= "20% off on MatchBox\n\n5 pcs\n\nRs 25";
//                    Pic.setImageUrl("http://www.bigbasket.com/media/uploads/p/s/40027412_1-home-lite-matchbox-big.jpg", imageLoader);
//                }
//                else if(position%10==7)
//                {
//                    s= "99% off on Scrubber\n\n1.0L\n\nRs 8";
//                    Pic.setImageUrl("http://www.bigbasket.com/media/uploads/p/s/40003996_1-good-home-stainless-steel-scrubber-magnetic-grade.jpg", imageLoader);
//                }
//                else if(position%10==8)
//                {
//                    s= "20% off on Nescafe\n\n500gm\n\nRs 160";
//                    Pic.setImageUrl("http://www.bigbasket.com/media/uploads/p/s/267376_2-nescafe-coffee-classic.jpg", imageLoader);
//                }
//                else if(position%10==9)
//                {
//                    s= "20% off on Parachute Oil\n\n350gm\n\nRs 180";
//                    Pic.setImageUrl("http://p1.zopnow.com/images/products/320/parachute-parachute-advansed-coconut-hair-oil-300-ml.png", imageLoader);
//                }
//                else
//                {
//                    s= "20% off on Maggi\n\n500gm\n\nRs 120";
//                    Pic.setImageUrl("http://www.quizified.in/uploads/266165_1-maggi-meri-masala.jpg", imageLoader);
//                }


                gridView.setOnClickListener(this);
                gridView.setTag(position);
            }
        }

    @Override
    public void onClick(View v) {
        //eventHandler.adjustCameraOrViewPager(false);
        Intent i = new Intent(v.getContext(),Details.class);
        i.putExtra("position",Integer.parseInt(v.getTag().toString()));
        ActivityTransitionLauncher.with(getActivity(v)).from(v).launch(i);
        //v.getContext().startActivity(i);
    }

    private Activity getActivity(View v) {
        Context context = v.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}