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
//    Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

//    ListViewAdapter의 생성자

    public ListViewAdapter()
    {

    }

//    Adapter에 사용되는 데이터의 개수를 return함
    public int getCount()
    {
        return listViewItemList.size();
    }

//    position에 위치한 데이터를 화면에 출력하는데 사용될 View를 return함
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final int pos = position;
        final Context context = parent.getContext();

//        "Listview_item" Layout을 inflate하여 convertView 참조 획득
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

//        화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);
        Button button = (Button) convertView.findViewById(R.id.button2);

//        Data set(ListViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

//        item 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getTitle());
        descTextView.setText(listViewItem.getDesc());
        button.setText(listViewItem.getButtonStr());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                음 여기 뭐 넣을까..?
                String str = ((Button)v).getText().toString();
                if(str.equalsIgnoreCase("Start"))
                {
                    ((Button) v).setText("End");
//                    start 버튼 누르면 실행되는 곳!
                }
                else if(str.equalsIgnoreCase("end"))
                {
//                    결과 값 가져와서 setText 하는 곳?
                }
                else
                {
                    ((Button) v).setText("Start");
//                    결과 값 다 보고 누르면 start 로 바꿔주는거?
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

//    아이템 데이터 추가 함수
    public void addItem(String title, String desc)
    {
        ListViewItem item = new ListViewItem();

        item.setTitle(title);
        item.setDesc(desc);
        item.setButtonStr("Start");

        listViewItemList.add(item);
    }
}
