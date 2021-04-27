package edu.temple.bookshelf;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

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

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.getID());
        jsonObject.put("title", this.getTitle());
        jsonObject.put("author", this.getAuthor());
        jsonObject.put("url", this.getURL());
        jsonObject.put("duration", this.getDuration());
        return jsonObject;
    }

    public static Book fromJson(JSONObject json) throws JSONException {
        return new Book(
                json.getInt("id"),
                json.getString("title"),
                json.getString("author"),
                json.getString("url"),
                json.getInt("duration")
        );
    }
}
