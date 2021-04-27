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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import edu.temple.audiobookplayer.AudiobookService;

import static java.lang.String.valueOf;

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
    public static final String PLAYINGBOOK = "playingBook";
    public static final String BOOKLIST = "booklist";
    public static final String TIMESTAMP = "timestamp";
    int duration, progress;
    SeekBar bar;
    Button pause,play,stop;
    TextView nowplaying;
    playerFragment player_fragment;
    Intent intent;
    private static SharedPreferences sharedPrefs;
    private static int timestamp;
    private static Book playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("-------------------------------------main onCreate()","Start onCreate of main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefs = getSharedPreferences(getPackageName(),MODE_PRIVATE);
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
            File savedlist = new File(this.getFilesDir().getAbsolutePath()+"/save.json");
            if(savedlist.exists()){
                System.out.println("getting list from json file");
                bookList = BookList.fromJson(savedlist);
            }
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
        if ((player_fragment = (playerFragment) fragmentManager.findFragmentById(R.id.container3)) == null) {
            player_fragment = new playerFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.container3, player_fragment)
                    .commit();
        }

        intent = getIntent();
        // if screen is landscape: show detail in container 2
        //if screen is portrait, replace list with detail in container1
        if(landscapeTracker){
            Log.i("----------------------------------------main OnCreate()","landscape mode");
//            if(intent.hasExtra("booklist")){
//                Bundle extras = getIntent().getExtras();
//                bookList = extras.getParcelable("booklist");
//            }
            fragmentManager.beginTransaction()
                    //.replace(R.id.container, BookListFragment.newInstance(bookList), "booklist")
                    .replace(R.id.container2, BookDetialsFragment.newInstance(selected),"bookDetails")
                    //.replace(R.id.container3, playerFragment.newInstance(selected), "player")
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



    @Override
    public void itemSelected(int i){
        selected = bookList.getBook(i);
        Log.i("-----------------------------in main bookSelected()", i+"th book selected. Title:"+selected.getTitle()+" Author: "+selected.getAuthor());
        Log.i("------------------------in main bookSelected()","is there two frags on screen?: "+landscapeTracker);
        if(landscapeTracker){
            Log.i("-------------------------------------in main bookSelected()","in if(twoPane) now");
            //display_fragment.displayBook(selected);
            display_fragment.displayBook(selected);
//            fragmentManager.beginTransaction()
//            .replace(R.id.container2, BookDetialsFragment.newInstance(selected))
//            .replace(R.id.container3, playerFragment.newInstance(selected))
//            .commit();
        }else{
            fragmentManager.beginTransaction()
                    .replace(R.id.container, display_fragment.newInstance(selected), "bookDetails")
                    //.replace(R.id.container3, playerFragment.newInstance(selected))
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
            //Log.i("------------------------in main onActivityResult()","add this to book list: "+"id: "+add_this_to_booklist.getBook(0).getID()+"; title: "+add_this_to_booklist.getBook(0).getTitle()+"; author: "+add_this_to_booklist.getBook(0).getAuthor()+ "; cover url: "+add_this_to_booklist.getBook(0).getURL());
            bookList.addList((BookList) data.getParcelableExtra(SearchActivity.BOOKLIST_KEY));
            File file = new File(this.getFilesDir().getAbsolutePath()+"/save.json");
            bookList.saveList(file);
            Log.i("------------------------in main onActivityResult()","booklist size: "+bookList.bookListSize());
            if(bookList.bookListSize()==0){
                Toast.makeText(this,"no match found", Toast.LENGTH_SHORT).show();
            }
            //Log.i("------------------------in main onActivityResult()","show book info: "+"id: "+bookList.getBook(0).getID()+"; title: "+bookList.getBook(0).getTitle()+"; author: "+bookList.getBook(0).getAuthor()+ "; cover url: "+bookList.getBook(0).getURL());
            Log.i("------------------------in main onActivityResult()","go to showResult()");
            showResult();
        }
    }
    @Override
    public void onBackPressed() {

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
            if(msg.obj != null && playing != null){
                timestamp=bookProgress.getProgress();
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
    public void play() {
        if(connect){
            Log.i("-----------------------------------------main play():", "connected");
            //pause whatever is playing first
            //pause();
            playing = selected;
            //if file exist, play from file
            File f = new File(getFilesDir(),"/"+playing.getID()+".mp3");
            if(f.exists()){
                Log.i("-----------------------------------------main play():", "local file detected");
                //if saved progress >= 10 , start 10 second earlier, else start from beginning
                int startPpoint;
                int saved = sharedPrefs.getInt(TIMESTAMP+playing.getID(),0);
                System.out.println("timestamp in pref: "+saved);
                if(saved>=10){
                    startPpoint = saved-10;
                }else{startPpoint=0;}
                mediaControlBinder.setProgressHandler(playerHandler);
                mediaControlBinder.play(f, startPpoint);
                startService(intent);
                duration = selected.getDuration();

            }else{
                //else steam and download
                Log.i("-----------------------------------------main play()", "start streaming");
                startService(intent);
                duration = selected.getDuration();
                mediaControlBinder.play(playing.getID());
                //download the mp3 file
                new Thread(() -> {
                    try {
                        URL url = new URL("https://kamorris.com/lab/audlib/download.php?id=" + playing.getID());
                        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
                        FileOutputStream fileOutputStream = new FileOutputStream(f);
                        FileChannel fileChannel = fileOutputStream.getChannel();

                        fileOutputStream.getChannel()
                                .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                mediaControlBinder.setProgressHandler(playerHandler);

            }
        }
        if(playing != null){
            TextView nowPlaying = findViewById(R.id.nowPlaying);
            nowPlaying.setText("Now Playing:"+playing.getTitle());
        }
    }

    @Override
    public void pause() {
        //pause book and save progress
        if(playing!=null){
            System.out.println("timestamp:"+timestamp);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putInt(PLAYINGBOOK, playing.getID());
            editor.putInt(TIMESTAMP+playing.getID(),timestamp);
            editor.commit();
        }

        mediaControlBinder.pause();
    }

    @Override
    public void stop() {
        //clear info in preference
        if(playing!=null){
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.remove(TIMESTAMP+playing.getID());
            editor.putInt(PLAYINGBOOK, -1);
            editor.commit();
        }
        playing=null;
        mediaControlBinder.stop();
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}