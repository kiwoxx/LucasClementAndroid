package smd.doggystyle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListLikesActivity extends AppCompatActivity {

    List<Pair<String, Bitmap>> likedDogs;
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_likes);

        rv = (RecyclerView) findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(this));

        URL urlJsonRandomImage = null;
        try {
            urlJsonRandomImage = new URL("https://dog.ceo//api//img//pomeranian//n02112018_1611.jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        likedDogs = new ArrayList<>();
        DownloadRandomImagesTask imageDownloader = new DownloadRandomImagesTask();
        imageDownloader.execute(urlJsonRandomImage);
    }

    public class DownloadRandomImagesTask extends AsyncTask<URL, Integer, Long> {

        protected Long doInBackground(URL... urls) {
            try {
                HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = connection.getInputStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        likedDogs.add(new Pair<String, Bitmap>("bob", bitmap));
                        likedDogs.add(new Pair<String, Bitmap>("bob", bitmap));
                        likedDogs.add(new Pair<String, Bitmap>("bob", bitmap));
                        likedDogs.add(new Pair<String, Bitmap>("bob", bitmap));
                        Adapter adapter = new Adapter(likedDogs);
                        rv.setAdapter(adapter);
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0L;
        }
        protected void onProgressUpdate(Integer... progress) {
        }
        protected void onPostExecute(Long result) {
            Log.i("Downloaded ", "result" + result);
        }
    }
}

