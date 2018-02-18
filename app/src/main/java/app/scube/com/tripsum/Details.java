package app.scube.com.tripsum;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;


/**
 * Created by sanjeev on 11/16/2017.
 */
public class Details extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Cursor cursor;
    TableLayout tl;
    TableRow tr;
    TextView date,item,amount,name,members,perHead;
    int tripId = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tl = (TableLayout) findViewById(R.id.maintable);
        tl.setColumnStretchable(0, true);
        tl.setColumnStretchable(1, true);
        tl.setColumnStretchable(2, true);
        tl.setColumnStretchable(3, true);
        tl.setColumnStretchable(4, true);
        tl.setColumnStretchable(5, true);
        //tl.setStretchAllColumns(true);
        Intent prevIntent = this.getIntent();
        if(prevIntent != null)
        {
            tripId = prevIntent.getExtras().getInt("tripId");
        }
        openHelper = new DatabaseHelper(this);
        db = openHelper.getReadableDatabase();
        //cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME+" WHERE "+DatabaseHelper.COL_1+"=?",new String[]{email,pass});
        cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_3+" where "+DatabaseHelper.TABLE_NAME_3_COL_7+"="+tripId,null);
        startManagingCursor(cursor);
        addHeaders();
        addData();
        /*if(cursor != null)
        {
            System.out.println("Entered inside cursor");
          totalAmount = 0;
            while(cursor.moveToNext())
            {
                System.out.println("cursor having data");
                System.out.println("total Amount "+totalAmount);
                totalAmount = totalAmount+cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_4));
                System.out.println("total Amount After "+totalAmount);
            }
        }
        */
        cursor.close();
        db.close();
        openHelper.close();

    }
    public void addHeaders(){

        /** Create a TableRow dynamically **/
        tr = new TableRow(this);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        /** Creating another textview **/
        TextView Date = new TextView(this);
        //Date.setHeight(75);
        Date.setBackgroundColor(Color.parseColor("#37474f"));
        Date.setText("Date");
        Date.setTextColor(Color.WHITE);
        Date.setPadding(10, 50, 5, 50);
        Date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(Date); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView item = new TextView(this);
       // item.setHeight(75);
        item.setBackgroundColor(Color.parseColor("#37474f"));
        item.setText("Item");
        item.setTextColor(Color.WHITE);
        item.setPadding(10, 50, 5, 50);
        item.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(item); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView amount = new TextView(this);
       // amount.setHeight(75);
        amount.setBackgroundColor(Color.parseColor("#37474f"));
        amount.setText("Amount");
        amount.setTextColor(Color.WHITE);
        amount.setPadding(10, 50, 5, 50);
        amount.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(amount); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView name = new TextView(this);
        //name.setHeight(75);
        name.setBackgroundColor(Color.parseColor("#37474f"));
        name.setText("Spent By");
        name.setTextColor(Color.WHITE);
        name.setPadding(10, 50, 5, 50);
        name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(name); // Adding textView to tablerow.

        /** Creating a TextView to add to the row **/
        TextView persons = new TextView(this);
        //persons.setHeight(75);
        persons.setBackgroundColor(Color.parseColor("#37474f"));
        persons.setText("Persons");
        persons.setTextColor(Color.WHITE);
        persons.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        persons.setPadding(10, 50, 5, 50);
        tr.addView(persons);  // Adding textView to tablerow.

        TextView perHead = new TextView(this);
        //perHead.setHeight(75);
        perHead.setBackgroundColor(Color.parseColor("#37474f"));
        perHead.setText("Per Head");
        perHead.setTextColor(Color.WHITE);
        perHead.setPadding(10, 50, 5, 50);
        perHead.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(perHead); // Adding textView to tablerow.

        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

    }

    /** This function add the data to the table **/
    public void addData() {

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
                tr.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                /** Creating a TextView to add to the row **/
                date = new TextView(this);
               // System.out.println(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_1)));
                date.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_1)));
                date.setBackgroundColor(Color.parseColor(color));
                date.setTextColor(Color.BLACK);
                date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                date.setPadding(10, 50, 50, 50);
                tr.addView(date);  // Adding textView to tablerow.

                /** Creating a TextView to add to the row **/
                item = new TextView(this);
                //System.out.println(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_2)));
                item.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_2)));
                item.setBackgroundColor(Color.parseColor(color));
                item.setTextColor(Color.BLACK);
                item.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                item.setPadding(10, 50, 50, 50);
                tr.addView(item);  // Adding textView to tablerow.

                /** Creating another textview **/
                amount = new TextView(this);
                amount.setText(Double.toString(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_3))));
                amount.setBackgroundColor(Color.parseColor(color));
                amount.setTextColor(Color.BLACK);
                amount.setPadding(10, 50, 50, 50);
                amount.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(amount); // Adding textView to tablerow.


                /** Creating another textview **/
                name = new TextView(this);
                //System.out.println(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_4)));
                name.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_4)));
                name.setBackgroundColor(Color.parseColor(color));
                name.setTextColor(Color.BLACK);
                name.setPadding(10, 50, 50, 50);
                name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(name); // Adding textView to tablerow.

                /** Creating another textview **/
                members = new TextView(this);
                members.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_5))));
                members.setBackgroundColor(Color.parseColor(color));
                members.setTextColor(Color.BLACK);
                members.setPadding(10, 50, 50, 50);
                members.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(members); // Adding textView to tablerow.

                /** Creating another textview **/
                perHead = new TextView(this);
                Double perHeadAmount = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_3_COL_6));
                DecimalFormat dFormat = new DecimalFormat("#.##");
                dFormat.setRoundingMode(RoundingMode.HALF_EVEN);
                String perHeadAm = dFormat.format(perHeadAmount);
                perHead.setBackgroundColor(Color.parseColor(color));
                perHead.setText(perHeadAm);
                perHead.setTextColor(Color.BLACK);
                perHead.setPadding(10, 50, 50, 50);
                perHead.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(perHead); // Adding textView to tablerow.
                // Add the TableRow to the TableLayout
                tl.addView(tr, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));

            }
        }
    }
   /* @Override
    public void onBackPressed() {
        Intent intent = new Intent(Details.this,ShowActivity.class);
        startActivity(intent);

    }*/
}
