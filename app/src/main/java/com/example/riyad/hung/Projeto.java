package com.example.riyad.hung;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riyad on 01/10/2016.
 */
public class Projeto {
    private static final long seriaLVersionUID = 6601006766832473959L;
    public long id;
    public String nome;
    public int img;
    public String desc;

    public List<Atividade> atividades;


    public static List<Projeto> getProjetos() {
        List<Projeto> projetos = new ArrayList<Projeto>();

        return projetos;
    }

    public void adicionaAtividade(Atividade atividade){
        atividades.add(atividade);
    }

    public void removeAtividade(Atividade atividade){
        int id = atividades.indexOf(atividade);
        atividades.remove(id);
    }
}
