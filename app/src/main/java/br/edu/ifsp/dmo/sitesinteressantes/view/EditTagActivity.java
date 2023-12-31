package br.edu.ifsp.dmo.sitesinteressantes.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import br.edu.ifsp.dmo.sitesinteressantes.R;
import br.edu.ifsp.dmo.sitesinteressantes.model.TagSite;
import br.edu.ifsp.dmo.sitesinteressantes.view.adapter.ItemClick;
import br.edu.ifsp.dmo.sitesinteressantes.view.adapter.TagAdapter;
import br.edu.ifsp.dmo.sitesinteressantes.view.viewmodel.tag.TagViewModel;

public class EditTagActivity extends AppCompatActivity implements ItemClick {

    private RecyclerView recyclerView;
    private FloatingActionButton button;
    private List<TagSite> tagSites;
    private TagViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag);

        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setTitle("Configurar TAGs");
            bar.setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this).get(TagViewModel.class);

        findById();
        setClicks();

        extracted();
    }

    private void extracted() {
        viewModel.recuperateAll().observe(this,
                Observer -> {
                    tagSites = Observer;
                    configList();
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clickAction(int position) {
        View tela = getLayoutInflater().inflate(R.layout.tag_new, null);
        EditText editText = tela.findViewById(R.id.edittext_tag);
        editText.setText(tagSites.get(position).getTag());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(tela)
                .setTitle("Editar TAG")
                .setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TagSite tagSite = new TagSite(editText.getText().toString().trim());
                        viewModel.update(tagSites.get(position), tagSite).observe(
                                EditTagActivity.this,
                                observer ->{
                                    if (observer.booleanValue()){
                                        Toast.makeText(EditTagActivity.this, "Dados atualizados com sucesso.", Toast.LENGTH_SHORT).show();
                                        extracted();
                                    }else {
                                        Toast.makeText(EditTagActivity.this, "Erro ao atualizar os dados.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Apagar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        viewModel.delete(tagSites.get(position)).observe(
                                EditTagActivity.this,
                                observer ->{
                                    if (observer.booleanValue()){
                                        Toast.makeText(EditTagActivity.this, "Dados removidos com sucesso.", Toast.LENGTH_SHORT).show();
                                        extracted();
                                    }else {
                                        Toast.makeText(EditTagActivity.this, "Erro ao remover os dados.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void configList(){
        TagAdapter adapter = new TagAdapter(tagSites, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void findById(){
        recyclerView = findViewById(R.id.recyclerview_tags);
        button = findViewById(R.id.button_add_tag);
    }

    private void new_tag(){
        View tela = getLayoutInflater().inflate(R.layout.tag_new, null);
        EditText editText = tela.findViewById(R.id.edittext_tag);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(tela)
                .setTitle("Nova TAG")
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TagSite tagSite = new TagSite(editText.getText().toString().trim());
                        viewModel.create(tagSite).observe(
                                EditTagActivity.this,
                                observer ->{
                                    if (observer.booleanValue()){
                                        Toast.makeText(EditTagActivity.this, "Dados salvos com sucesso.", Toast.LENGTH_SHORT).show();
                                        extracted();
                                    }else {
                                        Toast.makeText(EditTagActivity.this, "Erro ao Salvar os dados.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Calcelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setClicks(){
        button.setOnClickListener(view -> new_tag());
    }
}