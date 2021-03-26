package edu.temple.bookshelf;

import java.util.ArrayList;

public class BookList {

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

}
