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
public class DataListAdapter extends BaseAdapter implements ListAdapter {

    private Context mContext;
    private JSONArray mData;
    private int count ;
    private String[] date ;
    private String[] comment ;
    private String[] fc;
    private String[] money ;
    private String[] tmp ;
    public DataListAdapter(Context context, int count, String[] date, String[] comment, String[] fc, String[] money, String[] tmp) {
        this.mContext = context ;
        this.count = count ;
        this.date = date ;
        this.comment = comment ;
        this.fc = fc ;
        this.money = money ;
        this.tmp = tmp;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return comment[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.data_list_row, null);
        }

        ImageView icon = (ImageView) view.findViewById(R.id.item_icon);
        TextView commentText = (TextView) view.findViewById(R.id.item_name);
        TextView fcText = (TextView) view.findViewById(R.id.item_comment);
        TextView dateText = (TextView) view.findViewById(R.id.date_text);
        TextView moneyText = (TextView) view.findViewById(R.id.money_text);
//        TextView tmpText = (TextView) view.findViewById(R.id.tmp_text);


        dateText.setText(date[position]);
        commentText.setText(comment[position] + (tmp[position] == null || tmp[position].equals("")? "" : " - " + tmp[position]));
        if(fc[position].equals("feed")){
            fcText.setText(R.string.feed);
        }else{
            fcText.setText(R.string.cost);
        }
        dateText.setText(date[position]);
        moneyText.setText(money[position]+"$");

        LinearLayout bg = (LinearLayout) view.findViewById(R.id.detail_content);
        if(fc[position].equals("feed")){
            bg.setBackgroundColor(Color.parseColor("#2bacb5"));
            icon.setImageResource(R.drawable.feed);
        }else{
            bg.setBackgroundColor(Color.parseColor("#e84d5b"));
            icon.setImageResource(R.drawable.cost);
        }



        return view;
    }

}