package app.scube.com.tripsum;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by sanjeev on 12/14/2017.
 */
public class TripSummary extends AppCompatActivity {

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ListView list;
    String[] myDataString = {"Trip Name : ","No.of Members : ","Total Expenses : ","Per Head Expenses : "};
    Integer[] imgid = { R.drawable.ic_card_travel_black_24dp, R.drawable.ic_group_black_24dp,
                      R.drawable.ic_attach_money_black_24dp, R.drawable.ic_person_black_24dp};
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripsummary_layout);
       // getSupportActionBar().show();
        listView = (ListView)findViewById(R.id.MyListView);
        int tripId = 0;
        String tripName = null;
        int totalMembers = 0;
        double totalExpenses =0;
        double perHeadAmount = 0;
        openHelper = new DatabaseHelper(getApplicationContext());
        db = openHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" where "+DatabaseHelper.TABLE_NAME_1_COL_3+"='Active'",null);
        if (cursor.moveToFirst() && cursor.getCount() > 0)
        {
            tripId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_1));
            tripName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_2));
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_2+" where "+DatabaseHelper.TABLE_NAME_2_COL_4+"="+tripId,null);
        totalMembers = cursor.getCount();
        if (cursor.getCount() > 0)
        {
            while(cursor.moveToNext()) {
                totalExpenses = totalExpenses + cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_3));
            }
        }
        System.out.println("*****************Total Expenses : "+totalExpenses+"********************************");
        cursor.close();
       /* cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_3+" where "+DatabaseHelper.TABLE_NAME_3_COL_7+"="+tripId,null);
        if (cursor.moveToFirst() && cursor.getCount() > 0)
        {
            totalExpenses = totalExpenses+cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_3));
        }
        cursor.close();*/
        db.close();
        openHelper.close();
        perHeadAmount = totalExpenses/totalMembers;
        DecimalFormat dFormat = new DecimalFormat("#.##");
        dFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        String perHeadAm = dFormat.format(perHeadAmount);
        myDataString[0]=myDataString[0].concat(tripName);
        myDataString[1]=myDataString[1].concat(String.valueOf(totalMembers));
        myDataString[2]=myDataString[2].concat(String.valueOf(totalExpenses));
        myDataString[3]=myDataString[3].concat(String.valueOf(perHeadAm));
       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.mylistview,R.id.Itemname,myDataString);
        listView.setAdapter(adapter);*/
        CustomListAdapter adapter=new CustomListAdapter(this, myDataString, imgid);
        list=(ListView)findViewById(R.id.MyListView);
        list.setAdapter(adapter);
    }
    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(TripSummary.this, MainActivity.class);
        startActivity(intent);
    }*/
}
