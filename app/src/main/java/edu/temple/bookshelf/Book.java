package edu.temple.bookshelf;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    String title;
    String author;
    int id;
    String coverURL;
    int duration;

    public Book(int id, String title, String author, String URL, int duration){
        this.title = title;
        this.author = author;
        this.id = id;
        this.coverURL = URL;
        this.duration = duration;
    }

    protected Book(Parcel in) {
        title = in.readString();
        author = in.readString();
        coverURL = in.readString();
        id = in.readInt();
        duration = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(coverURL);
        dest.writeInt(id);
        dest.writeInt(duration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public int getID(){return id;}

    public String getURL(){return coverURL;}

    public int getDuration(){return duration;}
}
