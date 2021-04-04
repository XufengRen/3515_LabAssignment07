package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookSelectedInterface {

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;
    BookDetialsFragment display_fragment;
    boolean landscapeTracker;
    Book selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("-------------------------------------main onCreate()","Start onCreate of main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if info storeed in instance state
        if(savedInstanceState != null){
            selected = savedInstanceState.getParcelable("selectedBook");
        }

        //check if screen id landscape
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
                    .add(R.id.container, BookListFragment.newInstance(bookList()))
            .commit();
        }

        //create display fragment if there isn't any
        display_fragment = (selected == null) ? new BookDetialsFragment():BookDetialsFragment.newInstance(selected);

        // if screen is landscape: show detail in container 2
        //if screen is portrait, replace list with detail in container1
        if(landscapeTracker){
            fragmentManager.beginTransaction().
                    replace(R.id.container2, display_fragment)
                    .commit();
        }else if (selected != null){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, display_fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private BookList bookList(){
        BookList myList = new BookList();
        myList.addBook("t1","a1");
        myList.addBook("t2","a2");
        myList.addBook("t3","a3");
        myList.addBook("t4","a4");
        myList.addBook("t5","a5");
        myList.addBook("t6","a6");
        myList.addBook("t7","a7");
        myList.addBook("t8","a8");
        myList.addBook("t9","a9");
        return myList;
    }

    @Override
    public void itemSelected(int i){
        selected = bookList().getBook(i);
        Log.i("-----------------------------in main bookSelected()", i+"th book selected. Title:"+selected.getTitle()+" Author: "+selected.getAuthor());
        Log.i("------------------------in main bookSelected()","is there two frags on screen?: "+landscapeTracker);
        if(landscapeTracker){
            Log.i("-------------------------------------in main bookSelected()","in if(twoPane) now");
            display_fragment.displayBook(selected);
        }else{
            fragmentManager.beginTransaction()
                    .replace(R.id.container, display_fragment.newInstance(selected))
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable("selectedBook", selected);
    }

    @Override
    public void onBackPressed() {
        selected = null;
        super.onBackPressed();
    }
}