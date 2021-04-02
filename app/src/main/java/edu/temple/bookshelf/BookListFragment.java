package edu.temple.bookshelf;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookListFragment extends Fragment {

    private static BookList booklist;
    private Book book;
    Context context;
    BookSelectedInterface parentActivity;

    public BookListFragment() {
        // Required empty public constructor
    }

    public static BookListFragment newInstance(BookList booklist) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putParcelable("Booklist", booklist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if(context instanceof BookSelectedInterface){
            parentActivity = (BookSelectedInterface) context;
        }else{
            throw new RuntimeException("Exception");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentActivity = null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            booklist = getArguments().getParcelable("Booklist");
        }
    }
    public interface BookSelectedInterface{
        void itemSelected(int i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        ListView list = view.findViewById(R.id.list_f01);
        listAdapter adapter = new listAdapter(view.getContext(), booklist);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view1, position, id) -> {
            parentActivity.itemSelected(position);
        });
        return inflater.inflate(R.layout.fragment_book_list, container, false);
    }
}