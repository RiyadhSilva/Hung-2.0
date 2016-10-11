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
import android.widget.EditText;
import android.widget.Toast;

public class NovoProjetoActivity extends AppCompatActivity {
    private EditText nome;
    private EditText desc;
    private Projeto projeto;
    private Button bt_atualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_projeto);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_action_add);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Novo Projeto");

        projeto = new Projeto();

        if(bundle != null){
            if(((String)bundle.get("func")).equals("atualizar")){
                projeto.id = (Long) bundle.get("id");
                actionBar.setTitle("Atualizar Projeto");
                bt_atualizar = (Button) findViewById(R.id.novo_projeto_bCriar);
                bt_atualizar.setText("atualizar");
            } else {
                projeto = new Projeto();
            }
        }

        nome = (EditText) findViewById(R.id.novo_projeto_tv_nome);
        desc = (EditText) findViewById(R.id.novo_projeto_tv_desc);


    }

    public void criarNovoProjeto(View view){
        projeto.nome = nome.getText().toString();
        projeto.desc = desc.getText().toString();
        projeto.img  = R.drawable.ic_projeto;

        //Conexao com o banco
        ProjetoDB projetoDB = new ProjetoDB(this);
        //Salva no banco
        projetoDB.save(projeto);
        if (bt_atualizar == null){
            toast("Criado com sucesso!");
        } else{
            toast("Atualizado com sucesso!");
        }
        finish();



    }

    private void toast (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_novo_projeto_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
      if (id == R.id.action_close){
            //Finaliza a intent atual
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
