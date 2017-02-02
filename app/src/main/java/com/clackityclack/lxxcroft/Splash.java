package com.clackityclack.lxxcroft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by cle99 on 02/02/2017.
 */

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle splashScreen) {
        super.onCreate ( splashScreen );
        setContentView ( R.layout.splash );

        Thread splash = new Thread () {

            String sLoad = "Loading";
            float currentSize = 20;
            float startSize = 20;
            float endSize = 35;

            public int cycle = 0;
            TextView load = (TextView) findViewById ( R.id.loading );
            TextView splash70 = (TextView) findViewById ( R.id.splash );

            public void run() {
                try {
                    while (cycle < 100) {
                        Thread.sleep ( 25 );
                        runOnUiThread ( new Runnable () {
                            @Override
                            public void run() {
                                load.setText ( sLoad );
                                splash70.setTextSize ( TypedValue.COMPLEX_UNIT_SP, currentSize );
                                currentSize += (endSize - startSize) / 100;
                                if (cycle % 20 == 0) {
                                    sLoad += ".";
                                }
                                cycle += 1;
                            }
                        });
                    }
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

