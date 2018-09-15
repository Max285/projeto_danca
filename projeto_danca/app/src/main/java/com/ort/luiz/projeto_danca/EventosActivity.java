package com.ort.luiz.projeto_danca;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventosActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference acontecendoRef, eventosRef;

    TextView scrollingText;

    private AlertDialog.Builder dialogo;

    String selecionado = "";

    private ListView listaItens;
    String[]itens = {"1"};

    Button btnDia1, btnDia2, btnDia3, btnVoltarEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        database = FirebaseDatabase.getInstance();

        btnDia1 = findViewById(R.id.btnDia1Id);
        btnDia2 = findViewById(R.id.btnDia2Id);
        btnDia3 = findViewById(R.id.btnDia3Id);
        btnVoltarEventos = findViewById(R.id.btnVoltarEventosId);

        btnDia1.setOnClickListener((V)->{
            selecionado = "Dia1";
            agendaEventos(selecionado);

            btnDia1.setBackgroundResource(R.color.White);
            btnDia2.setBackgroundResource(R.drawable.button_border);
            btnDia3.setBackgroundResource(R.drawable.button_border);
        });

        btnDia2.setOnClickListener((V)->{
            selecionado = "Dia2";
            agendaEventos(selecionado);

            btnDia1.setBackgroundResource(R.drawable.button_border);
            btnDia2.setBackgroundResource(R.color.White);
            btnDia3.setBackgroundResource(R.drawable.button_border);
        });

        btnDia3.setOnClickListener((V)->{
            selecionado = "Dia3";
            agendaEventos(selecionado);

            btnDia1.setBackgroundResource(R.drawable.button_border);
            btnDia2.setBackgroundResource(R.drawable.button_border);
            btnDia3.setBackgroundResource(R.color.White);
        });

        btnVoltarEventos.setOnClickListener((V)->{
            btnVoltarEventos.setBackgroundResource(R.color.White);
            startActivity(new Intent(this, MainActivity.class));
        });

        //Eventos
        eventosRef = database.getReference("Eventos");

        //Acontecendo agora
        acontecendoRef = database.getReference("Acontecendo_agora");
        acontecendoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scrollingText = findViewById(R.id.scrollingTextId2);
                String valor = dataSnapshot.getValue().toString();
                scrollingText.setText(valor);
                scrollingText.setSelected(true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }});


    }

    private void agendaEventos (String selecionado) {
        eventosRef.child(selecionado).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valor = dataSnapshot.getValue().toString();
                String valor1 = valor.replace("{", "");
                valor = valor1.replace("}", "");
                valor1 = valor.replace("=", " -> ");
                itens = valor1.split(", ");
                //Toast.makeText(getApplicationContext(),valor,Toast.LENGTH_SHORT).show();

                listaItens = findViewById(R.id.listViewId);
                ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        itens
                );
                //Lista de itens
                listaItens.setAdapter(adaptador);
                listaItens.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                    String valorClicado;
                    valorClicado = listaItens.getItemAtPosition(position).toString().substring(0, listaItens.getItemAtPosition(position).toString().indexOf("-"));

                    dialogo = new AlertDialog.Builder(EventosActivity.this);
                    dialogo.setTitle("Adicionar Evento");

                    dialogo.setMessage("Deixe as notificações ligadas para ser avisado sobre o início do " + valorClicado);
                    dialogo.setCancelable(false);

                    dialogo.setNeutralButton("Ok", (dialog, which) ->
                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show()
                    );

                    dialogo.create();
                    dialogo.show();
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}