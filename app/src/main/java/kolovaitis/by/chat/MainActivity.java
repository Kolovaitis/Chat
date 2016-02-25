package kolovaitis.by.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    public static ClientThread clientThread;
    public static String currentMessage = null;
    public static int[] colors = new int[6];
    public static int pastColor = 3;
    public boolean wasFirst = true;
    public long mynumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img = (ImageView) findViewById(R.id.imageView);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();
        ((LinearLayout) findViewById(R.id.linearLayout)).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                ((ScrollView) findViewById(R.id.scrollView)).fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        if (!isNetworkConnected()) {
            Toast.makeText(this, "Please on internet on your device and rerun the app", Toast.LENGTH_LONG).show();
            finish();
        }
        MyTask.canWork = true;
        MyTask.mainActivity = this;
        clientThread = new ClientThread();
        work();
        Thread thread = new Thread(MainActivity.clientThread);
        thread.start();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clientThread.setFinished(true);
        MyTask.canWork = false;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void send(View v) {
int sum=0;
        String s=((EditText) findViewById(R.id.editText)).getText()+"";
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)!=0){

                sum=1;
                break;
            }
        }if(sum!=0){
        MainActivity.currentMessage = ((EditText) findViewById(R.id.editText)).getText() + "";
        Log.d("sending message", ((EditText) findViewById(R.id.editText)).getText() + "");
        ((EditText) findViewById(R.id.editText)).setText("");}
    }

    public void work() {
        Preferences pref = Preferences.systemRoot();
        if (pref.getBoolean("flag", false)) {
            String string = pref.get("data", "no data");
            pref.putBoolean("flag", false);
            Log.d("text", pref.get("data", "no data"));
            LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
            LinearLayout linear = new LinearLayout(this);
            linear.setOrientation(LinearLayout.VERTICAL);

            TextView human = new TextView(this);
            TextView textView = new TextView(this);
            try {
                human.setText(" " + string.substring(0, string.indexOf(":")) + ":");
                human.setGravity(Gravity.LEFT);
                human.setTextSize(15);


                human.setPadding(0, 20, 0, 0);


                linear.addView(human);
                textView.setGravity(Gravity.LEFT);
                textView.setText(string.substring(string.indexOf(":") + 1));

                // linear.setGravity(View.TEXT_ALIGNMENT_CENTER);
            } catch (Exception e) {
                textView.setText(string);
                textView.setGravity(Gravity.CENTER);

            }
            if (wasFirst) {
                wasFirst = false;
                mynumber = Long.parseLong((textView.getText() + "").substring(6));
            }
            textView.setTextSize(20);
            textView.setBackgroundResource(R.drawable.opponent);
            textView.setPadding(100, 0, 70, 20);
            if (human.getText().toString().contains(mynumber + "")) {
                textView.setBackgroundResource(R.drawable.me);
                human.setGravity(Gravity.RIGHT);
                textView.setPadding(70, 0, 100, 20);
            }



            textView.setTextColor(Color.parseColor("#9e9e9e"));
            human.setTextColor(Color.parseColor("#9e9e9e"));
            linear.addView(textView);
            layout.addView(linear);

        }

        MyTask task = new MyTask();


        task.execute();
    }
}
