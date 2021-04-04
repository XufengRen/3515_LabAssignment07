package edu.temple.bookshelf;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class listAdapter extends BaseAdapter {
    Context context;
    BookList bookList;
    LayoutInflater layoutInflater;


    public listAdapter(Context context, BookList list){
        Log.i("------------------------------------list adapter initiated", "");
        this.context = context;
        bookList = list;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bookList.bookListSize();
    }

    @Override
    public Object getItem(int position) {
        return bookList.getBook(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Log.i("------------------------------------list adapter getView()", "");
        if(view ==null){
            Log.i("------------------------------------list adapter getView()", "inflate list item view if view is null");
            view = layoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }
        TextView title = (TextView)view.findViewById(R.id.list_title);
        TextView author = (TextView)view.findViewById(R.id.list_author);
        String list_title = bookList.getBook(position).getTitle();
        String list_author = bookList.getBook(position).getAuthor();
        title.setText(list_title);
        author.setText(list_author);

        return view;
    }
}
