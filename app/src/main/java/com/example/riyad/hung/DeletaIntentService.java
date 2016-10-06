package com.example.riyad.hung;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riyad on 02/10/2016.
 */
public class DeletaIntentService extends IntentService {
    public DeletaIntentService (){
        super("DeletaIntentService");
    }

    private static final int MAX = 15;
    private static final String TAG = "livro";
    private boolean running;
    private String nome;

    @Override
    protected void onHandleIntent(Intent intent){
        running = true;
        nome = intent.getStringExtra("nome");
        Log.d(TAG, "DeletaIntentService executando...");
        String sProjetoID = intent.getStringExtra("id");
        Long projeto_id = Long.parseLong(sProjetoID);
        List<Atividade> atividades_do_projeto = new ArrayList<>();
        AtividadeDB atividadeDB = new AtividadeDB(this);
        atividades_do_projeto = atividadeDB.findAllByProjetoID(projeto_id);

        int count = 0;

        for (Atividade a:
             atividades_do_projeto) {
            if(a.projetoID == projeto_id){
                AtividadeDB aDB = new AtividadeDB(this);
                aDB.delete(a);
                count++;
                System.out.println(count + " atividade(s) excluida(s)!");
                fazAlgumaCoisa();
            }
        }

        Log.d(TAG, "DeletaIntentService fim.");
    }

    private void fazAlgumaCoisa(){
        try{
            //Simula algum processamento
            Thread.sleep(1000);
        }catch (InterruptedException e){
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //Ao encerrar o servico, altera a flag para a thread parar
        running = false;
        Log.d(TAG, "DeletaIntentService.onDestroy()");
        notificacao("Hung", "Atividades deletadas do " + nome + " !" );
    }

    private void notificacao(String cTitle, String cText) {
        int id = 1;
        String contentTitle = cTitle;
        String contentText = cText;
        Intent intent = new Intent(this, MainActivity.class);
        NotificationUtil.create(this, intent, contentTitle, contentText, id);
    }
}
