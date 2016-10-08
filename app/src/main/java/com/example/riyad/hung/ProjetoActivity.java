package com.example.riyad.hung;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjetoActivity extends AppCompatActivity {
    private String nome;
    private String desc;
    public Long projetoID;
    private TextView tv_desc;
    private RecyclerView recyclerView;
    private Projeto projeto;
    private List<Atividade> atividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        projeto = new Projeto();

        if(bundle != null){
            nome = (String) bundle.get("nome");
            desc = (String) bundle.get("desc");
            projetoID   = (Long) bundle.get("id");
            projeto.nome = nome;
            projeto.desc = desc;
            projeto.id = projetoID;

        }

        System.out.println("O id do projeto eh: " + projetoID);

        getSupportActionBar().setTitle(nome + " - Atividades");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_desc = (TextView) findViewById(R.id.projeto_tv_desc);
        tv_desc.setText(desc);

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.atividade_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        //Lista e adapter
        AtividadeDB atividadeDB = new AtividadeDB(this);
        atividades = atividadeDB.findAllByProjetoID(projetoID);

        recyclerView.setAdapter(new AtividadeAdapter(this, atividades, onClickAtividade()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_projeto_menu, menu);
        return true;
    }

    //OnClick Projeto
    private AtividadeAdapter.AtividadeOnClickListener onClickAtividade(){
        return new AtividadeAdapter.AtividadeOnClickListener(){
            @Override
            public void onClickAtividade(View view, int idx){
                AtividadeDB atividadeDB = new AtividadeDB(view.getContext());
                List<Atividade> atividades = atividadeDB.findAllByProjetoID(projetoID);
                Atividade a = atividades.get(idx);
                Intent i = new Intent(view.getContext(), AtividadeActivity.class);
                i.putExtra("nome", a.nome);
                i.putExtra("desc", a.desc);
                i.putExtra("atividade", a);
                i.putExtra("projeto_id", a.id);
                startActivity(i);
            }
        };
    }

    private Activity getActivity(){ return this;}


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_linear_layout){
            //Troca o modo de vizualizacao para lista
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            return true;
        } else if(id == R.id.action_grid_layout){
            //Troca o modo de vizualicao para grid
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            return true;
        } else if (id == R.id.action_new){
            //Chama a intent NovaAtividade
            Intent i = new Intent(this, NovaAtividadeActivity.class);
            i.putExtra("projeto_id", projetoID);
            startActivity(i);
            return true;
        } else if (id == R.id.action_ordenar_prioridade){
            ordenaPrioridade(atividades);
            return true;
        } else if (id == R.id.action_ordenar_maior_valor){
            ordenaMaiorValor(atividades);
            return true;
        } else if (id == R.id.action_graph){
            Intent i = new Intent(this, GraphActivity.class);
            i.putExtra("id", projetoID);
            i.putExtra("nome", projeto.nome);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ordenaMaiorValor(List<Atividade> atividades) {
        Collections.sort(atividades, new Comparator<Atividade>() {
            @Override
            public int compare(Atividade t1, Atividade t2) {
                Double c1 = Double.parseDouble(t1.custo);
                Double c2 = Double.parseDouble(t2.custo);
                return c1.compareTo(c2);
            }
        });

        Collections.reverse(atividades);

        recyclerView.setAdapter(new AtividadeAdapter(this, atividades, onClickAtividade()));


    }

    private void ordenaPrioridade(List<Atividade> atividades) {
        List<Atividade> ordenada  = new ArrayList<>();

        for (Atividade a:
                atividades) {
            if(a.prioridade.equals("alta")){
                ordenada.add(a);
            }
        }

        for (Atividade a:
                atividades) {
            if(a.prioridade.equals("normal")){
                ordenada.add(a);
            }
        }

        for (Atividade a:
                atividades) {
            if(a.prioridade.equals("baixa")){
                ordenada.add(a);
            }
        }

        recyclerView.setAdapter(new AtividadeAdapter(this, ordenada, onClickAtividade()));

    }

    @Override
    public void onResume(){
        //Lista e adapter
        AtividadeDB atividadeDB = new AtividadeDB(this);
        List<Atividade> atividades = atividadeDB.findAllByProjetoID(projetoID);
        recyclerView.setAdapter(new AtividadeAdapter(this, atividades, onClickAtividade()));
        super.onResume();
    }

    public void atualizarProjeto(View view){
        Intent i = new Intent(this, NovoProjetoActivity.class);
        i.putExtra("id", projeto.id);
        i.putExtra("func", "atualizar");
        startActivity(i);
        finish();
    }

    public void excluirProjeto(View view){
        ProjetoDB projetoDB = new ProjetoDB(view.getContext());
        projetoDB.delete(projeto);
        //To-Do Apagar atividades do projeto
        toast("O projeto: " + nome + " foi excluido!");
        finish();
        Intent s = new Intent(this, DeletaIntentService.class);
        s.putExtra("id", String.valueOf(projeto.id));
        s.putExtra("nome", projeto.nome);
        startService(s);
    }

    private void toast (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
