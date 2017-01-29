package com.clackityclack.lxxcroft;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

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
    private Gson gson;
    private String def, req, paradeJSON;
    private ParadeDetail pd;



    public AttemptUpdate(Context context) {

        this.context = context;
        paradeInfo = context.getSharedPreferences("LXXparade", Context.MODE_PRIVATE);
        paradeEditor = paradeInfo.edit();
    }

    String html;
    TextView paradeDate, typhoonFlight, tornadoFlight, hawkFlight, tucanoFlight, dutyNCO;
    Matcher m;
    Pattern p;

    private String date, typhoon, tornado, hawk, tucano, nco;

    @Override
    protected Void doInBackground(Void... voids) {

        try {

            //Document d = Jsoup.connect("http://www.lxxsquadron.com/cadets-staff/details-for-next-parade").get();
            Connection c = Jsoup.connect("http://www.lxxsquadron.com/cadets-staff/details-for-next-parade");

            Connection.Response resp = c.execute();
            Document d; Log.i("resp", resp.statusMessage());

            if (resp.statusCode() == 200) {
                d = c.get();
                Element table = d.select("table").get(0);
                Elements rows = table.select("tr");

                Boolean test = false;

                for (int i = 0; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cells = row.select("td");

                    if (cells.get(0).text().contains("Other Information") && test == false) {
                        test = true;
                    }

                    if (test == false) {

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
}

