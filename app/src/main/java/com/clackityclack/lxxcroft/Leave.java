package com.clackityclack.lxxcroft;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by cle99 on 15/01/2017.
 */

public class Leave extends Fragment {

    SharedPreferences userData;
    SharedPreferences.Editor editor;
    TextView dateFrom, dateTo, reason;
    Button submit;
    Mail m;
    Boolean emailSent = false;
    String[] months = {"Jan", "Feb", "Mar", "Apr",
                               "May", "Jun", "Jul", "Aug",
                               "Sep", "Oct", "Nov", "Dec"};

    String rank, fName, lName, email;

    public class Mail extends javax.mail.Authenticator {
        private String _user;
        private String _pass;

        private String[] _to;
        private String _from;

        private String _port;
        private String _sport;

        private String _host;

        private String _subject;
        private String _body;

        private boolean _auth;

        private boolean _debuggable;

        private Multipart _multipart;


        public Mail() {
            _host = "smtp.gmail.com"; // default smtp server
            _port = "465"; // default smtp port
            _sport = "465"; // default socketfactory port

            _user = ""; // username
            _pass = ""; // password
            _from = ""; // email sent from
            _subject = "test subject"; // email subject
            _body = "test body"; // email body

            _debuggable = false; // debug mode on or off - default off
            _auth = true; // smtp authentication - default on

            _multipart = new MimeMultipart();

            // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
        }

        public Mail(String user, String pass) {
            this();

            _user = user;
            _pass = pass;
        }

        public boolean send() throws Exception {  // r = rank, e = email
            Properties props = _setProperties();


            if(!_user.equals("") && !_pass.equals("") && _to.length > 0 && !_from.equals("") && !_subject.equals("") && !_body.equals("")) {
                /*
                Log.i("user", _user);
                Log.i("pass", _pass);
                Log.i("length", String.valueOf(_to.length));
                Log.i("from", _from);
                Log.i("subject", _subject);
                Log.i("body", _body);
                */

                Session session = Session.getInstance(props, this);

                MimeMessage msg = new MimeMessage(session);

                msg.setFrom(new InternetAddress(_from));

                InternetAddress[] addressTo = new InternetAddress[_to.length];
                for (int i = 0; i < _to.length; i++) {
                    addressTo[i] = new InternetAddress(_to[i]);
                }

                msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

                msg.setSubject(_subject);
                msg.setSentDate(new Date());

                // setup message body
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(_body);
                _multipart.addBodyPart(messageBodyPart);

                // Put parts in message
                msg.setContent(_multipart);

                // send email
                Transport.send(msg);

                return true;
            } else {
                return false;
            }
        }

/*
public void addAttachment(String filename) throws Exception {
BodyPart messageBodyPart = new MimeBodyPart();
DataSource source = new FileDataSource(filename);
messageBodyPart.setDataHandler(new DataHandler(source));
messageBodyPart.setFileName(filename);

_multipart.addBodyPart(messageBodyPart);
}
*/

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(_user, _pass);
        }

        private Properties _setProperties() {
            Properties props = new Properties();

            props.put("mail.smtp.host", _host);

            if(_debuggable) {
                props.put("mail.debug", "true");
            }

            if(_auth) {
                props.put("mail.smtp.auth", "true");
            }

            props.put("mail.smtp.port", _port);
            props.put("mail.smtp.socketFactory.port", _sport);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");

            return props;
        }

        // the getters and setters
        public String getBody() {
            return _body;
        }

        public void setBody(String _body) {
            this._body = _body;
        }

        public void setTo(String[] to) {
            this._to = to;
        }

        public void setFrom(String from) {
            this._from = from;
        }

