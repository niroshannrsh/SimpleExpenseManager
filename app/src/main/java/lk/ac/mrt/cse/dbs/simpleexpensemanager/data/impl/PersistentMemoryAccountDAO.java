package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by niroshan on 12/4/15.
 */
public class PersistentMemoryAccountDAO extends SQLiteOpenHelper implements AccountDAO {




    public PersistentMemoryAccountDAO(Context context) {
        super(context,"130487B" ,null,1);;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "crete table if not exist account ( "+
                        "accountnumber  String primary key"+
                        "bankname String not null"+
                        "customer String not null"+
                        "balance double not null" +
                        ");"
        );



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist");
        onCreate(db);

    }



    @Override
    public List<String> getAccountNumbersList() {
        Cursor cursor = null;
        String empName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> accounts=new ArrayList<>();


        try{

            cursor =  db.rawQuery( "select accountnumber from account ", null );

            if (cursor != null) {
                while (!cursor.isAfterLast()) {
                    accounts.add(cursor.getString(cursor.getColumnIndex("accountnumber")));
                    System.out.println(cursor.getColumnIndex("accountnumber"));
                    cursor.moveToNext();
                }
            }




        }
        finally {

            cursor.close();
        }

        return accounts;


    }

    @Override
    public List<Account> getAccountsList()  {
        List<Account> accounts=new ArrayList<Account>();
        List<String>  accountsnumbers=new ArrayList<String>();
        accountsnumbers=getAccountNumbersList();

        for(String s : accountsnumbers)
        {
            String  accnum=s;

            try {
                Account acc = getAccount(s);
                accounts.add(acc);
            }
            catch (Exception e){
                System.out.println("error account number");
            }

        }
        return new ArrayList<>();

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        Cursor cursor = null;
        String empName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Account account=null;


        try{

            cursor =  db.rawQuery( "select * from account  where  accountnumber '"+accountNo+"'", null );

            if (cursor != null) {

                    String accnum=cursor.getString(cursor.getColumnIndex("accountnumber"));
                    String holdername=cursor.getString(cursor.getColumnIndex("customer"));
                    String bankname=cursor.getString(cursor.getColumnIndex("bankname"));
                    double balance=cursor.getDouble(cursor.getColumnIndex("balance"));
                    account=new Account(accnum,bankname,holdername,balance);
            }

            else{
                System.out.println("Account number does not exist");
            }




        }

        catch (Exception e){
            System.out.println("have some error occued");

        }
        finally {

            cursor.close();
        }



        return account;
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountnumber",account.getAccountNo());
        contentValues.put("bankname",account.getBankName());
        contentValues.put("customer", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());
        db.insert("account", null, contentValues);


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from account where accountnumber='"+accountNo+"'");

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        Double balance=getAccount(accountNo).getBalance();
        if(expenseType== ExpenseType.EXPENSE){
            balance=balance-amount;

        }
        else if(expenseType== ExpenseType.INCOME){
            balance=balance+amount;

        }

        String bal=balance.toString();
        db.execSQL("UPDATE Accounts SET balance='"+bal+"' WHERE accountnumber='"+accountNo+"'");

    }

}
