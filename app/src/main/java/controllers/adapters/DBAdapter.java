package controllers.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import controllers.database.DBHelper;
import model.Tr_ItineraryDetails;

/**
 * Created by DELL on 3/10/2017.
 */

public class DBAdapter{
    Context context;
    DBHelper dbHelper;
    SQLiteDatabase db;

    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    private void openDB(){
        try {
            db = dbHelper.getWritableDatabase();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void closeDB(){
        try{
            dbHelper.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public Cursor runQuery(String qry){
        openDB();
        Cursor res;
        res=db.rawQuery(qry,null);
        closeDB();
        return  res;

    }
    public ArrayList<String> getAllprinciples() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        openDB();
        Cursor res =  db.rawQuery( "SELECT DISTINCT PrincipleID FROM Tr_TabStock", null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("PrincipleID")));
            res.moveToNext();
        }
        closeDB();
        return array_list;

    }
    //get brands
    public ArrayList<String> getAllbrands(String qry) {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        openDB();
        Cursor res =  db.rawQuery( qry, null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("BrandID")));
            res.moveToNext();
        }
        closeDB();
        return array_list;

    }

    public boolean insertIntoCustomerImage(String imageCode,String customerID){
        openDB();
        db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('"+customerID+"','"+imageCode+"')");
        closeDB();
        return true;
    }

    public ArrayList<String> getAllArea() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        openDB();
        Cursor res =  db.rawQuery("select DISTINCT Area from Tr_NewCustomer", null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("Area")));
            res.moveToNext();
        }
        closeDB();
        return array_list;
    }

    public ArrayList<String> getAllTown() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        openDB();
        Cursor res =  db.rawQuery("select DISTINCT Town from Tr_NewCustomer", null );
        res.moveToFirst();

        array_list.add("All");
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("Town")));
            res.moveToNext();
        }
        closeDB();
        return array_list;
    }
    public ArrayList<String> getCusId() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        openDB();
        Cursor res =  db.rawQuery("select  NewCustomerID from Tr_NewCustomer", null );
        res.moveToFirst();


        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("NewCustomerID")));
            res.moveToNext();
        }
        closeDB();
        return array_list;
    }

    public  String itineraryDetails(Tr_ItineraryDetails it_d){
        openDB();

        String result="failDBclass";
        try{


            ContentValues contentValues = new ContentValues();
            contentValues.put("ItineraryID", it_d.getItineraryID());
            contentValues.put("ItineraryDate","2017-10-23");
            contentValues.put("CustomerNo",it_d.getCustomerNo());
            contentValues.put("IsPlaned",it_d.getIsPlaned());
            contentValues.put("IsInvoiced",it_d.getIsInvoiced());
            contentValues.put("LastUpdateDate","2017-03-17");


            if(db.insert("Tr_ItineraryDetails", null, contentValues)>0) {
                result = "success";
            }else
                result ="outer_if";

        }catch (SQLException e){
            e.printStackTrace();
            result=e.getMessage();
        }
        closeDB();
        return result;


    }
}
