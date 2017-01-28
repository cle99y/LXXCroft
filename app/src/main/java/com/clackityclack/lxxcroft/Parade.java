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

/**
 * Created by cle99 on 15/01/2017.
 */

public class Parade extends Fragment {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private TextView welcome;
    private String welcomeText, rank, fName, lName;

    public static Parade newInstance() {

        Parade f = new Parade();



        return f;
    }

    WebView wvH, wvB ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sp.edit();

        welcomeText = "";
        View v = inflater.inflate(R.layout.parade, container, false);
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

        wvB = (WebView) v.findViewById(R.id.ParadeWebViewBody);
        wvB.getSettings().setJavaScriptEnabled(true);
        wvB.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                wvB.loadUrl("javascript:(function() { " +
                        "document.getElementsByTagName('header')[0].style.display=\"none\" ; " +
                        "document.getElementsByTagName('footer')[0].style.display=\"none\" ; " +
                        "document.getElementsByClassName('metaslider')[0].style.display=\"none\" ; " +
                        "})()"
                );
            }
        });
        wvB.loadUrl("http://www.lxxsquadron.com/cadets-staff/details-for-next-parade/");

        return v;


    }

}
