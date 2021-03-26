package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    BookDetialsFragment display_fragment;
    BookLlistFragment list_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create booklist object and add elements
        BookList myList = new BookList();
        for (int i = 1; i <= 10; i++){
            String title = "book"+ String.valueOf(i);
            String author = "writer" + String.valueOf(i);
            myList.addBook(title, author);
        }

        display_fragment = new BookDetialsFragment();
        list_fragment = new BookLlistFragment();

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.container, list_fragment);

        fragmentTransaction.commit();
    }
}