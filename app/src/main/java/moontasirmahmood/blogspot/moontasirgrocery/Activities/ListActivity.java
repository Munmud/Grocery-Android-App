package moontasirmahmood.blogspot.moontasirgrocery.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import moontasirmahmood.blogspot.moontasirgrocery.Data.DatabaseHandler;
import moontasirmahmood.blogspot.moontasirgrocery.Model.Grocery;
import moontasirmahmood.blogspot.moontasirgrocery.R;
import moontasirmahmood.blogspot.moontasirgrocery.UI.myAdapter;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView ;
    private myAdapter adapter;
    private List<Grocery> groceryList ;
    private List<Grocery> listItems ;
    private DatabaseHandler db;
    private AlertDialog.Builder dialogBuilder ;
    private AlertDialog dialog ;
    private EditText groceryItem ;
    private EditText groceryQuantity ;
    private Button saveButton ;
    private Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        db = new DatabaseHandler(this);
        context = this.context ;


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopUpDialog();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        groceryList = new ArrayList<>();
        listItems = new ArrayList<>() ;

        groceryList = db.getAllGrocery();

        for (Grocery c : groceryList){
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQuantity(c.getQuantity());
            grocery.setDateItemAdded("Added on: " + c.getDateItemAdded());
            grocery.setId(c.getId());
            listItems.add(grocery) ;
        }
        Toast.makeText(this,String.valueOf(listItems.size()), Toast.LENGTH_LONG).show();

        adapter = new myAdapter(this , listItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        final Grocery grocery = new Grocery() ;

        grocery.setName(groceryItem.getText().toString());
        grocery.setQuantity(groceryQuantity.getText().toString());

        db.addGrocery(grocery);
        Snackbar.make(view, "Item added"  ,Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listItems.add(0,db.getGrocery(db.lastId()));
                adapter.notifyItemInserted(0);
                dialog.dismiss();
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);



            }
        },0);

    }


}