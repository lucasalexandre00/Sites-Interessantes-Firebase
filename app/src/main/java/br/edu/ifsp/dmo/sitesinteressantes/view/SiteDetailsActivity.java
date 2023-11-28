package br.edu.ifsp.dmo.sitesinteressantes.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;
import br.edu.ifsp.dmo.sitesinteressantes.R;
import br.edu.ifsp.dmo.sitesinteressantes.model.Site;
import br.edu.ifsp.dmo.sitesinteressantes.model.TagSite;
import br.edu.ifsp.dmo.sitesinteressantes.view.viewmodel.site.SiteViewModel;
import br.edu.ifsp.dmo.sitesinteressantes.view.viewmodel.tag.TagViewModel;

public class SiteDetailsActivity extends AppCompatActivity {

    private EditText tituloEditText;
    private EditText urlEditText;
    private Spinner tagSpinner;
    private Button button;
    private SiteViewModel siteViewModel;
    private TagViewModel tagViewModel;
    List<TagSite> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_details);

        tituloEditText = findViewById(R.id.edittext_title);
        urlEditText = findViewById(R.id.edittext_url);
        tagSpinner = findViewById(R.id.spinner_tag);
        button = findViewById(R.id.button_site_save);
        button.setOnClickListener(view -> save() );

        tagViewModel = new ViewModelProvider(this).get(TagViewModel.class);
        siteViewModel = new ViewModelProvider(this).get(SiteViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Novo site");
        }

        populateSpinner();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    private void save(){
        String titulo = tituloEditText.getText().toString();
        String url = urlEditText.getText().toString();
        TagSite tag = (TagSite) tagSpinner.getSelectedItem();

        if(titulo.isEmpty() || url.isEmpty()){
            Toast.makeText(this, "Informe todos os dados.", Toast.LENGTH_SHORT).show();
        }else{
            Site site = new Site(titulo, url, tag);
            siteViewModel.create(site);
            Toast.makeText(this, "Dados salvos com sucesso.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}