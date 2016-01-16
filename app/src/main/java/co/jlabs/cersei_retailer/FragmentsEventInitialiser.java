package co.jlabs.cersei_retailer;

/**
 * Created by Pradeep on 1/3/2016.
 */
public interface FragmentsEventInitialiser {
    void registerMyevent(int position,FragmentEventHandler eventHandler);
    void MyloadingCompleted(int position,Boolean Successfull);
    void updateCart(Boolean add);
}
