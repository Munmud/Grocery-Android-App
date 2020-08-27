package moontasirmahmood.blogspot.moontasirgrocery.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import java.util.List;

import moontasirmahmood.blogspot.moontasirgrocery.Data.DatabaseHandler;
import moontasirmahmood.blogspot.moontasirgrocery.Model.Grocery;
import moontasirmahmood.blogspot.moontasirgrocery.R;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    private Context context ;
    private List<Grocery> listItems;
    private AlertDialog.Builder alertDialogBuilder ;
    private AlertDialog dialog ;
    private LayoutInflater inflater ;

    public myAdapter(Context context, List<Grocery> listItems) {
        this.context = context;
        this.listItems = listItems;
    }


    @Override
    public myAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row , parent, false);
        return new ViewHolder(view , context);
    }

    @Override
    public void onBindViewHolder( myAdapter.ViewHolder holder, int position) {
        Grocery grocery = listItems.get(position);
        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.date.setText(grocery.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView groceryItemName;
        public TextView quantity ;
        public TextView date;
        public Button editButton ;
        public Button deleteButon ;
        public  int id ;

        public ViewHolder( View view , Context ctx) {
            super(view);
            context = ctx ;

            groceryItemName= (TextView) view.findViewById(R.id.name) ;
            quantity= (TextView)view.findViewById(R.id.quantity) ;
            date = (TextView) view.findViewById(R.id.dateAdded) ;
            editButton = (Button) view.findViewById(R.id.editButton) ;
            deleteButon = (Button) view.findViewById(R.id.deleteButton) ;

            editButton.setOnClickListener(this);
            deleteButon.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.editButton :
                    int position = getAdapterPosition();
                    Grocery grocery= listItems.get(position);
                    editItem(grocery);
                    break;
                case R.id.deleteButton :
                     position = getAdapterPosition() ;
                     grocery = listItems.get(position) ;
                    deleteItem(grocery.getId());
                    break;
            }
        }

        public void deleteItem(final int id){
            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context) ;
            View view = inflater.inflate(R.layout.confirmation_dialog, null) ;

            Button noButton  = (Button) view.findViewById(R.id.noButton) ;
            Button yesButton = (Button) view.findViewById(R.id.yesButton) ;

            alertDialogBuilder.setView(view) ;
            dialog = alertDialogBuilder.create() ;
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db = new DatabaseHandler(context) ;
                    db.deleteGrocery(id);
                    listItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            });

        }

        public void editItem(final Grocery grocery){
            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context) ;
            View view = inflater.inflate(R.layout.popup , null);
            alertDialogBuilder.setView(view) ;
            dialog = alertDialogBuilder.create() ;
            dialog.show();

            final EditText groceryItem = (EditText) view.findViewById(R.id.groceryItem) ;
            final EditText groceryQuantity = (EditText) view.findViewById(R.id.groceryQty) ;
            Button saveButton = (Button) view.findViewById(R.id.saveButton) ;

            groceryItem.setText(grocery.getName());
            groceryQuantity.setText(grocery.getQuantity());

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(groceryQuantity.getText().toString());

                    if (!groceryItem.getText().toString().isEmpty() && !groceryQuantity.getText().toString().isEmpty()){
                        DatabaseHandler db = new DatabaseHandler(context) ;
                        db.updateGrocery(grocery);
                        dialog.dismiss();
                        notifyItemChanged(getAdapterPosition(),grocery);


                    }
                    else {
                        Snackbar.make(view , "Add Grocery & Quantity",Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
