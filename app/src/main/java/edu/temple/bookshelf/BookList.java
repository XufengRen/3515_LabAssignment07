package edu.temple.bookshelf;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.stream.Stream;

public class BookList implements Parcelable {

    public ArrayList<Book> bookList;

    public BookList(){
        bookList = new ArrayList<Book>();
    }

    protected BookList(Parcel in) {
        bookList = in.createTypedArrayList(Book.CREATOR);
    }

    public static final Creator<BookList> CREATOR = new Creator<BookList>() {
        @Override
        public BookList createFromParcel(Parcel in) {
            return new BookList(in);
        }

        @Override
        public BookList[] newArray(int size) {
            return new BookList[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(bookList);
    }
}
