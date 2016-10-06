package com.example.riyad.hung;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by riyad on 02/10/2016.
 */
public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.AtividadesViewHolder> {
    protected static final String TAG = "Hung";
    private final List<Atividade> atividades;
    private final Context context;
    private final AtividadeOnClickListener onClickListener;

    public interface AtividadeOnClickListener{
        public void onClickAtividade(View view, int idx);
    }

    public AtividadeAdapter(Context context, List<Atividade> atividades, AtividadeOnClickListener onClickListener){
        this.context = context;
        this.atividades = atividades;
        this.onClickListener = onClickListener;
    }

    @Override
    public AtividadesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        //Este metodo cria uma subclasse de RecyclerView.ViewHolder
        //Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_atividade, viewGroup, false);
        //Cria a classe do ViewHolder
        AtividadesViewHolder holder = new AtividadesViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final AtividadesViewHolder holder, final int position){
        //Este metodo recebe o indice do elemento, e atualiza as views que estao dentro do ViewHolder
        final Atividade a = atividades.get(position);
        //Atualiza os valores nas views
        holder.tNome.setText(a.nome);
        holder.tDesc.setText(a.desc);
        holder.tCusto.setText(a.custo);
        //Trata a prioridade
        if(a.prioridade.equals("normal")){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#CAEBF2"));
            holder.tNome.setTextColor(Color.parseColor("#254D32"));
            holder.tDesc.setTextColor(Color.parseColor("#254D32"));
            holder.tCusto.setTextColor(Color.parseColor("#254D32"));
        } else if (a.prioridade.equals("baixa")){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#EFEFEF"));
            holder.tNome.setTextColor(Color.parseColor("#463239"));
            holder.tDesc.setTextColor(Color.parseColor("#463239"));
            holder.tCusto.setTextColor(Color.parseColor("#463239"));;

        } else if (a.prioridade.equals("alta")){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FF3B3F"));
            holder.tNome.setTextColor(Color.parseColor("#D0DB97"));
            holder.tDesc.setTextColor(Color.parseColor("#D0DB97"));
            holder.tCusto.setTextColor(Color.parseColor("#D0DB97"));

        }
        //Trata Evento de click
        holder.iDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtividadeDB atividadeDB = new AtividadeDB(view.getContext());
                atividadeDB.delete(a);
                atividades.remove(a);
                notifyItemRemoved(position);
                toast(a.nome + " foi deletada!");
            }

            private void toast(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
        holder.iBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, NovaAtividadeActivity.class);
                i.putExtra("atividade", a);
                context.startActivity(i);
                toast(a.nome + " foi iniciada!");
            }

            private void toast(String msg){
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
        //Click
        if(onClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //Chama o listener para informar que clicou no Projeto
                    onClickListener.onClickAtividade(holder.view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount(){
        return this.atividades != null ? this.atividades.size() : 0 ;
    }

    //Subclasse de RecyclerView.ViewHolder. contem todas as Views
    public static class AtividadesViewHolder extends RecyclerView.ViewHolder{
        public TextView tNome;
        public TextView tDesc;
        public TextView tCusto;
        public CardView cardView;
        public ImageButton iDelete;
        public ImageButton iBuild;
        private View view;
        public AtividadesViewHolder(View view){
            super(view);
            this.view = view;
            //Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.adapter_atividade_tNome);
            tDesc   = (TextView) view.findViewById(R.id.adapter_atividade_tDesc);
            tCusto  = (TextView) view.findViewById(R.id.adapter_atiidade_tCusto);
            cardView = (CardView) view.findViewById(R.id.adapter_atividade_cardView);
            iDelete = (ImageButton) view.findViewById(R.id.adapter_atividade_iDelete);
            iBuild = (ImageButton) view.findViewById(R.id.adapter_atividade_iBuild);

        }
    }

}
