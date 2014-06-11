package com.cs.app.momo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.accessibility.AccessibilityManager;
import android.widget.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;


public class DataFragment extends Fragment {
    OnHeadlineSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onStatusChanged(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    private static final String ARG_POSITION = "position";

    private int position;
    private View rootView;

    private ListView dataListView;
    private DataListAdapter mAdapter ;
    private Button dateButton;

    private int year, month, day;
    private TreeMap<Integer, String> map = new TreeMap<Integer, String>();
    private String[] date = null;
    private String[] comment = null;
    private String[] fc = null;
    private String[] money = null;
    private String[] tmp = null;
    private String[] idarry = null;

    public static DataFragment newInstance(int position) {
        DataFragment f = new DataFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_data, container, false);

        setCurrentDateOnView();

        dateButton = (Button) rootView.findViewById(R.id.detail_date);
        dateButton.setText(year + " " + setMonth(month));
        dateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CustomDatePickerDialog dp = new CustomDatePickerDialog(getActivity(),  mDateSetListner, year, month, day);

                DatePickerDialog obj = dp.getPicker();
                try{
                    Field[] datePickerDialogFields = obj.getClass().getDeclaredFields();
                    for (Field datePickerDialogField : datePickerDialogFields) {
                        if (datePickerDialogField.getName().equals("mDatePicker")) {
                            datePickerDialogField.setAccessible(true);
                            DatePicker datePicker = (DatePicker) datePickerDialogField.get(obj);
                            Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();
                            for (Field datePickerField : datePickerFields) {
                                if ("mDayPicker".equals(datePickerField.getName()) || "mDaySpinner".equals(datePickerField
                                        .getName())) {
                                    datePickerField.setAccessible(true);
                                    Object dayPicker = new Object();
                                    dayPicker = datePickerField.get(datePicker);
                                    ((View) dayPicker).setVisibility(View.GONE);
                                }
                            }
                        }

                    }
                }catch(Exception ex){
                }
                if (Build.VERSION.SDK_INT >= 11) {
                    obj.getDatePicker().setCalendarViewShown(false);
                    //datePickerDialog.getDatePicker().setSpinnersShown(false);
                }
                obj.show();
            }
        });


        dataListView = (ListView)rootView.findViewById(R.id.data_list);
        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //TODO event

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                //String getId = this.id[position];
                View dataOpView = inflater.inflate(R.layout.dataoperator,null);
                alertDialog.setView(dataOpView);
                final AlertDialog alert = alertDialog.create();
                final TextView dialogTitle = (TextView)dataOpView.findViewById(R.id.type_title);
                final TextView dialogDate = (TextView)dataOpView.findViewById(R.id.dialog_date);
                final TextView dialogKind = (TextView)dataOpView.findViewById(R.id.dialog_kind);
                final TextView dialogComment = (TextView)dataOpView.findViewById(R.id.dialog_comment);
                if (fc[position].equals("cost")) {
                    dialogTitle.setText(getResources().getString(R.string.cost_info));
                    dialogDate.setText(year + "/" + (month+1) + "/" + date[position]);
                    dialogKind.setText(comment[position]);
                    dialogComment.setText(tmp[position]);
                } else {
                    dialogTitle.setText(getResources().getString(R.string.feed_info));
                    dialogDate.setText(year + "/" + (month+1) + "/" + date[position]);
                    final TextView titleKind = (TextView)dataOpView.findViewById(R.id.title_kind);
                    titleKind.setVisibility(View.GONE);
                    dialogKind.setVisibility(View.GONE);
                    dialogComment.setText(tmp[position]);
                    //getActivity().finish();
                }
                final Button Modify = (Button)dataOpView.findViewById(R.id.modify);
                Modify.setOnTouchListener(
                        new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    Modify.setBackgroundResource(R.color.gray);
                                } else {
                                    if (event.getAction() == MotionEvent.ACTION_UP) {
                                        Modify.setBackgroundResource(R.color.white);
                                        Intent intent = new Intent();
                                        //final String getId = id[position];
                                        if (fc[position].equals("cost")) {
                                            intent.setClass(getActivity(), EditCost.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", idarry[position]);
                                            bundle.putString("year", String.valueOf(year));
                                            bundle.putString("month", String.valueOf(month));
                                            bundle.putString("Date", date[position]);
                                            bundle.putString("Comment", tmp[position]);
                                            bundle.putString("Kind", comment[position]);
                                            bundle.putString("Money", money[position]);
                                            /*display.setText("Date:" + year + "/" + month + "/" + date[position] + "\n"
                                                    + "Type:" + fc[position] + "\n"
                                                    + "Kind:" + comment[position] + "\n"
                                                    + "Comment:" + tmp[position] + "\n");*/
                                            intent.putExtras(bundle);

                                            startActivity(intent);
                                            alert.cancel();
                                            //setList();
                                            //getActivity().finish();
                                        } else {
                                            intent.setClass(getActivity(), EditFeed.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", idarry[position]);
                                            bundle.putString("year", String.valueOf(year));
                                            bundle.putString("month", String.valueOf(month));
                                            bundle.putString("Date", date[position]);
                                            bundle.putString("Comment", tmp[position]);
                                            bundle.putString("Money", money[position]);
                                            /*display.setText("Date:" + year + "/" + month + "/" + date[position] + "\n"
                                                    + "Type:" + fc[position] + "\n"
                                                    + "Comment:" + tmp[position] + "\n");*/
                                            intent.putExtras(bundle);
                                            //getActivity().finish();
                                            startActivity(intent);
                                            alert.cancel();
                                            //setList();
                                            //Toast.makeText(getActivity(), "A", Toast.LENGTH_SHORT).show();
                                            //setList();
                                            //getActivity().finish();
                                        }
                                    }
                                }
                                return false;
                            }
                        }
                );

                final Button Delete = (Button)dataOpView.findViewById(R.id.delete);
                Delete.setOnTouchListener(
                        new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    Delete.setBackgroundResource(R.color.gray);
                                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                    Delete.setBackgroundResource(R.color.white);
                                    DatabaseHandler db = new DatabaseHandler(getActivity(), String.valueOf(month), String.valueOf(year));
                                    //Log.v("id", "" + position);
                                    //Toast.makeText(getActivity(), ""+idarry[position], Toast.LENGTH_SHORT).show();
                                    db.delete(Integer.parseInt(idarry[position]), year, month, Integer.parseInt(money[position]), fc[position]);

                                    alert.cancel();

                                    setList();
                                    mCallback.onStatusChanged(0);

                                    //refresh status
                                    Time time = new Time("GMT+8");
                                    time.setToNow();
                                    int nowMonth = time.month;
                                    int nowYear = time.year;
                                    //db.create(String.valueOf(nowMonth), String.valueOf(nowYear));
                                    double total = db.getSum(String.valueOf(year), String.valueOf(month));
                                    double monthcost = db.getMonthCost(String.valueOf(year), String.valueOf(month));
                                    Log.d("total", Double.toString(total));
                                    Log.d("monthcost", Double.toString(monthcost));

                                    final double percent;
                                    if(monthcost != 0.0 && total != 0.0){
                                        percent =  total / (total + monthcost);
                                    }else if(monthcost == 0.0 && total != 0.0){
                                        percent = 1.0 ;
                                    }else{
                                        percent = 0.0;
                                    }
                                    Log.d("percent", Double.toString(percent));

                                    SharedPreferences settings = getActivity().getSharedPreferences("MOMO_SETTINGS", 0);
                                    SharedPreferences.Editor PE = settings.edit();

                                    PE.putBoolean("FIRST_COMMIT", true);
                                    if (percent * 100 > 40) {
                                        // normal
                                        PE.putString("MONSTER_STATUS", "normal");
                                    } else if (percent * 100 > 10) {
                                        //hungry
                                        PE.putString("MONSTER_STATUS", "hungry");
                                    } else {
                                        //death
                                        PE.putString("MONSTER_STATUS", "death");
                                    }
                                    PE.commit();

                                    if(MyWindowManager.smallWindow != null) {
                                        MyWindowManager.resetParamsSwitch = false;
                                        MyWindowManager.removeSmallWindow(getActivity());
                                        MyWindowManager.createSmallWindow(getActivity());
                                        MyWindowManager.resetParamsSwitch = true;
                                    }
                                }
                                return false;
                            }
                        }
                );


                alert.show();

