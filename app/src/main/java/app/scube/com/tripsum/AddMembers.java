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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sanjeev on 12/9/2017.
 */
public class AddMembers extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Cursor cursor;
    EditText memberName;
    Button buttonAdd,buttonExp;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmembers_layout);
        memberName = (EditText)findViewById(R.id.MemberName);
        buttonAdd = (Button)findViewById(R.id.buttonAdd);
        buttonExp = (Button)findViewById(R.id.buttonExp);
        textView = (TextView)findViewById(R.id.textView);
        openHelper = new DatabaseHelper(getApplicationContext());
        db = openHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" where "+DatabaseHelper.TABLE_NAME_1_COL_3+"='Active'",null);
        if (cursor.moveToFirst() && cursor.getCount() > 0)
        {
            String tripName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_2));
            textView.setText("Add Members in your trip : "+tripName);
        }
        cursor.close();
        db.close();
        openHelper.close();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long insertStatus = 0;
                String name = memberName.getText().toString();
                if(name != null && name.length() > 0)
                {
                   int tripId = getTripId();
                   // System.out.println("********************** TripId : "+tripId+"******************************");
                    if(tripId >= 0)
                    {
                        insertStatus = addMember(tripId,name);
                        if(insertStatus > 0)
                        {
                            memberName.setText("");
                            Toast.makeText(getApplicationContext(),"Member added !!!!",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Member failed to add !!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Currently no active trips....",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Name should not be empty !!!!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        buttonExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int tripId = getTripId();
                if(tripId > 0)
                {
                    openHelper = new DatabaseHelper(getApplicationContext());
                    db = openHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_2+" where "+DatabaseHelper.TABLE_NAME_2_COL_4+"="+tripId,null);
                    if (cursor.moveToFirst() && cursor.getCount() > 0)
                    {
                        Intent intent = new Intent(AddMembers.this, DateHandler.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"First add members to add expenses", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                    db.close();
                    openHelper.close();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Currently no active trips....",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int getTripId()
    {
        int tripId =0;
        openHelper = new DatabaseHelper(getApplicationContext());
        db = openHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" where "+DatabaseHelper.TABLE_NAME_1_COL_3+"='Active'",null);
        if (cursor.moveToFirst() && cursor.getCount() > 0)
        {
            tripId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_1));
        }
        cursor.close();
        db.close();
        openHelper.close();
        return tripId;
    }

    public long addMember(int tripId,String name)
    {
        openHelper = new DatabaseHelper(getApplicationContext());
        db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TABLE_NAME_2_COL_2, name);
        contentValues.put(DatabaseHelper.TABLE_NAME_2_COL_3, 0);
        contentValues.put(DatabaseHelper.TABLE_NAME_2_COL_4, tripId);


        long id = db.insert(DatabaseHelper.TABLE_NAME_2, null, contentValues);
        db.close();
        openHelper.close();
        return id;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AddMembers.this, MainActivity.class);
        startActivity(intent);
    }
}
