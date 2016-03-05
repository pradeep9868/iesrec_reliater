package co.jlabs.cersei_retailer.custom_components;

//Created by pradeep kumar (Jussconnect)

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Sqlite_cart extends SQLiteOpenHelper {

	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Cart";
    Context context;

	public Sqlite_cart(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;

    }

	@Override
	public void onCreate(SQLiteDatabase db) {
 		String CREATE_CartED_TABLE = "CREATE TABLE Cart ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                "offer_id INTEGER, " +
				"title TEXT, "+
				"weight TEXT,"+
                "price INTEGER,"+
				"point INTEGER,"+
				"img TEXT,"+
                "deliverable INTEGER,"+
				"quantity INTEGER);";

 		db.execSQL(CREATE_CartED_TABLE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Cart");

        this.onCreate(db);
	}
	//---------------------------------------------------------------------

	/**
     * CRUD operations (create "add", read "get", update, delete)
     */

    private static final String TABLE_Cart = "Cart";

    private static final String KEY_ID = "id";
    private static final String KEY_OFFER_ID = "offer_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_PRICE = "price";
    private static final String KEY_POINT = "point";
    private static final String KEY_IMG = "img";
    private static final String KEY_DEL = "deliverable";
    private static final String KEY_QUANTITY = "quantity";

    public int addToCart(Class_Cart tp){

        int quantity = findIfOfferAlreadyExistsInCart(tp.offer_id);

        if(quantity>0)
        {
            UpdateQuantity(tp.offer_id,quantity+1);
            quantity=quantity+1;
        }
        else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_OFFER_ID, tp.offer_id);
            values.put(KEY_TITLE, tp.title);
            values.put(KEY_WEIGHT, tp.weight);
            values.put(KEY_PRICE, tp.price);
            values.put(KEY_POINT, tp.point);
            values.put(KEY_IMG, tp.img);
            values.put(KEY_DEL, tp.deliverable);
            values.put(KEY_QUANTITY, 1);
            db.insert(TABLE_Cart, null, values);
            values.clear();

            db.close();
            quantity=1;
        }
        return quantity;
    }

    public int addToCart(JSONObject tp){

        int quantity =0;

        try {

            quantity= findIfOfferAlreadyExistsInCart(tp.getInt("offer_id"));

            if(quantity>0)
            {
                UpdateQuantity(tp.getInt("offer_id"),quantity+1);
                quantity=quantity+1;
            }
            else {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_OFFER_ID, tp.getInt("offer_id"));
                values.put(KEY_TITLE, tp.getString("title"));
                values.put(KEY_WEIGHT, tp.getString("weight"));
                values.put(KEY_PRICE, 80);
                values.put(KEY_POINT, tp.getInt("points"));
                values.put(KEY_IMG, tp.getString("img"));
                values.put(KEY_DEL, tp.getBoolean("delivery")?1:0);
                values.put(KEY_QUANTITY, 1);
                db.insert(TABLE_Cart, null, values);
                values.clear();

                db.close();
                quantity=1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return quantity;
    }

    public ArrayList<Class_Cart> getAllCart() {
    	ArrayList<Class_Cart> Carted = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Cart + " ORDER BY id desc";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {
        	
        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Cart tp = null;
        if (cursor.moveToFirst()) {
            do {
            	tp = new Class_Cart();
                tp.id=(Integer.parseInt(cursor.getString(0)));
            	tp.offer_id=(Integer.parseInt(cursor.getString(1)));
                tp.title=cursor.getString(2);
                tp.weight=cursor.getString(3);
                tp.price=(Integer.parseInt(cursor.getString(4)));
                tp.point=(Integer.parseInt(cursor.getString(5)));
                tp.img=cursor.getString(6);
                tp.deliverable=(Integer.parseInt(cursor.getString(7)));
                tp.quantity=(Integer.parseInt(cursor.getString(8)));
                Carted.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return Carted;
    }

    public int removeFromCart(int Offer_id)
    {
        int quantity = findIfOfferAlreadyExistsInCart(Offer_id);
        if(quantity>1)
        {
            UpdateQuantity(Offer_id,quantity-1);
            quantity=quantity-1;
        }
        else
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_Cart, KEY_OFFER_ID + " = ?", new String[]{String.valueOf(Offer_id)});
            db.close();
            quantity=0;
        }
        return quantity;
    }

    public int deleteFromCart(int Offer_id)
    {
        int quantity = findIfOfferAlreadyExistsInCart(Offer_id);
        if(quantity>=1)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_Cart, KEY_OFFER_ID + " = ?", new String[]{String.valueOf(Offer_id)});
            db.close();
        }
        return quantity;
    }

    public int findIfOfferAlreadyExistsInCart(int offer_id)
    {
        int quantity=0;
        String query = "SELECT  "+KEY_QUANTITY+" FROM " + TABLE_Cart + " WHERE "+KEY_OFFER_ID+" = "+offer_id+"";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            quantity=Integer.parseInt(cursor.getString(0));
        }
        db.close();
        return quantity;
    }

    public void UpdateQuantity(int offer_id,int quantity){
        //String query = "UPDATE "+TABLE_Notification+" set "+KEY_Seen+"='0' WHERE "+KEY_ID+">=0";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, quantity);
        while(db.inTransaction())
        {

        }
        db.update(TABLE_Cart, values, KEY_OFFER_ID + " = " + offer_id, null);
        db.close();
    }