//                alertDialog.show();

            }
        });

        map.put(0, getResources().getString(R.string.kind_food));
        map.put(1, getResources().getString(R.string.kind_traffic));
        map.put(2, getResources().getString(R.string.kind_fun));
        map.put(3, getResources().getString(R.string.kind_others));

        //setList();
        //dataListView.setItemChecked(mCurrentSelectedPosition, true);

        return rootView;
    }

    @Override
    public void onResume() {
        setList();
        super.onResume();
    }

    DatePickerDialog.OnDateSetListener mDateSetListner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int syear, int smonthOfYear,
                              int sdayOfMonth) {
            year = syear;
            month = smonthOfYear;
            day = sdayOfMonth;
            dateButton.setText(year + " " + setMonth(month));
            setList();
        }
    };

    public void setCurrentDateOnView() {
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
    }




    public void setList() {
        DatabaseHandler db = new DatabaseHandler(getActivity(), String.valueOf(month), String.valueOf(year));
        List<Statistics> list = db.getMonthPerday(new Statistics(String.valueOf(month), String.valueOf(year), 0));
        Log.v("size", String.valueOf(list.size()));
        if (list.size() != 0) {
            ArrayList<String> dateList = new ArrayList<String>();
            ArrayList<String> commentList = new ArrayList<String>();
            ArrayList<String> fcList = new ArrayList<String>();
            ArrayList<String> moneyList = new ArrayList<String>();
            ArrayList<String> tmpList = new ArrayList<String>();
            ArrayList<String> idList = new ArrayList<String>();


            for (int i = 0; i != list.size(); i++) {
                Statistics statistics = list.get(i);
                idList.add(statistics.getId());
                dateList.add(statistics.getDate());
                moneyList.add(String.valueOf(statistics.getMoney()));
                if (statistics.getType() == 0) {
                    fcList.add("feed");
                    commentList.add(getResources().getString(R.string.kind_yummy));
                } else {
                    fcList.add("cost");
                    commentList.add(map.get(statistics.getKind()));
                }
                tmpList.add(statistics.getComment());

            }
            idarry = new String[idList.size()];
            idList.toArray(idarry);
            date = new String[dateList.size()];
            dateList.toArray(date);
            comment = new String[commentList.size()];
            commentList.toArray(comment);
            fc = new String[fcList.size()];
            fcList.toArray(fc);
            money = new String[moneyList.size()];
            moneyList.toArray(money);
            tmp = new String[tmpList.size()];
            tmpList.toArray(tmp);
            mAdapter = new DataListAdapter(getActivity(), list.size(), date, comment, fc, money, tmp);
        /* modify the adapter */
            dataListView.setAdapter(mAdapter);

            ImageView nullView = (ImageView) rootView.findViewById(R.id.null_view);
            nullView.setVisibility(View.INVISIBLE);
        } else
        {
            ImageView nullView = (ImageView) rootView.findViewById(R.id.null_view);
            nullView.setVisibility(View.VISIBLE);
            mAdapter = new DataListAdapter(getActivity(), 0, null, null, null, null, null);
            dataListView.setAdapter(mAdapter);
        }
    }

    private String setMonth(int month) {
        String monthWord="unknown";
        if(month==0) {
            monthWord = getResources().getString(R.string.jan);
        }else if(month==1){
            monthWord = getResources().getString(R.string.feb);
        }else if(month==2){
            monthWord = getResources().getString(R.string.mar);
        }else if(month==3){
            monthWord = getResources().getString(R.string.apr);
        }else if(month==4){
            monthWord = getResources().getString(R.string.may);
        }else if(month==5){
            monthWord = getResources().getString(R.string.jun);
        }else if(month==6){
            monthWord = getResources().getString(R.string.jul);
        }else if(month==7){
            monthWord = getResources().getString(R.string.aug);
        }else if(month==8){
            monthWord = getResources().getString(R.string.sep);
        }else if(month==9){
            monthWord = getResources().getString(R.string.oct);
        }else if(month==10){
            monthWord = getResources().getString(R.string.nov);
        }else if(month==11){
            monthWord = getResources().getString(R.string.dec);
        }
        return "- " + monthWord;
    }

}
