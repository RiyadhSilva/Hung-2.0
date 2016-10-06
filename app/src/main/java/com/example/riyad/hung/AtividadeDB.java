package com.example.riyad.hung;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riyad on 01/10/2016.
 */
public class AtividadeDB extends SQLiteOpenHelper{
    private static final  String TAG = "sql";
    //Nome do banco
    public static final String NOME_BANCO = "hung_db";
    //Versao do banco
    private static final int VERSAO_BANCO = 2;

    public AtividadeDB(Context context){
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Criando a tabela Atividade...");
        db.execSQL("create table if not exists atividade(_id integer primary key " +
                "autoincrement, nome text, data text, desc text, custo text, " +
                "prioridade text, projeto_id integer);");
        Log.d(TAG, "Tabela criada com sucesso!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.d(TAG, "Criando a tabela Atividade...");
        db.execSQL("create table if not exists atividade(_id integer primary key " +
                "autoincrement, nome text, data text, desc text, custo text, " +
                "prioridade text, projeto_id integer);");
        Log.d(TAG, "Tabela criada com sucesso!");

    }

    //Insere uma nova atividade, ou atualiza se ja existir
    public long save(Atividade atividade){
        long id = atividade.id;
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("nome", atividade.nome);
            values.put("data", atividade.data);
            values.put("desc", atividade.desc);
            values.put("custo", atividade.custo);
            values.put("prioridade", atividade.prioridade);
            values.put("projeto_id", atividade.projetoID);
            if(id != 0){
                String _id = String.valueOf(atividade.id);
                String[]whereArgs = new String[]{_id};
                //atualiza atividade set values = ... where id=?
                int count = db.update("atividade", values, "_id=?", whereArgs);
                Log.d(TAG, "Atividade atualizada com sucesso!");
                return count;
            } else {
                //Insere into atividades values(...)
                id = db.insert("atividade", "", values);
                Log.d(TAG, "Atividade criada com sucesso!");
                return id;
            }
        }finally {
            db.close();
        }
    }

    //Deleta a atividade
    public int delete(Atividade atividade){
        SQLiteDatabase db = getWritableDatabase();
        try {
            //delete from atividade where _id=?
            int count = db.delete("atividade", "_id=?", new String[]{String.valueOf(atividade.id)});
            Log.d(TAG, "Atividade deletada com sucesso!");
            return count;
        } finally {
            db.close();
        }
    }

    //Consulta a lista com todas as atividades
    public List<Atividade> findAll(){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //Select * from post
            Cursor c = db.query("atividade", null, null, null, null, null, null, null);
                return toList(c);
            } finally {
                db.close();
        }
    }

    public List<Atividade> findAllByProjetoID(Long projetoID){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //Select * from atividade where projeto_id=?
            Cursor c = db.query("atividade", null, "projeto_id='" + String.valueOf(projetoID) + "'", null, null, null, null);
            return toList(c);
        }finally {
            db.close();
        }
    }

    //Le o cursor e cria a lista de atividades
    private List<Atividade> toList(Cursor c){
        List<Atividade> atividades = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                Atividade atividade = new Atividade();
                atividades.add(atividade);
                //Recupera os atributos de atividade
                atividade.id = c.getLong(c.getColumnIndex("_id"));
                atividade.nome = c.getString(c.getColumnIndex("nome"));
                atividade.data = c.getString(c.getColumnIndex("data"));
                atividade.desc = c.getString(c.getColumnIndex("desc"));
                atividade.custo = c.getString(c.getColumnIndex("custo"));
                atividade.prioridade = c.getString(c.getColumnIndex("prioridade"));
                atividade.projetoID = c.getLong(c.getColumnIndex("projeto_id"));
            } while(c.moveToNext());
        }

        return atividades;
    }

    //Executa um SQL
    public void execSQL(String sql){
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.execSQL(sql);
            Log.d(TAG, "Comando sql executado!");
        }finally {
            db.close();
        }
    }
}
