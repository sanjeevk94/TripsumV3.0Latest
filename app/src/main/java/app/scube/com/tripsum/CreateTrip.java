package app.scube.com.tripsum;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by sanjeev on 11/26/2017.
 */
public class CreateTrip extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    EditText editTextTrip;
    Button buttonCreate,buttonMem;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    long status;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createtrip_layout);
        editTextTrip = (EditText)findViewById(R.id.editTextTrip);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        buttonCreate = (Button)findViewById(R.id.buttonCreate);
        buttonMem = (Button)findViewById(R.id.buttonMem);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHelper = new DatabaseHelper(getApplicationContext());
                //openHelper.onUpgrade(db,0,1);
                String tripName = editTextTrip.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton)findViewById(selectedId);
                String tripStatus = radioButton.getText().toString();
                if (tripName != null && tripName.length()>0)
                {
                    openHelper = new DatabaseHelper(getApplicationContext());
                    db = openHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" where "+DatabaseHelper.TABLE_NAME_1_COL_3+"='Active'",null);
                    if (cursor.moveToFirst() && cursor.getCount() > 0)
                    {
                        Toast.makeText(getApplicationContext(),"Already a trip named '"+cursor.getString(1)+"' exists. Complete it to start new!!", Toast.LENGTH_LONG).show();
                        editTextTrip.setText("");
                    }
                    else
                    {
                        status = insertData(tripName, tripStatus);
                        if (status > 0)
                        {
                            Toast.makeText(getApplicationContext(), "Trip Created", Toast.LENGTH_SHORT).show();
                            editTextTrip.setText("");
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Trip Creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    cursor.close();
                    db.close();
                    openHelper.close();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Trip name should not be empty", Toast.LENGTH_SHORT).show();
                }
                //db.close();
                openHelper.close();

            }
        });


        buttonMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openHelper = new DatabaseHelper(getApplicationContext());
                    db = openHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" where "+DatabaseHelper.TABLE_NAME_1_COL_3+"='Active'",null);
                    if (cursor.moveToFirst() && cursor.getCount() > 0)
                    {
                        Intent intent = new Intent(CreateTrip.this, AddMembers.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Create trip to add members", Toast.LENGTH_SHORT).show();

                    }
                cursor.close();
                db.close();
                openHelper.close();
            }
        });
    }

  /* public String radioButtonClicked(View view) {
        String status=null;
        boolean checked = ((RadioButton) view).isChecked();
        // This check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioButtonActive:
                if (checked)
                    status = "Active";
                    Toast.makeText(getApplicationContext(), "Active", Toast.LENGTH_SHORT).show();
                break;

            case R.id.radioButtonInactive:
                status = "Inactive";
                Toast.makeText(getApplicationContext(), "Inactive", Toast.LENGTH_SHORT).show();
                break;
        }
        return status;
    }*/
    public long insertData(String tripName, String tripStatus)
    {
        db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
       // contentValues.put(DatabaseHelper.TABLE_NAME_1_COL_1,date);
        contentValues.put(DatabaseHelper.TABLE_NAME_1_COL_2,tripName);
        contentValues.put(DatabaseHelper.TABLE_NAME_1_COL_3,tripStatus);

        long id = db.insert(DatabaseHelper.TABLE_NAME_1,null,contentValues);


        return id;
    }
    /*@Override
    public void onBackPressed() {

        Intent intent = new Intent(CreateTrip.this,MainActivity.class);
        startActivity(intent);
       *//* AlertDialog.Builder builder = new AlertDialog.Builder(CreateTrip.this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to really exit ?");
        builder.setCancelable(true);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                moveTaskToBack(true);
            }
        });
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
*//*
    }*/
}
