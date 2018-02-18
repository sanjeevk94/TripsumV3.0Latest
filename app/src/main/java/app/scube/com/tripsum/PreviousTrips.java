package app.scube.com.tripsum;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by sanjeev on 12/16/2017.
 */
public class PreviousTrips extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Cursor cursor;
    Button buttonDetails;
    String[] myString = {"No.of Members : ","Total Expenses : ","Per Head Expenses : "};
    Integer[] img = { R.drawable.ic_group_black_24dp, R.drawable.ic_attach_money_black_24dp, R.drawable.ic_person_black_24dp};
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previoustrips_layout);
        buttonDetails =(Button)findViewById(R.id.buttonDetails);

        openHelper =  new DatabaseHelper(getApplicationContext());
        db = openHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" where "+DatabaseHelper.TABLE_NAME_1_COL_3+"='Inactive'",null);
        String[] trips = new String[cursor.getCount()];
        if (cursor.getCount() >= 0)
        {
            int i =0;
            while(cursor.moveToNext())
            {
                trips[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_2));
                System.out.println(trips[i]);
                i++;
            }
        }
        cursor.close();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,trips);
        final MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.spinner1);
        materialDesignSpinner.setAdapter(arrayAdapter);



        buttonDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                int tripId =0;
                String tripName = materialDesignSpinner.getText().toString();
                System.out.println("******************************** TripName : "+tripName+"*********************************");
                db = openHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" where "+DatabaseHelper.TABLE_NAME_1_COL_2+"='"+tripName+"'",null);
                while(cursor.moveToNext())
                {
                    tripId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_1));
                    System.out.println("***************************Trip Id : "+tripId+"**************************************");
                }
                cursor.close();
                cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_2+" where "+DatabaseHelper.TABLE_NAME_2_COL_4+"="+tripId,null);
                int noOfMembers = cursor.getCount();
                System.out.println("***************************No of Members "+noOfMembers+"**************************************");
                double totalAmount = 0;
                while(cursor.moveToNext())
                {
                    System.out.println("***************************Helloooo**************************************");
                    totalAmount = totalAmount + cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_3));
                }
                double perHeadAmount = totalAmount / noOfMembers;
                DecimalFormat dFormat = new DecimalFormat("#.##");
                dFormat.setRoundingMode(RoundingMode.HALF_EVEN);
                String perHeadAm = dFormat.format(perHeadAmount);
                System.out.println("***************************Per Head Amount : "+perHeadAmount+"**************************************");

                myString[0]=myString[0].concat(String.valueOf(noOfMembers));
                myString[1]=myString[1].concat(String.valueOf(totalAmount));
                myString[2]=myString[2].concat(String.valueOf(perHeadAm));
                //System.out.println(myString[0]);
                CustomListAdapter adapter=new CustomListAdapter(PreviousTrips.this, myString, img);
                listView=(ListView)findViewById(R.id.MyListView1);
                listView.setAdapter(adapter);
                cursor.close();
                db.close();
                openHelper.close();

            }
        });
        //System.out.println(myString[0]);



    }
    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(PreviousTrips.this, MainActivity.class);
        startActivity(intent);
    }*/
}