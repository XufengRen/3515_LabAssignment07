package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookSelectedInterface,playerFragment.playerFragmentInterface {

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;
    BookDetialsFragment display_fragment;
    boolean landscapeTracker;
    Book selected;
    BookList bookList;
    private final int REQUEST_CODE = 9527;
    AudiobookService.MediaControlBinder mediaControlBinder;
    boolean connect;
    private static final String PROGRESS ="progress";
    public static final String DURATION = "duration";
    int duration, progress;
    SeekBar bar;
    Button pause,play,stop;
    TextView nowplaying;
    playerFragment player_fragment;
    Intent intent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("-------------------------------------main onCreate()","Start onCreate of main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = findViewById(R.id.seekBar);

        findViewById(R.id.main_searchbutton).setOnClickListener((view) ->{
            startActivityForResult(new Intent(MainActivity.this, SearchActivity.class), 9527);
        });


        //check if info storeed in instance state
        if(savedInstanceState != null){
            selected = savedInstanceState.getParcelable("selectedBook");
            bookList = savedInstanceState.getParcelable("booklist");
            duration = savedInstanceState.getInt(DURATION);
            progress = savedInstanceState.getInt(PROGRESS);
        }else{
            bookList = new BookList();
        }

        //check if screen is landscape
        if(findViewById(R.id.container2) != null){
            landscapeTracker = true;
        }else{
            landscapeTracker = false;
        }

        //if container1 is null or show detail, put list in container
        //if f01 show list, popBackStack
        Fragment f01;
        f01 = fragmentManager.findFragmentById(R.id.container);
        if(f01 instanceof BookDetialsFragment){
            fragmentManager.popBackStack();
        }else if(!(f01 instanceof BookListFragment)){
            fragmentManager.beginTransaction()
                    .add(R.id.container, BookListFragment.newInstance(bookList), "booklist")
                    .commit();
        }

        //create display fragment if there isn't any
        display_fragment = (selected == null) ? new BookDetialsFragment():BookDetialsFragment.newInstance(selected);
        player_fragment = (selected == null) ? new playerFragment():playerFragment.newInstance(selected);
        intent = getIntent();
        // if screen is landscape: show detail in container 2
        //if screen is portrait, replace list with detail in container1
        if(landscapeTracker){
            Log.i("----------------------------------------main OnCreate()","landscape mode");
            if(intent.hasExtra("booklist")){
                Bundle extras = getIntent().getExtras();
                bookList = extras.getParcelable("booklist");
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.container, BookListFragment.newInstance(bookList), "booklist")
                    .replace(R.id.container2, BookDetialsFragment.newInstance(selected),"bookDetails")
                    .replace(R.id.container3, playerFragment.newInstance(selected), "player")
                    .commit();

        }else if (selected != null){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, display_fragment,"bookDetails")
                    .addToBackStack(null)
                    .commit();
        }
        intent = new Intent(this, AudiobookService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

//    private BookList bookList(){
//        BookList myList = new BookList();
//        myList.addBook("asdsaddsat1","a1");
//        myList.addBook("safdfdsft2","a2");
//        myList.addBook("fdbdgrhrdt3","a3");
//        myList.addBook("tbdfbdvdf4","a4");
//        myList.addBook("tbdfgrde5","a5");
//        myList.addBook("tbfdbrge6","a6");
//        myList.addBook("tvsdsfvsd7","a7");
//        myList.addBook("tsdvfdsfds8","a8");
//        myList.addBook("dsfsdfdst9","a9");
//        return myList;
//    }

    @Override
    public void itemSelected(int i){
        selected = bookList.getBook(i);
        Log.i("-----------------------------in main bookSelected()", i+"th book selected. Title:"+selected.getTitle()+" Author: "+selected.getAuthor());
        Log.i("------------------------in main bookSelected()","is there two frags on screen?: "+landscapeTracker);
        if(landscapeTracker){
            Log.i("-------------------------------------in main bookSelected()","in if(twoPane) now");
            //display_fragment.displayBook(selected);
            fragmentManager.beginTransaction()
            .replace(R.id.container2, BookDetialsFragment.newInstance(selected))
            .replace(R.id.container3, playerFragment.newInstance(selected))
            .commit();
        }else{
            fragmentManager.beginTransaction()
                    .replace(R.id.container, display_fragment.newInstance(selected), "bookDetails")
                    .replace(R.id.container3, playerFragment.newInstance(selected))
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void showResult(){
        boolean nullcheck;
        if((BookListFragment) fragmentManager.findFragmentByTag("booklist")==null){nullcheck = true;}else{nullcheck = false;}
        Log.i("------------------------in main showResult()","is booklist fragment null?: "+nullcheck);
        if(fragmentManager.findFragmentByTag("bookDetails") instanceof BookDetialsFragment){
            fragmentManager.popBackStack();
        }
        ((BookListFragment) fragmentManager.findFragmentByTag("booklist")).showResult();
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable("selectedBook", selected);
        outState.putParcelable("bookLlist", bookList);
        outState.putInt(PROGRESS, progress);
        outState.putInt(DURATION, duration);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        Log.i("------------------------in main onActivityResult()"," ");
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(bookList != null){
                bookList.clear();

            }
            BookList add_this_to_booklist = (BookList) data.getParcelableExtra(SearchActivity.BOOKLIST_KEY);
            Log.i("------------------------in main onActivityResult()","add this to book list: "+"id: "+add_this_to_booklist.getBook(0).getID()+"; title: "+add_this_to_booklist.getBook(0).getTitle()+"; author: "+add_this_to_booklist.getBook(0).getAuthor()+ "; cover url: "+add_this_to_booklist.getBook(0).getURL());
            bookList.addList((BookList) data.getParcelableExtra(SearchActivity.BOOKLIST_KEY));
            Log.i("------------------------in main onActivityResult()","booklist size: "+bookList.bookListSize());
            if(bookList.bookListSize()==0){
                Toast.makeText(this,"no match found", Toast.LENGTH_SHORT).show();
            }
            Log.i("------------------------in main onActivityResult()","show book info: "+"id: "+bookList.getBook(0).getID()+"; title: "+bookList.getBook(0).getTitle()+"; author: "+bookList.getBook(0).getAuthor()+ "; cover url: "+bookList.getBook(0).getURL());
            Log.i("------------------------in main onActivityResult()","go to showResult()");
            showResult();
        }
    }
    @Override
    public void onBackPressed() {
        selected = null;
        super.onBackPressed();
    }

    ServiceConnection serviceConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mediaControlBinder = (AudiobookService.MediaControlBinder) service;
            connect=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connect=false;
            mediaControlBinder = null;
        }
    };

    Handler playerHandler = new Handler(new Handler.Callback(){

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            AudiobookService.BookProgress bookProgress = (AudiobookService.BookProgress) msg.obj;

            bar = findViewById(R.id.seekBar);
            bar.setMax(duration);
            if(mediaControlBinder.isPlaying()){
                bar.setProgress(bookProgress.getProgress());
                progress = selected.getDuration();
            }

            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser){
                        mediaControlBinder.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            return false;
        }
    });

    @Override
    public void play(int i) {
        if(connect){
            startService(intent);
            duration = selected.getDuration();
            mediaControlBinder.play(i);
            mediaControlBinder.setProgressHandler(playerHandler);
        }
    }

    @Override
    public void pause(int i) {
        mediaControlBinder.pause();
    }

    @Override
    public void stop(int i) {
        mediaControlBinder.stop();
    }
}