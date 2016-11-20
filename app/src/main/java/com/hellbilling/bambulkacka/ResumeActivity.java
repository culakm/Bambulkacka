package com.hellbilling.bambulkacka;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.util.List;


public class ResumeActivity extends ActionBarActivity {

    // Pocitadla
    private int attempts = 0;
    private int correct = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        attempts = getIntent().getExtras().getInt("attempts");
        correct = getIntent().getExtras().getInt("correct");

        // zistenie widgetov z obrazovky

        TextView textSpravneCounter =(TextView)findViewById(R.id.correctCounterString);
        TextView textPokusovCounter =(TextView)findViewById(R.id.attemptsCounterString);
        textSpravneCounter.setText(correct + "");
        textPokusovCounter.setText(attempts + "");
    }

    public void buttonSendsMessage(View view) {
//        Intent intent;
//        intent = new Intent(this, Calculator.class);
//        startActivity(intent);
//        browser
//startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=MdGvU-YDElQ")));

        //https://developers.google.com/youtube/android/player/
//        Intent intent = YouTubeIntents.createUserIntent(this, channelName);
//        startActivity(intent);

        Resources res = getResources();
        String[] videos = res.getStringArray(R.array.videos);
        int i = Utils.generateRandomInt(0,videos.length - 1);
        String video = videos[i];

        // Run youtube application
        if (isPackageInstalled(getApplicationContext(),"com.google.android.youtube")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + video));
            startActivity(intent);
        }
        else {
        // Run youtube in browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + video)));
        }
    }

    public void buttonSendsMessageAgain(View view) {
        goHome();
    }

    private static boolean isPackageInstalled(Context context, String packageName) {
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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.results:
                intent = new Intent(this, ResultsActivity.class);
                startActivity(intent);
                return(true);
            // Startovanie settings aktivity
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("preferencesType","main");
                startActivity(intent);
                return(true);
            default:
                return(super.onOptionsItemSelected(item));
        }
    }


    @Override
    public void onBackPressed(){
        goHome();
    }

    private void goHome(){
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
