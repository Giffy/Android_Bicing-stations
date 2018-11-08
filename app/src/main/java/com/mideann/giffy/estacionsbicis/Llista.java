package com.mideann.giffy.estacionsbicis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Llista extends AppCompatActivity {

    private EditText editText;
    private ArrayList<Estacio> llistaEstacions;
    private ArrayAdapter<Estacio> llistaAdapter;
    private RecyclerView recycleView;
    private String titol;
    private String publisher;
    private String idioma;
    private String pdfUrl;
    private MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista);


        this.app = (MyApp) this.getApplication();
        this.llistaEstacions = app.getEstacions();


        this.recycleView = this.findViewById(R.id.id_RecycleView);
        LlistaAdapter adapter = new LlistaAdapter(llistaEstacions);
        this.recycleView.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(this);
        //layout.setOrientation(LinearLayoutManager.HORIZONTAL);   //sets scroll horitzontal
        //GridLayoutManager layout = new GridLayoutManager( this, 2 );

        this.recycleView.setLayoutManager(layout);
        // Crea una separación entre listViews
        // MainActivity.this.recycleView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));


    }
}
