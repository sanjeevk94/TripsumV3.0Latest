package app.scube.com.tripsum;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sanjeev K on 06-02-2018.
 */

public class StopActivity extends Fragment implements OnBackPressedListener {
    private final String text_recyclerViewTitleText[] = {
            "Delete Expenses","End Trip"};
    private final int images_recyclerViewImages[] = {
            R.drawable.deleteexp,R.drawable.endtrip};

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Cursor cursor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stop_layout,container,false);
        initRecyclerViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initRecyclerViews(View view) {
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.stop);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<AndroidVersion> av = prepareData();
        AndroidDataAdapter mAdapter = new AndroidDataAdapter(getActivity(), av);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        switch (i) {
                            case 0:
                                openHelper = new DatabaseHelper(getActivity());
                                db = openHelper.getReadableDatabase();
                                cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" where "+DatabaseHelper.TABLE_NAME_1_COL_3+"='Active'",null);
                                if (cursor.moveToFirst() && cursor.getCount() > 0)
                                {
                                    int tripId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_NAME_1_COL_1));
                                    cursor.close();
                                    cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_3+" where "+DatabaseHelper.TABLE_NAME_3_COL_7+"="+tripId,null);
                                    if (cursor.moveToFirst() && cursor.getCount() > 0) {
                                        Intent intent = new Intent(StopActivity.this.getActivity(), DeleteExpenses.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(),"No expenses added......", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Currently no active trips....", Toast.LENGTH_SHORT).show();
                                }
                                cursor.close();
                                db.close();
                                openHelper.close();
                                break;

                            case 1:
                                openHelper = new DatabaseHelper(getActivity());
                                db = openHelper.getReadableDatabase();
                                cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME_1+" where "+DatabaseHelper.TABLE_NAME_1_COL_3+"='Active'",null);
                                if (cursor.moveToFirst() && cursor.getCount() > 0)
                                {
                                    Intent intent = new Intent(StopActivity.this.getActivity(),EndTrip.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Currently no active trips....", Toast.LENGTH_SHORT).show();
                                }
                                cursor.close();
                                db.close();
                                openHelper.close();
                                break;

                        }
                    }
                })
        );

    }
    private ArrayList<AndroidVersion> prepareData() {

        ArrayList<AndroidVersion> av = new ArrayList<>();
        for (int i = 0; i < text_recyclerViewTitleText.length; i++) {
            AndroidVersion mAndroidVersion = new AndroidVersion();
            mAndroidVersion.setAndroidVersionName(text_recyclerViewTitleText[i]);
            mAndroidVersion.setrecyclerViewImage(images_recyclerViewImages[i]);
            av.add(mAndroidVersion);
        }
        return av;
    }

    @Override
    public boolean onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StopActivity.this.getActivity());
        builder.setTitle("Exit");
        builder.setMessage("Do you want to really exit ?");
        builder.setCancelable(true);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
                getActivity().moveTaskToBack(true);
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
        return true;
    }
}
