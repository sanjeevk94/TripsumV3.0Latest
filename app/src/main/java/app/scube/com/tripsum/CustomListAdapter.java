package app.scube.com.tripsum;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sanjeev K on 07-02-2018.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.mylistview, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylistview, null,true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.circleView);
        TextView extratxt = (TextView) rowView.findViewById(R.id.Itemname);

        imageView.setImageResource(imgid[position]);
        extratxt.setText(itemname[position]);
        return rowView;

    };
}
