package co.jlabs.cersei_retailer;

/**
 * Created by Wadi on 19-12-2015.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
            public void onClick(View v) {
                Toast.makeText(context,"Removed From Cart",Toast.LENGTH_SHORT).show();
                int quantity=cart.deleteFromCart(offers_list.get((int)v.getTag()).offer_id);
                totalItemInCartTextHandler.handleText_cart(-quantity);
                offers_list.remove((int)v.getTag());
                notifyDataSetChanged();
            }
        });

        viewHolder.addOrRemoveCart.addOnItemClickListner(new AddOrRemoveCart.ItemsClickListener() {
            @Override
            public int addItemClicked(int position) {
                Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
                totalItemInCartTextHandler.handleText_cart(1);
                return cart.addToCart(offers_list.get(position));
            }

            @Override
            public int removeItemClicked(int position) {
                Toast.makeText(context,"Removed From Cart",Toast.LENGTH_SHORT).show();

                int quantity=cart.removeFromCart(offers_list.get(position).offer_id);
                if (quantity == 0) {
                    offers_list.remove(position);
                    notifyDataSetChanged();
                }
                totalItemInCartTextHandler.handleText_cart(-1);
                return quantity;
            }
        },position,offers_list.get(position).quantity);


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