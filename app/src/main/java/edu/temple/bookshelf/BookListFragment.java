package edu.temple.bookshelf;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookListFragment extends Fragment {

    private BookList booklist;
    private Book book;
    Context context;
    BookSelectedInterface bookSelectedInterface;
    ListView list;

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
        Log.i("--------------------------------------BookListFragment onAttach()","");
        super.onAttach(context);
        if(context instanceof BookSelectedInterface){
            bookSelectedInterface = (BookSelectedInterface) context;
        }
        else{
            throw new RuntimeException("implement interface in main activity");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("--------------------------------------BookListFragment onCreate()","");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            booklist = getArguments().getParcelable("Booklist");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("--------------------------------------BookListFragment onCreateView()","");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        list = view.findViewById(R.id.list_f01);
        listAdapter adapter = new listAdapter(view.getContext(), booklist);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view1, position, id) -> {
            Log.i("--------------------------------------BookListFragment onCreateView()","Enter on item click listener");
            bookSelectedInterface.itemSelected(position);
        });
        return view;
    }

    @Override
    public void onDetach() {
        Log.i("--------------------------------------BookListFragment onDetach()","");
        super.onDetach();
        bookSelectedInterface = null;
    }

    public interface BookSelectedInterface{
        void itemSelected(int i);
    }

    public void showResult(){
        boolean nullcheck;
        if(list==null){
            nullcheck=true;
        }else{nullcheck=false;}
        Log.i("--------------------------------------BookListFragment, showResult()","is list null?: "+nullcheck);
        ((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
    }
}