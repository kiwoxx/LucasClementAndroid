package smd.doggystyle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    Button _secondAppliButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadComponents();
        addListeners();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.dogs_race,
                 android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadComponents() {
        spinner = findViewById(R.id.spinner);
        _secondAppliButton = findViewById(R.id.button_second_appli);
    }
    private void addListeners(){
        _secondAppliButton.setClickable(true);
        _secondAppliButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent gameActivity = new Intent(MainActivity.this, LikeActivity.class);
                gameActivity.putExtra("dogRace", spinner.getSelectedItem().toString());
                startActivity(gameActivity);
            }
        });
    }
}
