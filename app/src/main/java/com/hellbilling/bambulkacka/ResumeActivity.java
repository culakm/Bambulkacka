package com.hellbilling.bambulkacka;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;


public class ResumeActivity extends ActionBarActivity {

    // Pocitadla
    int pokusov = 0;
    int spravne = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        pokusov = getIntent().getExtras().getInt("pokusov");
        spravne = getIntent().getExtras().getInt("spravne");

        // zistenie widgetov z obrazovky

        TextView textSpravneCounter =(TextView)findViewById(R.id.textSpravneCounter);
        TextView textPokusovCounter =(TextView)findViewById(R.id.textPokusovCounter);
        textSpravneCounter.setText(spravne + "");
        textPokusovCounter.setText(pokusov + "");
    }

    public void buttonSendsMessage(View view) {
//        Intent intent;
//        intent = new Intent(this, Kalkulacka.class);
//        startActivity(intent);
//        browser
//startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=MdGvU-YDElQ")));

        //https://developers.google.com/youtube/android/player/
//        Intent intent = YouTubeIntents.createUserIntent(this, channelName);
//        startActivity(intent);

        // Run youtube application
        if (isPackageInstalled(getApplicationContext(),"com.google.android.youtube")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + "p6q7jR63dGc"));
            startActivity(intent);
        }
        else {
        // Run youtube in browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=MdGvU-YDElQ")));
        }
    }

    public void buttonSendsMessageAgain(View view) {
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resume, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
