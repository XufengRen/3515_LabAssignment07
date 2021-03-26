package edu.temple.bookshelf;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class listAdapter extends BaseAdapter {
    Context context;
    BookList bookList;
    
    public listAdapter(Context context, int res, BookList list){
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
        LinearLayout view;
        TextView title;
        TextView author;
        String list_title = bookList.getBook(position).getTitle();
        String list_author = bookList.getBook(position).getAuthor();

        view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);

        title = new TextView(context);
        title.setTextSize(32);
        title.setGravity(Gravity.CENTER);
        title.setText(list_title);

        author = new TextView(context);
        author.setTextSize(16);
        author.setGravity(Gravity.CENTER);
        author.setText(list_author);

        view.addView(title);
        view.addView(author);

        return view;
    }
}
