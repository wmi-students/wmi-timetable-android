package pl.edu.amu.wmi.wmitimetable;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;


public class AboutActivity extends AppCompatActivity {

    private static final String[] AUTHORS = {
            "Wojciech Klessa <info@polskiemedia.com>",
            "Szymon Michalczyk <szymon@szymonmichalczyk.com>",
            "Kacper Pieszyk <kacpie@st.amu.edu.pl>",
            "Wojciech Łoza"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setVersion();
        setAuthors();
    }

    private void setVersion() {
        Resources res = getResources();
        String version = String.format(res.getString(R.string.about_version), BuildConfig.VERSION_NAME);
        String buildTime = String.format(res.getString(R.string.about_build_time),
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", new Locale("pl", "PL")).format(new Date(BuildConfig.TIMESTAMP)));
        TextView aboutVersion = (TextView) findViewById(R.id.aboutVersion);
        TextView aboutBuildTime = (TextView) findViewById(R.id.aboutBuildTime);

        aboutVersion.setText(version);
        aboutBuildTime.setText(buildTime);
    }

    private void setAuthors() {
        ListView listView = (ListView) findViewById(R.id.aboutAuthorsList);
        ArrayList<String> listItems = new ArrayList<>(Arrays.asList(AUTHORS));
        ListAdapter listAdapter = new ArrayAdapter<>(
                this, R.layout.about_authors_item, listItems);

        listView.setAdapter(listAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
