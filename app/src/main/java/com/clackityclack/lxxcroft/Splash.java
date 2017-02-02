package com.clackityclack.lxxcroft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by cle99 on 02/02/2017.
 */

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle splashScreen) {
        super.onCreate ( splashScreen );
        setContentView ( R.layout.splash );

        Thread splash = new Thread () {

            public void run() {

                try {
                    sleep ( 2500 );
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                } finally {
                    Intent startMain = new Intent ( Splash.this, MainActivity.class );
                    startActivity ( startMain );
                }

            }

        };
        splash.start ();
    }
}
