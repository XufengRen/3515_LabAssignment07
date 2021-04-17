package edu.temple.bookshelf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class BookDetialsFragment extends Fragment {

    TextView title, author;
    private Book book;
    ImageView image;

    public BookDetialsFragment() {
        // Required empty public constructor
    }


    public static BookDetialsFragment newInstance(Book book) {
        Log.i("---------------------------------------------------BookDetailsFragment newInstance", " new bookdetailsfragment initiated with book: "+book.getTitle());
        BookDetialsFragment fragment = new BookDetialsFragment();
        Bundle args = new Bundle();
        args.putParcelable("Book", book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("---------------------------------------------------BookDetailsFragment onCreate()","in onCreate() now");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.i("---------------------------------------------------BookDetailsFragment onCreate()","argument is not null");
            book = getArguments().getParcelable("Book");
            Log.i("---------------------------------------------------BookDetailsFragment onCreate()","got argument");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("---------------------------------------------------BookDetailsFragment onCreateView()","in onCreateView() now");
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_display, container, false);
        title = view.findViewById(R.id.display_title);
        author = view.findViewById(R.id.display_author);
        image = view.findViewById(R.id.display_img);
        int i;
        if(book == null){ i = 1;}else{ i = 0;}
        Log.i("---------------------------------------------------BookDetailsFragment onCreateView()","is the book null?"+i);
        if (book != null){

            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            Log.i("---------------------------------------------------BookDetailsFragment onCreateView()","showing image with address:"+ book.getURL());
//            URL url = null;
//            try {
//                url = new URL(book.getURL());
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//            Bitmap bmp = null;
//            try {
//                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            } catch (NetworkOnMainThreadException | IOException e) {
//                e.printStackTrace();
//            }
//            image.setImageBitmap(bmp);
            Picasso.get().load(book.getURL()).into(image);
        }

        return view;
    }

    public void displayBook(Book book){
        Log.i("---------------------------------------------------BookDetailsFragment displayBook()","displaying book");
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
    }
}