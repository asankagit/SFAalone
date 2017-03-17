package activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.fragmenttest.R;

import java.util.ArrayList;

import controllers.adapters.DBAdapter;
import controllers.database.DBHelper;
import model.Tr_ItineraryDetails;
import model.Tr_NewCustomer;

public class AddExtraCustomer extends AppCompatActivity {
    Spinner spinner_area;
    Spinner spinner_town;
    SearchView cus_name;
    String area;
    String town;
    String customer_name;
    Button btn_add_itinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_extra_customer);


        spinner_area = (Spinner) findViewById(R.id.spinner_area_ec);
        spinner_town = (Spinner) findViewById(R.id.spinner_town_ec);
        cus_name =(SearchView) findViewById(R.id.search_txt_customer_ec);
        btn_add_itinerary =(Button) findViewById(R.id.btn_add_itineraray);

        spinner_town.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                area = spinner_area.getSelectedItem().toString();
                town = spinner_town.getSelectedItem().toString();
                customer_name = cus_name.getQuery().toString();

                getdata(town, area, customer_name);
                //sortBrandByPrinciple(town);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //onEventListenr for spinner brand
        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                area = spinner_area.getSelectedItem().toString();
                town = spinner_town.getSelectedItem().toString();
                customer_name = cus_name.getQuery().toString();
                getdata(town, area, customer_name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //searchView_sv event
        cus_name.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                area = spinner_area.getSelectedItem().toString();
                town = spinner_town.getSelectedItem().toString();
                customer_name = cus_name.getQuery().toString();
                getdata(town, area, customer_name);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView_sv.isExpanded() && TextUtils.isEmpty(newText)) {
                callSearch(newText);
//              }
                return true;
            }

            public void callSearch(String query) {
                //Do searching
            }

        });
        //set event listenfer for btn add_itinerary
        btn_add_itinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSelectedRows();
            }
        });




        setSpinner();//aulto load values to dropdown boxes
        getdata("All", "All", "d");//call default search;
    }

    public  void setSpinner(){

        DBAdapter adapter = new DBAdapter(this);

        ArrayList<String> arrayList1 = new ArrayList<String>();
        arrayList1=adapter.getAllArea();

        ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,arrayList1);
        spinner_area.setAdapter(adp);
        spinner_area.setVisibility(View.VISIBLE);


        //set Spinner values to principle dropdown menu;
        ArrayList<String> principleList = new ArrayList<String>();
        principleList=adapter.getAllTown();//adapter.getAllprinciples();


        ArrayAdapter<String> adp2 = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,principleList);
        spinner_town.setAdapter(adp2);
        spinner_town.setVisibility(View.VISIBLE);
    }


    public void getdata(String ...filter){

        String town=filter[0];
        String area=filter[1];
        String cusName=filter[2];

        String query="select * from Tr_NewCustomer ";
        /*SELECT doctors.doctor_id,doctors.doctor_name,visits.patient_name
        FROM doctors
        INNER JOIN visits
        ON doctors.doctor_id=visits.doctor_id*/




        if(town=="All" && area=="All"){
            if(cusName!=null){
                query+= " where CustomerName like'"+cusName+"%'";
            }
        }else if(town=="All" && area !="All"){
            query+= " where Area='"+area+"' ";
        }else if(town!="All" && area=="All"){
            query+= " where Town='"+town+"' ";
        }else{
            query+= " where Area='"+area+"'  AND Town='"+town+"' ";
        }
        if(cusName!=null && !(town=="All" && area=="All") ){
            query+=" AND CustomerName like'"+cusName+"%'";
        }
        //query="select * from Tr_TabStock";//WARNING !<<remover this after test>>

        //refresh the table;
        TableLayout table = (TableLayout)findViewById(R.id.table_add_extra_customer);
        //int n=table.getChildCount();
        LinearLayout l=(LinearLayout)findViewById(R.id.linear_layout_add_ec_data_row);
        int n=l.getChildCount();



        l.removeAllViews();
       /* table.removeViews(0,1);
                for(int index=1;index < n;index++) {
                    //table.removeView(table.getChildAt(index));
                        View view = l.getChildAt(index);
                        TableRow row = (TableRow) view;
                        l.removeView(row);

                }*/


        //end refresh

        try {
            Tr_NewCustomer pending_customer= new Tr_NewCustomer();

            DBHelper db = new DBHelper(this);
            Cursor res = db.getData(query);//return tupple id=1;



            while (res.moveToNext()) {

                pending_customer.setNewCustomerID(res.getString(res.getColumnIndex("NewCustomerID")));
                pending_customer.setCustomerName(res.getString(res.getColumnIndex("CustomerName")));
                pending_customer.setAddress(res.getString(res.getColumnIndex("Address")));
                pending_customer.setContactNo(res.getString(res.getColumnIndex("OwnerContactNo")));
                pending_customer.setUploadedStatus(res.getString(res.getColumnIndex("IsUpload")));
                pending_customer.setApprovedStatus(res.getString(res.getColumnIndex("ApproveStatus")));

               /* pending_customer.setDescription(res.getString(res.getColumnIndex("Description")));//should be modified
                pending_customer.setBatchNumber(res.getString(res.getColumnIndex("BatchNumber")));
                pending_customer.setExpireyDate(res.getString(res.getColumnIndex("ExpiryDate")));
                */


                update(pending_customer);
                //btnView_sv.setText(res.getCount());
            }

            db.close();
            //return product;

        }catch (Exception e){
            //btnView_sv.setText(e.getMessage());
        }
    }


    private void update( Tr_NewCustomer cus) {
        TableLayout table = (TableLayout)findViewById(R.id.table_add_extra_customer);
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.linear_layout_add_ec_data_row);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;



        LinearLayout col_1=new LinearLayout(this);
        LinearLayout col_2=new LinearLayout(this);
        LinearLayout col_3=new LinearLayout(this);
        LinearLayout col_4=new LinearLayout(this);
        LinearLayout col_5=new LinearLayout(this);


        col_1.setOrientation(LinearLayout.VERTICAL);
        col_2.setOrientation(LinearLayout.VERTICAL);
        col_3.setOrientation(LinearLayout.VERTICAL);
        col_4.setOrientation(LinearLayout.VERTICAL);
        col_5.setOrientation(LinearLayout.VERTICAL);

        /*col_1.setWeightSum(1.0f);
        col_2.setWeightSum(4.0f);
        col_3.setWeightSum(8.0f);
        col_4.setWeightSum(11.0f);
        col_5.setWeightSum(14.0f);*/

        TableRow.LayoutParams  col_param=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        col_param.weight=1.0f;
        TableRow.LayoutParams  col_param2=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        col_param2.weight=1.0f;

        col_1.setLayoutParams(col_param);
        col_2.setLayoutParams(col_param);
        col_3.setLayoutParams(col_param);
        col_4.setLayoutParams(col_param);
        col_5.setLayoutParams(col_param2);

        //new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        /*add style to table row*/
        ContextThemeWrapper wrappedContext = new ContextThemeWrapper(this, R.style.pending_customer_row);

        //add row
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));


        //add coloum_select
         final CheckBox cb_select = new CheckBox(wrappedContext);
        cb_select.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        /*set ClickEvent Listener for CheckBox;
        cb_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(cb_select.isChecked()){
                  getSelectedRows();//getParent().getParentForAccessibility());

                //}
            }
        });*/


        //add coloum_cusname
        TextView tv_cusName = new TextView(wrappedContext,null,0);
        tv_cusName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_cusName.setText(cus.getCustomerName());



        //add coloum_adress
        TextView tv_address = new TextView(wrappedContext,null,0);
        tv_address.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_address.setText(cus.getAddress());
        //tv_description.setWidth(0);

        //add coloum_contact
        TextView tv_contact = new TextView(wrappedContext,null,0);
        tv_contact.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_contact.setText(cus.getContactNo());

        //add coloum_town
        TextView tv_town = new TextView(wrappedContext,null,0);
        tv_town.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tv_town.setText(cus.getUploadedStatus());










        //add coloums to row
        col_1.addView(tv_cusName);
        col_2.addView(tv_address);
        col_4.addView(tv_town);
        col_3.addView(tv_contact);
        col_5.addView(cb_select);



        tr.addView(col_5);
        tr.addView(col_1);
        tr.addView(col_2);
        tr.addView(col_3);
        tr.addView(col_4);



        // table.addView(tr);
        linearLayout.addView(tr);
    }



    public  void getSelectedRows() {
        TableLayout table = (TableLayout) findViewById(R.id.table_add_extra_customer);
        LinearLayout data_LinearLayout=(LinearLayout) findViewById(R.id.linear_layout_add_ec_data_row);
        DBAdapter adp=new DBAdapter(this);



        //get selected row;
        try {
            int count = 0;
            for (int i = 0; i < data_LinearLayout.getChildCount(); i++) {
                ArrayList<String > cusId=new ArrayList<>();
                cusId=adp.getCusId();


                View parentRow = data_LinearLayout.getChildAt(i);//getTable rows
                if (parentRow instanceof TableRow) {

                    for (int j = 0; j <2/*((TableRow) parentRow).getChildCount()*/; j++) {//get columns
                        count++;
                        View innerLinear= ((TableRow) parentRow).getChildAt(j);//access to column_1
                        LinearLayout ll=(LinearLayout) innerLinear;//access to column_1 linearLayout

                        View cbox=  ll.getChildAt(0);//access to column_1 checkbox inside LinearLayout
                        View tb=ll.getChildAt(0);//acess to column 2 cusName textBox;

                        if (cbox instanceof CheckBox) {

                            if(((CheckBox) cbox).isChecked()) {
                                ((CheckBox) cbox).setText("<");

                                TableRow nametblrow= (TableRow) data_LinearLayout.getChildAt(i);
                                LinearLayout namell= (LinearLayout) nametblrow.getChildAt(1);
                                TextView nametv= (TextView) namell.getChildAt(0);

                                //add data to itirnararyDetails;nametv.setText(cusId.get(i));
                                addToItinerary(cusId.get(i));


                            }else
                                ((CheckBox) cbox).setText("");
                        }
                       /* if(tb instanceof  TextView)
                            Toast.makeText(this,((TextView) tb).getText(),Toast.LENGTH_LONG).show();*/


                    }
                }
            }
           // Toast.makeText(this, "count:"+count, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(this,"Error:"+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    private void addToItinerary(String id){
        Tr_ItineraryDetails itinerary=new Tr_ItineraryDetails();
        DBAdapter adp=new DBAdapter(this);

        itinerary.setCustomerNo(id);
        itinerary.setItineraryID("ITRY"+id);
        itinerary.setIsInvoiced(0);
        itinerary.setIsPlaned(1);

        Toast.makeText(this, "added:"+id, Toast.LENGTH_LONG).show();
        adp.itineraryDetails(itinerary);

    }
}
