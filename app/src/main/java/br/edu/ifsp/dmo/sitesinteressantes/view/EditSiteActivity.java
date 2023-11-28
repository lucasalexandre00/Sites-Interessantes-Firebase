package br.edu.ifsp.dmo.sitesinteressantes.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

public class EditSiteActivity extends AppCompatActivity {

    private EditText tituloEditText;
    private EditText urlEditText;
    private Spinner tagSpinner;
    private Button button;
    private SiteDao siteDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_site);


        //siteDao = new SiteDao(this);

        tituloEditText = findViewById(R.id.edittext_title_update);
        urlEditText = findViewById(R.id.edittext_url_update);
        tagSpinner = findViewById(R.id.spinner_tag_update);
        button = findViewById(R.id.button_site_save_update);
        button.setOnClickListener(view -> save() );

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Novo site");
        }

        populateSpinner();

        Intent intent = getIntent();
        if (intent != null) {
            //Site site = siteDao.recuperateAll().get(intent.getIntExtra("posicao_lista", 0));

            //tituloEditText.setText(site.getTitle());
            //urlEditText.setText(site.getUrl());
        }

    }

    private void populateSpinner(){
        //List<TagSite> tags = new TagSiteDao().recuperateAll();
        //tagSpinner.setAdapter(new ArrayAdapter<TagSite>(this, android.R.layout.simple_spinner_dropdown_item, tags));
    }
    private void save() {
        Site siteDesatualizado;
        Site siteAtualizado;
        Intent intent = getIntent();
        //siteDesatualizado = siteDao.recuperateAll().get(intent.getIntExtra("posicao_lista", 0));

        siteAtualizado = new Site(
                tituloEditText.getText().toString(),
                urlEditText.getText().toString(),
                (TagSite) tagSpinner.getSelectedItem()
        );


       /* if (siteDao.update(siteDesatualizado, siteAtualizado)){
            setResult(RESULT_OK, intent);
            finish();
        }*/
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}