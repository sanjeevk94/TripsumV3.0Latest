package app.scube.com.tripsum;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sanjeev on 11/25/2017.
 */
public class DateHandler extends AppCompatActivity {
    DatePicker datePicker;
    Button next,skip;
    int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_layout);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setSpinnersShown(false);
        next = (Button) findViewById(R.id.buttonNext);
        skip = (Button) findViewById(R.id.buttonSkip);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DateHandler.this,FormHandler.class);
                intent.putExtra("date",currentDate());
                startActivity(intent);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
                Intent intent = new Intent(DateHandler.this,FormHandler.class);
                intent.putExtra("date",date);
                startActivity(intent);

            }
        });
    }

    public String currentDate() {
        StringBuilder mcurrentDate = new StringBuilder();
        month = datePicker.getMonth() + 1;
        mcurrentDate.append(month + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear());
        return mcurrentDate.toString();
    }

    @Override
    public void onBackPressed() {
       Intent intent = new Intent(DateHandler.this,MainActivity.class);
        startActivity(intent);

    }

}
