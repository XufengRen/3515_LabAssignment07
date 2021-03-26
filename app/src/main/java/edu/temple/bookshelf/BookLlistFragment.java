package edu.temple.bookshelf;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookLlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookLlistFragment extends Fragment {

    private BookList bookList;
    private Book book;
    Context context;
    private OnItemSelectedInterface onItemSelectedInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookLlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BookLlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookLlistFragment newInstance(BookList bookList) {
        BookLlistFragment fragment = new BookLlistFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("BookList", bookList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookList = (BookList)getArguments().getParcelableArrayList("BookList");
        }else{
            bookList = new BookList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ListView view = (ListView) inflater.inflate(R.layout.fragment_book_list, container, false);
        view.setAdapter(new listAdapter(getActivity(), android.R.layout.simple_list_item_1, bookList));
        view.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return view;
    }

    public interface OnItemSelectedInterface{
        void itemSelected(Book book);
    }
}