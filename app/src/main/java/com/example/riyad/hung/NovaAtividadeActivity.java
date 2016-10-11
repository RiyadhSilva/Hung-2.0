package com.example.riyad.hung;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NovaAtividadeActivity extends AppCompatActivity {
    private EditText nome;
    private EditText desc;
    private EditText custo;
    private Long projetoID;
    private Atividade atividade;
    private Button bt_atualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_atividade);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_action_add);
        actionBar.setDisplayShowHomeEnabled(true);

        if(bundle != null){
            projetoID   = (Long) bundle.get("projeto_id");
            if((Atividade)bundle.get("atividade")!=null){
                atividade = (Atividade)bundle.get("atividade");
                actionBar.setTitle("Atualizar atividade");
                projetoID = atividade.projetoID;
                bt_atualizar = (Button) findViewById(R.id.activity_nova_atividade_bCriar);
                bt_atualizar.setText("Atualizar");
            } else {
                atividade = new Atividade();
                actionBar.setTitle("Nova atividade");
                projetoID = (Long) bundle.get("projeto_id");
            }

        }

        nome = (EditText) findViewById(R.id.nova_atividade_et_1);
        desc  = (EditText) findViewById(R.id.nova_atividade_et_2);
        custo = (EditText) findViewById(R.id.nova_atividade_et_3);

    }

    public void onCheckboxClicked(View view){
        //Verifica a prioridade
        boolean checked = ((CheckBox)view).isChecked();
        //Verifica qual foi selecionada
        switch (view.getId()){
            case R.id.nova_atividade_cb_baixo:
                if(checked){
                    atividade.prioridade = "baixa";
                }else{
                    toast("...");

                }
                break;
            case R.id.nova_atividade_cb_normal:
                if(checked){
                    atividade.prioridade = "normal";
                }else{
                    toast("...");

                }
                break;
            case R.id.nova_atividade_cb_alto:
                if(checked){
                    atividade.prioridade = "alta";
                }else{
                    toast("...");

                }
                break;
        }
    }

    public void criarNovaAtividade(View view){
        atividade.nome = nome.getText().toString();
        //Tratamento da data
        Date data = new Date(System.currentTimeMillis());
        String data_formatada = new SimpleDateFormat("dd/MM/yyyy").format(data);
        atividade.data = data_formatada;
        atividade.desc = desc.getText().toString();
        atividade.custo = custo.getText().toString();
        atividade.projetoID = projetoID;

        //Inicia conexao com o Banco
        AtividadeDB atividadeDB = new AtividadeDB(this);
        //Salva o atividade criado no banco
        atividadeDB.save(atividade);
        if (bt_atualizar == null){
            toast("Criada com sucesso!");
        } else{
            toast("Atualizada com sucesso!");
        }

        finish();

    }

    private void toast(String msg){

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Infla o menu com os botoes da actionbar
        getMenuInflater().inflate(R.menu.activity_nova_atividade_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_close){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
