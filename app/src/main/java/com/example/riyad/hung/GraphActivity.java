package com.example.riyad.hung;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    private TextView valorTotal;
    private TextView numAtividades;
    private GraphView graph;
    private BarGraphSeries<DataPoint> series;
    private List<Atividade> atividades = new ArrayList<>();
    private DataPoint[] dados;
    private int totalAtividades = 0;
    private int position = 0;
    private Long projetoID;
    private String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            projetoID   = (Long) bundle.get("id");
            nome = (String) bundle.get("nome");
        }

        getSupportActionBar().setTitle(nome + " - Atividades");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        valorTotal = (TextView) findViewById(R.id.graph_tv_1);
        numAtividades = (TextView) findViewById(R.id.graph_tv_2);

        //Inicia conexao com o banco
        AtividadeDB atDB = new AtividadeDB(this);
        atividades = atDB.findAllByProjetoID(projetoID);

        //Contabiliza os custos totais
        BigDecimal totalGastos = new BigDecimal("0");
        for (Atividade a:
                atividades) {
            BigDecimal atual = new BigDecimal(a.custo);
            totalGastos = totalGastos.add(atual);
            totalAtividades++;
        }

        //Seta os textViews com os valores obetidos
        valorTotal.setText(totalGastos.toString() + " R$");
        numAtividades.setText(String.valueOf(totalAtividades));

        //Incializa o grafico
        graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setScalable(true);
        graph.getViewport().setMaxX(Double.parseDouble(totalGastos.multiply(new BigDecimal("1.5")).toString()));
        graph.getViewport().setMaxY(Double.parseDouble(String.valueOf(totalAtividades * 2)));
        graph.getViewport().setMinX(0);
        graph.getViewport().setMinY(0);

        dados = new DataPoint[totalAtividades];
        //Faz uma busca nos dados
        Integer intCustos = 0;
        Integer numAtividade = 1;
        Integer intTotal = totalGastos.intValue();

        for (Atividade a:
                atividades) {
            intCustos = new BigDecimal(a.custo).intValue() + intCustos;
            DataPoint atual = new DataPoint(intCustos, numAtividade);
            System.out.println("Atividade " + numAtividade + ": " + intCustos);
            this.adiciona(atual);
            numAtividade++;
            intTotal = intTotal - intCustos;
        }

        DataPoint ultimo = new DataPoint(intCustos*2, numAtividade+1);


        series = new BarGraphSeries<>(dados);
        series.setAnimated(true);
        graph.addSeries(series);

        //Estilizando
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);
        //Desenhar valores no topo
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        //Adiciona um listener de evento
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                toast("Gráfico de custo:  " + dataPoint);
            }
        });


    }

    public void adiciona(DataPoint atual){
        this.dados[this.position] = atual;
        position++;
    }

    private void toast(String msg){

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_graph_menu, menu);
        return true;
    }
}
