package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by niroshan on 12/4/15.
 */
public class PersistentMemoryTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {


    public PersistentMemoryTransactionDAO(Context context) {

         super(context,"130487B" ,null,1);

    }

    public PersistentMemoryTransactionDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist");
        onCreate(db);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "crete table if not exist transaction ( "+
                        "transactionid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL "+
                        "accountnumber  String not null"+
                        "transactiontype  String not null"+
                        "date String not null"+
                        "amount double not null" +
                        ");"
        );
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountnumber",accountNo);
        contentValues.put("transactiontype",expenseType.toString());
        contentValues.put("date", date.toString());
        contentValues.put("amount",amount);
        db.insert("transaction", null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        Cursor cursor = null;
        String empName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        List<Transaction> transactions=new ArrayList<Transaction>();



        try{

            cursor =  db.rawQuery( "select *  from transaction ", null );

            if (cursor != null) {
                while (!cursor.isAfterLast()) {
                    String accnum=cursor.getString(cursor.getColumnIndex("accountnumber"));
                    String transactiontype=cursor.getString(cursor.getColumnIndex("transactiontype"));
                    ExpenseType expenseType= ExpenseType.EXPENSE;
                    if(transactiontype=="EXPENSE"){
                         expenseType= ExpenseType.EXPENSE;
                    }
                    else if(transactiontype=="INCOME")
                    {
                        expenseType= ExpenseType.EXPENSE;
                    }
                    else{
                        System.out.println("invalid transaction type");
                    }
                    Date date=new Date(cursor.getString(cursor.getColumnIndex("date")));

                    Double amount=cursor.getDouble(cursor.getColumnIndex("amount"));

                    System.out.println(cursor.getColumnIndex("accountnumber"));

                    Transaction newtra=new Transaction(date,accnum,expenseType,amount);
                    transactions.add(newtra);

                    cursor.moveToNext();
                }
            }




        }
        finally {

            cursor.close();
        }

        return  transactions;


    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        List<Transaction> transactions=new ArrayList<Transaction>();



        try{

            cursor =  db.rawQuery( "select *  from transaction  ORDER BY id DESC LIMIT "+limit+"", null );

            if (cursor != null) {
                while (!cursor.isAfterLast()) {
                    String accnum=cursor.getString(cursor.getColumnIndex("accountnumber"));
                    String transactiontype=cursor.getString(cursor.getColumnIndex("transactiontype"));
                    ExpenseType expenseType= ExpenseType.EXPENSE;
                    if(transactiontype=="EXPENSE"){
                        expenseType= ExpenseType.EXPENSE;
                    }
                    else if(transactiontype=="INCOME")
                    {
                        expenseType= ExpenseType.EXPENSE;
                    }
                    else{
                        System.out.println("invalid transaction type");
                    }
                    Date date=new Date(cursor.getString(cursor.getColumnIndex("date")));

                    Double amount=cursor.getDouble(cursor.getColumnIndex("amount"));

                    System.out.println(cursor.getColumnIndex("accountnumber"));

                    Transaction newtra=new Transaction(date,accnum,expenseType,amount);
                    transactions.add(newtra);

                    cursor.moveToNext();
                }
            }




        }
        finally {

            cursor.close();
        }

        return  transactions;

    }

}
