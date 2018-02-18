package app.scube.com.tripsum;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sanjeev K on 11-02-2018.
 */

public class DeleteExpenses extends AppCompatActivity {

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ListView lv;
    ArrayAdapter<String> adapter;
    int tripId = 0;
    String expList[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deleteexp_layout);
        lv = findViewById(R.id.MyExpList);
        openHelper = new DatabaseHelper(getApplicationContext());

        db = openHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_1 + " where " + DatabaseHelper.TABLE_NAME_1_COL_3 + "='Active'", null);
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            tripId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_1));
            //String tripName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_2));
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_3+" where "+DatabaseHelper.TABLE_NAME_3_COL_7+"="+tripId,null);
        expList = new String[cursor.getCount()];
        int i = 0;
        if (cursor.getCount() > 0) {

            while(cursor.moveToNext()) {

                expList[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_1)) + "         "
                        + cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_2)) + "         "
                        + cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_3))+"         "
                        +cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_4));
                i++;
            }
        }
       /* TextView textView = new TextView(getApplicationContext());
        textView.setText("Long press the item you want to delete");
        textView.setTextSize(25);
        textView.setTextColor(Color.GRAY);
        lv.addHeaderView(textView);*/
        adapter = new ArrayAdapter<String>(DeleteExpenses.this,android.R.layout.simple_list_item_1,expList);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            // setting onItemLongClickListener and passing the position to the function
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                removeItemFromList(position);

                return true;
            }
        });
    }
    protected void removeItemFromList(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(
                DeleteExpenses.this);

        alert.setTitle("Delete");
        alert.setMessage("Do you want delete this item?");
        alert.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TOD O Auto-generated method stub

                // main code on after clicking yes
                String listItems[] = expList[deletePosition].split("         ");
                System.out.println(listItems.toString());
                db = openHelper.getWritableDatabase();
                /*ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseHelper.TABLE_NAME_2_COL_3,totalAmount);*/
                db.delete(DatabaseHelper.TABLE_NAME_3,DatabaseHelper.TABLE_NAME_3_COL_1+"= '"+listItems[0]+"' and "+DatabaseHelper.TABLE_NAME_3_COL_2+"= '"+listItems[1]+"'",null);
                db.close();
                updateMemberAmount(new Double(listItems[2]),listItems[3],tripId);
                Toast.makeText(getApplicationContext(),"Expense deleted successfully",Toast.LENGTH_SHORT).show();
                db = openHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_3+" where "+DatabaseHelper.TABLE_NAME_3_COL_7+"="+tripId,null);
                expList = new String[cursor.getCount()];
                int j = 0;
                if (cursor.getCount() > 0) {

                    while(cursor.moveToNext()) {

                        expList[j] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_1)) + "         "
                                + cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_2)) + "         "
                                + cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_3))+"         "
                                +cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_4));
                        j++;
                    }
                }
                adapter = new ArrayAdapter<String>(DeleteExpenses.this,android.R.layout.simple_list_item_1,expList);
                lv.setAdapter(adapter);
                db.close();
                openHelper.close();
                //adapter.notifyDataSetInvalidated();

            }
        });
        alert.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        alert.show();

    }

    public void updateMemberAmount(double amount,String name,int tripId)
    {
        System.out.println("***********************"+name+"******************************");
        System.out.println("***********************"+amount+"******************************");
        System.out.println("***********************"+tripId+"******************************");
        double currentAmount = 0;
        openHelper = new DatabaseHelper(getApplicationContext());
        db = openHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from "+DatabaseHelper.TABLE_NAME_2+" where "+DatabaseHelper.TABLE_NAME_2_COL_4+"="+tripId+" and "+DatabaseHelper.TABLE_NAME_2_COL_2+"= '"+name+"'",null);
        if(cursor.moveToFirst() && cursor.getCount() > 0)
        {
            //System.out.println("***************************Name : "+cursor.getInt(3)+"**********************************");
            // totalAmount = cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_3);
            currentAmount = cursor.getDouble(2);
            currentAmount = currentAmount-amount;
        }

        cursor.close();
        db.close();

        db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TABLE_NAME_2_COL_3,currentAmount);
        db.update(DatabaseHelper.TABLE_NAME_2,contentValues,"member_name= '"+name+"' and trip_id="+tripId,null);
        db.close();
        openHelper.close();

    }

}