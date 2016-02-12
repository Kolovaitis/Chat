package kolovaitis.by.chat;

import android.os.AsyncTask;

import java.io.IOException;

/**
 *
 */
    public class MyTask extends AsyncTask {
        private static final int SLEEP_TIME = 100;
    public static boolean canWork=true;
  public static MainActivity mainActivity;


    @Override
        protected Object doInBackground(Object[] objects) {






            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if(canWork)mainActivity.work();
        }
    }
