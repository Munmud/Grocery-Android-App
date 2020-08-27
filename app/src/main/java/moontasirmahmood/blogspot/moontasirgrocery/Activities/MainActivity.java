package moontasirmahmood.blogspot.moontasirgrocery.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;


import java.util.List;

import moontasirmahmood.blogspot.moontasirgrocery.Data.DatabaseHandler;
import moontasirmahmood.blogspot.moontasirgrocery.Model.Grocery;
import moontasirmahmood.blogspot.moontasirgrocery.R;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder ;
    private AlertDialog dialog ;
    private EditText groceryItem ;
    private EditText groceryQuantity ;
    private Button saveButton ;
    private DatabaseHandler db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        byPassActivity();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createPopUpDialog() ;
            }
        });
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

    public void createPopUpDialog(){
        dialogBuilder = new AlertDialog.Builder(this) ;
        View view = getLayoutInflater().inflate(R.layout.popup , null) ;
        groceryItem = (EditText) view.findViewById(R.id.groceryItem) ;
        groceryQuantity = (EditText) view.findViewById(R.id.groceryQty) ;
        saveButton = (Button) view.findViewById(R.id.saveButton) ;

        dialogBuilder.setView(view) ;
        dialog = dialogBuilder.create();
        dialog.show();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!groceryItem.getText().toString().isEmpty() && !groceryQuantity.getText().toString().isEmpty())
                saveGroceryToDb(view);
                else {
                    Snackbar.make(view , "Add Grocery & Quantity",Snackbar.LENGTH_LONG).show();
                }


            }
        });


    }

    private void saveGroceryToDb(View view) {
        Grocery grocery = new Grocery() ;

        grocery.setName(groceryItem.getText().toString());
        grocery.setQuantity(groceryQuantity.getText().toString());

        db.addGrocery(grocery);
        Snackbar.make(view, "Item added"  ,Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this , ListActivity.class);
                finish();
                startActivity(intent);
            }
        },1000);

    }

    public void byPassActivity(){
        if (db.getGroceryCount()>0){
            Intent intent = new Intent(MainActivity.this , ListActivity.class);
            finish();
            startActivity(intent);

        }
    }
}