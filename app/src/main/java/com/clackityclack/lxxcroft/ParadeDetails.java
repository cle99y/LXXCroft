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
    private SharedPreferences.Editor paradeEditor;
    private SharedPreferences.Editor userEditor;
    private TextView welcome;
    private String welcomeText, rank, fName, lName, paradeJSON;
    private int oneHour = 3600000;
    private int delay, delay2;
    private Gson gson = new Gson();
    private ParadeDetail pd;
    private ArrayList<ParadeDetail> nextParade;
    private LinearLayout nextParadeLayout, nextParadeNoInfo;
    private View vRow, vRowDefault;
    private LayoutInflater rowInflater;
    private Boolean paradeDataIsOutOfDate;



    SharedPreferences.OnSharedPreferenceChangeListener userListener , paradeListener;

    public ArrayList<ParadeDetail> getFromJSON(String json) {

        gson = new Gson();
        ArrayList<ParadeDetail> list = gson.fromJson(json, ArrayList.class);

        return list;

    }

    private void setWelcomeMessage(SharedPreferences sp) {

        rank = sp.getString("rank", "empty");
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

    String html, date, typhoon, tornado, hawk, tucano, nco;
    TextView paradeDate, typhoonFlight, tornadoFlight, hawkFlight, tucanoFlight, dutyNCO;
    Matcher m;
    Pattern p;
    Activity activity;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        paradeInfo = getActivity().getSharedPreferences("LXXparade", Context.MODE_PRIVATE);
        paradeEditor = paradeInfo.edit();


        userData = getActivity().getSharedPreferences("LXXuser", Context.MODE_PRIVATE);
        userEditor = userData.edit();

        activity = getActivity();

        welcomeText = "";
        View v = inflater.inflate(R.layout.parade_details, container, false);
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
        nextParadeNoInfo = (LinearLayout) v.findViewById(R.id.the_parade_details_if_no_details);
        paradeDataIsOutOfDate = false;

        new AttemptUpdate(getContext()).execute();

        rowInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowInflater = LayoutInflater.from(getContext());

        vRowDefault = rowInflater.inflate(R.layout.parade_listview_row_if_no_http, null);
        ((TextView) vRowDefault.findViewById(R.id.noDataTop))
                .setText("Information for the next parade is not available");
        ((TextView) vRowDefault.findViewById(R.id.noDataBottom))
                .setText("Please check back regularly");
        nextParadeNoInfo.addView(vRowDefault);


        if (InternetTest.testConnection(getContext())) {

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                new AttemptUpdate(getContext()).execute();
                                paradeJSON = paradeInfo.getString("paradeDetail", "empty");
                                if (!paradeJSON.equals("empty")) {
                                    nextParade = gson.fromJson(paradeJSON,
                                            new TypeToken<ArrayList<ParadeDetail>>() {
                                            }.getType()); // type ArrayList<ParadeDetail>

                                    String today = DateValue.getToday();
                                    String pDate = DateValue.formatParadeData(nextParade.get(0).getReq());
                                    if (DateValue.getDateValue(today) > DateValue.getDateValue(pDate)) {
                                        paradeDataIsOutOfDate = true;
                                    }

                                    if (paradeDataIsOutOfDate == false &&
                                            nextParadeLayout.getChildCount() == 0) {
                                        nextParadeNoInfo.setVisibility(View.GONE);
                                        nextParadeLayout.setVisibility(View.VISIBLE);




                                        for (int i = 0; i < nextParade.size(); i++) {
                                            Log.i("DEF", nextParade.get(i).getDef());
                                            Log.i("REQ", nextParade.get(i).getReq());

                                            vRow = rowInflater.inflate(R.layout.parade_listview_row, null);
                                            ((TextView) vRow.findViewById(R.id.definition))
                                                    .setText(nextParade.get(i).getDef());
                                            ((TextView) vRow.findViewById(R.id.requirement))
                                                    .setText(nextParade.get(i).getReq());

                                            nextParadeLayout.addView(vRow);
                                        }
                                    }
                                }

                            } catch (Exception e) {

                                e.printStackTrace();

                            }

                        }
                    });
                }
            }, 5000, oneHour);

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
