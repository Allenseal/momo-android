package com.cs.app.momo;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.json.JSONArray;

/**
 * Created by Allens on 2014/3/17.
 */
public class SettingsListAdapter extends BaseAdapter implements ListAdapter {

    private Context mContext;
    private int count ;
    private String[] item ;
    private int[] imgId ;

    public SettingsListAdapter(Context context, int count, String[] item, int[] imgId) {
        this.mContext = context ;
        this.count = count ;
        this.item = item ;
        this.imgId = imgId;

    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return item[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.settings_list_row, null);
        }

        ImageView icon = (ImageView) view.findViewById(R.id.item_icon);
        TextView itemText = (TextView) view.findViewById(R.id.item_name);

        icon.setImageResource(imgId[position]);
        itemText.setText(item[position]);

        return view;
    }

}