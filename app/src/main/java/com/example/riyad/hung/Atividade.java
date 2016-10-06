package com.example.riyad.hung;

import java.io.Serializable;

/**
 * Created by riyad on 01/10/2016.
 */
public class Atividade implements Serializable{
    private static final long seriaLVersionUID = 6601006766832473959L;
    public long id;
    public String nome;
    public String data;
    public String desc;
    public String custo;
    public String prioridade;
    public long projetoID;

}
