package com.example.audiolibros;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class SelectorFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String[] menuContextItem ;

    RecyclerView recyclerViewLibros ;
    private Context contexto;

    public SelectorFragment() {
        // Required empty public constructor
    }

    public static SelectorFragment newInstance(String param1, String param2) {
        SelectorFragment fragment = new SelectorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof MainActivity){
            this.contexto = (MainActivity)context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);


        View layout =inflater
                .inflate(R.layout.fragment_selector_layout,
                        container,
                        false);

        recyclerViewLibros =
                layout.findViewById(R.id.recyclerViewLibros);

        AdaptadorLibrosFiltro miAdaptadorPersonalizado =
                new AdaptadorLibrosFiltro(getActivity() ,
                        Libro.ejemplosLibros()
                );

        miAdaptadorPersonalizado.setOnClickLister(view ->
                {
                    int pos =
                            recyclerViewLibros.
                                    getChildAdapterPosition(view);
                    Toast.makeText(getActivity(),
                            "ELement at " + pos,
                            Toast.LENGTH_LONG).show();

                    ((MainActivity)this.contexto).mostrarDetalle(pos, false);
                }
        );

        miAdaptadorPersonalizado.
                setOnLongClickItemListener(view -> {
                    menuContextItem =
                            getResources()
                                    .getStringArray(R.array.mnuContextItemSelector);

                    int posLibro = recyclerViewLibros.getChildAdapterPosition(view);


                    AlertDialog.Builder   dialog =
                            new AlertDialog.Builder(
                                    contexto)
                                    .setTitle("Audio Libros")
                                    .setItems(menuContextItem,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(getContext(), ""+ i,
                                                            Toast.LENGTH_LONG).show();
                                                    switch (i){
                                                        case 0:
                                                            Intent intent = new Intent(Intent.ACTION_SEND);
                                                            intent.setType("text/plain");
                                                            intent.putExtra(Intent.EXTRA_SUBJECT,
                                                                    Libro.ejemplosLibros()
                                                                            .elementAt(posLibro).getTitulo());

                                                            intent.putExtra(Intent.EXTRA_TEXT,
                                                                    Libro.ejemplosLibros()
                                                                            .elementAt(posLibro).getUrl());

                                                            startActivity(intent);

                                                            break;
                                                        case 1:
                                                            Libro.ejemplosLibros().add(Libro.ejemplosLibros().get(posLibro));
                                                            miAdaptadorPersonalizado.notifyItemInserted(Libro.ejemplosLibros().size() - 1);
                                                            break;
                                                        case 2:
                                                            Libro.ejemplosLibros().remove(posLibro);
                                                            miAdaptadorPersonalizado.notifyItemRemoved(posLibro);
                                                            break;
                                                    }

                                                }
                                            });

                    dialog.create().show();

                    return false;
                });

        recyclerViewLibros.setLayoutManager( new GridLayoutManager(getActivity(), 2));
        recyclerViewLibros.setAdapter(miAdaptadorPersonalizado);



        View myActivityView = getActivity().findViewById(R.id.layout);

        TabLayout tabs = myActivityView.findViewById(R.id.tabs);
        if(tabs!=null) {
            tabs.addTab(tabs.newTab().setText("Todos"));
            tabs.addTab(tabs.newTab().setText("Nuevos"));
            tabs.addTab(tabs.newTab().setText("Leidos"));
            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Log.d("OnTab", "OnTab");
                    switch (tab.getPosition()){
                        case 0: // Todos
                            miAdaptadorPersonalizado.setNovedad(false);
                            miAdaptadorPersonalizado.setLeido(false);
                            break;
                        case 1: // Nuevos
                            miAdaptadorPersonalizado.setNovedad(true);
                            miAdaptadorPersonalizado.setLeido(false);
                            break;
                        case 2: // Leidos
                            miAdaptadorPersonalizado.setNovedad(false);
                            miAdaptadorPersonalizado.setLeido(true);
                            break;
                    }
                    recyclerViewLibros.setAdapter(null);
                    recyclerViewLibros.setLayoutManager(null);
                    recyclerViewLibros.setAdapter(miAdaptadorPersonalizado);
                    recyclerViewLibros.setLayoutManager( new GridLayoutManager(getActivity(), 2));
                    miAdaptadorPersonalizado.notifyDataSetChanged();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }


        return layout;
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_selector, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_ultimo){
            ((MainActivity)contexto).irUltimoVisitado();
            return true;
        }
        else if(id == R.id.menu_buscar){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}