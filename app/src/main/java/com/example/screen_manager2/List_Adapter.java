package com.example.screen_manager2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class List_Adapter extends BaseAdapter implements View.OnClickListener{
    private ArrayList<Idea_Data> container;
    private Context context;
    ListitemPosition listitemPosition;

    public List_Adapter(Context context, ArrayList<Idea_Data> container){
        this.container=container;
        this.context=context;
        listitemPosition = (ListitemPosition)context;
    }
    @Override
    public int getCount() {
        return container.size();
    }

    @Override
    public Object getItem(int position) {
        return container.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(convertView==null)
            convertView = inflater.inflate(R.layout.list_layout,null);

        TextView t1 = convertView.findViewById(R.id.idea_name);
        TextView t2 = convertView.findViewById(R.id.description);
        TextView t3 = convertView.findViewById(R.id.idea_date);
        Button b1 = convertView.findViewById(R.id.modify);

        b1.setVisibility(container.get(position).visibility);
        b1.setOnClickListener(this);
        b1.setTag(position);

        t1.setText(container.get(position).getTitle());
        t2.setText(container.get(position).getMemo());
        t3.setText(container.get(position).getDate());

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modify:
                if(listitemPosition != null)
                    listitemPosition.showClickPosition((int)v.getTag());
                break;
        }
    }

    public interface ListitemPosition{
        void showClickPosition(int position);
    }

}
