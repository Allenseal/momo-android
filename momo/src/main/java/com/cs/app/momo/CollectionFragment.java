package com.cs.app.momo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dong on 2014/6/12.
 */
public class CollectionFragment extends Fragment {

    OnHeadlineSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onMonsterChanged(int position);
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

    private int[] img_normal = {
            R.drawable.penguin_nn, R.drawable.bear_nn, R.drawable.seal_nn
    };
    private int[] img_stageUp = {
            R.drawable.penguin_normal, R.drawable.bear_normal, R.drawable.seal_normal
    };
    private int[] img_stage = {
            R.drawable.penguin_stage, R.drawable.bear_stage, R.drawable.seal_stage
    };
    private String[] imgName = {
            "Penguin", "Bear", "Seal"
    };
    private String[] imgNum = {
            "penguin", "bear", "seal"
    };

    private Gallery gallery;
    private ImageSwitcher imageSwitcher;
    private ImageSwitcher imageSwitcher2;
    private SimpleAdapter simpleAdapter;
    private TextView titleName;
    private int postionNow = 0;


    public static CollectionFragment newInstance(int position) {
        CollectionFragment f = new CollectionFragment();
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
        rootView = inflater.inflate(R.layout.fragment_collection, container, false);
        imageSwitcher = (ImageSwitcher)rootView.findViewById(R.id.image_switcher);
        imageSwitcher2 = (ImageSwitcher)rootView.findViewById(R.id.image_switcher2);
        titleName = (TextView)rootView.findViewById(R.id.name);

        gallery = (Gallery)rootView.findViewById(R.id.gallery);
        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < img_stageUp.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("image_normal", img_normal[i]);
            item.put("image_stageUP", img_stageUp[i]);
            item.put("stage", img_stage[i]);
            item.put("name", imgName[i]);
            item.put("number", imgNum[i]);
            items.add(item);
        }
        simpleAdapter = new SimpleAdapter(getActivity(),
                items, R.layout.gallery_switcher, new String[]{"image_normal", "number"},
                new int[]{R.id.image, R.id.text});

        gallery.setAdapter(simpleAdapter);


        imageSwitcher.setFactory(new ViewFactory(){
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
                return imageView;
            }

        });
        imageSwitcher2.setFactory(new ViewFactory(){
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
                return imageView;
            }

        });

        imageSwitcher.setImageResource(img_stage[postionNow]);
        imageSwitcher2.setImageResource(img_stageUp[postionNow]);
        titleName.setText(imgName[postionNow]);

        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right));
        imageSwitcher2.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
        imageSwitcher2.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right));


        imageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(rootView.getContext(), "click!" + postionNow, Toast.LENGTH_SHORT).show();
                doSelection();
            }
        });


        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position,
                                    long id) {
                imageSwitcher.setImageResource(img_stage[position]);
                imageSwitcher2.setImageResource(img_stageUp[position]);
                titleName.setText(imgName[position]);
                postionNow = position;
            }
        });

        return rootView;
    }



    private void doSelection(){
        Dialog alertDialog = new AlertDialog.Builder(rootView.getContext()).
                setTitle(getResources().getString(R.string.select_monster)).
                setMessage(getResources().getString(R.string.select_sure) + " " + imgName[postionNow]+" ?").
                setIcon(R.drawable.ic_launcher).
                setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences settings = getActivity().getSharedPreferences("MOMO_SETTINGS",  0);
                        SharedPreferences.Editor PE = settings.edit();
                        PE.putInt("MONSTER_NUMBER", postionNow);
                        PE.putBoolean("FIRST_COMMIT", true);
                        PE.commit();
                        mCallback.onMonsterChanged(postionNow);

                        // TODO Auto-generated method stub
                    }
                }).
                setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // TODO Auto-generated method stub
                    }
                }).
                create();
        alertDialog.show();
    }
}





