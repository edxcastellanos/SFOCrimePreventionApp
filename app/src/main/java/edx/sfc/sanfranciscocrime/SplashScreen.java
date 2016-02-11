package edx.sfc.sanfranciscocrime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        Thread splashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(1000);
                    }
                }
                catch(InterruptedException ex){
                }
                finish();

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        };

        splashThread.start();
    }
}
