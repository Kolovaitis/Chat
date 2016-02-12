package kolovaitis.by.chat;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class ClientThread implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private DataOutputStream writer;
    private boolean finished = false;
    private static final int SERVER_PORT = 4444;
    private String data;
    private static final String SERVER_IP = "46.101.96.234";

    @Override
    public void run() {
        try {

            //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            socket = new Socket(SERVER_IP, SERVER_PORT);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Log.d("from clientThread", "all is ok");
            while (finished==false) {
                try {
                    Thread.sleep(10);




                } catch (Exception e) {
                    e.printStackTrace();
                }if(reader.ready())paint(reader.readLine());
                if(MainActivity.currentMessage!=null)write(MainActivity.currentMessage);
            }
            socket.close();
        } catch (IOException e) {
e.printStackTrace();
        }
    }

    public boolean getFinished() {
        return this.finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
public void write(String s){

        try {
            final PrintWriter writer = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            final String string = s;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    writer.println(string);
                }
            });
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainActivity.currentMessage = null;
    }

   public void paint(String s){




Preferences pref=Preferences.systemRoot();

       pref.put("data", s);
       pref.putBoolean("flag", true);


       Log.d("from clientThread", s);

}

}
