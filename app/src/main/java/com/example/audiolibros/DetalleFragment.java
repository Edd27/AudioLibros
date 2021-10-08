package com.example.audiolibros;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.IOException;

public class DetalleFragment extends Fragment implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl, View.OnTouchListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String mParam1;
    public static String mParam2;
    public static String ARG_INDEX_LIBRO = "idLibro";
    private TextView lblTitulo;
    private TextView lblAutor;
    private ImageView imgPortada;
    MediaPlayer mediaPlayer;
    MediaController mediaController;

    public DetalleFragment(){

    }

    public static DetalleFragment newIntance(String param1, String param2){
        DetalleFragment fragment = new DetalleFragment();
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View layout = inflater.inflate(R.layout.fragment_detalle_layout, container, false);
        layout.setOnTouchListener(this);
        Spinner spinner = layout.findViewById(R.id.spnGeneros);
        String[] generos = getResources().getStringArray(R.array.generos);
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, generos);
        spinner.setAdapter(adapter);
        Bundle args = getArguments();
        if(args != null){
            int idLibro = args.getInt(DetalleFragment.ARG_INDEX_LIBRO);
            setInfoLibro(idLibro, layout);
        }else{
            setInfoLibro(0,layout);
        }
        return layout;
    }

    public void setInfoLibro(int idLibro, View layout){
        Libro libro = Libro.ejemplosLibros().elementAt(idLibro);
        lblTitulo = layout.findViewById(R.id.titulo);
        lblAutor = layout.findViewById(R.id.autor);
        imgPortada = layout.findViewById(R.id.portada);
        lblTitulo.setText(libro.getTitulo());
        lblAutor.setText(libro.getAutor());
        imgPortada.setImageResource(libro.getRecursoImagen());
        if(mediaPlayer!= null){
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaController = new MediaController(getActivity());
        mediaPlayer.setOnPreparedListener(this);
        try{
            mediaPlayer.setDataSource(getActivity(), Uri.parse(libro.getUrl()));
            mediaPlayer.prepareAsync();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setInfoLibro(int pos){
        this.setInfoLibro(pos,getView());
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("AudioLibros", "Entramos en onPrepared de MediaPlayer");
        mediaPlayer.start();
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(getView().findViewById(R.id.fragment_detalle_layout_root));
        mediaController.setPadding(0,0,0,110);
        mediaController.setEnabled(true);
        mediaController.show();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mediaController.show();
        return false;
    }

    @Override
    public void onStop(){
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onStop();
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
