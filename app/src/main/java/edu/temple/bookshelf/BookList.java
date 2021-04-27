package edu.temple.bookshelf;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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

    public void addBook(Book book){
        bookList.add(book);
    }

    public void addList(BookList list){
        for (int i=0; i<list.bookListSize(); i++){
            this.bookList.add(list.getBook(i));
        }
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

    public void clear(){bookList.clear();}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(bookList);
    }

    public static final BookList fromJson(File file) {
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONArray jsonArray = new JSONArray(content);
            return BookList.fromJson(jsonArray);
        } catch (IOException ioe) {

        } catch (JSONException jse) {

        }
        return null;
    }
    public void saveList(File file) {
        // create the JSON array
        JSONArray arr = new JSONArray();
        for(Book book : bookList) {
            try {
                arr.put(book.toJson());
            } catch (JSONException jsonException) {

            }
        }
        // Swrite json to file
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(arr.toString(4));
            fw.close();
        } catch (IOException ioe) {
            System.out.println("IOException");

        } catch (JSONException jse) {
            System.out.println("JSONException");

        }
    }
}
