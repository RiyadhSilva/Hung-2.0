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
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        //Lista e adapter
        ProjetoDB projetoDB = new ProjetoDB(this);
        List<Projeto> projetos = projetoDB.findAll();


        recyclerView.setAdapter(new ProjetoAdapter(this, projetos, onClickProjeto()));

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_hung);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("Meus Projetos");

    }

    //OnClick Projeto
    private ProjetoAdapter.ProjetoOnClickListener onClickProjeto(){
        return new ProjetoAdapter.ProjetoOnClickListener(){
            @Override
            public void onClickProjeto(View view, int idx){
                ProjetoDB projetoDB = new ProjetoDB(view.getContext());
                List<Projeto> projetos = projetoDB.findAll();
                Projeto p = projetos.get(idx);
                Intent i = new Intent(view.getContext(), ProjetoActivity.class);
                i.putExtra("nome", p.nome);
                i.putExtra("desc", p.desc);
                i.putExtra("id", p.id);
                startActivity(i);
            }
        };
    }

    private Activity getActivity(){ return this;}

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

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
            //Chama a intent NovoProjeto
            Intent i = new Intent(this, NovoProjetoActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        //Lista e adapter
        ProjetoDB projetoDB = new ProjetoDB(this);
        List<Projeto> projetos = projetoDB.findAll();
        recyclerView.setAdapter(new ProjetoAdapter(this, projetos, onClickProjeto()));
        super.onResume();
    }


}
