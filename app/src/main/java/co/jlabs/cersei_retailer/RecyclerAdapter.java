package co.jlabs.cersei_retailer;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.jlabs.cersei_retailer.ActivityTransition.ActivityTransitionLauncher;
import co.jlabs.cersei_retailer.custom_components.AddOrRemoveCart;
import co.jlabs.cersei_retailer.custom_components.MyImageView;
import co.jlabs.cersei_retailer.custom_components.Sqlite_cart;
import co.jlabs.cersei_retailer.custom_components.VolleyImageInterface;

/**
 * Created by Pradeep on 12/31/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int VIEW_HEADER = 0;
    private static final int VIEW_NORMAL = 1;
    private View headerView;
    ImageLoader imageLoader;
    JSONArray json_offers;
    Sqlite_cart cart;
    Context context;
    RecyclerAdapter adapter;
    FragmentsEventInitialiser eventInitialiser;
    //private int lastPosition = -1;


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
            public HeaderViewHolder(View v) {
                super(v);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            View gridItem;
            TextView text,deliverable;
            View mask;
            MyImageView Pic;
            AddOrRemoveCart addOrRemoveCart;


            public ViewHolder(View v) {
                super(v);
                gridItem = v;
                text=((TextView)v.findViewById(R.id.text));
                Pic=(MyImageView) v.findViewById(R.id.pic);
                deliverable= (TextView) v.findViewById(R.id.deliverable);
                mask=v.findViewById(R.id.mask);
                addOrRemoveCart= (AddOrRemoveCart) v.findViewById(R.id.add_or_remove_cart);
            }
        }



        public RecyclerAdapter(Context context,JSONArray json,FragmentsEventInitialiser eventInitialiser) {
            this.adapter=this;
            this.json_offers=json;
            cart = new Sqlite_cart(context);
            this.context=context;
            imageLoader = AppController.getInstance().getImageLoader();
            this.eventInitialiser=eventInitialiser;
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
                final ViewHolder holder = (ViewHolder) viewHolder;
                String s1,s2,s3;

                try {
                    s1=((JSONObject) json_offers.get(position - 1)).getString("title");
                    s2=((JSONObject) json_offers.get(position - 1)).getString("weight");
                    s3=((JSONObject) json_offers.get(position - 1)).getString("price");
                    holder.text.setText(StaticCatelog.SpanIt2(s1, s2, s3));
                    holder.Pic.setImageUrl(((JSONObject) json_offers.get(position - 1)).getString("img"), imageLoader);
                    holder.deliverable.setTextColor(((JSONObject) json_offers.get(position - 1)).getBoolean("delivery")? 0xffFFA000:0xfff20022);
                    holder.Pic.setOnImageChangeListner(new VolleyImageInterface() {
                        @Override
                        public void adjustColor(int color) {
                            holder.mask.setBackgroundColor(color);
                        }
                    });
                    holder.addOrRemoveCart.addOnItemClickListner(new AddOrRemoveCart.ItemsClickListener() {
                        @Override
                        public int addItemClicked(int position) {
                            Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
                            int quantity=0;
                            eventInitialiser.updateCart(true);
                            try{

                                quantity=cart.addToCart(((JSONObject) json_offers.get(position - 1)));
                            }
                            catch (Exception e)
                            {

                            }

                            return quantity;
                        }

                        @Override
                        public int removeItemClicked(View v,int position) {
                            Toast.makeText(context,"Removed From Cart",Toast.LENGTH_SHORT).show();
                            int quantity=0;
                            eventInitialiser.updateCart(false);
                            try {
                                quantity=cart.removeFromCart(((JSONObject) json_offers.get((position - 1))).getInt("offer_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return quantity;
                        }
                    },position,cart.findIfOfferAlreadyExistsInCart(((JSONObject) json_offers.get((position - 1))).getInt("offer_id")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                holder.gridItem.setOnClickListener(this);
                holder.gridItem.setTag(position);
            }

//            Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//            viewHolder.itemView.startAnimation(animation);
//            lastPosition = position;
        }

    @Override
    public void onClick(View v) {
        //eventHandler.adjustCameraOrViewPager(false);
        Intent i = new Intent(v.getContext(),Details.class);
        i.putExtra("position",Integer.parseInt(v.getTag().toString()));
        i.putExtra("offers",json_offers.toString());
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