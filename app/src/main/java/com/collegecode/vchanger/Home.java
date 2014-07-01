package com.collegecode.vchanger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;


public class Home extends Activity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        backupDialog();
        /*
        if(!preferences.getBoolean("isFirst", false))
        {
            backupDialog();
            preferences.edit().putBoolean("isFirst", true).apply();
        }*/
    }

    public void backupDialog(){
        new Backup().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class Backup extends AsyncTask<Void, Void, Void>{
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setIndeterminate(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Backing Up");
            dialog.setMessage("Please wait while we backup your original voice pack...");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String zip = Environment.getExternalStorageDirectory() + "/Android/data/com.google.android.apps.maps/testdata/voice";
            String backup = Environment.getExternalStorageDirectory() + "/GMaps-VoiceBackup/backup.zip";
            File file = new File(zip);

            for(int i = 0 ; i < file.listFiles().length; i++){
                System.out.println(file.listFiles()[i].getPath());
            }

            Zipper zipper = new Zipper();
            zipper.zipFileAtPath(zip, backup);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            Toast.makeText(context, "Current voice pack has been backed up!",Toast.LENGTH_LONG).show();
            super.onPostExecute(aVoid);
        }
    }
}
