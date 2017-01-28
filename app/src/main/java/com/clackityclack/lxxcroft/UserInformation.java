package com.clackityclack.lxxcroft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.layout.simple_spinner_dropdown_item;

/**
 * Created by cle99 on 21/01/2017.
 */

public class UserInformation extends Fragment {

    public static UserInformation newInstance () {
        UserInformation f = new UserInformation();
        return f;
    }

    WebView wv;
    EditText firstName, lastName, emailAddress;
    SharedPreferences userData;
    SharedPreferences.Editor editor;
    ArrayAdapter<String> spinAdapter;
    View v;
    String rank, savedText;

    public static void restartActivity(Activity act){

        Intent intent=new Intent();
        intent.setClass(act, act.getClass());
        act.startActivity(intent);
        act.recreate();

    }

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.user_info, container, false);


        userData = getContext().getSharedPreferences("LXXuser", Context.MODE_PRIVATE);
        editor = userData.edit();

        firstName = (EditText) v.findViewById(R.id.first_name);
        savedText = userData.getString("firstName", "empty");
        if (!savedText.equals("empty")) {
            firstName.setText(savedText, TextView.BufferType.EDITABLE);
        }

        lastName = (EditText) v.findViewById(R.id.last_name);
        savedText = userData.getString("lastName", "empty");
        if (!savedText.equals("empty")) {
            lastName.setText(savedText, TextView.BufferType.EDITABLE);
        }

        emailAddress = (EditText) v.findViewById(R.id.email_addreass);
        savedText = userData.getString("emailAddress", "empty");
        if (!savedText.equals("empty")) {
            emailAddress.setText(savedText, TextView.BufferType.EDITABLE);
        }

//--SPINNER-----------------------------------------------------------------------------------------
        final Spinner spinner = (Spinner) v.findViewById(R.id.rank_select);
        String [] ranks = {"Select. . .", "Cadet", "Corporal", "Sergeant",
                "Flight Sergeant", "Cadet Warrant Officer"};
        spinner.setSelection(0);

        String compareText = userData.getString("rank", "empty");

        // Create an ArrayAdapter using the string array and a default spinner layout

        spinAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, ranks);
        // Specify the layout to use when the list of choices appears
        spinAdapter.setDropDownViewResource(simple_spinner_dropdown_item) ;
        // Apply the spinAdapter to the spinner
        spinner.setAdapter(spinAdapter);
        if (!compareText.equals("empty")) {
            int spinPos = spinAdapter.getPosition(compareText);
            spinner.setSelection(spinPos);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                rank = parent.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                rank = "";
            }
        });
//--------------------------------------------------------------------------------------------------
        Button submit = (Button) v.findViewById(R.id.btn_submit);
        Button clear = (Button) v.findViewById(R.id.clear_data);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (emailAddress.getText().toString().indexOf("@") == -1) {
                    emailAddress.setText("");
                    if (userData.contains("emailAddress")) {
                        editor.remove("emailAddress");
                    }
                }

                if (firstName.getText().toString().equals("") ||
                    lastName.getText().toString().equals("") ||
                    rank == "Select. . .") {

                    Toast.makeText(getContext(),"Please complete all required fields", Toast.LENGTH_SHORT).show();

                } else {
                    //store saved information

                    editor.putString("firstName", firstName.getText().toString());
                    editor.putString("lastName", lastName.getText().toString());
                    editor.putString("rank", rank);
                    editor.putString("emailAddress", emailAddress.getText().toString());
                    if(editor.commit()) {
                        Toast.makeText(getContext(),"User profile successfully saved",
                                Toast.LENGTH_SHORT).show();
                        //restartActivity(getActivity());
                    }
                }
                //Toast.makeText(getContext(),emailAddress.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (userData.contains("firstName")) {
                    editor.remove("firstName");
                    firstName.setText("");
                }
                if (userData.contains("lastName")) {
                    editor.remove("lastName");
                    lastName.setText("");
                }
                if (userData.contains("rank")) {
                    editor.remove("rank");
                    spinner.setSelection(0);
                }
                if (userData.contains("emailAddress")) {
                    editor.remove("emailAddress");
                    emailAddress.setText("");
                }
                if (editor.commit()) {
                    Toast.makeText(getContext(), "User data deleted - new data required for leave requests",
                            Toast.LENGTH_LONG).show();
                    Activity act = getActivity();

                }

            }
        });
        InputMethodManager imm;


        return v;

    }
}
