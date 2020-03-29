package com.example.dbhelper;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    SQLiteDatabase filmsDB;
    SimpleCursorAdapter adapter;
    EditText title,rating,year;
    Spinner spinner_genre;
    ToggleButton toggleButton;
    ToggleButton toggleButton_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper helper = new DBHelper(this);
        filmsDB = helper.getWritableDatabase();

        lv = findViewById(R.id.filmslist);
        title = findViewById(R.id.title);
        //genre = findViewById(R.id.genre);
        rating = findViewById(R.id.rating);
        year = findViewById(R.id.year);
        spinner_genre = findViewById(R.id.spinner);

        Cursor tunes = filmsDB.rawQuery("SELECT * FROM films", null);
        String[] filmlist_fields = tunes.getColumnNames();

        int[] views = { R.id.id, R.id.title, R.id.genre, R.id.rating, R.id.year };

        // этот адаптер отображает в ListView перечень полей (столбцов)
        adapter = new SimpleCursorAdapter(this, R.layout.film_item, tunes, filmlist_fields, views, 0 );
        lv.setAdapter(adapter);

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int id = buttonView.getId();
                // в зависимости от того, какую кнопку нажали, меняем статус переменных
                if (isChecked) {
                    Toast toast = Toast.makeText(getApplicationContext(), "asc", Toast.LENGTH_SHORT);
                    toast.show();
                    adapter.changeCursor(filmsDB.rawQuery("SELECT * FROM films ORDER BY year", null ));
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "desc", Toast.LENGTH_SHORT);
                    toast.show();
                    adapter.changeCursor(filmsDB.rawQuery("SELECT * FROM films ORDER BY year DESC", null ));
                }
            }
        });

        toggleButton_rating = (ToggleButton) findViewById(R.id.toggleButton_rating);
        toggleButton_rating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int id = buttonView.getId();
                // в зависимости от того, какую кнопку нажали, меняем статус переменных
                if (isChecked) {
                    Toast toast = Toast.makeText(getApplicationContext(), "asc", Toast.LENGTH_SHORT);
                    toast.show();
                    adapter.changeCursor(filmsDB.rawQuery("SELECT * FROM films ORDER BY rating", null ));
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "desc", Toast.LENGTH_SHORT);
                    toast.show();
                    adapter.changeCursor(filmsDB.rawQuery("SELECT * FROM films ORDER BY rating DESC", null ));
                }
            }
        });


    }


//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        int id = buttonView.getId();
//        // в зависимости от того, какую кнопку нажали, меняем статус переменных
//        if (isChecked) {
//            // The toggle is enabled
//        } else {
//            // The toggle is disabled
//        }
//    }

    public void onClick(View v) {
        String film_name = title.getText().toString();
        //String film_genre = genre.getText().toString();
        String film_rating = rating.getText().toString();
        String film_year = year.getText().toString();
        String cur_genre = spinner_genre.getSelectedItem().toString();
        if (film_name.equals(""))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Insert a title!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (cur_genre.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Insert a genre!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (film_rating.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Insert a rating!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (film_year.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Insert an year!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            int year = Integer.parseInt(film_year);
            int rating = Integer.parseInt(film_rating);
            Object[] args = {film_name, cur_genre,  rating,year};
            filmsDB.execSQL("INSERT INTO films (title, genre, rating, year) values (?, ?, ?, ?)", args);
            adapter.changeCursor(filmsDB.rawQuery("SELECT * FROM films", null ));
        }
        //Object[] args = {"Breaking Bad", "Criminal",  10,2008}; // значения для подстановки в запрос
        // можно явно указать все значения
        // db.execSQL("INSERT INTO playlist (artist, title, year, duration) values ('K2', 'Track', 1992, 100)");

        // или подставить в шаблон, можно использовать любые функции, доступные в языке SQL
        //filmsDB.execSQL("INSERT INTO films (title, genre, rating, year) values (?, ?, ?, ?)", args);
        // execSQL позволяет подставить значения в запрос

        // не забывайте выбрать записи заново и обновить источник данных в адаптере
        //adapter.changeCursor(filmsDB.rawQuery("SELECT * FROM films ORDER BY year", null ));
    }
    public void onClearClick(View v) {
        try {
            filmsDB.execSQL("DELETE FROM films");

        } catch (SQLException e) { Log.d("mytag", e.getLocalizedMessage()); }

        adapter.changeCursor(filmsDB.rawQuery("SELECT * FROM films", null ));
    }

    public void onCountClick(View v){
        //int sum = filmsDB.rawQuery("SELECT COUNT(*) FROM films",null);
        Cursor tunes = filmsDB.rawQuery("SELECT * FROM films", null);
        int filmlist_fields = tunes.getCount();


        Toast toast = Toast.makeText(getApplicationContext(), "Amount of films in db: " + filmlist_fields, Toast.LENGTH_SHORT);
        toast.show();


    }
}
