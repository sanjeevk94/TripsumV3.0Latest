package app.scube.com.tripsum;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by sanjeev on 12/12/2017.
 */
public class IndividualExpenses extends AppCompatActivity {

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Cursor cursor;
    TableLayout tl;
    TableRow tr;
    TextView name, totalAmount,balance;
    double total=0;
    int noOfMem = 0;
    double perHeadAmount = 0;
    int tripId = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individualexpenses_layout);
        tl = (TableLayout) findViewById(R.id.indExp);
        tl.setColumnStretchable(0, true);
        tl.setColumnStretchable(1, true);
        tl.setColumnStretchable(2, true);
        openHelper = new DatabaseHelper(this);
        db = openHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" where "+DatabaseHelper.TABLE_NAME_1_COL_3+"='Active'",null);
        while(cursor.moveToNext() && cursor.getCount() > 0)
        {
            tripId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_1));
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_2+" where "+DatabaseHelper.TABLE_NAME_2_COL_4+"="+tripId,null);
        noOfMem = cursor.getCount();
        while(cursor.moveToNext())
        {
            total = total+cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_3));
        }
        perHeadAmount = total/noOfMem;
        cursor.close();
        //addHeaders();
        addData();
        cursor.close();
        db.close();
        openHelper.close();
    }
    public void addData() {
        double remBal =0.0;
        cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_2+" where "+DatabaseHelper.TABLE_NAME_2_COL_4+"="+tripId,null);
        String[] colors = new String[]{"#fafafa","#e0e0e0"};
        String color = null;
        int i = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                i++;
                if(i % 2 == 0)
                {
                    color = colors[1];
                }
                else
                {
                    color = colors[0];
                }
                /** Create a TableRow dynamically **/
                tr = new TableRow(this);
               /* tr.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT,
                        TableLayout.LayoutParams.FILL_PARENT));*/

                /** Creating a TextView to add to the row **/
                name = new TextView(this);
                // System.out.println(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_1)));
                name.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_2)));

                name.setBackgroundColor(Color.parseColor(color));
                name.setTextColor(Color.BLACK);
                name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                name.setPadding(10, 50, 60, 50);
                tr.addView(name);  // Adding textView to tablerow.

                /** Creating a TextView to add to the row **/
                totalAmount = new TextView(this);
                //System.out.println(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_2)));
                totalAmount.setText(Double.toString(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_3))));
                totalAmount.setBackgroundColor(Color.parseColor(color));
                totalAmount.setTextColor(Color.BLACK);
                totalAmount.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                totalAmount.setPadding(10, 50, 60, 50);
                tr.addView(totalAmount);  // Adding textView to tablerow.

                remBal = perHeadAmount - cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_3));
                DecimalFormat dFormat = new DecimalFormat("#.##");
                dFormat.setRoundingMode(RoundingMode.HALF_EVEN);
                String perHeadAm = dFormat.format(remBal);
                balance = new TextView(this);
                //System.out.println(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_2)));
                balance.setText(perHeadAm);
                balance.setBackgroundColor(Color.parseColor(color));
                balance.setTextColor(Color.BLACK);
                balance.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                balance.setPadding(10, 50, 60, 50);
                tr.addView(balance);  // Adding textView to tablerow.
                //tl.addView(tr);
                tl.addView(tr, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT,
                        TableLayout.LayoutParams.FILL_PARENT));
            }
        }
    }
    public void addHeaders() {

        /** Create a TableRow dynamically **/
        tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        /** Creating another textview **/
        TextView name = new TextView(this);
        //Date.setHeight(75);
        name.setBackgroundColor(Color.parseColor("#37474f"));
        name.setText("Name");
        name.setTextColor(Color.WHITE);
        name.setPadding(10, 50, 60, 50);
        name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(name); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView amount = new TextView(this);
        // item.setHeight(75);
        amount.setBackgroundColor(Color.parseColor("#37474f"));
        amount.setText("Amount Spent");
        amount.setTextColor(Color.WHITE);
        amount.setPadding(10, 50, 60, 50);
        amount.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(amount); // Adding textView to tablerow.

        TextView balance = new TextView(this);
        // amount.setHeight(75);
        balance.setBackgroundColor(Color.parseColor("#37474f"));
        balance.setText("Balance to Pay");
        balance.setTextColor(Color.WHITE);
        balance.setPadding(10, 50, 60, 50);
        balance.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(balance);

        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
    }
    /*@Override
    public void onBackPressed() {

        Intent intent = new Intent(IndividualExpenses.this, MainActivity.class);
        startActivity(intent);
    }*/
    }