/*
    public String getCartCode(String cID) {
        String rcode="";
        String query = "SELECT  "+KEY_RCODE+" FROM " + TABLE_Cart + " WHERE "+KEY_CID+" = '"+cID+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            rcode=cursor.getString(0);
        }

        if(rcode.equals(""))
        {
            String qry = "SELECT  count("+KEY_RCODE+") FROM " + TABLE_Cart ;
            while(db.inTransaction())
            {

            }
            db.beginTransaction();
            cursor = db.rawQuery(qry, null);
            db.endTransaction();
            if (cursor.moveToFirst()) {
                rcode=cursor.getString(0);
                if(Integer.parseInt(rcode)>=2) {
                }
                else
                {
                    rcode="";
                }
            }
        }

        db.close();
        return rcode;
    }


    public void addSaved(Class_Cart tp){
        if(getSavedCode(tp.cID).equals("")) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_VID, tp.v_id);
            values.put(KEY_VNAME, tp.v_name);
            values.put(KEY_ADDR, tp.addr);
            values.put(KEY_DEAL, tp.deal);
            values.put(KEY_CID, tp.cID);
            values.put(KEY_M_TYPE, tp.M_Type);
            values.put(KEY_G_TYPE, tp.G_TYPE);
            values.put(KEY_VALID_DATE, tp.valid_date);
            values.put(KEY_Cart_DATE, tp.Cart_date);
            db.insert(TABLE_Saved, null, values);
            values.clear();

            db.close();
        }
    }

    public ArrayList<Class_Cart> getAllSaved() {
        ArrayList<Class_Cart> Carted = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_Saved + " ORDER BY id desc";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Cart tp = null;
        if (cursor.moveToFirst()) {
            do {
                tp = new Class_Cart();
                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.v_id=(Integer.parseInt(cursor.getString(1)));
                tp.v_name=cursor.getString(2);
                tp.addr=cursor.getString(3);
                tp.deal=cursor.getString(4);
                tp.cID=cursor.getString(5);
                tp.M_Type=cursor.getString(6);
                tp.G_TYPE=cursor.getString(7);
                tp.valid_date=cursor.getString(8);
                tp.Cart_date=cursor.getString(9);
                Carted.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return Carted;
    }

    public String getSavedCode(String cID) {
        String rcode="";
        String query = "SELECT  "+KEY_ID+" FROM " + TABLE_Saved + " WHERE "+KEY_CID+" = '"+cID+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            rcode=cursor.getString(0);
        }
        db.close();
        return rcode;
    }

    public void deleteSavedCode(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Saved, KEY_CID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    public void addNotification(Class_Cart tp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VID, tp.v_id);
        values.put(KEY_M_TYPE, tp.M_Type);
        values.put(KEY_VNAME, tp.v_name);
        values.put(KEY_ADDR, tp.addr);
        values.put(KEY_DEAL, tp.deal);
        values.put(KEY_CID, tp.cID);
        values.put(KEY_Seen, 1);
        values.put(KEY_VALID_DATE, tp.valid_date);
        values.put(KEY_Cart_DATE, tp.Cart_date);
        db.insert(TABLE_Notification,null,values);
        values.clear();

        db.close();
    }

    public ArrayList<Class_Cart> getAllNotification(int n) {
        ArrayList<Class_Cart> Carted = new ArrayList<>();
        String query = "SELECT  id,"+KEY_VID+","+KEY_M_TYPE+","+KEY_VNAME+","+KEY_ADDR+","+KEY_DEAL+","+KEY_CID+","+KEY_VALID_DATE+","+KEY_Cart_DATE+","+KEY_VALID_DATE+"-'"+(new Date().getTime()/1000L)+"' as now FROM " + TABLE_Notification + " where now < 9 ORDER BY id desc";

        if(n>0)
        {
            query=query+" LIMIT "+n;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        Class_Cart tp = null;
        if (cursor.moveToFirst()) {
            do {
                tp = new Class_Cart();
                tp.id=(Integer.parseInt(cursor.getString(0)));
                tp.v_id=(Integer.parseInt(cursor.getString(1)));
                tp.M_Type=cursor.getString(2);
                tp.v_name=cursor.getString(3);
                tp.addr=cursor.getString(4);
                tp.deal=cursor.getString(5);
                tp.cID=cursor.getString(6);
                tp.valid_date="";
                tp.Cart_date=cursor.getString(8);
                Carted.add(tp);
            } while (cursor.moveToNext());
        }
        db.close();
        return Carted;
    }

    public String getAllNotificationNumber() {
        //String query = "SELECT  * FROM " + TABLE_Notification + " where "+KEY_VALID_DATE+">'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"' ORDER BY id desc";
        String query = "SELECT  count(id),"+KEY_VALID_DATE+"-'"+(new Date().getTime()/1000L)+"' as now FROM " + TABLE_Notification + " where now < 9 and "+KEY_Seen+">0 ORDER BY id desc";
        String notif="0";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            notif=cursor.getString(0);
        }
        db.close();
        return notif;
    }
public String getSavedNotification(String cID) {
        String rcode="";
        String query = "SELECT  "+KEY_ID+" FROM " + TABLE_Notification + " WHERE "+KEY_CID+" = '"+cID+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        while(db.inTransaction())
        {

        }
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        db.endTransaction();
        if (cursor.moveToFirst()) {
            rcode=cursor.getString(0);
        }
        db.close();
        return rcode;
    }*/


}
