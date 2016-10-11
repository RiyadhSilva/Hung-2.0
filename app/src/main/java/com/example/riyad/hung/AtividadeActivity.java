package com.example.riyad.hung;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;

public class AtividadeActivity extends AppCompatActivity {
    private String nome;
    private String desc;
    private Long projeto_id;
    private TextView tv_desc;
    private Atividade atividade;
    private TimePicker timePicker;
    private int hora;
    private int minutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            nome = (String) bundle.get("nome");
            desc = (String) bundle.get("desc");
            projeto_id = (Long) bundle.get("projeto_id");
            atividade = (Atividade) bundle.get("atividade");

        }

        getSupportActionBar().setTitle(nome);

        tv_desc = (TextView) findViewById(R.id.atividade_tv_desc);
        tv_desc.setText(desc);
        timePicker = (TimePicker) findViewById(R.id.activity_atividade_time_picker);
        timePicker.setIs24HourView(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_atividade_menu, menu);
        return true;
    }

    public void iniciarAtividade(View view){
        hora = timePicker.getCurrentHour();
        minutos = timePicker.getCurrentMinute();
        Intent s = new Intent(this, IniciaIntentService.class);
        s.putExtra("hora", String.valueOf(hora));
        s.putExtra("minutos", String.valueOf(minutos));
        s.putExtra("nome", atividade.nome);
        s.putExtra("id", atividade.id);
        startService(s);
        toast("Tarefa :" + nome + " foi iniciada!");
        finish();

    }

    public void atualizarAtividade(View view){
        Intent i = new Intent(this, NovaAtividadeActivity.class);
        i.putExtra("atividade", atividade);
        startActivity(i);
        finish();
    }

    public void excluirAtividade(View view){
        AtividadeDB atividadeDB = new AtividadeDB(view.getContext());
        atividadeDB.delete(atividade);
        toast("Tarefa : " + nome + " foi excluida!");
        finish();
    }

    private void toast (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_close){
            //Troca o modo de vizualizacao para lista
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
