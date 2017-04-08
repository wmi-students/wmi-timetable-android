package pl.edu.amu.wmi.wmitimetable;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.edu.amu.wmi.wmitimetable.model.SpecialFilter;
import pl.edu.amu.wmi.wmitimetable.service.DataService;

public class SpecialFilterActivity extends AppCompatActivity {

    public final static String SPECIAL_FILTER_PARAM_NAME = "special_filter_param";

    DataService dataService;

    @BindView(R.id.edit_subject)
    EditText editSubject;

    @BindView(R.id.edit_study)
    EditText editStudy;

    @BindView(R.id.edit_year)
    EditText editYear;

    @BindView(R.id.edit_group)
    EditText editGroup;

    SpecialFilter specialFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        specialFilter = (SpecialFilter) getIntent().getSerializableExtra(SPECIAL_FILTER_PARAM_NAME);
        dataService = new DataService(this);

        loadData();
    }

    private void loadData() {
        if(specialFilter == null)return;

        editSubject.setText(specialFilter.getSubject());
        editStudy.setText(specialFilter.getStudy());
        editYear.setText(specialFilter.getYear());
        editGroup.setText(specialFilter.getGroup());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if( id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonSaveClicked(View view){
        if(!validateFields()){
            showInvalidateInfo();
            return;
        }

        String subject = editSubject.getText().toString();
        String study = editStudy.getText().toString();
        String year = editYear.getText().toString();
        String group = editGroup.getText().toString();

        if(specialFilter == null){
            specialFilter = new SpecialFilter();
            dataService.getSpecialFilters().add(specialFilter);
        }else {
            int index = dataService.getSpecialFilters().indexOf(specialFilter);
            specialFilter = dataService.getSpecialFilters().get(index);
        }

        specialFilter.setSubject(subject);
        specialFilter.setStudy(study);
        specialFilter.setYear(year);
        specialFilter.setGroup(group);

        dataService.saveSpecialFilters();

        finish();
    }

    private void showInvalidateInfo() {
        Snackbar.make(findViewById(android.R.id.content), "Wypełnij wszystkie pola.", Snackbar.LENGTH_LONG).show();
    }

    private boolean validateFields() {
        return !editSubject.getText().toString().isEmpty() &&
                !editStudy.getText().toString().isEmpty() &&
                !editYear.getText().toString().isEmpty();
    }
}
