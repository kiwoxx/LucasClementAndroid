package smd.doggystyle;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

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
    Url       jsonDatas;
    URL       urlJsonRandomImage = null;
    Button    likeButton;
    Button    dislikeButton;
    int       dogNumber;
    TextView  nomRace;
    Toolbar   toolbar;
    Bundle    extras;

    private void loadComponents() {
        likeButton    = findViewById(R.id.buttonLike);
        dislikeButton = findViewById(R.id.buttonDislike);
        nomRace       = (TextView)findViewById(R.id.nomRace);
        dogImage      = (ImageView) findViewById(R.id.imageView1);
        toolbar       = (Toolbar) findViewById(R.id.my_toolbar);
    }

    private void addListeners(){
        likeButton.setClickable(true);
        likeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, R.string.love_confirm, duration);
                toast.show();

                Intent gameActivity = new Intent(LikeActivity.this, LikeActivity.class);
                gameActivity.putExtra("dogRace", getIntent().getExtras().getString("dogRace"));
                gameActivity.putExtra("dogNumber", dogNumber + 1);
                startActivity(gameActivity);
                createNotify();
                finish();
            }
        });

        dislikeButton.setClickable(true);
        dislikeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(LikeActivity.this);
                builder.setMessage(R.string.dislike_confirm)
                        .setCancelable(true)
                        .setPositiveButton("Yeah :(", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent gameActivity = new Intent(LikeActivity.this, LikeActivity.class);
                                gameActivity.putExtra("dogRace", getIntent().getExtras().getString("dogRace"));
                                gameActivity.putExtra("dogNumber", dogNumber + 1);
                                startActivity(gameActivity);
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        extras = getIntent().getExtras();
        nomRace.setText(extras.getString("dogRace"));
        dogNumber = extras.containsKey("dogNumber") ? extras.getInt("dogNumber") : 0;
        String[] dogNames = getResources().getStringArray(R.array.dogs_name);
        String dogName = dogNames[dogNumber] == null ? dogNames[1] : dogNames[dogNumber];
        String description = dogName + ", " + extras.getString("dogRace");
        nomRace.setText(description);
        Log.i("dogNumber => ", "numero " + dogNumber);

        setSupportActionBar(toolbar);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        loadComponents();
        addListeners();

        try {
            urlJsonRandomImage = new URL("https://dog.ceo/api/breed/"+extras.getString("dogRace")+"/images");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        DownloadJsonTask downloadJsonTask = new DownloadJsonTask();
        downloadJsonTask.execute(urlJsonRandomImage);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuButton:
                Intent listLikesActivity = new Intent(LikeActivity.this, ListLikesActivity.class);
                startActivity(listLikesActivity);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void createNotify(){
        Notification notification ;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "DoggyChannel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel for doggy likes");
            manager.createNotificationChannel(channel);
        }

        Notification.Builder builder = new Notification.Builder(LikeActivity.this, "default");
        Intent intent = new Intent("com.rj.notitfications.SECACTIVITY");
        PendingIntent pendingIntent = PendingIntent.getActivity(LikeActivity.this, 1, intent, 0);

        builder.setAutoCancel(false);
        builder.setTicker("Love added");
        builder.setContentTitle("new love added");
        builder.setContentText("find him in your love list");
        builder.setSmallIcon(R.drawable.ic_love);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setNumber(100);
        builder.build();

        notification = builder.getNotification();
        manager.notify(11, notification);
    }
}
