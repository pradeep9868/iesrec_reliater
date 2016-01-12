package co.jlabs.cersei_retailer;

/**
 * Created by Pradeep on 12/25/2015.
 */
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Fragment_Cart extends Fragment implements FragmentEventHandler {
    int fragVal;
    FragmentsEventInitialiser eventInitialiser=null;

    static Fragment_Cart init(int val) {
        Fragment_Cart truitonFrag = new Fragment_Cart();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.cart_page, container,false);
        ListView lv = (ListView) layoutView.findViewById(R.id.list_view);
        Adapter_Cart adapter_cart = new Adapter_Cart(getContext());
        lv.setAdapter(adapter_cart);
        return layoutView;
    }


    @Override
    public void adjustCameraOrViewPager(boolean on) {
        //Do nothing for this
    }

    @Override
    public void startLoadbylocation(String location) {
        //will be requiring to delete items on cart that are not in current location
        tellThatLoadedSuccessfully();
    }

    public void addInitialisationEvent(FragmentsEventInitialiser eventInitialiser)
    {
        this.eventInitialiser=eventInitialiser;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(eventInitialiser!=null)
            eventInitialiser.registerMyevent(4,this);
        tellThatLoadedSuccessfully();

    }

    public void tellThatLoadedSuccessfully()
    {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (eventInitialiser != null)
                    eventInitialiser.MyloadingCompleted(4);
            }
        }, 1000);

    }
}