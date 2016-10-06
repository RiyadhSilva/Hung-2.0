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
public class ProjetoDB extends SQLiteOpenHelper {
    private static final  String TAG = "sql";
    //Nome do banco
    public static final String NOME_BANCO = "hung_db";
    //Versao do banco
    private static final int VERSAO_BANCO = 2;

    public ProjetoDB(Context context){
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Criando a tabela Projeto...");
        db.execSQL("create table if not exists projeto(_id integer primary key " +
                "autoincrement, nome text, img integer, desc text);");

        Log.d(TAG, "Tabela criada com sucesso!");

        Log.d(TAG, "Criando a tabela Atividade...");
        db.execSQL("create table if not exists atividade(_id integer primary key " +
                "autoincrement, nome text, data text, desc text, custo text, " +
                "prioridade text, projeto_id integer);");
        Log.d(TAG, "Tabela criada com sucesso!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //Insere uma nova atividade, ou atualiza se ja existir
    public long save(Projeto projeto){
        long id = projeto.id;
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("nome", projeto.nome);
            values.put("img", projeto.img);
            values.put("desc", projeto.desc);
            if(id != 0){
                String _id = String.valueOf(projeto.id);
                String[]whereArgs = new String[]{_id};
                //atualiza projeto set values = ... where id=?
                int count = db.update("projeto", values, "_id=?", whereArgs);
                Log.d(TAG, "Projeto atualizado com sucesso!");
                return count;
            } else {
                //Insere into projeto values(...)
                id = db.insert("projeto", "", values);
                Log.d(TAG, "Projeto criada com sucesso!");
                return id;
            }
        }finally {
            db.close();
        }
    }

    //Deleta o projeto
    public int delete(Projeto projeto){
        SQLiteDatabase db = getWritableDatabase();
        try {
            //delete from projeto where _id=?
            int count = db.delete("projeto", "_id=?", new String[]{String.valueOf(projeto.id)});
            Log.d(TAG, "Projeto deletado com sucesso!");
            return count;
        } finally {
            db.close();
        }
    }

    //Consulta a lista com todos os projetos
    public List<Projeto> findAll(){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //Select * from post
            Cursor c = db.query("projeto", null, null, null, null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    public List<Projeto> findAllByID(Long projetoID){
        SQLiteDatabase db = getWritableDatabase();
        try{
            //Select * from projeto where _id=?
            Cursor c = db.query("projeto", null, "_id='" + String.valueOf(projetoID) + "'", null, null, null, null);
            return toList(c);
        }finally {
            db.close();
        }
    }

    //Le o cursor e cria a lista de projetos
    private List<Projeto> toList(Cursor c){
        List<Projeto> projetos = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                Projeto projeto = new Projeto();
                projetos.add(projeto);
                //Recupera os atributos de projeto
                projeto.id = c.getLong(c.getColumnIndex("_id"));
                projeto.nome = c.getString(c.getColumnIndex("nome"));
                projeto.img = c.getInt(c.getColumnIndex("img"));
                projeto.desc = c.getString(c.getColumnIndex("desc"));
            } while(c.moveToNext());
        }

        return projetos;
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
