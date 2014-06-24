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
import android.widget.*;
import com.suredigit.inappfeedback.FeedbackDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;


public class SettingsFragment extends Fragment {


    private static final String ARG_POSITION = "position";

    private int position;
    private View rootView;

    private ListView settingsListView;
    private SettingsListAdapter mAdapter ;

    private FeedbackDialog feedBack;

    public static SettingsFragment newInstance(int position) {
        SettingsFragment f = new SettingsFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //feedback
        feedBack = new FeedbackDialog(getActivity(), "AF-48817AB891AB-A0");

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public void onPause() {
        super.onPause();
        feedBack.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);



        settingsListView = (ListView)rootView.findViewById(R.id.data_list);

        settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //TODO event
                if(position == 0){
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(homeIntent);

                    Intent intent = new Intent(getActivity(), FloatWindowService.class);
                    //intent.putExtras(bundle);
                    getActivity().startService(intent);

                    getActivity().finish();
                }else if(position == 1){
                    feedBack.show();
                }
            }
        });

        //setList();
        //dataListView.setItemChecked(mCurrentSelectedPosition, true);

        return rootView;
    }

    @Override
    public void onResume() {
        setList();
        super.onResume();
    }



    public void setList() {

        String[] item = {
                getResources().getString(R.string.widget),
                getResources().getString(R.string.feedback)
        };
        int[] imgId = {
                R.drawable.ic_action_important,
                R.drawable.ic_action_email
        };
        mAdapter = new SettingsListAdapter(getActivity(), item.length, item, imgId);
        /* modify the adapter */
        settingsListView.setAdapter(mAdapter);

    }


}
