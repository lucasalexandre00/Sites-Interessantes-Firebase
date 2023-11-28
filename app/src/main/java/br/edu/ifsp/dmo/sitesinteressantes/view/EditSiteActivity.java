package br.edu.ifsp.dmo.sitesinteressantes.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import br.edu.ifsp.dmo.sitesinteressantes.R;
import br.edu.ifsp.dmo.sitesinteressantes.model.Site;
import br.edu.ifsp.dmo.sitesinteressantes.model.TagSite;
import br.edu.ifsp.dmo.sitesinteressantes.model.dao.SiteDao;
import br.edu.ifsp.dmo.sitesinteressantes.model.dao.TagSiteDao;
import br.edu.ifsp.dmo.sitesinteressantes.view.viewmodel.site.SiteViewModel;
import br.edu.ifsp.dmo.sitesinteressantes.view.viewmodel.tag.TagViewModel;

public class EditSiteActivity extends AppCompatActivity {

    private EditText tituloEditText;
    private EditText urlEditText;
    private Spinner tagSpinner;
    private Button button;
    private SiteViewModel siteViewModel;
    private TagViewModel tagViewModel;
    private List<TagSite> tags;
    private Site site;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_site);

        tituloEditText = findViewById(R.id.edittext_title_update);
        urlEditText = findViewById(R.id.edittext_url_update);
        tagSpinner = findViewById(R.id.spinner_tag_update);
        button = findViewById(R.id.button_site_save_update);
        button.setOnClickListener(view -> save() );

        tagViewModel = new ViewModelProvider(this).get(TagViewModel.class);
        siteViewModel = new ViewModelProvider(this).get(SiteViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Novo site");
        }

        populateSpinner();

        Intent intent = getIntent();
        if (intent != null) {
            siteViewModel.recuperateAll().observe(
                    this,
                    observer -> {
                        site = observer.get(intent.getIntExtra("posicao_lista", 0));
                        tituloEditText.setText(site.getTitle());
                        urlEditText.setText(site.getUrl());
                    }
            );
        }
    }

    private void populateSpinner(){
        tagViewModel.recuperateAll().observe(
                this,
                observer -> {
                    tags = observer;
                    tagSpinner.setAdapter(new ArrayAdapter<TagSite>(this, android.R.layout.simple_spinner_dropdown_item, tags));

                }
        );
    }
    private void save() {
        Site siteAtualizado;
        Intent intent = getIntent();

        siteAtualizado = new Site(
                site.getId(),
                tituloEditText.getText().toString(),
                urlEditText.getText().toString(),
                (TagSite) tagSpinner.getSelectedItem()
        );

        siteViewModel.update(siteAtualizado).observe(
                this,
                observe -> {
                    if (observe.booleanValue()){
                        setResult(RESULT_OK, intent);
                        finish();
                    }else {
                        setResult(RESULT_CANCELED, intent);
                        finish();
                    }
                }
        );
    }
}