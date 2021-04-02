package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;
    BookDetialsFragment display_fragment;
    boolean boo;
    Book bookSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            bookSelected = savedInstanceState.getParcelable("selectedBook");
        }
        boo = findViewById(R.id.container2) != null;

        Fragment f01;
        f01 = fragmentManager.findFragmentById(R.id.container);
        if(f01 instanceof BookDetialsFragment){
            fragmentManager.popBackStack();
        }else if(!(f01 instanceof BookListFragment)){
            fragmentManager.beginTransaction()
                    .add(R.id.container, BookListFragment.newInstance(getList()))
            .commit();
        }

        display_fragment = (bookSelected == null) ? new BookDetialsFragment():BookDetialsFragment.newInstance(bookSelected);
        if(boo){
            fragmentManager.beginTransaction().
                    replace(R.id.container2, display_fragment)
                    .commit();
        }else if (bookSelected != null){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, display_fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private BookList getList(){
        BookList myList = new BookList();
        myList.addBook("t1","a1");
        myList.addBook("t2","a2");
        myList.addBook("t3","a3");

        return myList;
    }
}