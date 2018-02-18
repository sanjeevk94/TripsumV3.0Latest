package app.scube.com.tripsum;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by sanjeev on 11/15/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "register.db";
    public static final String TABLE_NAME_1="trip";
    public static final String TABLE_NAME_2="members";
    public static final String TABLE_NAME_3= "expenses";
    public static final String TABLE_NAME_1_COL_1 = "trip_id";
    public static final String TABLE_NAME_1_COL_2 = "trip_name";
    public static final String TABLE_NAME_1_COL_3 = "status";

    public static final String TABLE_NAME_2_COL_1 = "member_id";
    public static final String TABLE_NAME_2_COL_2 = "member_name";
    public static final String TABLE_NAME_2_COL_3 ="amount_spent";
    public static final String TABLE_NAME_2_COL_4 = "trip_id";

    public static final String TABLE_NAME_3_COL_1 = "date";
    public static final String TABLE_NAME_3_COL_2 = "item";
    public static final String TABLE_NAME_3_COL_3 = "amount";
    public static final String TABLE_NAME_3_COL_4 = "name";
    public static final String TABLE_NAME_3_COL_5 = "members";
    public static final String TABLE_NAME_3_COL_6 = "per_head_amount";
    public static final String TABLE_NAME_3_COL_7 = "trip_id";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE "+TABLE_NAME_1 + "(trip_id INTEGER PRIMARY KEY,trip_name TEXT,status TEXT)");
            db.execSQL("CREATE TABLE "+TABLE_NAME_2 + "(member_id INTEGER PRIMARY KEY,member_name TEXT,amount_spent REAL,trip_id INTEGER,FOREIGN KEY (trip_id) REFERENCES "+TABLE_NAME_1+" (trip_id))");
            db.execSQL("CREATE TABLE " + TABLE_NAME_3 + "(date TEXT,item TEXT,amount REAL,name TEXT,members INTEGER,per_head_amount REAL,trip_id INTEGER,PRIMARY KEY(date,item),FOREIGN KEY (trip_id) REFERENCES "+TABLE_NAME_1+" (trip_id))");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       /* try
        {
            db.execSQL("Drop table if exists "+DatabaseHelper.TABLE_NAME_3);
            db.execSQL("Drop table if exists "+DatabaseHelper.TABLE_NAME_2);
            db.execSQL("Drop table if exists "+DatabaseHelper.TABLE_NAME_1);
            onCreate(db);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
*/
    }
    public void close(DatabaseHelper db)
    {
        db.close();
    }
}
