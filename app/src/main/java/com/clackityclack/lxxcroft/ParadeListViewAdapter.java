package com.clackityclack.lxxcroft;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cle99 on 27/01/2017.
 */

class ParadeListViewAdapter extends ArrayAdapter<ParadeDetail> {

    private Context context;
    private ArrayList<ParadeDetail> allRows;

    private LayoutInflater mInflater;
    private boolean mNotifyOnChange = true;

    ParadeListViewAdapter(Context context, ArrayList<ParadeDetail> mParade) {
        super(context, R.layout.parade_listview_row);
        this.context = context;
        this.allRows = mParade;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return allRows.size();
    }

    @Override
    public ParadeDetail getItem(int position) {
        return allRows.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getPosition(ParadeDetail item) {
        return allRows.indexOf(item);
    }

    @Override
    public int getViewTypeCount() {
        return 1; //Number of types + 1 !!!!!!!!
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case 1:
                    convertView = mInflater.inflate(R.layout.parade_listview_row,parent, false);
                    holder.definition = (TextView) convertView.findViewById(R.id.definition);
                    holder.requirement = (TextView) convertView.findViewById(R.id.requirement);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.definition.setText(allRows.get(position).getDef());
        holder.requirement.setText(allRows.get(position).getReq());
        holder.pos = position;
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }


    //---------------static views for each row-----------//
    static class ViewHolder {

        TextView definition;
        TextView requirement;
        int pos; //to store the position of the item within the list
    }
}
