package moontasirmahmood.blogspot.moontasirgrocery.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import moontasirmahmood.blogspot.moontasirgrocery.Model.Grocery;
import moontasirmahmood.blogspot.moontasirgrocery.Util.Constants;

public class DatabaseHandler extends SQLiteOpenHelper {
    private Context ctx ;
    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx = context ;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_GROCERY_TABLE =
                "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY, "
                + Constants.KEY_GROCERY_ITEM + " TEXT, "
                + Constants.KEY_QTY_NUMBER + " TEXT, "
                + Constants.KEY_DATE_NAME + " LONG)" ;
        sqLiteDatabase.execSQL(CREATE_GROCERY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues() ;
        values.put(Constants.KEY_GROCERY_ITEM , grocery.getName()) ;
        values.put(Constants.KEY_QTY_NUMBER , grocery.getQuantity()) ;
        values.put(Constants.KEY_DATE_NAME , java.lang.System.currentTimeMillis()) ;

        db.insert(Constants.TABLE_NAME , null , values) ;
        db.close();

    }

    public Grocery getGrocery(int id){
        SQLiteDatabase db = this.getReadableDatabase() ;
        Cursor cursor = db.query(
                Constants.TABLE_NAME ,
                new String[]  {Constants.KEY_ID, Constants.KEY_GROCERY_ITEM , Constants.KEY_QTY_NUMBER , Constants.KEY_DATE_NAME} ,
                Constants.KEY_ID + "=?",
                new String[]  {String.valueOf(id)} ,
                null,null,null
        );

        Grocery grocery = new Grocery( );


        if (cursor!=null)
            cursor.moveToFirst();

        grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
        grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

        // convert time Stamp to something readable
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
        grocery.setDateItemAdded(formatedDate);


        cursor.close();
        db.close();
        return  grocery ;


    }

    public List<Grocery> getAllGrocery(){
        List <Grocery> groceryList = new ArrayList<>() ;
        SQLiteDatabase db = getReadableDatabase() ;
        Cursor cursor = db.query(
                Constants.TABLE_NAME ,
                new String[]  {Constants.KEY_ID, Constants.KEY_GROCERY_ITEM , Constants.KEY_QTY_NUMBER , Constants.KEY_DATE_NAME} ,
                null,
                null ,
                null,
                null,
                Constants.KEY_DATE_NAME + " DESC"
        );

        if (cursor.moveToFirst()){
            do{
                Grocery grocery = new Grocery( );

                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));
                // convert time Stamp to something readable
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
                grocery.setDateItemAdded(formatedDate);

                groceryList.add(grocery) ;
            }while (cursor.moveToNext());
        }
//        cursor.close();
//        db.close();
        return groceryList ;

    }

    public void updateGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues values = new ContentValues() ;

        values.put(Constants.KEY_GROCERY_ITEM,grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER,grocery.getQuantity());


        int temp =  db.update(Constants.TABLE_NAME , values , Constants.KEY_ID +"=?" ,new String[]{String.valueOf(grocery.getId())} );
        db.close() ;
    }

    public void deleteGrocery(int id){
        SQLiteDatabase db = this.getWritableDatabase() ;
        db.delete(Constants.TABLE_NAME , Constants.KEY_ID + "=?" ,new String[]{String.valueOf(id)} );
        db.close();
    }

    public int getGroceryCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase() ;
        Cursor cursor = db.rawQuery(countQuery , null) ;

        if (cursor != null)
        return  cursor.getCount() ;
        else return 0 ;



    }

    public int lastId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " + Constants.TABLE_NAME, null );
        cursor.moveToLast();
        return Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID)));
    }

}
