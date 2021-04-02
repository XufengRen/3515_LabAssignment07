package edu.temple.bookshelf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class listAdapter extends BaseAdapter {
    Context context;
    BookList bookList;
    LayoutInflater layoutInflater;
    public listAdapter(Context context, BookList list){
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

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view ==null){
            view = layoutInflater.inflate(R.id.listItem, parent, false);
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
