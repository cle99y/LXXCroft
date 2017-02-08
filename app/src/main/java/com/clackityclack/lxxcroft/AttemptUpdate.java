package com.clackityclack.lxxcroft;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Created by cle99 on 25/01/2017.
 */

public class AttemptUpdate extends AsyncTask<Void, Void, Void> {

    private final SharedPreferences paradeInfo;
    private SharedPreferences.Editor paradeEditor;
    private final Context context;
    private ArrayList<ParadeDetail> paradeDetails = new ArrayList<>();
    private JSONArray jsonArray;

    private String def, req, paradeJSON;
    private ParadeDetail pd;
    public Boolean isComplete;

    private Gson gson = new Gson();

    private ArrayList<ParadeDetail> nextParade;
    private LinearLayout nextParadeLayout, nextParadeNoInfo, nextParadeChecking;
    private View vRow, vRowDefault;
    private LayoutInflater rowInflater;
    private Boolean paradeDataIsOutOfDate = false;
    private View paradeView;



    public AttemptUpdate(Context context, View view) {

        this.context = context;
        paradeInfo = context.getSharedPreferences("LXXparade", Context.MODE_PRIVATE);
        paradeEditor = paradeInfo.edit();
        isComplete = false;
        paradeView = view;

        nextParadeChecking = (LinearLayout) paradeView.findViewById(R.id.the_parade_details_checking);
        nextParadeNoInfo = (LinearLayout) paradeView.findViewById(R.id.the_parade_details_if_no_details);
        nextParadeLayout = (LinearLayout) paradeView.findViewById(R.id.the_parade_details_from_website);

        rowInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public interface AsyncResponse {
        void processFinish(String output);
    }



    String html;
    TextView paradeDate, typhoonFlight, tornadoFlight, hawkFlight, tucanoFlight, dutyNCO;
    Matcher m;
    Pattern p;

    private String date, typhoon, tornado, hawk, tucano, nco;

    @Override
    protected void onPreExecute() {

        nextParadeLayout.setVisibility ( View.GONE );
        nextParadeNoInfo.setVisibility ( View.GONE );
        nextParadeChecking.setVisibility ( View.VISIBLE );

    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {

            //Document d = Jsoup.connect("http://www.lxxsquadron.com/cadets-staff/details-for-next-parade").get();
            Connection c = Jsoup.connect("http://www.lxxsquadron.com/cadets-staff/details-for-next-parade");

            Connection.Response resp = c.execute();
            Document d; Log.i("resp", resp.statusMessage());

            if (resp.statusCode() == 200) {

                //if http connection exists, html document and parse table
                d = c.get();
                Element table = d.select("table").get(0);
                Elements rows = table.select("tr");

                Boolean testForEndOfRows = false;

                for (int i = 0; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cells = row.select("td");

                    if (cells.get(0).text().contains("Other Information") && !testForEndOfRows) {
                        testForEndOfRows = true;
                    }

                    if (!testForEndOfRows) {

                        def = cells.get(0).text();
                        req = cells.get(1).text();
                        if (!def.equals("")) {
                            // end of list
                            paradeDetails.add(new ParadeDetail(def, req));
                        }
                    }
                }

                gson = new Gson();
                paradeJSON = gson.toJson(paradeDetails);

                paradeEditor.putString("paradeDetail", paradeJSON);
                paradeEditor.commit();

            } else {

                paradeEditor.putString("paradeDetail", "empty");

            }


        } catch (IOException e) {

            e.printStackTrace();
            paradeEditor.putString("paradeDetail", "empty");

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void v) {

        paradeJSON = paradeInfo.getString ( "paradeDetail", "empty" );
        if (!paradeJSON.equals ( "empty" )) {
            nextParade = gson.fromJson ( paradeJSON,
                    new TypeToken<ArrayList<ParadeDetail>>() {
                    }.getType () ); // type ArrayList<ParadeDetail>

            String today = DateValue.getToday ();
            String pDate = DateValue.formatParadeData ( nextParade.get ( 0 ).getReq () );
            if (DateValue.getDateValue ( today ) > DateValue.getDateValue ( pDate )) {
                paradeDataIsOutOfDate = true;
            }

                if (!paradeDataIsOutOfDate &&
                    nextParadeLayout.getChildCount () == 0) {


                for (int i = 0; i < nextParade.size (); i++) {

                    vRow = rowInflater.inflate ( R.layout.parade_listview_row, null );
                    ((TextView) vRow.findViewById ( R.id.main_top ))
                            .setText ( nextParade.get ( i ).getDef () );
                    ((TextView) vRow.findViewById ( R.id.main_bottom ))
                            .setText ( nextParade.get ( i ).getReq () );

                    nextParadeLayout.addView ( vRow );
                }
                nextParadeLayout.setVisibility ( View.VISIBLE );
                nextParadeNoInfo.setVisibility ( View.GONE );
                nextParadeChecking.setVisibility ( View.GONE );


                } else {

                    vRowDefault = rowInflater.inflate ( R.layout.parade_listview_row_if_no_data_available, null );
//                            ((TextView) vRowDefault.findViewById ( R.id.noDataTop ))
//                                    .setText ( "Information for the next parade is not available" );
//                            ((TextView) vRowDefault.findViewById ( R.id.noDataBottom ))
//                                    .setText ( "Please check back regularly" );

                    if (nextParadeNoInfo.getChildCount () == 0) {
                        nextParadeNoInfo.addView ( vRowDefault );
                        nextParadeNoInfo.setClickable ( true );
                    }
                    nextParadeLayout.setVisibility ( View.GONE );
                    nextParadeNoInfo.setVisibility ( View.VISIBLE );
                    nextParadeChecking.setVisibility ( View.GONE );


                }

        }

    }


}