        public void setSubject(String subject) {
            this._subject = subject;
        }


    }

    public int getDateValue (String d) {    // d representing a date value dd mmm yyyy


        String[] dSplit = d.split(" ");
        int dVal = 0;
        dVal += Integer.parseInt(dSplit[2]);
        dVal *= 1 + Arrays.asList(months).indexOf(dSplit[1]);
        dVal += Integer.parseInt(dSplit[0]);

        return dVal;
    }

    public void DateListener (final TextView tView) {

        tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar=Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(dayOfMonth) + " " + months[monthOfYear]
                                + " " + String.valueOf(year);
                        tView.setText(date);

                    }
                }, yy, mm, dd);

                datePicker.show();
            }
        });

    }

    public String[] contactsToArray(String r, String[] a) {
        List<String> list = new ArrayList<String>();

        list.add(a[0]);
        if (!r.equals("Cadet")) {
            list.add(a[1]);
        }
        if (!a[2].equals("")) {
            list.add(a[2]);
        }

        int sizeOfArray = list.size();
        String[] res = new  String[sizeOfArray];
        for (int i = 0; i < sizeOfArray; i++) {
            res[i] = list.get(i);
        }



        return res;
    }


    //@RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        userData = getActivity().getSharedPreferences("LXXuser",Context.MODE_PRIVATE);
        editor = userData.edit();
        rank = userData.getString("rank", "empty");
        fName = userData.getString("firstName", "empty");
        lName = userData.getString("lastName", "empty");
        email = userData.getString("emailAddress", "empty");

        View v = inflater.inflate(R.layout.leave, container, false);

        dateFrom = (TextView) v.findViewById(R.id.dateFrom);
        dateTo = (TextView) v.findViewById(R.id.dateTo);
        reason = (EditText) v.findViewById(R.id.reason);

        m = new Mail("lxxleaverequest@gmail.com", "meet in the horseshoe");

        String[] emailAddressList = {"chilemerlot@gmail.com", "paulcle99@gmail.com", email};
        String[] toArr = contactsToArray(rank, emailAddressList);

        m.setTo(toArr);
        m.setFrom("lxxleaverequest@gmail.com");
        m.setSubject("New Leave Request");
        m.setBody("version with async task");

        DateListener(dateFrom);
        DateListener((dateTo));

        submit = (Button) v.findViewById(R.id.leaveSubmit);
        submit.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                Log.i("email address", email);

                String d1 = dateFrom.getText().toString();
                String d2 = dateTo.getText().toString();
                String excuse = reason.getText().toString();

                if (fName.equals("empty")) {
                    Toast.makeText(getContext(), "USER INFO error: Please provide user name & rank",
                            Toast.LENGTH_LONG).show();
                } else if (d1.equals("") || d2.equals("") || excuse.equals("")) {
                    Toast.makeText(getContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (getDateValue(d1) >= getDateValue(d2)) {
                        Toast.makeText(getContext(), "Invalid date range"
                        , Toast.LENGTH_SHORT).show();
                    } else {
                        submit.setText("Sending. . .");
                        m.setBody("The following Leave Request has been submitted by: " +
                                System.lineSeparator() + System.lineSeparator() +
                                rank + " " + fName + " " + lName +
                                System.lineSeparator() + System.lineSeparator() +
                                "between the dates: " + d1 + " and " + d2 +
                                System.lineSeparator() + System.lineSeparator() +
                                "The reason given is as follows: " + System.lineSeparator() +
                                System.lineSeparator() +
                                reason.getText().toString()
                        );

                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... voids) {



                                try {
                                    if (m.send()) emailSent = true;


                                } catch(Exception e) {
                                    //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                                    Log.e("MailApp", "Could not send email", e);
                                }

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void v) {
                                if (emailSent) {
                                    Toast.makeText(getContext(), "email sent successfully", Toast.LENGTH_LONG).show();
                                    submit.setText("Submit");
                                } else {
                                    Toast.makeText(getContext(), "email  not sent.", Toast.LENGTH_LONG).show();
                                    submit.setText("Submit");
                                }
                                dateFrom.setText(null);
                                dateTo.setText(null);
                                reason.setText(null);

                            }

                        }.execute();

                    }

                }

            }
        });


//--------------------------------------------------------------------------------------------------

//        userData = getActivity().getPreferences(Context.MODE_PRIVATE);
//        editor = userData.edit();
//
//        dummy.setText(userData.getString("emailAddress", "empty") + " " +
//                      userData.getString("firstName", "empty") + " " +
//                      userData.getString("lastName", "empty"));


        return v;

    }
//--------------------------------------------------------------------------------------------------


    public static Leave newInstance() {

        Leave f = new Leave();

        return f;
    }
}
