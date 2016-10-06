package com.example.riyad.hung;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by riyad on 01/10/2016.
 */
public class ProjetoAdapter extends RecyclerView.Adapter<ProjetoAdapter.ProjetosViewHolder> {
    protected static final String TAG = "Hung";
    private final List<Projeto> projetos;
    private final Context context;
    private final ProjetoOnClickListener onClickListener;

    public interface ProjetoOnClickListener{
        public void onClickProjeto(View view, int idx);
    }

    public ProjetoAdapter(Context context, List<Projeto> projetos, ProjetoOnClickListener onClickListener){
        this.context = context;
        this.projetos = projetos;
        this.onClickListener = onClickListener;
    }

    @Override
    public ProjetosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        //Este metodo cria uma subclasse de RecyclerView.ViewHolder
        //Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_projeto, viewGroup, false);
        //Cria a classe do ViewHolder
        ProjetosViewHolder holder = new ProjetosViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ProjetosViewHolder holder, final int position){
        //Este metodo recebe o indice do elemento, e atualiza as views que estao dentro do ViewHolder
        Projeto p = projetos.get(position);
        //Atualiza os valores nas views
        holder.tNome.setText(p.nome);
        holder.img.setImageResource(R.drawable.ic_managment);
        //Click
        if(onClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //Chama o listener para informar que clicou no Projeto
                    onClickListener.onClickProjeto(holder.view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount(){
        return this.projetos != null ? this.projetos.size() : 0 ;
    }

    //Subclasse de RecyclerView.ViewHolder. contem todas as Views
    public static class ProjetosViewHolder extends RecyclerView.ViewHolder{
        public TextView tNome;
        public ImageView img;

        private View view;
        public ProjetosViewHolder(View view){
            super(view);
            this.view = view;
            //Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.tNome);
            img   = (ImageView) view.findViewById(R.id.img);

        }
    }
}
