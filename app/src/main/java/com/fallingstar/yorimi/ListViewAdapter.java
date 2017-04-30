package com.fallingstar.yorimi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jiran on 2017-04-27.
 */

public class ListViewAdapter extends BaseAdapter {
    /*
    Define variables.
     */
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    /*
    purpose : Initiator for ListViewAdapter
     */
    public ListViewAdapter() {
    }

    /*
     purpose : Return the number of data of Adapter
      */
    public int getCount() {
        return listViewItemList.size();
    }

    /*
    purpose : Return the View instance that will use to print a data,
                that has specific postion value 'position'.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        /*
         Get convertView's reference by inflate the layout.listview_time.
          */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        /*
         Get reference from View(that inflate from layout.listview_item).
          */
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);
        Button button = (Button) convertView.findViewById(R.id.button2);

        /*
         Get data reference from data set(listViewitemList) that placed at 'position'.
          */
        ListViewItem listViewItem = listViewItemList.get(position);

        /*
         Apply the data to each widget that in listViewItem.
          */
        titleTextView.setText(listViewItem.getTitle());
        descTextView.setText(listViewItem.getDesc());
        button.setText(listViewItem.getButtonStr());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = ((Button) v).getText().toString();
                if (str.equalsIgnoreCase("Start")) {
                    /*
                    TODO : add action for alarm starting trigger
                     */
                    ((Button) v).setText("End");

                } else if (str.equalsIgnoreCase("end")) {
                    /*
                    TODO : add action for alarm ending trigger
                     */
                    ((Button) v).setText("Start");
                }
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    /*
    purpose : add item to list with title and description.
     */
    public void addItem(String title, String desc) {
        ListViewItem item = new ListViewItem();

        item.setTitle(title);
        item.setDesc(desc);
        item.setButtonStr("Start");

        listViewItemList.add(item);
    }
}
