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

    List<Pair<String, URL>> likedDogs;
    List<Pair<String, Bitmap>> listNamesImages;
    RecyclerView rv;
    int likeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_likes);

        rv = (RecyclerView) findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listNamesImages = new ArrayList<>();
        try {
            likedDogs = new ArrayList<>();
            likedDogs.add(new Pair<String, URL>("bob", new URL("https://dog.ceo//api//img//pomeranian//n02112018_1611.jpg")));
            likedDogs.add(new Pair<String, URL>("max", new URL("https://dog.ceo//api//img//redbone//n02090379_1020.jpg")));
            likedDogs.add(new Pair<String, URL>("nasus", new URL("https://dog.ceo//api//img//terrier-australian//n02096294_2206.jpg")));
            likedDogs.add(new Pair<String, URL>("napoleon", new URL("https://dog.ceo//api//img//eskimo//n02109961_2609.jpg")));
            likedDogs.add(new Pair<String, URL>("freddy", new URL("https://dog.ceo//api//img//sheepdog-english//n02105641_9289.jpg")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        DownloadRandomImagesTask imageDownloader = new DownloadRandomImagesTask();
        likeCount = 0;
        imageDownloader.execute();
    }

    public class DownloadRandomImagesTask extends AsyncTask<URL, Integer, Long> {

        protected Long doInBackground(URL... urls) {
            try {
                HttpURLConnection connection = (HttpURLConnection) likedDogs.get(likeCount).second.openConnection();
                InputStream inputStream = connection.getInputStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listNamesImages.add(new Pair<String, Bitmap>(likedDogs.get(likeCount).first, bitmap));
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
            DownloadRandomImagesTask imageDownloader = new DownloadRandomImagesTask();
            likeCount += 1;
            if(likeCount == 5){
                Adapter adapter = new Adapter(listNamesImages);
                rv.setAdapter(adapter);
            } else {
                imageDownloader.execute();
            }
        }
    }
}

