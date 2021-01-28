package com.ooteedemo.householdneeds;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ooteedemo.householdneeds.data.DatabaseHandler;
import com.ooteedemo.householdneeds.model.Item;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText householdItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHandler = new DatabaseHandler(this);

        bypassActivity();

//        Check if item was saved
        List<Item> items = databaseHandler.getAllItems();
        for (Item item: items) {
            Log.d("AllItems", "onCreate: "+item.getItemName()+item.getDateItemAdded());
        }



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void bypassActivity() {
        if (databaseHandler.getItemCount()>0) {
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }

    private void saveItem(View view) {
        Item item = new Item();

        String newItem = householdItem.getText().toString().trim();
        String newColor = itemColor.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        int size = Integer.parseInt(itemSize.getText().toString().trim());

        item.setItemName(newItem);
        item.setItemColor(newColor);
        item.setItemQuantity(quantity);
        item.setItemSize(size);

        databaseHandler.addItem(item);

        Snackbar.make(view, "Item Saved",Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        },1200);
    }

    private void createPopupDialog() {
        builder  = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);

        householdItem = view.findViewById(R.id.householdItem);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemColor = view.findViewById(R.id.itemColor);
        itemSize = view.findViewById(R.id.itemSize);

        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!householdItem.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty()
                        && !itemColor.getText().toString().isEmpty()
                        && !itemSize.getText().toString().isEmpty()){
                    saveItem(v);
                } else {
                    Snackbar.make(v,"Things can't be empty",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}