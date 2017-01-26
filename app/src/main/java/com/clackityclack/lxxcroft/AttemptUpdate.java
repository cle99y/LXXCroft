package com.clackityclack.lxxcroft;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cle99 on 25/01/2017.
 */

public class AttemptUpdate extends AsyncTask<Void, Void, Void> {

    SharedPreferences sp;

    SharedPreferences.Editor editor;
    static int var = 0;
    String[] sArray;
    String obj;
    Context appContext;

    public AttemptUpdate() {

        appContext = ParadeDetails.getContextOfApplication();
        if (appContext == null) {Log.i("context", "NULL");}
        sp = PreferenceManager.getDefaultSharedPreferences(appContext);
        editor = sp.edit();

    }

    String html;
    TextView paradeDate, typhoonFlight, tornadoFlight, hawkFlight, tucanoFlight, dutyNCO;
    Matcher m;
    Pattern p;

    public String splitText (String s, String r) {

        p = Pattern.compile(r);
        m = p.matcher(s);

        if (m.find()) {
            return m.group(1);
        } else {
            return "Data unavailable!";
        }

    }

    private String date, typhoon, tornado, hawk, tucano, nco;

    @Override
    protected Void doInBackground(Void... voids) {

        try {

            Document d = Jsoup.connect("http://www.lxxsquadron.com/cadets-staff/details-for-next-parade").get();
            html = d.text();
            Log.i("HTML", html);

            date = splitText(html, "Next Parade Date: (.*?)Typhoon"); Log.i("DATE", date);
            editor.putString("pdate", date);

            typhoon = splitText(html, "Typhoon Flight: (.*?)Tornado"); Log.i("TYPHOON", typhoon);
            editor.putString("typhoon", typhoon);

            tornado = splitText(html, "Tornado Flight: (.*?)Hawk"); Log.i("TORNADO", tornado);
            editor.putString("tornado", tornado);

            hawk = splitText(html, "Hawk Flight: (.*?)Tucano"); Log.i("HAWK", hawk);
            editor.putString("hawk", hawk);

            tucano = splitText(html, "Tucano Flight: (.*?)Duty"); Log.i("TUCANO", tucano);
            editor.putString("tucano", tucano);

            nco = splitText(html, "Duty NCO: (.*?)Other"); Log.i("NCO", nco);
            editor.putString("nco", nco);

            if (editor.commit()) {
                Log.i("SharedPref", sp.getString("nco", "bollocks"));
            }



        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;
    }
}

