package com.example.riyad.hung;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by riyad on 03/10/2016.
 */
public class IniciaIntentService extends IntentService{

    public IniciaIntentService(){
        super("IniciaIntentService");
    }

    private static final int MAX = 1;
    private static final String TAG = "livro";
    private boolean running;
    private String nome;
    private DateFormat df;
    private Date date;

    @Override
    protected void onHandleIntent(Intent intent){
        df = new SimpleDateFormat("HH");
        date = new Date();
        String sHoraAgora = df.format(date);
        int hora_agora = Integer.parseInt(sHoraAgora);
        df = new SimpleDateFormat("mm");
        String sMinutoAgora = df.format(date);
        int minuto_agora = Integer.parseInt(sMinutoAgora);
        System.out.println(hora_agora);
        nome = intent.getStringExtra("nome");
        String sHoraProgramada = intent.getStringExtra("hora");
        int hora_programada = Integer.parseInt(sHoraProgramada);
        String sMinutosProgramados = intent.getStringExtra("minutos");
        int minutos_programados = Integer.parseInt(sMinutosProgramados);
        Long timer;
        Long programado;

        //Este metodo executa em uma thread depedendo do caso
        if((hora_programada - hora_agora) == 0){
            System.out.println("Caso 1 = 0");
            running = true;
            timer = System.currentTimeMillis() / 1000;
            programado = timer + ((minutos_programados - minuto_agora) * 60);
            Long sub = programado - timer;
            System.out.println("Subtracao = " + sub);
            while(timer < programado){
                fazAlgumaCoisa();
                Log.d(TAG, "IniciaIntentService executando... " + sub);
                sub--;
                timer++;
            }
        } else if (hora_programada - hora_agora > 0){
            System.out.println("Caso 2 > 0");
            running = true;
            timer = System.currentTimeMillis() / 1000;
            programado = timer + ((minutos_programados - minuto_agora) * 60) + ((hora_programada-hora_agora) * 60 * 60);
            Long sub = programado - timer;
            while (timer < programado){
                fazAlgumaCoisa();
                Log.d(TAG, "IniciaIntentService executando... " + sub);
                sub--;
                timer++;
            }
        } else if (hora_programada - hora_agora < 0){
            System.out.println("Caso 3 < 0");
            running = true;
            System.out.print("Horário errado!");
            Log.d(TAG, "IniciaIntentService erro = Horario invalido!... ");
        }

        //Quando ele terminar, o metodo stopSelf() sera chamado automaticamente
        Log.d(TAG, "IniciaIntentService fim.");
        notificacao("Hung", "Fim do tempo da atividade  " + nome + " !" );
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
        Log.d(TAG, "IniciaIntentService.onDestroy()");
        notificacao("Hung", "Fim de todas as atividade !" );
    }

    private void notificacao(String cTitle, String cText) {
        df = new SimpleDateFormat("mm");
        date = new Date();
        String sMinutoAgora = df.format(date);
        int minuto_agora = Integer.parseInt(sMinutoAgora);

        int id = minuto_agora;
        String contentTitle = cTitle;
        String contentText = cText;
        Intent intent = new Intent(this, MainActivity.class);
        NotificationUtil.create(this, intent, contentTitle, contentText, id);
    }
}
