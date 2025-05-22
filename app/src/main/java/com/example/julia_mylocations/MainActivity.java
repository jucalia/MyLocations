package com.example.julia_mylocations;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Itens do Menu
        String menu [] = new String [] {"Minha casa na cidade atual", "Minha casa em Viçosa", "Meu departamento", "Fechar aplicação"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        setListAdapter(arrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        String aux = l.getItemAtPosition(position).toString();
        Intent intent = new Intent(this, SecondActivity.class);

        Toast.makeText(this, aux, Toast.LENGTH_SHORT).show();

        switch(position){
            case 0:
                intent.putExtra("location_id", position);
                startActivity(intent);
            case 1:
                intent.putExtra("location_id", position);
                startActivity(intent);
            case 2:
                intent.putExtra("location_id", position);
                startActivity(intent);
            case 3:
                finish(); //fecha o app

        }

    }
}