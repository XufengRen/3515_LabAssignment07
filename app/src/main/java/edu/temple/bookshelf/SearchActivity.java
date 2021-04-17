package edu.temple.bookshelf;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {
    private final String urlPrefic = "https://kamorris.com/lab/cis3515/search.php?term=";
    public static final String BOOKLIST_KEY = "booklist";

    private final String id = "id",title = "title", author = "author", cover_url = "cover_url", duration = "duration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        findViewById(R.id.searchButton).setOnClickListener((view)->{
            Log.i("----------------------------------------------SearchActivity onCreate()","search button clicked");
            String searchURL = urlPrefic + ((EditText)findViewById(R.id.searchEditText)).getText().toString();
            Log.i("----------------------------------------------SearchActivity onCreate(), onClickListener()","searchURL: "+searchURL);
            requestQueue.add(new JsonArrayRequest(searchURL, new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response){
                    Intent resultIntent = new Intent();
                    BookList return_this_to_main_activity = jsonToBookList(response);
                    Log.i("-----------------------------------------------Search Activity, onCreate(), onResponse()","url: "+return_this_to_main_activity.getBook(0).getURL());
                    resultIntent.putExtra(BOOKLIST_KEY, return_this_to_main_activity);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError e){
                    Toast.makeText(getApplicationContext(),"something is wrong", Toast.LENGTH_SHORT);
                }
            }));
        });

        findViewById(R.id.cancelButton).setOnClickListener((v -> {finish();}));
    }

    private BookList jsonToBookList(JSONArray jsonArray){
        BookList bookList = new BookList();
        JSONObject tempBook;
        Log.i("-----------------------------------------------Search Activity, jsontoBookList","jsonArray length: "+jsonArray.length());
        for(int i = 0; i<jsonArray.length(); i++){
            try{
                tempBook = jsonArray.getJSONObject(i);
                Log.i("-----------------------------------------------Search Activity, json obj",": "+tempBook);
                Book temp = new Book(tempBook.getInt(id), tempBook.getString(title), tempBook.getString(author), tempBook.getString(cover_url), tempBook.getInt(duration));
                Log.i("-----------------------------------------------Search Activity, json to book list","new book object with url: "+temp.getURL());
                bookList.addBook(temp);
                Log.i("-----------------------------------------------Search Activity, json to book list","book info from json:"+" id: "+tempBook.getInt(id)+"; title: "+tempBook.getString(title)+"; author: "+ tempBook.getString(author)+ "; url: "+tempBook.getString(cover_url));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i("-----------------------------------------------Search Activity, json to book list","list length:"+bookList.bookListSize());
        Log.i("-----------------------------------------------Search Activity, json to book list","url: "+bookList.getBook(0).getURL());
        return bookList;
    }
}