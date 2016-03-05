package co.jlabs.cersei_retailer.custom_components;

/**
 * Created by JussConnect on 7/2/2015.
 */
public class Class_Cart {
    int id;
    public Integer offer_id;
    public String title;
    public String weight;
    public Integer price;
    public Integer point;
    public String img;
    public Integer deliverable;
    public Integer quantity;
    Class_Cart(){
        offer_id = new Integer("0");
        title = new String();
        weight= new String();
        price= new Integer("0");
        point = new Integer("0");
        img= new String();
        deliverable = new Integer("0");
        quantity = new Integer("0");
    }
}
