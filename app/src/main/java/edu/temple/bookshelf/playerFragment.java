package edu.temple.bookshelf;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link playerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class playerFragment extends Fragment {

    Book book;
    TextView nowPlaying;
    Button pause,play, stop;
    SeekBar bar;
    playerFragmentInterface playerInterface;
    private static final String PLAYER = "player";
    private static final String ARG_PARAM2 = "param2";



    public playerFragment() {
        // Required empty public constructor
    }


//    public static playerFragment newInstance(Book book) {
//        playerFragment fragment = new playerFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(PLAYER,book);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            book = getArguments().getParcelable(PLAYER);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        pause = view.findViewById(R.id.pause);
        play = view.findViewById(R.id.play);
        stop = view.findViewById(R.id.stop);
        bar = view.findViewById(R.id.seekBar);
        nowPlaying = view.findViewById(R.id.nowPlaying);

        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                playerInterface.play();

            }
        });
        pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                playerInterface.pause();
            }
        });
        stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                playerInterface.stop();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof playerFragmentInterface){
            playerInterface = (playerFragment.playerFragmentInterface) context;
        }else{
            throw new RuntimeException("Implement playerFragmentInterface");
        }
    }



    interface playerFragmentInterface{
        void play();
        void pause();
        void stop();
    }
}