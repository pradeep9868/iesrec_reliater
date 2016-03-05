package co.jlabs.cersei_retailer;

/**
 * Created by Wadi on 19-12-2015.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.jlabs.cersei_retailer.custom_components.AddOrRemoveCart;
import co.jlabs.cersei_retailer.custom_components.Class_Cart;
import co.jlabs.cersei_retailer.custom_components.MyImageView;
import co.jlabs.cersei_retailer.custom_components.Sqlite_cart;
import co.jlabs.cersei_retailer.custom_components.TextView_Triangle;
import co.jlabs.cersei_retailer.custom_components.VolleyImageInterface;


public class Adapter_Cart extends BaseAdapter {
    private Context context;
    ImageLoader imageLoader;
    ArrayList<Class_Cart> offers_list;
    Sqlite_cart cart;
    Fragment_Cart.Total_item_in_cart_text_handler totalItemInCartTextHandler;
    private int lastPosition = -1;
    Animation animation;


    public Adapter_Cart(Context context,ArrayList<Class_Cart>  offers_list,Fragment_Cart.Total_item_in_cart_text_handler handler) {
        this.context = context;
        cart=new Sqlite_cart(context);
        this.offers_list=offers_list;
        this.totalItemInCartTextHandler=handler;
        imageLoader = AppController.getInstance().getImageLoader();
    }

    static class ViewHolder
    {
        TextView title;
        TextView weight;
        TextView price;
        TextView_Triangle points;
        MyImageView Pic;
        AddOrRemoveCart addOrRemoveCart;
        View Close;
    }

    public View getView(int position, View gridView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder;

        if (gridView == null) {

            gridView = inflater.inflate(R.layout.adap_cart, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) gridView.findViewById(R.id.title);
            viewHolder.weight = (TextView) gridView.findViewById(R.id.weight);
            viewHolder.price = (TextView) gridView.findViewById(R.id.price);
            viewHolder.points = (TextView_Triangle) gridView.findViewById(R.id.points);
            viewHolder.Pic= (MyImageView) gridView.findViewById(R.id.pic);
            viewHolder.addOrRemoveCart= (AddOrRemoveCart) gridView.findViewById(R.id.add_or_remove_cart);
            viewHolder.Close=gridView.findViewById(R.id.close);
            gridView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) gridView.getTag();
        }
        viewHolder.title.setText(offers_list.get(position).title);
        viewHolder.weight.setText(offers_list.get(position).weight);
        viewHolder.price.setText("Rs " + offers_list.get(position).price);
        viewHolder.points.setText("" + context.getResources().getString(R.string.rating) + offers_list.get(position).point);
        viewHolder.Pic.setImageUrl(offers_list.get(position).img, imageLoader);
        viewHolder.Close.setTag(position);
        viewHolder.Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(context, "Removed From Cart", Toast.LENGTH_SHORT).show();
                int quantity = cart.deleteFromCart(offers_list.get((int) v.getTag()).offer_id);
                double price=offers_list.get((int) v.getTag()).price;
                totalItemInCartTextHandler.handleText_cart(-quantity,price*-quantity);
                View main = ((ViewGroup) v.getParent().getParent().getParent().getParent());
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                main.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        offers_list.remove((int) v.getTag());
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

        viewHolder.addOrRemoveCart.addOnItemClickListner(new AddOrRemoveCart.ItemsClickListener() {
            @Override
            public int addItemClicked(int position) {
                Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
                totalItemInCartTextHandler.handleText_cart(1,offers_list.get(position).price);
                return cart.addToCart(offers_list.get(position));
            }

            @Override
            public int removeItemClicked(View v,final int position) {
                Toast.makeText(context, "Removed From Cart", Toast.LENGTH_SHORT).show();

                int quantity = cart.removeFromCart(offers_list.get(position).offer_id);
                if (quantity == 0) {
                    View main = ((ViewGroup) v.getParent().getParent().getParent().getParent().getParent().getParent().getParent());
                    //Log.i("Myapp", "" + main.getId());
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                    main.startAnimation(animation);

                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            offers_list.remove(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
                totalItemInCartTextHandler.handleText_cart(-1,-offers_list.get(position).price);
                return quantity;
            }
        }, position, offers_list.get(position).quantity);
        if((position > lastPosition)) {
            animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
            gridView.startAnimation(animation);
        }
        lastPosition = position;

        return gridView;
    }

    @Override
    public int getCount() {
        return offers_list.size();
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