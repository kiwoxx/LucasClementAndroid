package smd.doggystyle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LikeActivity extends AppCompatActivity {
    ImageView dogImage;
    Url jsonDatas;
    URL urlJsonRandomImage = null;
    Button likeButton;
    Button dislikeButton;
    int dogNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        loadComponents();
        addListeners();
        Bundle extras = getIntent().getExtras();
        TextView nomRace = (TextView)findViewById(R.id.nomRace);
        nomRace.setText(extras.getString("dogRace"));
        dogNumber = extras.containsKey("dogNumber") ? extras.getInt("dogNumber") : 0;
        Log.i("dogNumber => ", "numero " + dogNumber);
        try {
            urlJsonRandomImage = new URL("https://dog.ceo/api/breed/"+extras.getString("dogRace")+"/images");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        DownloadJsonTask downloadJsonTask = new DownloadJsonTask();
        downloadJsonTask.execute(urlJsonRandomImage);

        dogImage = (ImageView) findViewById(R.id.imageView1);
    }

    public class DownloadImageTask extends AsyncTask<URL, Integer, Long> {

        protected Long doInBackground(URL... urls) {
            try {
                HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = connection.getInputStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dogImage.setImageBitmap(bitmap);
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
    public class DownloadJsonTask extends AsyncTask<URL, Integer, Long> {

        protected Long doInBackground(URL... urls) {
            try {
                HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();
                InputStream is = connection.getInputStream();
                Gson gson = new Gson();
                Log.i("test gson ", "gson instancié");

                jsonDatas =  new Gson().fromJson( new InputStreamReader(is) , new TypeToken<Url>(){}.getType());
                Log.i("jsonDatas => ", jsonDatas == null ? "null" : "données extraites du Json !");

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
            Log.i("urlFromJson => ", jsonDatas.message.get(dogNumber));

            try {
                new DownloadImageTask().execute(new URL(jsonDatas.message.get(dogNumber)));
            } catch (MalformedURLException e) {
            e.printStackTrace();
            }
        }
    }

    private void loadComponents() {
        likeButton    = findViewById(R.id.buttonLike);
        dislikeButton = findViewById(R.id.buttonDislike);
    }

    private void addListeners(){
        likeButton.setClickable(true);
        likeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent gameActivity = new Intent(LikeActivity.this, LikeActivity.class);
                gameActivity.putExtra("dogRace", getIntent().getExtras().getString("dogRace"));
                gameActivity.putExtra("dogNumber", dogNumber + 1);
                startActivity(gameActivity);
            }
        });

        dislikeButton.setClickable(true);
        dislikeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent gameActivity = new Intent(LikeActivity.this, LikeActivity.class);
                gameActivity.putExtra("dogRace", getIntent().getExtras().getString("dogRace"));
                gameActivity.putExtra("dogNumber", dogNumber + 1);
                startActivity(gameActivity);
            }
        });
    }
}
