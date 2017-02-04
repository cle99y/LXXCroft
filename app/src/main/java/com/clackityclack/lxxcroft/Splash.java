package com.clackityclack.lxxcroft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by cle99 on 02/02/2017.
 */

public class Splash extends Activity {

    private void RunAnimation() {
        Animation a = AnimationUtils.loadAnimation ( this, R.anim.scale );
        a.reset ();
        TextView tv = (TextView) findViewById ( R.id.splash );
        tv.clearAnimation ();
        tv.startAnimation ( a );
    }

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
            //TextView load = (TextView) findViewById ( R.id.loading );
            TextView splash70 = (TextView) findViewById ( R.id.splash );

            public void run() {
                try {
                    runOnUiThread ( new Runnable () {
                        @Override
                        public void run() {
                            RunAnimation ();
                        }
                    });
                    sleep ( 3000 );

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

