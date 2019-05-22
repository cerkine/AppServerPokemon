package com.example.appserverpokemon;

import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appserverpokemon.model.ListaPujas;
import com.example.appserverpokemon.model.Partida;
import com.example.appserverpokemon.model.Pokemon;
import com.example.appserverpokemon.model.Pujas;
import com.example.appserverpokemon.model.Team;
import com.example.appserverpokemon.model.UserGame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    List<Pokemon> pkemonList;
    TextView estado ;
    List<String> idPujas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        estado = findViewById(R.id.estado);
        db = FirebaseFirestore.getInstance();
        pkemonList = new ArrayList<>();
        db.collection("ListaPokemon").orderBy("id").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    int i =0;
                    for (QueryDocumentSnapshot snapshot: task.getResult()) {
                        pkemonList.add(snapshot.toObject(Pokemon.class));
                        i++;
                        estado.setText("Ready");
                    }
                }
            }
        });

        Button btnCombates = findViewById(R.id.Combates);
        Button btnRefrescarMercado = findViewById(R.id.Mercado);
        Button btnPujas = findViewById(R.id.Pujas);


        btnCombates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estado.setText("Wait");
                combatir();
            }
        });

        btnRefrescarMercado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estado.setText("Wait");
                refrescarMercado();
            }
        });
        btnPujas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estado.setText("Wait");
                asignarPujas();
            }
        });
    }

//    private void asignarPujas() {
//
//
//        db.collection("Partidas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    Iterator<QueryDocumentSnapshot> a = task.getResult().iterator();
//
//                    for (QueryDocumentSnapshot snapshot: task.getResult()){
//                        idPujas = new ArrayList<>();
//                        for (int i = 0; i<10;i++){
//                            idjugadores.put(i,"");
//                            pujas.put(i,0);
//                        }
//
//
//                        Partida partida = snapshot.toObject(Partida.class);
//                        for (int i=0;i<partida.getUsers().size(); i++){
//                            idPujas.add(partida.getUsers().get(i).getTeamID());
//
//                            db.collection("Pujas").document(partida.getUsers().get(i).getTeamID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    if (task.isSuccessful()){
//                                        Pujas listaPujas = task.getResult().toObject(Pujas.class);
//                                        for (int j =0;j<listaPujas.getPujas().size();j++){
//                                            if (pujas.get(j) < listaPujas.getPujas().get(j)){
//                                                idjugadores.put(j,task.getResult().getId());
//                                                pujas.put(j,listaPujas.getPujas().get(j));
//                                            }
//                                        }
//
//                                    }
//                                }
//                            });
//                        }
//
//
//                    }
//                }
//            }
//        });
//    }

    private void asignarPujas() {

        estado.setText("Wait");
        db.collection("Partidas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Iterator<QueryDocumentSnapshot> a = task.getResult().iterator();
                    consultarPartidas(a);
                }
                estado.setText("Done");
            }
        });

    }


    void consultarPartidas(Iterator<QueryDocumentSnapshot> a){
        if(!a.hasNext()) return;
        Map<Integer,String> idjugadores = new HashMap<>();
        Map<Integer,Integer> pujas = new HashMap<>();

        QueryDocumentSnapshot queryDocumentSnapshot = a.next();
        idPujas = new ArrayList<>();
        for (int i = 0; i<10;i++){
            idjugadores.put(i,"");
            pujas.put(i,0);
        }


        Partida partida = queryDocumentSnapshot.toObject(Partida.class);

        consultarPujas(partida, 0,idjugadores,pujas);

//        subirDatos(partida,idjugadores,pujas);/*--------------------------------*/

        consultarPartidas(a);
    }

    private void subirDatos(final Partida partida, Map<Integer, String> idjugadores, Map<Integer, Integer> pujas) {
        for (int i = 0; i < idjugadores.size(); i++) {
            if (pujas.get(i)>0){
                final String idPujaJugador = idjugadores.get(i);

                for (int j = 0; j < partida.getUsers().size(); j++) {
                    if (partida.getUsers().get(j).getPujasID().equals(idjugadores.get(i))){
                        final String idTeamJugador = partida.getUsers().get(j).getTeamID();
                        final int pokemonPos = i;
                        db.collection("Equipos").document(idTeamJugador).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    final Team team = task.getResult().toObject(Team.class);
                                    db.collection("Mercado").document(partida.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                ListaPujas mercado = task.getResult().toObject(ListaPujas.class);
                                                team.getEquipo().add(mercado.getLista().get(pokemonPos));
                                                db.collection("Equipos").document(idTeamJugador).update("equipo", team.getEquipo());
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }

            }
        }
    }

    void consultarPujas(final Partida partida, final int i, final Map<Integer, String> idjugadores, final Map<Integer, Integer> pujas){

        if(i>=partida.getUsers().size()) {
            subirDatos(partida,idjugadores,pujas);
            return;
        }

        idPujas.add(partida.getUsers().get(i).getTeamID());

        db.collection("Pujas").document(partida.getUsers().get(i).getPujasID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Pujas listaPujas = new Pujas();
                    listaPujas = task.getResult().toObject(Pujas.class);
                    for (int j =0;j<listaPujas.getPujas().size();j++){
                        if (pujas.get(j) < listaPujas.getPujas().get(j)){
                            idjugadores.put(j,task.getResult().getId());
                            pujas.put(j,listaPujas.getPujas().get(j));

                        }
                    }
                    consultarPujas(partida, i+1, idjugadores, pujas);

                }
            }
        });

    }

    private void refrescarMercado() {



        db.collection("Partidas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    int i =0;
                    for (QueryDocumentSnapshot snapshot: task.getResult()) {
                        ListaPujas lista = new ListaPujas();
                        List<Pokemon> listaMercado = hacerListaMercado();
                        lista.setLista((ArrayList<Pokemon>) listaMercado);

                        db.collection("Mercado").document(snapshot.getId()).set(lista);

                    }
                    estado.setText("Ready");
                }
            }
        });
    }

    private List<Pokemon> hacerListaMercado() {
        int cantidad = 0;
        Random random = new Random();
        int[] idCogidos = new int[10];
        do {

            for (int i = 0; i < idCogidos.length; i++) {
                idCogidos[i] = random.nextInt(151);
            }

            for (int i = 0; i < idCogidos.length; i++) {
                cantidad += pkemonList.get(idCogidos[i]).getPrice();
            }
        }while (cantidad>18000);
        List<Pokemon> listaMercado = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            listaMercado.add(pkemonList.get(idCogidos[i]));
        }

        return listaMercado;
    }

    private void combatir() {

    }
}
