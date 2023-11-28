package br.edu.ifsp.dmo.sitesinteressantes.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import br.edu.ifsp.dmo.sitesinteressantes.R;
import br.edu.ifsp.dmo.sitesinteressantes.model.Site;
import br.edu.ifsp.dmo.sitesinteressantes.model.dao.SiteDao;
import br.edu.ifsp.dmo.sitesinteressantes.view.adapter.SiteAdapter;
import br.edu.ifsp.dmo.sitesinteressantes.view.adapter.SiteClickListener;
import br.edu.ifsp.dmo.sitesinteressantes.view.viewmodel.site.SiteViewModel;

public class MainActivity extends AppCompatActivity implements SiteClickListener {

    private FloatingActionButton button;
    private RecyclerView recyclerView;
    private List<Site> siteList;

    private SiteViewModel siteViewModel;

    private ActivityResultLauncher<Intent> resultLauncher;

    private Site site;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        siteViewModel = new ViewModelProvider(this).get(SiteViewModel.class);

        button = findViewById(R.id.button_add_site);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(this, SiteDetailsActivity.class);
            startActivity(intent);
        });
        recyclerView = findViewById(R.id.recyclerview_sites);

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    configList();
                }
        );
        configList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_tag){
            Intent intent = new Intent(this, EditTagActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void configList(){
        if (siteList != null && siteList.size() == 0){
            SiteAdapter adapter = new SiteAdapter(siteList, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }else {
            siteViewModel.recuperateAll().observe(this,
                    observer -> {
                        siteList = observer;
                        SiteAdapter adapter = new SiteAdapter(siteList, this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    }
            );
        }
    }

    @Override
    public void clickEditSite(int position) {

        Intent intent = new Intent(this, EditSiteActivity.class);
        intent.putExtra("posicao_lista", position);

        resultLauncher.launch(intent);
    }

    @Override
    public void clickDeleteSite(int position) {
        siteViewModel.delete(siteList.get(position)).observe(
                this,
                observe -> {
                    if (observe.booleanValue()){
                        siteList.remove(position);
                        onResume();
                        Toast.makeText(this, "Dados removidos com sucesso.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Erro ao remover os dados.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}