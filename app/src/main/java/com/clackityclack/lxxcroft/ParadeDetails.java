package com.clackityclack.lxxcroft;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cle99 on 15/01/2017.
 */

public class ParadeDetails extends Fragment {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private TextView welcome;
    private String welcomeText, rank, fName, lName;

    public static ParadeDetails newInstance() {

        ParadeDetails f = new ParadeDetails();



        return f;
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

    public class GetWebData extends AsyncTask<Void, Void, Void> {

        private String date, typhoon, tornado, hawk, tucano, nco;

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                Document d = Jsoup.connect("http://www.lxxsquadron.com/cadets-staff/details-for-next-parade").get();
                html = d.text();
                Log.i("HTML", html);

                date = splitText(html, "Next Parade Date: (.*?)Typhoon"); Log.i("DATE", date);
                typhoon = splitText(html, "Typhoon Flight: (.*?)Tornado"); Log.i("TYPHOON", typhoon);
                tornado = splitText(html, "Tornado Flight: (.*?)Hawk"); Log.i("TORNADO", tornado);
                hawk = splitText(html, "Hawk Flight: (.*?)Tucano"); Log.i("HAWK", hawk);
                tucano = splitText(html, "Tucano Flight: (.*?)Duty"); Log.i("TUCANO", tucano);
                nco = splitText(html, "Duty NCO: (.*?)Other"); Log.i("NCO", nco);
                Log.i("text", typhoon);

            } catch (IOException e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            paradeDate.setText(date);
            typhoonFlight.setText(typhoon);
            tornadoFlight.setText(tornado);
            hawkFlight.setText(hawk);
            tucanoFlight.setText(tucano);
            dutyNCO.setText(nco);

        }
    }

    WebView wvH, wvB ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sp.edit();


        welcomeText = "";
        View v = inflater.inflate(R.layout.parade_details, container, false);
        welcome = (TextView) v.findViewById(R.id.welcome);
        if (!sp.getString("firstName", "empty").equals("empty")) {

            rank = sp.getString("rank", "empty");
            fName = sp.getString("firstName", "empty");
            lName = sp.getString("lastName", "empty");
            welcomeText = "Welcome back " + rank + " " + fName + " " + lName;
            if (welcomeText != "") {
                welcome.setVisibility(View.VISIBLE);
            }
        }

        welcome.setText(welcomeText);

        paradeDate = (TextView) v.findViewById(R.id.date);
        typhoonFlight = (TextView) v.findViewById(R.id.typhoon);
        tornadoFlight = (TextView) v.findViewById(R.id.tornado);
        hawkFlight = (TextView) v.findViewById(R.id.hawk);
        tucanoFlight = (TextView) v.findViewById(R.id.tucano);
        dutyNCO = (TextView) v.findViewById(R.id.nco);
        new GetWebData().execute();


        return v;


    }

}
