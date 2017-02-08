package com.clackityclack.lxxcroft;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cle99 on 15/01/2017.
 */

public class ParadeDetails extends Fragment {

    private SharedPreferences paradeInfo, userData;
    private SharedPreferences.Editor paradeEditor, userEditor;
    private TextView welcome;
    private String welcomeText, rank, fName, lName, paradeJSON;
    private int oneHour = 3600000; //60s x 60s x 1000 ms/s
    private Gson gson = new Gson();
    private LinearLayout nextParadeLayout, nextParadeNoInfo, nextParadeChecking;
    private View vRowDefault;
    private LayoutInflater rowInflater;
    private Activity activity;

    SharedPreferences.OnSharedPreferenceChangeListener userListener , paradeListener;

    public ArrayList<ParadeDetail> getFromJSON(String json) {

        gson = new Gson();
        ArrayList<ParadeDetail> list = gson.fromJson(json, ArrayList.class);

        return list;

    }

    public void getWebData(Activity a, View view) {

        try {

            new AttemptUpdate (getContext(), view).execute ();

        } catch (Exception e) {

            e.printStackTrace ();

        }
    }

    private void setWelcomeMessage(SharedPreferences sp) {

        rank = sp.getString("rank", "empty");
        rank = (rank.equals("Staff")) ? "" : rank;  // dont display staff 'rank'
        fName = sp.getString("firstName", "empty");
        lName = sp.getString("lastName", "empty");
        welcomeText = "Welcome " + rank + " " + fName + " " + lName;
        if (!welcomeText.contains("empty")) {
            // if user exists, bring welcome text into view
            welcome.setVisibility(View.VISIBLE);
            welcome.setText(welcomeText);
        } else {
            // hide message if no user
            welcome.setVisibility(View.GONE);
        }
    }


    public static ParadeDetails newInstance() {

        ParadeDetails f = new ParadeDetails();

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        paradeInfo = getActivity().getSharedPreferences("LXXparade", Context.MODE_PRIVATE);
        paradeEditor = paradeInfo.edit();


        userData = getActivity().getSharedPreferences("LXXuser", Context.MODE_PRIVATE);
        userEditor = userData.edit();

        activity = getActivity();

        welcomeText = "";
        final View v = inflater.inflate(R.layout.parade_details, container, false);
        welcome = (TextView) v.findViewById(R.id.welcome);

        if (!userData.getString("firstName", "empty").equals("empty")) {
            setWelcomeMessage(userData);
        }


        //if (!paradeInfo.getString("firstName", "empty").equals("empty")) {
            //welcome.setVisibility(View.VISIBLE);

        userListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sp, String key) {

                /* this listener updates the welcome message when the user enters or updates
                   the USER INFO tab.
                */
                setWelcomeMessage(sp);

            }

        }; userData.registerOnSharedPreferenceChangeListener(userListener);

        nextParadeLayout = (LinearLayout) v.findViewById(R.id.the_parade_details_from_website);
        nextParadeLayout.setVisibility ( View.GONE );

        nextParadeNoInfo = (LinearLayout) v.findViewById(R.id.the_parade_details_if_no_details);
        nextParadeNoInfo.setVisibility ( View.GONE );

        nextParadeChecking = (LinearLayout) v.findViewById ( R.id.the_parade_details_checking );
        nextParadeChecking.setVisibility ( View.VISIBLE );



        //new AttemptUpdate(getContext()).execute();


        rowInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //rowInflater = LayoutInflater.from(getContext());

        vRowDefault = rowInflater.inflate(R.layout.parade_listview_row_checking, null);



        nextParadeChecking.addView(vRowDefault);

        nextParadeNoInfo.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {



                getWebData (activity, v);

            }
        } );


        if (InternetTest.testConnection(getContext())) {

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    getWebData(activity, v);


                }
            }, 0, oneHour);

        } else {

            Toast.makeText(getContext(),"Network Connection not detected", Toast.LENGTH_LONG).show();

        }

        paradeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sp, String key) {



                // placeholder for future code if needed

            }
        }; paradeInfo.registerOnSharedPreferenceChangeListener(paradeListener);

        return v;

    }

}
