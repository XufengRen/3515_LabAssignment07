package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BookDetialsFragment extends Fragment {



    private String mParam1;
    private String mParam2;
    private static Book book;
    public BookDetialsFragment() {
        // Required empty public constructor
    }


    public static BookDetialsFragment newInstance(Book book) {
        BookDetialsFragment fragment = new BookDetialsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("Book", book);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_fragment, container, false);
    }
}