package edu.temple.bookshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class listAdapter extends BaseAdapter {
    Context context;
    BookList bookList;
    
    public listAdapter(Context context, BookList list){
        this.context = context;
        bookList = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_item_layout,parent,false);
        return null;
    }
}
