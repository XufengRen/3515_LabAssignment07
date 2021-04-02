package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
        BookDetialsFragment fragment = new BookDetialsFragment();
        Bundle args = new Bundle();
        args.putParcelable("Book", book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = getArguments().getParcelable("Book");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_display_fragment, container, false);
        title = view.findViewById(R.id.display_title);
        author = view.findViewById(R.id.display_author);
        if (book != null){
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
        }

        return view;
    }
}