package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BookDetialsFragment extends Fragment {

    TextView title, author;
    private Book book;


    public BookDetialsFragment() {
        // Required empty public constructor
    }


    public static BookDetialsFragment newInstance(Book book) {
        Log.i("---------------------------------------------------BookDetailsFragment newInstance", " new bookdetailsfragment initiated with book: "+book.getTitle());
        BookDetialsFragment fragment = new BookDetialsFragment();
        Bundle args = new Bundle();
        args.putParcelable("Book", book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("---------------------------------------------------BookDetailsFragment onCreate()","in onCreate() now");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = getArguments().getParcelable("Book");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("---------------------------------------------------BookDetailsFragment onCreateView()","in onCreateView() now");
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_display, container, false);
        title = view.findViewById(R.id.display_title);
        author = view.findViewById(R.id.display_author);
        int i;
        if(book == null){ i = 1;}else{ i = 0;}
        Log.i("---------------------------------------------------BookDetailsFragment onCreateView()","is the book null?"+i);
        if (book != null){
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
        }

        return view;
    }

    public void displayBook(Book book){
        Log.i("---------------------------------------------------BookDetailsFragment displayBook()","displaying book");
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
    }
}