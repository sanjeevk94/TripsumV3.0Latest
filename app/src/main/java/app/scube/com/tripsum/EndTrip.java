package app.scube.com.tripsum;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by sanjeev on 12/14/2017.
 */
public class EndTrip extends AppCompatActivity {

    EditText editTextTrip;
    Button buttonEnd, buttonHome;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String tripName = null;
    int tripId = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endtrip_layout);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTextTrip = (EditText)findViewById(R.id.editTextTrip);
        buttonEnd = (Button)findViewById(R.id.buttonEnd);
        buttonHome = (Button)findViewById(R.id.buttonHome);
        openHelper = new DatabaseHelper(getApplicationContext());
        db = openHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" where "+DatabaseHelper.TABLE_NAME_1_COL_3+"='Active'",null);
        if (cursor.moveToFirst() && cursor.getCount() > 0)
        {
            tripId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_1));
            tripName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_2));
        }
        cursor.close();
        db.close();
        openHelper.close();
        editTextTrip.setText(tripName);
        editTextTrip.setEnabled(false);
        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tripName != null && tripName.length() >= 0)
                {
                        int updateStatus = updateTripStatus(tripId, "Inactive");
                        if (updateStatus > 0) {
                            Toast.makeText(getApplicationContext(), "Ended Current trip : " + tripName, Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                            Toast.makeText(getApplicationContext(), "Failed to end trip :" + tripName, Toast.LENGTH_SHORT).show();
                        }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Trip name should not be empty" + tripName, Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndTrip.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public int updateTripStatus(int tripId,String status)
    {
        int updateStatus = 0;
        openHelper = new DatabaseHelper(getApplicationContext());
        db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TABLE_NAME_1_COL_3,status);
        updateStatus = db.update(DatabaseHelper.TABLE_NAME_1, contentValues, "trip_id=" + tripId, null);
        db.close();
        openHelper.close();
        return updateStatus;

    }

   /* @Override
    public void onBackPressed() {

        Intent intent = new Intent(EndTrip.this, MainActivity.class);
        startActivity(intent);
    }*/

}
