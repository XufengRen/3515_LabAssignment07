package edu.temple.bookshelf;

import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.stream.Stream;

public class BookList extends ArrayList<Parcelable> {

    public ArrayList<Book> bookList;

    public BookList(){
        bookList = new ArrayList<Book>();
    }

    public void addBook(String title, String author){
        Book book = new Book(title, author);
        bookList.add(book);
    }

    public void removeBook(Book book){
        bookList.remove(book);
    }

    public Book getBook(int i){
        return bookList.get(i);
    }

    public int bookListSize(){
        return bookList.size();
    }

    @NonNull
    @Override
    public Stream<Parcelable> stream() {
        return null;
    }
}
