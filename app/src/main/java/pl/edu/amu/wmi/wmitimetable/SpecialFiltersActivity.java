package pl.edu.amu.wmi.wmitimetable;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pl.edu.amu.wmi.wmitimetable.adapter.SpecialFiltersAdapter;
import pl.edu.amu.wmi.wmitimetable.model.SpecialFilter;
import pl.edu.amu.wmi.wmitimetable.service.DataService;

public class SpecialFiltersActivity extends AppCompatActivity {

    SpecialFiltersAdapter adapter;
    ListView listView;
    List<SpecialFilter> specialFilters;
    DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Filtry specjalne");
        dataService = new DataService(this);
        listView = (ListView) findViewById(R.id.list_filters);
        loadData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        dataService.loadSpecialFilters();
        specialFilters = dataService.getSpecialFilters();
        adapter = new SpecialFiltersAdapter(this,R.layout.special_filter_item, specialFilters);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                SpecialFilter specialFilter = (SpecialFilter) parent.getItemAtPosition(position);
                Intent intent = new Intent(SpecialFiltersActivity.this, SpecialFilterActivity.class);
                intent.putExtra(SpecialFilterActivity.SPECIAL_FILTER_PARAM_NAME, specialFilter);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if( id == android.R.id.home){
            finish();
            return true;
        }

        if (id == R.id.action_add) {
            goSpecialFilter();
            return true;
        }

        if (id == R.id.action_clear) {
            clearSpecialFilters();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearSpecialFilters() {
        dataService.setSpecialFilters(new ArrayList<SpecialFilter>());
        dataService.saveSpecialFilters();
        loadData();
    }

    private void goSpecialFilter() {
        Intent intent = new Intent(this, SpecialFilterActivity.class);
        startActivity(intent);
    }
}
