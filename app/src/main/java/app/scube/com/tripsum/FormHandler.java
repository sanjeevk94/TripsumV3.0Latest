package app.scube.com.tripsum;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by sanjeev on 11/25/2017.
 */
public class FormHandler extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button save,home;
    EditText editTextAmount,editTextItem;
    TextView textView;
    Cursor cursor;
    long status;
    int noOfMembers = 0;
   // StringBuilder[] category = new StringBuilder[25];
    String date = null;
    int tripId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_layout);
        Intent prevIntent = this.getIntent();
        if(prevIntent != null)
        {
            date = prevIntent.getExtras().getString("date");
        }
        save = (Button)findViewById(R.id.buttonSave);
        home = (Button)findViewById(R.id.buttonHome);

        textView = (TextView)findViewById(R.id.textView);
        openHelper = new DatabaseHelper(getApplicationContext());
        db = openHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" where "+DatabaseHelper.TABLE_NAME_1_COL_3+"='Active'",null);
        if (cursor.moveToFirst() && cursor.getCount() > 0)
        {
            tripId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_1));
            String tripName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_2));
            textView.setText("Add Expenses in your trip : "+tripName);
        }
        cursor.close();
        cursor = db.rawQuery("SELECT "+DatabaseHelper.TABLE_NAME_2_COL_2+" FROM "+DatabaseHelper.TABLE_NAME_2+" where "+DatabaseHelper.TABLE_NAME_2_COL_4+"="+tripId,null);
        noOfMembers = cursor.getCount();
        String[] category = new String[cursor.getCount()];
        if (cursor.getCount() > 0)
        {
            int i =0;
            /*System.out.println("************************************ No of rows : "+cursor.getCount()+"*******************************************");
            System.out.println("************************************Hai*******************************************");*/
            while(cursor.moveToNext())
            {
                category[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_2));
                System.out.println(category[i]);
                i++;
            }
        }
       /* else
        {
            Toast.makeText(getApplication(),"No members added in the Trip !!!!",Toast.LENGTH_SHORT).show();
        }*/
        cursor.close();
        db.close();
        openHelper.close();


        editTextItem = (EditText)findViewById(R.id.editTextItem);
        //editTextType.setVisibility(View.INVISIBLE);
        editTextAmount = (EditText)findViewById(R.id.editTextAmount);
       // editTextSpentBy = (EditText)findViewById(R.id.editTextSpentBy);
       /* editTextMembers = (EditText)findViewById(R.id.editTextMembers);
        editTextMembers.setText(String.valueOf(noOfMembers));*/
        /*spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);*/
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,category);
        final MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.spinner);
        materialDesignSpinner.setAdapter(arrayAdapter);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* openHelper = new DatabaseHelper(getApplicationContext());
                db = openHelper.getWritableDatabase();*/
                //openHelper.onUpgrade(db,0,1);

                String amount = editTextAmount.getText().toString();
                String name = materialDesignSpinner.getText().toString();
                String item = editTextItem.getText().toString();
                //String members = editTextMembers.getText().toString();
                if( date != null && date.length()>0 )
                {
                    if(item != null && item.length()>0)
                    {
                         if (amount != null && amount.length() >0)
                            {
                                double amountInInt = Double.parseDouble(amount);

                                if (name != null && name.length() > 0)
                                {
                                    /*if (members != null && members.length() > 0)
                                    {
                                    int membersInInt = Integer.parseInt(members);*/
                                    double perHeadAmount = amountInInt/noOfMembers;
                                    status = insertData(date, item, amountInInt, name, noOfMembers,perHeadAmount,tripId);
                                        if(status > 0) {
                                            Toast.makeText(getApplicationContext(), "Expenses saved", Toast.LENGTH_SHORT).show();
                                            updateMemberAmount(name,amountInInt,tripId);
                                            editTextItem.setText("");
                                            editTextAmount.setText("");
                                            //editTextMembers.setText(String.valueOf(noOfMembers));
                                         }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Expenses failed to save", Toast.LENGTH_SHORT).show();
                                        }
                                    /*}
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Members should not be empty",Toast.LENGTH_SHORT).show();
                                    }*/
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Name should not be empty",Toast.LENGTH_SHORT).show();
                                }
                             }
                        else
                         {
                             Toast.makeText(getApplicationContext(),"Amount should not be empty",Toast.LENGTH_SHORT).show();
                         }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Item should not be empty",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                       Toast.makeText(getApplicationContext(),"Date should not be empty",Toast.LENGTH_SHORT).show();
                }
                /*db.close();
                openHelper.close();*/
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormHandler.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public long insertData(String date, String item, double amount, String name, int members,double perHeadAmount,int tripId)
    {
        openHelper = new DatabaseHelper(getApplicationContext());
        db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TABLE_NAME_3_COL_1,date);
        contentValues.put(DatabaseHelper.TABLE_NAME_3_COL_2,item);
        contentValues.put(DatabaseHelper.TABLE_NAME_3_COL_3,amount);
        contentValues.put(DatabaseHelper.TABLE_NAME_3_COL_4,name);
        contentValues.put(DatabaseHelper.TABLE_NAME_3_COL_5,members);
        contentValues.put(DatabaseHelper.TABLE_NAME_3_COL_6,perHeadAmount);
        contentValues.put(DatabaseHelper.TABLE_NAME_3_COL_7,tripId);


        long id = db.insert(DatabaseHelper.TABLE_NAME_3,null,contentValues);
        db.close();
        openHelper.close();

        return id;
    }

    public void updateMemberAmount(String name,double amount,int tripId)
    {
        double totalAmount = 0;
        openHelper = new DatabaseHelper(getApplicationContext());
        db = openHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from "+DatabaseHelper.TABLE_NAME_2+" where "+DatabaseHelper.TABLE_NAME_2_COL_4+"="+tripId+" and "+DatabaseHelper.TABLE_NAME_2_COL_2+"='"+name+"'",null);
        if(cursor.moveToNext())
        {
            //System.out.println("***************************Name : "+cursor.getInt(3)+"**********************************");
           // totalAmount = cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_2_COL_3);
            totalAmount = cursor.getDouble(2);
            totalAmount = totalAmount+amount;
        }

        cursor.close();
        db.close();

        db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TABLE_NAME_2_COL_3,totalAmount);
        db.update(DatabaseHelper.TABLE_NAME_2,contentValues,"member_name='"+name+"' and trip_id="+tripId,null);
        db.close();
        openHelper.close();

    }


    /*@Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getApplicationContext(),category[position] ,Toast.LENGTH_LONG).show();


        if(category[position].equalsIgnoreCase("Food"))
        {
            editTextType.setVisibility(View.VISIBLE);
            editTextType.setHint("Tiffen/Lunch/Dinner");
        }
        else if(category[position].equalsIgnoreCase("Transport"))
        {
            editTextType.setVisibility(View.VISIBLE);
            editTextType.setHint("From -> To");
        }
        else if(category[position].equalsIgnoreCase("Tickets"))
        {
            editTextType.setVisibility(View.VISIBLE);
            editTextType.setHint("Purpose");
        }
        Item = category[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }*/
    /*@Override
    public void onBackPressed() {

        Intent intent = new Intent(FormHandler.this, DateHandler.class);
        startActivity(intent);
    }*/

}
